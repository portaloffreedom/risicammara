package risicammara2.server;

import risicammara2.global.MessaggioInvio;
import risicammara2.global.MessaggioLista;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammara2.global.MessaggioPlayer;
import risicammara2.global.Player;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioChat;
import risicammaraServer.messaggiManage.MessaggioComandi;

/**
 * Questa classe processa tutti i messaggi comando diretti al server.
 * Si occupa delle azioni da intraprendere rispetto ai comandi ricevuti.
 * @author stengun
 */
class ServMsgProc extends Thread{
    private ArrayBlockingQueue<Messaggio> codasmp;
    private ArrayBlockingQueue<MessaggioInvio> codainvio;
    private ArrayBlockingQueue<MsgNotify> notifiche;
    private ConcurrentHashMap<Long,Player> connessi;
    private boolean stop;
    
    public ServMsgProc(ConcurrentHashMap<Long,Player> connessi, ArrayBlockingQueue<Messaggio> lettura) {
        this.connessi = connessi;
        this.codasmp = lettura;
        this.stop = false;
        
    }

    @Override
    public void run() 
    {
        MessaggioComandi tmp = null;
        while(!stop){
            try {
                tmp = (MessaggioComandi)codasmp.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, "Error messaggio smp", ex);
                tmp = null;
                continue;
            }
            this.parseCommand(tmp);
            
        }
    }
    
//Interfaccia per essere gestito da fuori.    

    public void newPlayer(long who) 
    {
        try {
            this.sendMsg(new MessaggioLista(connessi,0), who);
            this.sendMsgToAll(new MessaggioChat(0, connessi.get(who).getNome()+" è entrato in gioco"));
            this.sendMsgToAll(new MessaggioPlayer(who, connessi.get(who)));
        } catch (InterruptedException ex) {
            System.err.println("Errore SMP gestione connessione.");
        }
    }

    public void kickedPlayer(long who) 
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void remPlayer(long who) 
    {
        connessi.remove(who);
    }
    
    public void setCodainvio(ArrayBlockingQueue<MessaggioInvio> codainvio) 
    {
        this.codainvio = codainvio;
    }

    public void setCodaNotifiche(ArrayBlockingQueue<MsgNotify> notifiche) 
    {
        this.notifiche = notifiche;
    }

    //Metodi privati
    
    private void sendMsg(Messaggio msg,Long plto) throws InterruptedException
    {
        codainvio.put(new MessaggioInvio(msg, plto));
    }
    
    private void sendMsgToAll(Messaggio msg) throws InterruptedException
    {
        this.sendMsg(msg, new Long(0));
    }
    
    private void parseCommand(MessaggioComandi comando) 
    {
        switch(comando.getComando())
        {
            case AVVIAPARTITA:
                try {
                    //chi invia i messaggi "avvio partita in corso" è il server"
                    notifiche.put(new MsgNotify(0, Notify.AVVIA));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case KICKPLAYER:
                try {
                    //Chi invia la notifica di kick è il server.
                    notifiche.put(new MsgNotify(comando.getOptParameter(), Notify.ESPELLI));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case SETPRONTO:
            default:
                break;
        }
    }

    void setStop(boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
