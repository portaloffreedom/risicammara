package risicammara2.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammara2.global.MessaggioInvio;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import risicammara2.global.Player;

/**
 *
 * @author stengun
 */
class Msgout extends Thread{
    private ConcurrentHashMap<Long,Player> connessi;
    private ArrayBlockingQueue<MessaggioInvio> coda;
    private boolean stop;
    public Msgout(ConcurrentHashMap<Long,Player> connessi,ArrayBlockingQueue<MessaggioInvio> coda) {
        this.connessi = connessi;
        this.coda = coda;
    }

    @Override
    public void run() {
        while(!stop){
            MessaggioInvio tmp = null;
            try {
                tmp = coda.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(Msgout.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(tmp == null) continue;
            if(tmp.getMsg() == null) continue;
            long dest = tmp.getDestinatario();
            if(dest == 0)
            {
                Iterator<Player> it = connessi.values().iterator();
                while(it.hasNext()) {
                    try {
                        it.next().getThread().invia(tmp.getMsg());
                    } catch (IOException ex) {
                        Logger.getLogger(Msgout.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else {
                try {
                    connessi.get(dest).getThread().invia(tmp.getMsg());
                } catch (IOException ex) {
                    Logger.getLogger(Msgout.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }

    void setStop(boolean b) {
        this.stop = b;
    }
    
    
    
}
