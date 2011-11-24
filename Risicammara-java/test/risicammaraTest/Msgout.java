/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package risicammaraTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import risicammaraServer.messaggiManage.Messaggio;

/**
 *
 * @author stengun
 */
class Msgout extends Thread{
    private ConcurrentHashMap<Long,Player> connessi;
    private ArrayBlockingQueue<Messaggio> coda;
    
    public Msgout(ConcurrentHashMap<Long,Player> connessi,ArrayBlockingQueue<Messaggio> coda) {
        this.connessi = connessi;
        this.coda = coda;
    }

    @Override
    public void run() {
        
    }
    
    
    
}
