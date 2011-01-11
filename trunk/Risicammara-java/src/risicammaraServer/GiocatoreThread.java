/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.net.Socket;
import risicammarajava.Colore_t;
import risicammarajava.playerManage.Giocatore;

/**
 *
 * @author matteo
 */
public class GiocatoreThread extends Giocatore implements Runnable {

    private Socket comunicatore;
    
    public GiocatoreThread(Socket comunicatore){
        super(null, Colore_t.NULLO);
        this.comunicatore=comunicatore;
    }

    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
