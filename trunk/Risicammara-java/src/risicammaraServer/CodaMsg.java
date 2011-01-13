/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.util.Comparator;
import java.util.PriorityQueue;
import risicammaraServer.MessageManage.Messaggio;

/**
 *
 * @author Sten_Gun
 */
public class CodaMsg {
    private PriorityQueue<Messaggio> coda;
    private boolean occupato;
    private Comparator ordinatore;

    public CodaMsg(){
        coda = new PriorityQueue<Messaggio>();
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
    }

    public synchronized Messaggio get(){
        if(coda.isEmpty()) try {
            wait();
        } catch (InterruptedException ex) {
            System.out.println("Thread terminato in modo anomalo: "+ex.getStackTrace());
            System.exit(-1);
        }
        return coda.poll();
    }
}
