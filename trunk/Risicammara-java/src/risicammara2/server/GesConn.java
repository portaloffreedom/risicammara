package risicammara2.server;

import risicammara2.global.Player;
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
    private ConcurrentHashMap<Long,Player> collegati;
    private ConnectionListener ascoltatore;
    
    
    public GesConn(ConcurrentHashMap<Long,Player> collegati,ArrayBlockingQueue<MsgNotify> notifiche,int port)
    {
        this.collegati = collegati;
        this.stop = false;
        this.notifiche = notifiche;
        this.connessioni = new ArrayBlockingQueue<Socket>(Server_test.MAX_PLAYERS);
        this.ascoltatore = new ConnectionListener(connessioni,port);
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
            Player newplayer = ackPlayer(s);
            if( newplayer == null)
            {
                ascoltatore.setGiocatoriconnessi(collegati.size());
                continue;
            }
            collegati.put(newplayer.getThread().getId(), newplayer);
            try {
                notifiche.put(new MsgNotify(newplayer.getThread().getId(), Notify.CONNESSIONE));
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
    
    private Player ackPlayer(Socket s)
    {
        Player newplayer = new Player("giocatore "+connessioni.size());
        try {
            newplayer.setThread(new Threadplayer(s));
        } catch (IOException ex) {
            System.err.println("Errore nella creazione del thread giocatore.");
            return null;
        }
        try {
            newplayer.getThread().invia(MessaggioComandi.creaMSGconnected(newplayer.getThread().getId()));
        } catch (IOException ex) {
            System.err.println("Errore invio connessione");
            return null;
        }
        Messaggio tmpmsg = null;
        try {
            tmpmsg = newplayer.getThread().readMsg();
        } catch (IOException ex) {
            System.err.println("Errore ricezione HELO, aborting.");
            return null;
        }
        switch(tmpmsg.getType())
        {
            case HELO:
                Object[] dati = ((MessaggioHelo)tmpmsg).getData();
                //Player tmplayer = newplayer.getPlayer();
                newplayer.setNome((String)dati[0]);
                newplayer.getThread().setClientVersion((Double)dati[1]);
                if(!hasGoodVersion(newplayer.getThread()))
                {
                    try {                    
                        newplayer.getThread().invia(MessaggioErrore.creaMsgConnectionRefused(0));
                        newplayer.getThread().close();
                    } catch (IOException ex) {
                        System.err.println("Errore chiusura giocatore.");
                    }
                    return null;
                }
                newplayer.getThread().setName(newplayer.getNome());
                newplayer.setAvatar(null);
                return newplayer;
            default:
                System.out.println("Messaggio non riconosciuto per connessione, dropping");
                try {
                    newplayer.getThread().invia(MessaggioErrore.creaMsgConnectionRefused(0));
                    newplayer.getThread().close();
                } catch (IOException ex) {
                    System.err.println("Errore chiusura giocatore.");
                }
                return null;
        }
    }

    void setStop(boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
