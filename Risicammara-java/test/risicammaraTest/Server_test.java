package risicammaraTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraServer.messaggiManage.Messaggio;

/**
 * Classe principale che controlla direttamente tutti i thread che gestiscono
 * la partita. Tutte le strutture dati e le costanti sono dentro questa classe.
 * @author stengun
 */
public class Server_test extends Thread
{
    /* Costanti statiche globali */
    static final int MAX_PLAYERS = 20;
    static final double MIN_SUPPORTED_VERSION = 0.1;
    static final int DEFAULT_PORT = 12345;
    /* Threads */
    private GesConn gestoreconn;
    private ServMsgProc smp;
    private MsgIn msgin;
    private Msgout msgout;
    private GesMatch gestorematch;
    /* strutture dati*/
    private ConcurrentHashMap<Long,Threadplayer> connessi;
    private ArrayBlockingQueue<MsgNotify> notifiche;
    private ArrayBlockingQueue<Messaggio> codasmp,codagioco,codaincoming;
    private ArrayBlockingQueue<MessaggioInvio> codainvio;

    /**
     * Inizializza tutti i thread e le strutture dati
     */
    public Server_test(){
        this.codaincoming = new ArrayBlockingQueue<Messaggio>(50,true);
        this.codainvio = new ArrayBlockingQueue<MessaggioInvio>(50, true);
        this.codagioco = new ArrayBlockingQueue<Messaggio>(50, true);
        this.codasmp = new ArrayBlockingQueue<Messaggio>(50, true);
        this.notifiche = new ArrayBlockingQueue<MsgNotify>(50, true);
        this.connessi = new ConcurrentHashMap<Long,Threadplayer>();
        this.gestoreconn = new GesConn(connessi,notifiche);
// Inizializza server message processor e imposta le sue strutture dati.
        this.smp = new ServMsgProc(connessi,codasmp);
        smp.setCodainvio(codainvio);
        smp.setCodaNotifiche(notifiche);
// Inizializza l'ascoltatore dei messaggi in arrivo dai connessi e imposta le strutture dati.
        this.msgin = new MsgIn(connessi,codaincoming);
        msgin.setCodaSmp(codasmp);
        msgin.setCodagioco(codagioco);
        msgin.setCodainvio(codainvio);
// Inizializza il thread di invio messaggi ai connessi e imposta le strutture dati.
        this.msgout = new Msgout(connessi,codainvio);
// Inizializza il gestore della partita e ne imposta le strutture dati.
        this.gestorematch = new GesMatch(codagioco);
        gestorematch.setCodainvio(codainvio);
        gestorematch.setCodasmp(codasmp);
    };
    /**
     * Avvia il thread del server
     */
    @Override
    public void run(){
        MsgNotify tmp = null;
        System.out.print("Inizializzazione...");
        this.smp.start();
        this.msgin.start();
        this.msgout.start();
        this.gestoreconn.start();
        System.out.print("Inizializzazione terminata");
        while(true){
            try {
                tmp = this.notifiche.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(Server_test.class.getName()).log(Level.SEVERE, "Deadlock notifiche", ex);
            }
            switch(tmp.getNotifica()){
                case BANNA:
                    break;
                case CONNESSIONE:
                    msgin.avviaThreadGiocatore(tmp.getSender());
                    smp.newPlayer(tmp.getSender());
                    break;
                case ESPELLI:
                    smp.kickedPlayer(tmp.getSender());
                    break;
                case DISCONNESSIONE:
                    smp.remPlayer(tmp.getSender());
                    break;
                case AVVIA:
                    this.gestorematch.start();
                    break;
                case TERMINA:
                    this.gestorematch.terminate();
                    break;
                default:
                    break;
            }
        }
    }
}
