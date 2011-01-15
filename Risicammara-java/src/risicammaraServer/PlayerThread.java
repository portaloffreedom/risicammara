/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.MessageManage.MessaggioComandi;
import risicammaraServer.MessageManage.comandi_t;

/**
 *
 * @author Sten_Gun
 */
public class PlayerThread extends Thread{
    private boolean stop;
    private ObjectInputStream playerInput;
    private int playerIndex;
    private CodaMsg coda;
    public PlayerThread(CodaMsg coda,ObjectInputStream playerInput,int playerIndex){
        this.coda = coda;
        this.playerIndex = playerIndex;
        this.playerInput = playerInput;
    }

    @Override
    public void run() {
        this.stop = false;
        
        while(!stop){
            try {
                coda.Send((Messaggio) this.playerInput.readObject());
            } catch (IOException ex) {
                System.err.println("Giocatore "+playerIndex+" non raggiungibile: "+ex.getMessage());
                stop = true;
                coda.Send(new MessaggioComandi(comandi_t.DISCONNECT, playerIndex));
            } catch (ClassNotFoundException ex) {
                System.err.println("Messaggio non riconosciuto: "+ex);
            }
        }
        System.out.println("Thread giocatore "+ playerIndex+" stoppato");
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    

}
