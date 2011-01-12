

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
    
    public Giocatore_Net(Socket comunicatore){
        super(null, Colore_t.NULLO);
        this.comunicatore=comunicatore;
    }

    public Giocatore_Net(){
        this(null);
    }

    public void setSocket(Socket sock){
        this.comunicatore = sock;
    }
    public Socket getSocket(){
        return comunicatore;
    }

}
