package risicammara2.server;

import risicammara2.global.MessaggioInvio;
import java.util.concurrent.ArrayBlockingQueue;
import risicammaraServer.messaggiManage.Messaggio;

/**
 *
 * @author stengun
 */
class GesMatch extends Thread{
    private ArrayBlockingQueue<Messaggio> codagioco,codasmp;
    private ArrayBlockingQueue<MessaggioInvio> codainvio;
    private boolean stop,winner;
    
    public GesMatch(ArrayBlockingQueue<Messaggio> codagioco) 
    {
        this.codagioco = codagioco;
        this.stop = false;
        this.winner = false;
    }

    @Override
    public void run() {
        prepara_partita();
        while(!stop && !winner)
        {
            Messaggio msg = null;
            try {
                msg = codagioco.take();
            } catch (InterruptedException ex) {
                System.err.println("Error retrieving codagioco message: "+ex);
            } finally {
                if( msg == null ) continue;
            }
            
            
        }
    }
    
    

    public void setCodainvio(ArrayBlockingQueue<MessaggioInvio> codainvio) 
    {
        this.codainvio = codainvio;
    }

    public void setCodasmp(ArrayBlockingQueue<Messaggio> codasmp) 
    {
        this.codasmp = codasmp;
    }

    public void terminate() 
    {
        /*
         * Togli il lock da tutte le trutture bloccate
         * informa tutti i thread che stanno in ascolto sui lock
         * invia a tutti il messaggio di termine partita/ritorno alla sala d'attesa
         * chiudi questo thread.
         */
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void prepara_partita() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    
}
