package risicammara2.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioChat;

/**
 * Classe che implementa il Thread associato a un preciso giocatore.
 * Il thread corrispondente ascolta messaggi in entrata dal giocatore e li mette su
 * una coda per il loro parsing.
 * @author stengun
 */
public class Threadplayer extends Thread
{
    private boolean stop,pronto,leader,valid;
    private ObjectInputStream ois;
    private ObjectOutputStream obos;
    private ArrayBlockingQueue<Messaggio> codath;
    //private Player giocatore;
    //Identificazione del client
    private Socket tcpsocket;
    private DatagramSocket udpsocket;
    private double clientVersion;
    //private Os_type sisoperativo; // Questo è per la versione android.

    public Threadplayer(/*Player pl,*/Socket tcpsock) throws IOException {
        this.stop = false;
        this.pronto = false;
        this.leader = false;
        this.codath = null;
        //this.giocatore = pl;
        
        this.tcpsocket = tcpsock;
        this.udpsocket = new DatagramSocket(tcpsock.getRemoteSocketAddress());
        this.ois = new ObjectInputStream(tcpsock.getInputStream());
        this.obos = new ObjectOutputStream(tcpsock.getOutputStream());
    }

    /**
     * Imposta la coda dove mettere i messaggi ricevuti dal giocatore.
     * @param codath struttura che accoglie i messaggi
     */
    public void setCodath(ArrayBlockingQueue<Messaggio> codath) {
        if(codath != null) this.codath = codath;
    }
    
    //Metodi di servizio per impostare campi tecnici del giocatore.
    public void setClientVersion(double clientVersion) {
        this.clientVersion = clientVersion;
    }
    @Override
    public void run()
    {
        while(!stop)
        {
            if(codath == null)
            {
                System.err.println("Coda nulla su thread "+this.getName());
                this.stop = true;
                continue;
            }
            Messaggio msg = null;
            try {
                msg = this.readMsg();
            } catch (IOException ex) {
                System.err.println("Errore nella lettura da socket: "+ex
                        +"\n su thread "+this.getName());
                this.stop = true;
                continue;
            }
            if(msg == null){
                continue;
            }
            try {
                codath.put(msg);
            } catch (InterruptedException ex) {
                System.err.println("Thread "+this.getName()+" Interrotto.");
                continue;
            }
        }
    }
 
    /**
     * Restituisce il giocatore associato al thread.
     * @return 
     */
    /*public Player getPlayer()
    {
        return giocatore;
    }*/
    
    /**
     * Ferma in modo sicuro il Thread.
     * @param b true per fermare il thread.
     */
    public void setStop(boolean b) 
    {
        this.stop = b;
        try {
            this.close();
        } catch (IOException ex) {
            System.err.println("Errore setting stop."+ex);
        }
        
    }
    /**
     * Legge un messaggio dall'inputStream e ritorna l'oggetto ricevuto.
     * In caso di errore IO o classe non trovata restituisce un oggetto null.
     * @return Oggetto Messaggio. Null se avviene un errore su stream/obj.
     */
    public Messaggio readMsg() throws IOException
    {
        try {
        return (Messaggio)ois.readObject();

        } catch (ClassNotFoundException ex) {
            System.err.println("Classe non trovata su"+this.getName()+"\n"+ex);
            return null;
        }
    }
        
    public void invia(String chat) throws IOException{
        this.invia(new MessaggioChat(this.getId(), chat));
    }
    
    public void invia(Messaggio msg) throws IOException{
        obos.writeObject(msg);
    }
    
    public void close() throws IOException 
    {
        tcpsocket.close();
        udpsocket.close();
    }

    public final double getVersion() {
        return this.clientVersion;
    }
    
//    public final String getPlayerName()
//    {
//        return giocatore.getNome();
//    }
    
}
