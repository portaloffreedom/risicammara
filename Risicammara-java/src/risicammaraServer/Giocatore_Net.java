

package risicammaraServer;

import java.net.Socket;
import risicammarajava.Colore_t;
import risicammarajava.playerManage.Giocatore;

/**
 *
 * @author matteo
 */
public class Giocatore_Net extends Giocatore {

    private Socket comunicatore;
    private Thread thread_player;
    
    public Giocatore_Net(Socket comunicatore){
        super(null, Colore_t.NULLO);
        this.comunicatore=comunicatore;
        thread_player = null;
    }

    public Giocatore_Net(){
        this(null);
    }

    public void AssignThread(Thread t){
        this.thread_player = t;
    }
    public Thread getThread(){
        return thread_player;
    }
    public void setSocket(Socket sock){
        this.comunicatore = sock;
    }
    public Socket getSocket(){
        return comunicatore;
    }

    public void setNome(String nom){
        super.nome = nom;
    }

}