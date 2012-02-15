package risicammara2.server;

import risicammara2.global.MessaggioInvio;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammara2.global.Player;
import risicammaraServer.messaggiManage.Messaggio;

/**
 * Parser thread per i messaggi in entrata. Smista i messaggi in arrivo dai giocatori 
 * e li sistema nell'apposita coda per alleggerire il lavoro al thread SMP.
 * Effettua un controllo sul pacchetto ricevuto, se questo è valido viene
 * sistemato dentro la coda di uno specifico thread (invio diretto, gioco o server)
 * @author stengun
 */
class MsgIn extends Thread
{
    
    private ConcurrentHashMap<Long,Player> connessi;
    private ArrayBlockingQueue<Messaggio> codasmp,codagioco,codaincoming;
    private ArrayBlockingQueue<MessaggioInvio> codainvio;
    private boolean stop;
    private int count;
    
    public MsgIn(ConcurrentHashMap<Long,Player> connessi,ArrayBlockingQueue<Messaggio> codaincoming) 
    {
        this.connessi = connessi;
        this.codaincoming = codaincoming;
        this.stop = false;
        this.count = 0;
    }

    @Override
    public void run() 
    {
        while(!stop)
        {
            Messaggio tmp = null;
            try {
                tmp = codaincoming.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(MsgIn.class.getName()).log(Level.SEVERE, 
                        "Errore coda message parser", ex);
            }
            //Capita che un messaggio non venga preso per bene
            if(tmp == null)
            {
                if(count == MsgIn.NUMTENTATIVI) stop = true;
                count++;
                continue;
            }
            else count = 0;
            parseMsg(tmp);
        }
    }
    /**
     * Avvia il thread indicato. Se non esiste, non fa nulla.
     * @param threadid l'id del thread da avviare.
     */
    public void avviaThreadGiocatore(Long threadid)
    {
        Threadplayer tmpthr = connessi.get(threadid).getThread();
        if(tmpthr == null) return;
        tmpthr.setCodath(codaincoming);
        tmpthr.start();
    }
    
    public void setCodaSmp(ArrayBlockingQueue<Messaggio> coda)
    {
        this.codasmp = coda;
    }

    public void setCodagioco(ArrayBlockingQueue<Messaggio> codagioco) 
    {
        this.codagioco = codagioco;
    }

    public void setCodainvio(ArrayBlockingQueue<MessaggioInvio> codainvio) 
    {
        this.codainvio = codainvio;
    }
    // Metodi privati ---------------------------
    private void parseMsg(Messaggio msg)
    {
        switch(msg.getType()){
            case CHAT:
                try {
                    codainvio.put(new MessaggioInvio(msg, 0));
                } catch (InterruptedException ex) {
                    Logger.getLogger(MsgIn.class.getName()).log(Level.SEVERE, "Errore put.", ex);
                }
                break;
            case MODIFICANICKCOLORE:
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

    void setStop(boolean b) {
        this.stop = b;
    }
    
}
