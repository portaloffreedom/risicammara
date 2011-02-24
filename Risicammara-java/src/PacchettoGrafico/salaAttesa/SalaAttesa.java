package PacchettoGrafico.salaAttesa;

import risicammaraJava.playerManage.ListaGiocatoriClient;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraClient.Obbiettivi_t;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.*;

/**
 *
 * @author matteo
 */
public class SalaAttesa extends JFrame implements Runnable {
    
    /** Dimensioni iniziali e posizione della finestra */
    final static Rectangle finestraR = new Rectangle(100, 100, 600, 305);
    
    // variabili locali per il funzionamento interno
    public Connessione server;
    /** Riferimento al Client: unico scopo fare partire la fase successiva di gioco */
    private Client meStesso;

    PannelloSalaAttesa pannello;

    //roba ricevuta dal sever
    /** Indice del Giocatore che sta utilizzando l'attuale Client */
    int indexGiocatore;
    /** Intera lista dei giocatori */
    ListaPlayers listaGiocatori;

    public SalaAttesa(Socket server, Client meStesso) {
        super("Sala d'Attesa");
        this.server = null;
        this.meStesso = meStesso;
        ImageIcon icona = new ImageIcon(this.getClass().getResource(Client.RISICAMLOGO));
        this.setIconImage(icona.getImage());

        this.indexGiocatore = -2;
        this.listaGiocatori = null;


        try {
            this.server = creaConnessione(server);
        } catch (IOException ex) {
            System.err.println("Il sistema non è riuscito ad aprire gli stream"
                    + "di output o di input dal server oppure non è riuscito a"
                    + "ricevere il messaggio dal server. Errore: "+ex);
            System.exit(3);
        } catch (ClassNotFoundException ex) {
            System.err.println("Errore di lettura nel messaggio di conferma del"
                    + " server: "+ex);
            System.exit(4);
        }


        this.addWindowListener(new WindowListenerSalaAttesa(this));
        this.setMinimumSize(finestraR.getSize());
        super.setMinimumSize(finestraR.getSize());
        super.setLocationByPlatform(true);
        //this.setBounds(finestraR);
        //this.setResizable(false);

        this.pannello = new PannelloSalaAttesa(indexGiocatore, listaGiocatori, this);
        this.getContentPane().add(pannello);
    }

    @Override
    public void run() {
        this.setVisible(true);
        Messaggio arrivo = null;

        while (true) {
            try {
                arrivo = server.ricevi();
            } catch (IOException ex) {
                System.err.println("Errore! Client disconnesso :"+ex);
                System.err.println(ex.getStackTrace());
                System.exit(15);
            } catch (ClassNotFoundException ex) {
                System.err.println("Attenzione! messaggio arrivato irriconoscibile: "+ex);
            }

            System.out.println("Nuovo messaggio arrivato: "+arrivo);

            switch (arrivo.getType()) {
                case CHAT:
                    MessaggioChat msgChat = (MessaggioChat) arrivo;
                    pannello.stampaMessaggio(msgChat.toString(listaGiocatori));
                    System.out.println("Messaggio Chat| "+msgChat.toString(listaGiocatori));
                    break;

                case AGGIORNADATIGIOCATORE:
                    MessaggioAggiornaDatiGiocatore msgUpdateGioct = (MessaggioAggiornaDatiGiocatore) arrivo;
                    Giocatore gioctAggiornato = listaGiocatori.get(msgUpdateGioct.getWho());

                    String messaggio = "Il Giocatore \""+gioctAggiornato.getNome()+"\" ha cambiato ";
                    boolean nomeCambiato = false;
                    boolean coloreCambiato = false;
                    if (!gioctAggiornato.getNome().equals(msgUpdateGioct.getNick()))         nomeCambiato = true;
                    if (!gioctAggiornato.getArmyColour().equals(msgUpdateGioct.getColor()))  coloreCambiato = true;

                    if (!coloreCambiato && !nomeCambiato){
                        break;
                    }
                    if (nomeCambiato) {
                        messaggio = messaggio+"il nome in \""+msgUpdateGioct.getNick()+'"';
                    }
                    if (nomeCambiato && coloreCambiato) {
                        messaggio = messaggio+" e ";
                    }
                    if (coloreCambiato) {
                        messaggio = messaggio+"colore in "+msgUpdateGioct.getColor();
                    }
                    messaggio= messaggio+".";
                    
                    pannello.stampaMessaggioComando(messaggio);

                    //if (gioctrAggiornato.equals(lobby))
                    pannello.setInfoGiocatore(msgUpdateGioct.getWho(), msgUpdateGioct.getNick(), msgUpdateGioct.getColor());
                    break;

                case AGGIUNGIGIOCATORE:
                    MessaggioAddPlayer msgAddPlayer = (MessaggioAddPlayer) arrivo;
                    Giocatore nuovoGiocatore = msgAddPlayer.getPlayer();
                    int indexNewG = listaGiocatori.addPlayer(nuovoGiocatore);
                    pannello.setInfoGiocatore(indexNewG, nuovoGiocatore.getNome(), nuovoGiocatore.getArmyColour());
                    pannello.giocatoreVisible(indexNewG, true);
                    pannello.stampaMessaggioComando("Entrato nuovo Giocatore.");
                    break;

                case COMMAND: {
                    MessaggioComandi msgComando = (MessaggioComandi) arrivo;
                    switch (msgComando.getComando()) {
                        case DISCONNECT:
                            int indexRimozione = msgComando.getSender();
                            String nomeGiocatore = listaGiocatori.getNomeByIndex(indexRimozione);
                            pannello.giocatoreVisible(indexRimozione, false);
                            listaGiocatori.remPlayer(indexRimozione);
                            pannello.stampaMessaggioComando("Giocatore \""+nomeGiocatore+"\" uscito.");
                            break;

                        case LEADER:
                            diventaLeader();
                            break;

                        case KICKPLAYER:
                            pannello.stampaMessaggio("Giocatore \""+listaGiocatori.getNomeByIndex(msgComando.getOptParameter())+
                                    "\" è stato kickato da \""+listaGiocatori.getNomeByIndex(msgComando.getSender())+"\"");
                            break;

                        case SETPRONTO:
                            pannello.invertiPronto(msgComando.getSender());
                            break;

                        case AVVIAPARTITA:
                            pannello.stampaMessaggioComando("Partita Avviata!");
                            this.avviaPartita();
                            return; //termina il thread

                        default:
                            System.err.println("Comando non riconosciuto: "+msgComando.getComando());
                            break;
                    }
                    }
                    break;
                default:
                    System.err.println("Messaggio ignorato (il programma potrebbe non funzionare più bene)\n"
                                      +"Il messaggio ignorato era "+arrivo.getType()+":\""+arrivo+"\"");
                    break;
            }

        }
    }

