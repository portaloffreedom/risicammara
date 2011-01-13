/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.util.PriorityQueue;
import risicammaraServer.MessageManage.Messaggio;

/**
 *
 * @author Sten_Gun
 */
public class CodaMsg {
    private PriorityQueue<Messaggio> coda;
    private boolean occupato;

    public CodaMsg(){
        coda = new PriorityQueue<Messaggio>();
        this.occupato = false;
    }

    public synchronized void Send(Messaggio msg){
        if(occupato) try {
            wait();
        } catch (InterruptedException ex) {
            System.out.println("Thread terminato in modo anomalo: "+ex.getStackTrace());
            System.exit(-1);
        }
        occupato = true;
        coda.offer(msg);
        occupato = false;
        notifyAll();
    }

    public synchronized Messaggio get(){
        if(occupato || coda.isEmpty()) try {
            wait();
        } catch (InterruptedException ex) {
            System.out.println("Thread terminato in modo anomalo: "+ex.getStackTrace());
            System.exit(-1);
        }
        occupato = true;
        Messaggio ms = coda.poll();
        occupato = false;
        return ms;
    }
}
