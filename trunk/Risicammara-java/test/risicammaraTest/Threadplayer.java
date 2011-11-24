package risicammaraTest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import risicammaraServer.messaggiManage.Messaggio;

/**
 *
 * @author stengun
 */
class Threadplayer extends Thread
{
    private boolean stop,pronto,leader;
    private ObjectInputStream ois;
    private ArrayBlockingQueue<Messaggio> codath;

    public Threadplayer(Player pl,ArrayBlockingQueue<Messaggio> codath) throws IOException {
        this.stop = false;
        this.pronto = false;
        this.leader = false;
        this.codath = codath;
        this.ois = new ObjectInputStream(pl.getTcpInputStream());
    }
    
    @Override
    public void run()
    {
        while(!stop)
        {
            Messaggio msg = this.readMsg();
            if(msg == null) continue;
            try {
                codath.put(msg);
            } catch (InterruptedException ex) {
                System.err.println("Thread "+this.getName()+" Interrotto.");
                continue;
            }
        }
    }
    /**
     * Ferma in modo sicuro il Thread.
     * @param b true per fermare il thread.
     */
    public void setStop(boolean b) 
    {
        this.stop = b;
    }
    /**
     * Legge un messaggio dall'inputStream e ritorna l'oggetto ricevuto.
     * In caso di errore IO o classe non trovata restituisce un oggetto null.
     * @return Oggetto Messaggio. Null se avviene un errore su stream/obj.
     */
    private Messaggio readMsg()
    {
        try {
            return (Messaggio)ois.readObject();
        } catch (IOException ex) {
            System.err.println("Errore IO su thread"+this.getName()+"\n"+ex);
            return null;
        } catch (ClassNotFoundException ex) {
            System.err.println("Classe non trovata su"+this.getName()+"\n"+ex);
            return null;
        }
    }
    
}
