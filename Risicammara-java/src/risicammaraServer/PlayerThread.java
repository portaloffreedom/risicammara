/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.MessageManage.MessaggioComandi;
import risicammaraServer.MessageManage.comandi_t;

/**
 *
 * @author Sten_Gun
 */
public class PlayerThread extends Thread{
    private boolean stop;
    private Socket player_socket;
    private int player_index;
    private CodaMsg coda;
    public PlayerThread(CodaMsg coda,Socket playerSocket,int player_index){
        this.coda = coda;
        this.player_index = player_index;
        this.player_socket = playerSocket;
    }

    @Override
    public void run() {
        this.stop = false;
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(player_socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Errore input thread Giocatore " + player_index+": "+ex.getMessage());
        }
        while(!stop){
            try {
                coda.Send((Messaggio)is.readObject());
            } catch (Exception ex) {
                System.err.println("Giocatore "+player_index+" non raggiungibile: "+ex.getMessage());
                stop = true;
                coda.Send(new MessaggioComandi(comandi_t.DISCONNECT, player_index));
            }
        }
        System.out.println("Thread giocatore "+ player_index+" stoppato");
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    

}
