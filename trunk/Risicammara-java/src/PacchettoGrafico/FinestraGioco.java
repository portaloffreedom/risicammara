package PacchettoGrafico;

import PacchettoGrafico.PannelloGiocoPackage.AscoltatorePlanciaAttacco;
import PacchettoGrafico.PannelloGiocoPackage.GestoreFasi;
import risicammaraJava.playerManage.ListaGiocatoriClient;
import PacchettoGrafico.PannelloGiocoPackage.PannelloGioco;
import PacchettoGrafico.PannelloGiocoPackage.PlanciaImmagine;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioArmateDisponibili;
import risicammaraServer.messaggiManage.MessaggioAttaccoVinto;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioDichiaraAttacco;
import risicammaraServer.messaggiManage.MessaggioFase;
import risicammaraServer.messaggiManage.MessaggioRisultatoLanci;
import risicammaraServer.messaggiManage.MessaggioSpostaArmate;
import risicammaraServer.messaggiManage.comandi_t;

/**
 * Finestra della interfaccia di gioco.
 * @author matteo
 */
public class FinestraGioco extends JFrame implements Runnable {
    private Connessione server;
    private ListaGiocatoriClient listaGiocatori;
    private PartitaClient partita;
    private GestoreFasi gestoreFasi;
    private PlanciaImmagine plancia;
    private RichiestaNumeroArmate richiestaNumeroArmate;

    private PannelloGioco pannello;

    public FinestraGioco(Connessione server, PartitaClient partita) {
        super("Risicammara - "+partita.getMeStesso().getNome());
        this.server = server;
        this.listaGiocatori = partita.getListaGiocatori();
        this.partita = partita;
        this.richiestaNumeroArmate = null;
        ImageIcon icona = new ImageIcon(this.getClass().getResource(Client.RISICAMLOGO));
        this.setIconImage(icona.getImage());

        Container contestoFinestra = this.getContentPane();
        this.pannello = new PannelloGioco(240, partita);
        contestoFinestra.add(pannello);

        Dimension dimensioniMinime = new Dimension(900, 600);
        super.setLocationByPlatform(true);
        this.setMinimumSize(dimensioniMinime);
        
        this.plancia = pannello.getPlanciaImmagine();
        this.gestoreFasi = new GestoreFasi(pannello.getBarraFasi(),server, partita,plancia ,pannello.getAttivatoreGrafica());

        this.addWindowListener(new WindowListenerImpl(server));
        this.setVisible(true);
    }

    @Override
    public void run() {
        /* APPUNTI
         * A tutti i giocatori un MessaggioFase che dice la fase in cui siamo, a
         * tutti i giocatori tranne quello ceh gioca arriva un messaggio
         * TurnOfPlayer(comandO) che dice chi sta giocando, e al giocatore che
         * deve giocare arriva un StartYourTurn (comando)
         */
        try {

            MessaggioFase msgFase = (MessaggioFase) server.ricevi();
            if (msgFase.getFase() != Fasi_t.PREPARTITA) {
                System.err.println("Errore, server non sincronizzato");
                Client.RiavviaClient();
            }

            this.cicloPreFase();

            this.cicloPartita();


        } catch (IOException ex) {
            System.err.println("Connessione al server persa o corrotta.");
            Client.RiavviaClient();
        } catch (ClassNotFoundException ex) {
            System.err.println("Pacchetto inviato al server irriconoscibile. Disconnessione...");
            Client.RiavviaClient();
        }
    }

