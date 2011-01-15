

package risicammaraServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.Giocatore;

/**
 *
 * @author matteo
 */
public class Giocatore_Net extends Giocatore {

    transient private Socket comunicatore;
    transient private ObjectOutputStream clientOut;
    transient private ObjectInputStream  clientIn;
    transient private Thread thread_player;
    
    public Giocatore_Net(Socket comunicatore){
        super(null, Colore_t.NULLO);
        this.comunicatore = comunicatore;
        thread_player = null;

        try {    
            this.inizializzaStream();
        } catch (IOException ex) {
            System.err.println("Errore nell'apertura delle comunicazioni con il nuovo giocatore_net creato"+ex.getLocalizedMessage());
        }
    }

    private synchronized void inizializzaStream() throws IOException {
            this.clientOut = new ObjectOutputStream(new BufferedOutputStream(comunicatore.getOutputStream()));
            this.clientOut.flush();
            this.clientIn = new ObjectInputStream(new BufferedInputStream(comunicatore.getInputStream()));
    }

    @Deprecated
    public Giocatore_Net(){
        this(null);
    }

    public ObjectInputStream getClientIn() {
        return clientIn;
    }

    public ObjectOutputStream getClientOut() {
        return clientOut;
    }

    public void AssignThread(Thread t){
        this.thread_player = t;
    }
    public Thread getThread(){
        return thread_player;
    }

    @Deprecated
    public void setSocket(Socket sock){
        this.comunicatore = sock;
    }

    @Deprecated
    public Socket getSocket(){
        return comunicatore;
    }

    public void closeSocket() throws IOException{
        this.clientIn.close();
        this.clientOut.close();
        this.comunicatore.close();
    }

}