    private Connessione creaConnessione(Socket server) throws IOException, ClassNotFoundException {
        /*
        if (server.isInputShutdown()) {
            System.err.println("Il server non permette di leggere da lui");
        }
        if (server.isOutputShutdown()) {
            System.err.println("Il server non permette di scrivere su di lui");
        }

        this.scriviServer = new ObjectOutputStream(new BufferedOutputStream(this.server.getOutputStream()));
        this.scriviServer.flush();
        this.leggiServer  = new ObjectInputStream(new BufferedInputStream(this.server.getInputStream()));
         */
        Connessione connessioneServer = new Connessione(server);
        MessaggioConfermaNuovoGiocatore msg = (MessaggioConfermaNuovoGiocatore) connessioneServer.ricevi();
        this.indexGiocatore = msg.getPlyIndex();
        this.listaGiocatori = msg.getPlyList();
        return connessioneServer;
    }
    
    private void avviaPartita(){
        this.pannello.stampaMessaggioComando("Caricamento Partita in corso...");
        meStesso.inizializzaPartita(server, listaGiocatori, indexGiocatore);
        this.pannello.stampaMessaggioComando("Partita Caricata!");

        //libera la memoria della finestra di SalaAttesa
        this.setVisible(false);
        super.dispose();
    }

    /** Funzione da chiamare per fare diventare leader il giocatore */
    private void diventaLeader() {
        //this.leader = true;
        pannello.diventaLeader();
    }

    public void eliminaGiocatore(int index) {
        listaGiocatori.remPlayer(index);
        pannello.eliminaGiocatore(index);
    }

    public boolean pronto(int indexGiocatore) {
        return pannello.pronto(indexGiocatore);
    }

    public void invertiPronto(int indexGiocatore){
        pannello.invertiPronto(indexGiocatore);
    }

    // <editor-fold defaultstate="collapsed" desc="WindowListener">
    private static class WindowListenerSalaAttesa implements WindowListener {

        private SalaAttesa salaAttesa;

        public WindowListenerSalaAttesa(SalaAttesa salaAttesa) {
            this.salaAttesa = salaAttesa;
        }

        public void windowOpened(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void windowClosing(WindowEvent e) {
            try {
                salaAttesa.server.chiudiConnessione();
            } catch (IOException ex) {
                System.err.println("Errore nel chiudere il collegamento col server: " + ex);
            }
            System.exit(0);
        }

        public void windowClosed(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void windowIconified(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void windowDeiconified(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void windowActivated(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void windowDeactivated(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }// </editor-fold>
}
