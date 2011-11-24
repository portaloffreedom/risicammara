package risicammaraTest;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraServer.messaggiManage.Messaggio;

/**
 * Questa classe gestisce le connessioni in ingresso.
 * Si occupa principalmente dell'aggiornamento della struttura dati che contiene
 * tutti i giocatori connessi al server, sia spettatori che giocatori.
 * Questa classe esegue un altro thread che accetta tutte
 * le connessioni in entrata e viene controllato solo dal gestore connessioni.
 * Questo gestore connessione viene controllato dalla classe principale "Server".
 * Comunica con il server tramite una struttura dati "notifiche", attraverso la
 * quale invia i suoi messaggi e viene poi direttamente controllato dal server.
 * @author stengun
 */
class GesConn extends Thread{
    
    private boolean stop;
    private ArrayBlockingQueue<Player> connessioni;
    private ArrayBlockingQueue<Messaggio> codath;
    private ConcurrentHashMap<Long,Player> collegati;
    private ConnectionListener ascoltatore;
    private Player templayer;
    
    
    public GesConn(ConcurrentHashMap<Long,Player> collegati){
        this.templayer = null;
        this.collegati = collegati;
        this.stop = false;
        this.connessioni = new ArrayBlockingQueue<Player>(Server_test.MAX_PLAYERS);
        this.ascoltatore = new ConnectionListener(connessioni,Server_test.DEFAULT_PORT);
    }

    public void setCodath(ArrayBlockingQueue<Messaggio> codath) {
        this.codath = codath;
    }

    @Override
    public void run() {
        ascoltatore.start();
        while(!stop){
            try {
                templayer = connessioni.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(GesConn.class.getName()).log(Level.SEVERE, "Thread interrotto sulla ricezione", ex);
                stop = true;
                continue;
            }
            Threadplayer tmpth = null;
            try {
                tmpth = new Threadplayer(templayer,codath);
            } catch (IOException ex) {
                System.err.println("Thread non inizializzabile, IO error"+this.getName());
            } finally {
                if(tmpth == null) continue;
            }
            tmpth.start();
            if(hasGoodVersion(templayer) && !collegati.contains(templayer))
            {
                
                collegati.put(new Long(tmpth.getId()),templayer);
                continue;
            }
            tmpth.setStop(true);
        }
    }
    /**
     * Ferma il thread in modo sicuro.
     * @param stop true per impostare lo stop, false altrimenti.
     */
    public void stop(boolean stop){
        this.stop = stop;
    }
    /**
     * Gestisce la rimozione di un giocatore da parte del server.
     * @param thread_id id del thread del giocatore sulla hashmap
     */
    public void disconnessione(Long thread_id)
    {
        collegati.remove(thread_id);
        ascoltatore.setGiocatoriconnessi(collegati.size());
    }
    // PRIVATE METHODS
    
    private boolean hasGoodVersion(Player giocatore){
        if(giocatore.getVersion() >= Server_test.MIN_SUPPORTED_VERSION)
            return true;
        else 
            return false;
    }
}
