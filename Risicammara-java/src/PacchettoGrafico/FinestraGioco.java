package PacchettoGrafico;

import PacchettoGrafico.PannelloGiocoPackage.RichiestaNumeroArmate;
import PacchettoGrafico.PannelloGiocoPackage.AscoltatorePlanciaAttacco;
import PacchettoGrafico.PannelloGiocoPackage.GestoreFasi;
import risicammaraJava.playerManage.ListaGiocatoriClient;
import PacchettoGrafico.PannelloGiocoPackage.PannelloGioco;
import PacchettoGrafico.PannelloGiocoPackage.PlanciaImmagine;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.*;

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
    private RichiestaNumeroArmate richiestaNumeroArmateAttacco;

    private PannelloGioco pannello;


    /**
     * Costruisce la finestra di gioco di Risicammara.
     * @param server la Connessione al server
     * @param partita La partita lato Client.
     */
    public FinestraGioco(Connessione server, PartitaClient partita) {
        super("Risicammara - "+partita.getMeStesso().getNome());
        this.server = server;
        this.listaGiocatori = partita.getListaGiocatori();
        this.partita = partita;

        ImageIcon icona = new ImageIcon(this.getClass().getResource(Client.RISICAMLOGO));
        this.setIconImage(icona.getImage());

        Container contestoFinestra = this.getContentPane();
        this.pannello = new PannelloGioco(240,partita);
        contestoFinestra.add(pannello);
        
        Dimension dimensioniMinime = new Dimension(950, 600);
        super.setLocationByPlatform(true);
        this.setMinimumSize(dimensioniMinime);
        
        this.plancia = pannello.getPlanciaImmagine();
        this.gestoreFasi = new GestoreFasi(partita, plancia, pannello);
        this.richiestaNumeroArmateAttacco = new RichiestaNumeroArmate(pannello.getBottoneFaseAttacco(), server, partita);
        this.richiestaNumeroArmateAttacco.setOkActionListener(richiestaNumeroArmateAttacco);

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
                riavviaPartitaErrore("Errore, server non sincronizzato");
                return;
            }

            this.cicloPreFase();

            this.cicloPartita();


        } catch (IOException ex) {
            riavviaPartitaErrore("Connessione al server persa o corrotta.");
            return;
        } catch (ClassNotFoundException ex) {
            riavviaPartitaErrore("Pacchetto inviato al server irriconoscibile. Disconnessione...");
            return;
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
                    gestoreFasi.finePreFase();
                    if (Client.DEBUG)
                        System.out.println("Finita la prefase");
                    return;  //è finita la prefase
                case COMMAND:
                    MessaggioComandi msgComandi = (MessaggioComandi) msg;
                    if (Client.DEBUG && (msgComandi.getComando() != comandi_t.TURNOFPLAYER)){
                        riavviaPartitaErrore("Errore di sincronizzazione col server");
                    }
                    if(msgComandi.getComando() == comandi_t.DISCONNECT) riavviaPartitaErrore("Giocatori disconnessi");
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
                            if (partita.eMioTurno())
                                gestoreFasi.terminaAttaccoInCorso();
                            else {
                                territori_t attaccante = partita.getTerritorioAttaccante();
                                territori_t difensore  = partita.getTerritorioAttaccato();
                                plancia.ripristinaTerritorio(attaccante);
                                plancia.ripristinaTerritorio(difensore);
                            }
                            break;
                        }

                        case VINCITORE: {
                            if (msgComandi.getSender() == partita.getMeStessoIndex())
                                riavviaPartitaErrore("HAI VINTO!");
                            else
                                riavviaPartitaErrore("HA VINTO "+listaGiocatori.getNomeByIndex(msg.getSender()));
                            return;
                        }
                        
                        case DISCONNECT:{
                            riavviaPartitaErrore("Giocatori disconnessi!");
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
                        this.richiestaNumeroArmateAttacco.imposta(attaccante.getTerritorio(), difensore.getTerritorio(),armateDaSpostare);
                        if (armateDaSpostare <= 0){
                            richiestaNumeroArmateAttacco.spostaArmate(0);
                            break;
                        }
                        richiestaNumeroArmateAttacco.attiva();
                    }
                    break;
                }
                case RISULTATOLANCI: {
                    MessaggioRisultatoLanci msgDado = (MessaggioRisultatoLanci) msg;
                    int valoriAttacco[] = msgDado.getValoriAttacco();
                    int valoriDifesa[]  = msgDado.getValoriDifesa();

                    pannello.setRisultatoDadi(valoriAttacco, valoriDifesa);
                    
                    break;
                }
                case CARTA: {
                    MessaggioCarta msgCarta = (MessaggioCarta) msg;
                    Carta carta = msgCarta.getCarta();
                    if (carta == null) {
                        //TODO l'altro giocatore ha pescato una carta
                    }
                    else {
                        partita.aggiungiCartaMeStesso(carta);
                    }
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

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            //TODO casella di conferma uscita
            try {
                server.chiudiConnessione();
            } catch (IOException ex) {
                Client.RiavviaClientErrore("Connessione al server non chiusa nella maniera corretta");
            } finally {
                e.getWindow().setVisible(false);
                e.getWindow().dispose();
            }
            Client.RiavviaClient();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }// </editor-fold>

    

    /**
     * Riavvia la partita in caso di errore.
     * @param messaggio Il messaggio di errore
     */
    public void riavviaPartitaErrore(String messaggio){
        try { server.chiudiConnessione(); } catch (IOException ex) { }
        this.setVisible(false);
        this.dispose();
        Client.RiavviaClientErrore(messaggio);
    }

}
