package PacchettoGrafico;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioFase;
import risicammaraServer.messaggiManage.comandi_t;

/**
 * Finestra della interfaccia di gioco.
 * @author matteo
 */
public class FinestraGioco extends JFrame implements Runnable {
    private Connessione server;
    private ListaGiocatoriClient listaGiocatori;
    private PlanciaClient plancia;
    private GestoreFasi gestoreFasi;

    private PannelloGioco pannello;

    public FinestraGioco(Connessione server, PartitaClient partita) {
        super("Risicammara");
        this.server = server;
        this.listaGiocatori = partita.getListaGiocatori();
        this.plancia = partita.getPlancia();

        this.setIconImage(new ImageIcon("./risorse/risicamlogo.png").getImage());

        Container contestoFinestra = this.getContentPane();
        this.pannello = new PannelloGioco(240, partita);
        contestoFinestra.add(pannello);

        Rectangle rect = new Rectangle(this.pannello.getDimensioniMinime());
        //rect.width+=20;
        //rect.height+=60;
        //contestoFinestra.setMinimumSize(rect.getSize());
        //contestoFinestra.setSize(rect.getSize());
        rect.setLocation(50, 50);
        this.setBounds(rect);
        //this.getContentPane().setMinimumSize(rect.getSize());
        //this.setMinimumSize(rect.getSize());

        this.gestoreFasi = new GestoreFasi(pannello.getBarraFasi());

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
                    return;  //è finita la prefase
                case COMMAND:
                    MessaggioComandi msgComandi = (MessaggioComandi) msg;
                    if (Client.DEBUG && (msgComandi.getComando() != comandi_t.TURNOFPLAYER)){
                        System.err.println("Errore si sincronizzazione col server");
                        Client.RiavviaClient();
                    }
                    if (msgComandi.getSender() == listaGiocatori.meStessoIndex()){
                        gestoreFasi.avanzaFase();
                        //TODO distribuisci le 3 armate
                    }
                    break;
                case CAMBIAARMATETERRITORIO:
                    MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio) msg;
                    //TODO inserisci armate nel territorio
                    break;
            }
        }
    }

    private void cicloPartita() throws IOException, ClassNotFoundException {
        Messaggio msg = null;
        while (true) {
            msg = server.ricevi();
            switch (msg.getType()) {
                default:
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