    private void cicloPreFase() throws ClassNotFoundException, IOException {
        Messaggio msg = null;
        //gestoreFasi.faseToAttesa();
        while (true) {
            msg = server.ricevi();
            if (Client.DEBUG)
                System.out.println("Arrivato messaggio: "+msg);
            switch (msg.getType()) {
                case FASE:
                    gestoreFasi.faseToAttesa();
                    if (Client.DEBUG)
                        System.out.println("Finita la prefase");
                    return;  //è finita la prefase
                case COMMAND:
                    MessaggioComandi msgComandi = (MessaggioComandi) msg;
                    if (Client.DEBUG && (msgComandi.getComando() != comandi_t.TURNOFPLAYER)){
                        System.err.println("Errore si sincronizzazione col server");
                        Client.RiavviaClient();
                    }
                    if (msgComandi.getSender() == listaGiocatori.meStessoIndex()){
                        gestoreFasi.avanzaFase();
                    }
                    else
                        gestoreFasi.faseToAttesa();
                    break;
                case CAMBIAARMATETERRITORIO:
                    MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio) msg;
                    plancia.aggiornaArmateTerritorio(msgArmate.getArmate(), msgArmate.getTerritorio());
                    if (gestoreFasi.getFaseCorrente() == Fasi_t.RINFORZO)
                        gestoreFasi.diminuisciArmateRinforzoDisponibili();
                    break;

                case ARMATEDISPONIBILI:
                    MessaggioArmateDisponibili msgMad = (MessaggioArmateDisponibili) msg;
                    gestoreFasi.setArmateRinforzoDisponibili(msgMad.getNumarm());
                    break;
            }
        }
    }

    private void cicloPartita() throws IOException, ClassNotFoundException {
        Messaggio msg = null;
        while (true) {
            msg = server.ricevi();
            if (Client.DEBUG)
                System.out.println("Arrivato messaggio: "+msg);
            switch (msg.getType()) {
                case COMMAND: {
                    MessaggioComandi msgComandi = (MessaggioComandi) msg;
                    switch (msgComandi.getComando()) {
                        case TURNOFPLAYER: {
                            int giocatoreDiTurno = msg.getSender();
                            partita.avanzaGiocatoreDiTurno(giocatoreDiTurno);
                            
                            if (giocatoreDiTurno == listaGiocatori.meStessoIndex()) {
                                //TODO rimpicciolisci tutte le frecce degli altri
                                System.out.println("tocca a te");
                            }
                            else {
                                //TODO tocca al giocatore avanza le frecce
                                System.out.println("tocca al giocatore "+giocatoreDiTurno);
                            }
                            break;
                        }

                        case ATTACCOTERMINATO: {
                            territori_t attaccante = partita.getTerritorioAttaccante();
                            territori_t difensore  = partita.getTerritorioAttaccato();
                            plancia.ripristinaTerritorio(attaccante);
                            plancia.ripristinaTerritorio(difensore);
                            break;
                        }
                            
                        default:
                            System.err.println("MESSAGGIO COMANDO NON RICONOSCIUTO! "+msg);
                            break;
                    }
                    break;
                }
                
                case ARMATEDISPONIBILI: {
                    MessaggioArmateDisponibili msgMad = (MessaggioArmateDisponibili) msg;
                    gestoreFasi.setArmateRinforzoDisponibili(msgMad.getNumarm());
                    break;
                }
                
                case CAMBIAARMATETERRITORIO: {
                    MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio) msg;
                    plancia.aggiornaArmateTerritorio(msgArmate.getArmate(), msgArmate.getTerritorio());
                    if (gestoreFasi.getFaseCorrente() == Fasi_t.RINFORZO)
                        gestoreFasi.diminuisciArmateRinforzoDisponibili();
                    break;
                }
                case SPOSTAARMATE: {
                    MessaggioSpostaArmate msgSpostaArmate = (MessaggioSpostaArmate) msg;
                    int armateSpostate = msgSpostaArmate.getNumarmate();
                    plancia.aggiornaArmateTerritorio(-armateSpostate, msgSpostaArmate.getSorgente());
                    plancia.aggiornaArmateTerritorio(armateSpostate, msgSpostaArmate.getArrivo());
                    break;
                }

                case FASE: {
                    if (!partita.eMioTurno())
                        break;
                    MessaggioFase msgFase = (MessaggioFase) msg;
                    gestoreFasi.setFase(msgFase.getFase());
                    if (Client.DEBUG)
                        System.out.println("Fase impostata a:"+gestoreFasi.getFaseCorrente()+" = "+msgFase.getFase());
                    break;
                }

                case DICHIARAATTACCO: {
                    MessaggioDichiaraAttacco msgAttacco = (MessaggioDichiaraAttacco) msg;
                    if (msgAttacco.getSender() == listaGiocatori.meStessoIndex())
                        break;
                    territori_t attaccante = msgAttacco.getTerritorio_attaccante();
                    territori_t difensore  = msgAttacco.getTerritorio_difensore();
                    partita.setTerritorioAttaccante(attaccante);
                    partita.setTerritorioAttaccato(difensore);
                    plancia.coloraSfumato(attaccante, AscoltatorePlanciaAttacco.Attacco, AscoltatorePlanciaAttacco.pesantezzaSfumatura);
                    plancia.coloraSfumato(difensore, AscoltatorePlanciaAttacco.Difesa, AscoltatorePlanciaAttacco.pesantezzaSfumatura);
                    break;
                }
                    
                case ATTACCOVINTO: {
                    MessaggioAttaccoVinto msgVincita = (MessaggioAttaccoVinto) msg;
                    int armateSpostate = msgVincita.getArmSpost();
                    TerritorioPlanciaClient attaccante = plancia.getTerritorio(partita.getTerritorioAttaccante());
                    TerritorioPlanciaClient difensore = plancia.getTerritorio(partita.getTerritorioAttaccato());
                    
                    difensore.setProprietario(attaccante.getProprietario());
                    plancia.aggiornaArmateTerritorio(-armateSpostate, attaccante);
                    plancia.setArmateTerritorio(armateSpostate, difensore);
                    
                    if (attaccante.getProprietario() == listaGiocatori.meStessoIndex()){
                        //chiedere se vuoi spostare altre armate
                        int armateDaSpostare = attaccante.getArmate()-1;
                        ArmateSpostateListener armateSpostateListener = new ArmateSpostateListener(server, partita, attaccante.getTerritorio(), difensore.getTerritorio());
                            if (armateDaSpostare <= 0){
                                armateSpostateListener.spostaArmate(0);
                                break;
                            }
                        richiestaNumeroArmate = new RichiestaNumeroArmate("Quante Armate vuoi spostare nel territorio Conquistato?", armateDaSpostare);
                        armateSpostateListener.setRichiestaNumeroArmate(richiestaNumeroArmate);
                        richiestaNumeroArmate.addOKActionListener(armateSpostateListener);
                    }
                    break;
                }
                case RISULTATOLANCI: {
                    MessaggioRisultatoLanci msgDado = (MessaggioRisultatoLanci) msg;
                    int valoreLancio;
                    
                    String attaccante = partita.getGiocatoreAttaccante().getNome();
                    System.out.println("Risultato dado ("+attaccante+") :");
                    while (true) {
                        valoreLancio = msgDado.getLancioAttacco();
                        if (valoreLancio < 0) break;
                        System.out.print(" "+valoreLancio);
                    }
                    System.out.println();
                    
                    String difensore = partita.getGiocatoreAttaccato().getNome();
                    System.out.println("Risultato dado ("+difensore+") :");
                    while (true) {
                        valoreLancio = msgDado.getLancioDifesa();
                        if (valoreLancio < 0) break;
                        System.out.print(" "+valoreLancio);
                    }
                    System.out.println();
                    
                    break;
                }

                default:
                    System.err.println("MESSAGGIO NON RICONOSCIUTO! "+msg);
                    break;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="ascoltatore della finestra">
    private static class WindowListenerImpl implements WindowListener {

        Connessione server;

        public WindowListenerImpl(Connessione server) {
            this.server = server;
        }

        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
            //TODO casella di conferma uscita
            try {
                server.chiudiConnessione();
            } catch (IOException ex) {
                System.err.println("Connessione al server non chiusa nella maniera corretta");
                System.exit(1234);
            }
            System.exit(0);
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }
    }// </editor-fold>

    private class ArmateSpostateListener implements ActionListener {
        private RichiestaNumeroArmate richiestaNumeroArmate;
        private Connessione server;
        private PartitaClient partita;
        private territori_t sorgente;
        private territori_t destinazione;

        public ArmateSpostateListener(Connessione server, PartitaClient partita, territori_t sorgente, territori_t destinazione) {
            this.server = server;
            this.partita = partita;
            this.sorgente = sorgente;
            this.destinazione = destinazione;
        }

        public void setRichiestaNumeroArmate(RichiestaNumeroArmate richiestaNumeroArmate) {
            this.richiestaNumeroArmate = richiestaNumeroArmate;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            int armateDaSpostare = richiestaNumeroArmate.getNumArmate();
            
            try {
                spostaArmate(armateDaSpostare);
            } catch (IOException ex) {
                System.err.println("Messaggio sposta armate non inviato con successo: "+ex);
            }
            
            richiestaNumeroArmate.setVisible(false);
            richiestaNumeroArmate.dispose();
            richiestaNumeroArmate = null;
        }
        
        
        public void spostaArmate(int armate) throws IOException {
            server.spedisci(new MessaggioSpostaArmate(partita.getMeStessoIndex(), sorgente, destinazione, armate));
        }
    }

}
