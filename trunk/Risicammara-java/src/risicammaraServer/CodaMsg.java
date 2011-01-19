/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.messaggio_t;

/**
 *  Rappresenta una semlpice coda di messaggi
 * @author Sten_Gun
 */
public class CodaMsg {
    private Queue<Messaggio> coda;
    private boolean occupato;

    public CodaMsg(){
        coda = new LinkedList<Messaggio>();
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
        coda.add(msg);
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
        notify();
        return ms;
    }
 
}
