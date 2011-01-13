/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author matteo
 */
public class AscoltatoreLobby implements Runnable {
    private Thread server;
    private int porta;
    private ServerSocket ascoltatore;
    private int numeroConnessioni;
    private final int numeroMassimoConnessi = 6;

    public AscoltatoreLobby(Thread server, int porta) {
        this.server = server;
        this.porta = porta;
    }

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

    private void ascolta() throws IOException {
        Socket giocatore = null;

        while (true) {
            //TODO fermarsi quando coda piena → gestito dal'oggetto
            //giocatori.addgiocatore();

            giocatore = ascoltatore.accept();
            //TODO spedire messaggio nuovo giocatore
            //coda.send(new nuovoGiocatoreMessage(giocatore));
        }
    }
}
