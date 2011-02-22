package PacchettoGrafico;

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioNonValido;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioArmateDisponibili;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioDichiaraAttacco;
import risicammaraServer.messaggiManage.MessaggioFase;
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

    private PannelloGioco pannello;

    public FinestraGioco(Connessione server, PartitaClient partita) {
        super("Risicammara");
        this.server = server;
        this.listaGiocatori = partita.getListaGiocatori();
        this.partita = partita;

        this.setIconImage(new ImageIcon("./risorse/risicamlogo.png").getImage());

        Container contestoFinestra = this.getContentPane();
        this.pannello = new PannelloGioco(240, partita);
        contestoFinestra.add(pannello);

        Rectangle rect = new Rectangle(50, 50, 900, 600);
        this.setBounds(rect);
        this.setMinimumSize(rect.getSize());
        
        this.plancia = pannello.getPlanciaImmagine();
        this.gestoreFasi = new GestoreFasi(pannello.getBarraFasi(),server, partita,plancia ,pannello.getAttivatoreGrafica());

        this.addWindowListener(new WindowListenerImpl(server));
        this.setVisible(true);
    }

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
                    if (gestoreFasi.getFaseCorrente() == ContatoreFasi.RINFORZO)
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
                        case TURNOFPLAYER:
                        if (msgComandi.getSender() == listaGiocatori.meStessoIndex())
                            gestoreFasi.avanzaFase();
                        else {
                            //TODO tocca al giocatore msgComandi.getSender()
                            System.out.println("tocca al giocatore "+msgComandi.getSender());
                        }
                        break;

                        case ATTACCOTERMINATO:
                            try {
                                territori_t attaccante = partita.getTerritorioAttaccante();
                                territori_t difensore  = partita.getTerritorioAttaccato();
                                plancia.ripristinaTerritorio(attaccante.getIdTerritorio());
                                plancia.aggiornaTerritorio(attaccante.getIdTerritorio());
                                plancia.ripristinaTerritorio(difensore.getIdTerritorio());
                                plancia.aggiornaTerritorio(difensore);
                            } catch (TerritorioNonValido ex) {
                                System.err.println("Non è riuscito a decolorare i territori:\n"+ex);
                            }
                            break;
                    }
                    break;
                }
                
                case ARMATEDISPONIBILI:
                    MessaggioArmateDisponibili msgMad = (MessaggioArmateDisponibili) msg;
                    gestoreFasi.setArmateRinforzoDisponibili(msgMad.getNumarm());
                    break;
                
                case CAMBIAARMATETERRITORIO:
                    MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio) msg;
                    plancia.aggiornaArmateTerritorio(msgArmate.getArmate(), msgArmate.getTerritorio());
                    if (gestoreFasi.getFaseCorrente() == ContatoreFasi.RINFORZO)
                        gestoreFasi.diminuisciArmateRinforzoDisponibili();
                    break;

                case FASE:
                    if (gestoreFasi.getFaseCorrente() == ContatoreFasi.RINFORZO)
                        gestoreFasi.avanzaFase();
                    break;

                case DICHIARAATTACCO:
                    MessaggioDichiaraAttacco msgAttacco = (MessaggioDichiaraAttacco) msg;
                    if (msgAttacco.getSender() == listaGiocatori.meStessoIndex())
                        break;
                    //TODO colora di rosso e di blu
                    territori_t attaccante = msgAttacco.getTerritorio_attaccante();
                    territori_t difensore  = msgAttacco.getTerritorio_difensore();
                    partita.setTerritorioAttaccante(attaccante);
                    partita.setTerritorioAttaccato(difensore);
                    try {
                        plancia.coloraSfumato(attaccante.getIdTerritorio(), AscoltatorePlanciaAttacco.Attacco, AscoltatorePlanciaAttacco.pesantezzaSfumatura);
                        plancia.aggiornaTerritorio(attaccante);
                        plancia.coloraSfumato(difensore.getIdTerritorio(), AscoltatorePlanciaAttacco.Difesa, AscoltatorePlanciaAttacco.pesantezzaSfumatura);
                        plancia.aggiornaTerritorio(difensore);
                    } catch (TerritorioNonValido ex) {
                        System.err.println("Non è riuscito a colorare i territori:\n"+ex);
                    }
                    break;

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

}
