package risicammaraTest;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioErrore;
import risicammaraServer.messaggiManage.MessaggioHelo;

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
class GesConn extends Thread
{
    
    private boolean stop;
    private ArrayBlockingQueue<Socket> connessioni;
    private ArrayBlockingQueue<MsgNotify> notifiche;
    private ConcurrentHashMap<Long,Threadplayer> collegati;
    private ConnectionListener ascoltatore;
    
    
    public GesConn(ConcurrentHashMap<Long,Threadplayer> collegati,ArrayBlockingQueue<MsgNotify> notifiche)
    {
        this.collegati = collegati;
        this.stop = false;
        this.notifiche = notifiche;
        this.connessioni = new ArrayBlockingQueue<Socket>(Server_test.MAX_PLAYERS);
        this.ascoltatore = new ConnectionListener(connessioni,Server_test.DEFAULT_PORT);
    }

    @Override
    public void run()
    {
        ascoltatore.start();
        while(!stop){
            Socket s = null;
            try {
                s = connessioni.take();
            } catch (InterruptedException ex) {
                System.err.println("Errore prelievo socket dalla coda, Interrupted exception");
                continue;
            }
            Threadplayer newplayer = null;
            if(!ackPlayer(s, newplayer))
            {
                ascoltatore.setGiocatoriconnessi(collegati.size());
                continue;
            }
            collegati.put(newplayer.getId(), newplayer);
            try {
                notifiche.put(new MsgNotify(newplayer.getId(), Notify.CONNESSIONE));
            } catch (InterruptedException ex) {
                System.err.println("WARNING! Errore notifica su server per connessione");
                continue;
            }
        }
    }
    /**
     * Ferma il thread in modo sicuro.
     * @param stop true per impostare lo stop, false altrimenti.
     */
    public void stop(boolean stop)
    {
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
    
    private boolean hasGoodVersion(Threadplayer thplayer)
    {
        if(thplayer.getVersion() >= Server_test.MIN_SUPPORTED_VERSION)
            return true;
        else 
            return false;
    }
    
    private boolean ackPlayer(Socket s, Threadplayer newplayer)
    {
        try {
            newplayer = new Threadplayer(new Player("giocatore "+connessioni.size()), s);
        } catch (IOException ex) {
            System.err.println("Errore nella creazione del thread giocatore.");
            return false;
        }
        Messaggio tmpmsg = null;
        try {
            tmpmsg = newplayer.readMsg();
        } catch (IOException ex) {
            System.err.println("Errore ricezione HELO, aborting.");
            return false;
        }
        switch(tmpmsg.getType())
        {
            case HELO:
                Object[] dati = ((MessaggioHelo)tmpmsg).getData();
                if(!hasGoodVersion(newplayer))
                {
                    try {                    
                        newplayer.invia(MessaggioErrore.creaMsgConnectionRefused(0));
                        newplayer.close();
                    } catch (IOException ex) {
                        System.err.println("Errore chiusura giocatore.");
                    }
                    return false;
                }
                Player tmplayer = newplayer.getPlayer();
                tmplayer.setNome((String)dati[0]);
                newplayer.setClientVersion((Double)dati[1]);
                try {
                    newplayer.invia(MessaggioComandi.creaMSGconnected(newplayer.getId()));
                } catch (IOException ex) {
                    System.err.println("Errore invio connessione");
                    return false;
                }
                return true;
            default:
                System.out.println("Messaggio non riconosciuto per connessione, dropping");
                try {
                    newplayer.invia(MessaggioErrore.creaMsgConnectionRefused(0));
                    newplayer.close();
                } catch (IOException ex) {
                    System.err.println("Errore chiusura giocatore.");
                }
                return false;
        }
    }
}
