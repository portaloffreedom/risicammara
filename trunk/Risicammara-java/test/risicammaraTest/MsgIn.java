package risicammaraTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraServer.messaggiManage.Messaggio;

/**
 * Parser thread per i messaggi in entrata. Smista i messaggi e li sistema
 * nell'apposita coda.
 * Effettua un controllo sul pacchetto ricevuto, se questo è valido viene
 * sistemato dentro la coda di uno specifico thread (invio diretto, gioco o server)
 * @author stengun
 */
class MsgIn extends Thread{
    
    private ConcurrentHashMap<Long,Player> connessi;
    private ArrayBlockingQueue<Messaggio> codasmp,codainvio,codagioco,codaincoming;
    private boolean stop;
    private int count;
    
    public MsgIn(ConcurrentHashMap<Long,Player> connessi,ArrayBlockingQueue<Messaggio> codaincoming) {
        this.connessi = connessi;
        this.codaincoming = codaincoming;
        this.stop = false;
        this.count = 0;
    }

    @Override
    public void run() {
        while(!stop){
            Messaggio tmp = null;
            try {
                tmp = codaincoming.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(MsgIn.class.getName()).log(Level.SEVERE, 
                        "Errore coda message parser", ex);
            }
            //Capita che un messaggio non venga preso per bene
            if(tmp == null){
                if(count == MsgIn.NUMTENTATIVI) stop = true;
                count++;
                continue;
            }
            else count = 0;
            parseMsg(tmp);
        }
    }
    
    
    
    public void setCodaSmp(ArrayBlockingQueue<Messaggio> coda){
        this.codasmp = coda;
    }

    public void setCodagioco(ArrayBlockingQueue<Messaggio> codagioco) {
        this.codagioco = codagioco;
    }

    public void setCodainvio(ArrayBlockingQueue<Messaggio> codainvio) {
        this.codainvio = codainvio;
    }
    // Metodi privati ---------------------------
    private void parseMsg(Messaggio msg){
        switch(msg.getType()){
            case CHAT:
                try {
                    codainvio.put(msg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MsgIn.class.getName()).log(Level.SEVERE, "Errore put.", ex);
                }
                break;
            case COMMAND:
                try {
                    codasmp.put(msg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MsgIn.class.getName()).log(Level.SEVERE, "errore put command", ex);
                }
                break;
            default:
                try {
                    codagioco.put(msg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MsgIn.class.getName()).log(Level.SEVERE, "Errore put gioco", ex);
                }
                break;
                
        }
    }
    
    
    // Metodi e variabili statiche ---------------------------
    public static final int NUMTENTATIVI = 5;
    
}
