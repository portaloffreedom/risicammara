package risicammaraServer;

import java.util.LinkedList;
import java.util.Queue;
import risicammaraServer.messaggiManage.Messaggio;

/**
 *  Rappresenta una semplice coda di messaggi per permettere
 * ai thread di dialogare con il server. Questo oggetto è Sincronizzato
 * e può essere usato come monitor.
 * @author Sten_Gun
 */
public class CodaMsg {
    private Queue<Messaggio> coda;
    private boolean occupato;

    /**
     *Costruisce la coda e imposta i valori di default.
     */
    public CodaMsg(){
        coda = new LinkedList<Messaggio>();
        this.occupato = false;
    }

    /**
     * Aggiunge un messaggio alla coda
     * @param msg messaggio da aggiungere.
     */
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

    /**
     * Preleva un messaggio dalla coda.
     * @return Il messaggio prelevato
     */
    public synchronized Messaggio get(){
        if(occupato || coda.isEmpty()) try {
            wait();
        } catch (InterruptedException ex) {
            System.out.println("Thread terminato in modo anomalo (coda msg): "
                    +ex);
            System.exit(-1);
        }
        occupato = true;
        Messaggio ms = coda.poll();
        occupato = false;
        notify();
        return ms;
    }
 
}
