package risicammaraTest;

import java.util.concurrent.ArrayBlockingQueue;
import risicammaraServer.messaggiManage.Messaggio;

/**
 *
 * @author stengun
 */
class GesMatch extends Thread{
    private ArrayBlockingQueue<Messaggio> codagioco,codasmp,codainvio;
    public GesMatch(ArrayBlockingQueue<Messaggio> codagioco) {
        this.codagioco = codagioco;
    }

    public void setCodainvio(ArrayBlockingQueue<Messaggio> codainvio) {
        this.codainvio = codainvio;
    }

    public void setCodasmp(ArrayBlockingQueue<Messaggio> codasmp) {
        this.codasmp = codasmp;
    }

    public void terminate() {
        /*
         * Togli il lock da tutte le trutture bloccate
         * informa tutti i thread che stanno in ascolto sui lock
         * invia a tutti il messaggio di termine partita/ritorno alla sala d'attesa
         * chiudi questo thread.
         */
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    
}
