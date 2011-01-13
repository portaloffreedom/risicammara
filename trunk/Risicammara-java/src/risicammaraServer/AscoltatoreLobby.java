/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import risicammaraServer.MessageManage.MessaggioNuovoGiocatore;

/**
 * Classe (implementata come Thread) che rimane in ascolto di nuove connessioni,
 * e nel caso le notifica al Thread Server che penserà a processarle.
 * @author matteo
 */
public class AscoltatoreLobby implements Runnable {
    /** Variabile che memorizza la coda che server per mandare messaggi al "Server" */
    private CodaMsg coda;

    /** Variabile intera che identifica la porta in cui l'Ascoltatore deve rimanere in ascolto */
    private int porta;
    /** Variabile che memorizza il ServerSocket che deve rimanere in ascolto */
    private ServerSocket ascoltatore;

    /**
     * Inizializza semplicemente i parametri necessari a fare partire un Socket
     * in ascolto a nuove connessioni.
     * @param porta porta su cui il server deve rimanere in ascolto
     * @param coda coda con cui l'ascoltatore comunica con il server (Thread)
     * principale
     */
    public AscoltatoreLobby(int porta, CodaMsg coda) {
        this.porta = porta;
        this.coda = coda;
    }

    /**
     * Fa partire il lavoro di ascolto di nuove connessioni. Implementazioni
     * dell'interfaccia Runnable per creare un nuovo Thread si cui questo è il
     * nuovo "main"
     */
    public void run() {
        try {
            this.ascoltatore = new ServerSocket(this.porta);
        } catch (IOException ex) {
            System.err.println("Impossibile aprire una connessione sulla porta: "+this.porta);
            System.err.println("Errore: "+ex.getStackTrace());
            System.exit(1);
        }


        try {
            this.ascolta();
        } catch (IOException ex) {
            System.err.println("Errore nell'ascoltare nuovi client: "+ex);
            System.exit(2);
        }
    }

    /**
     * Comincia ad ascoltare sul ServerSocket e notifica al Thread server ogni
     * volta che si connette un nuovo giocatore
     * <p>
     * Da Implelentare: Si blocca al compimento della 6 connessione fino a che
     * il server non si notifica una disconnessione o non vuole terminare il
     * Thread (quando inizia la partita)
     * @throws IOException lancia IOException nel caso in cui il ServerSocket
     * non riesca (più) ad ascoltare.
     */
    private void ascolta() throws IOException {
        Socket giocatore = null;

        while (true) {
            //TODO fermarsi quando coda piena → gestito dal'oggetto

            giocatore = ascoltatore.accept();
            coda.Send(new MessaggioNuovoGiocatore(giocatore));
            //TODO spedire messaggio nuovo giocatore
            //coda.send(new nuovoGiocatoreMessage(giocatore));
        }
    }
}
