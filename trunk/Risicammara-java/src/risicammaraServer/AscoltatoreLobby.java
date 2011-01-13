/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import risicammaraServer.MessageManage.MessaggioNuovoGiocatore;
import risicammaraServer.MessageManage.Messaggio_Comandi;

/**
 *
 * @author matteo
 */
public class AscoltatoreLobby implements Runnable {
    private Thread server;
    private CodaMsg coda;

    private int porta;
    private ServerSocket ascoltatore;

    public AscoltatoreLobby(Thread server, int porta, CodaMsg coda) {
        this.server = server;
        this.porta = porta;
        this.coda = coda;
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
            coda.Send(new MessaggioNuovoGiocatore(giocatore));
        }
    }
}
