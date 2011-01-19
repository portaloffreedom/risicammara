/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.messaggiManage.*;

/**
 *
 * @author matteo
 */
public class SalaAttesa extends JFrame implements Runnable {
    
    /** Dimensioni iniziali e posizione della finestra */
    final static Rectangle finestraR = new Rectangle(100, 100, 600, 305);
    
    // variabili locali per il funzionamento interno
    private Socket server;
    private ObjectOutputStream scriviServer;
    private ObjectInputStream  leggiServer;
    /** Variabile che serve per decidere quando deve smettere la sala d'attesa e
     * cominciare la partita */
    private boolean lobby;
    PannelloSalaAttesa pannello;

    //roba ricevuta dal sever
    /** Indice del Giocatore che sta utilizzando l'attuale Client */
    int indexGiocatore;
    /** Intera lista dei giocatori */
    ListaPlayers listaGiocatori;

    public SalaAttesa(Socket server) {
        super("Sala d'Attesa");
        this.server=server;
        //this.leader=leader;
        this.lobby = true; //imposta lo "stato" come lobby

        this.indexGiocatore = -2;
        this.listaGiocatori = null;


        try {
            creaConnessione();
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
        this.setBounds(finestraR);
        this.setResizable(false);  //TODO implementare coi pannelli invece che hard coding in modo che sia resizable

        this.pannello = new PannelloSalaAttesa(indexGiocatore, listaGiocatori, this);
        this.getContentPane().add(pannello);
    }

    public void run() {
        this.setVisible(true);
        
        Messaggio arrivo = null;

        while (lobby) {
            try {
                arrivo = (Messaggio) leggiServer.readObject();
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

                case COMMAND:
                    MessaggioComandi msgComando = (MessaggioComandi) arrivo;
                    switch (msgComando.getComando()) {
                        case DISCONNECT:
                            int indexRimozione = msgComando.getSender();
                            String nomeGiocatore = listaGiocatori.getNomeByIndex(indexRimozione);
                            listaGiocatori.remPlayer(indexRimozione);
                            pannello.giocatoreVisible(indexRimozione, false);
                            pannello.stampaMessaggioComando("Giocatore \""+nomeGiocatore+"\" uscito.");
                            break;

                        case LEADER:
                            diventaLeader();
                            break;

                        case KICKPLAYER:
                            pannello.stampaMessaggio("Giocatore \""+listaGiocatori.getNomeByIndex(msgComando.getReceiver())+
                                    "\" è stato kickato da \""+listaGiocatori.getNomeByIndex(msgComando.getSender())+"\"");
                            break;

                        default:
                            System.err.println("Comando non riconosciuto: "+msgComando.getComando());
                            break;
                    }
                    break;
                default:
                    System.err.println("Messaggio ignorato (il programma potrebbe non funzionare più bene)\n"
                                      +"Il messaggio ignorato era "+arrivo.getType()+":\""+arrivo+"\"");
                    break;
            }

        }
    }

    private void creaConnessione() throws IOException, ClassNotFoundException {
        if (server.isInputShutdown()) {
            System.err.println("Il server non permette di leggere da lui");
        }
        if (server.isOutputShutdown()) {
            System.err.println("Il server non permette di scrivere su di lui");
        }

        this.scriviServer = new ObjectOutputStream(new BufferedOutputStream(this.server.getOutputStream()));
        this.scriviServer.flush();
        this.leggiServer  = new ObjectInputStream(new BufferedInputStream(this.server.getInputStream()));
        MessaggioConfermaNuovoGiocatore msg = (MessaggioConfermaNuovoGiocatore) leggiServer.readObject();
        this.indexGiocatore = msg.getPlyIndex();
        this.listaGiocatori = msg.getPlyList();
    }

    /** Funzione da chiamare per fare diventare leader il giocatore */
    private void diventaLeader() {
        //this.leader = true;
        pannello.diventaLeader();
    }

    public void mandaMessaggio (Messaggio messaggio) throws IOException {
        scriviServer.writeObject(messaggio);
        scriviServer.flush();
    }

    public void chiudiServer () throws IOException {
        scriviServer.flush();
        scriviServer.close();
        server.close();
    }

    public void eliminaGiocatore(int index) {
        listaGiocatori.remPlayer(index);
        pannello.eliminaGiocatore(index);
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
            //try {
            //    new ObjectOutputStream(server.getOutputStream()).writeObject(new MessaggioComandi(comandi_t.DISCONNECT, indexGiocatore));
            //} catch (IOException ex) {
            //    System.err.println("Errore nel mandare il messaggio di \"hang-up\": "+ex);
            //}
            try {
                salaAttesa.chiudiServer();
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
