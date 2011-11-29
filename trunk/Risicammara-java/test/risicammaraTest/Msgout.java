package risicammaraTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author stengun
 */
class Msgout extends Thread{
    private ConcurrentHashMap<Long,Threadplayer> connessi;
    private ArrayBlockingQueue<MessaggioInvio> coda;
    
    public Msgout(ConcurrentHashMap<Long,Threadplayer> connessi,ArrayBlockingQueue<MessaggioInvio> coda) {
        this.connessi = connessi;
        this.coda = coda;
    }

    @Override
    public void run() {
        
    }
    
    
    
}
