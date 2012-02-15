package risicammara2.server;

import java.util.Iterator;
import risicammara2.global.MessaggioInvio;
import risicammara2.global.MessaggioLista;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammara2.global.MessaggioPlayer;
import risicammara2.global.Player;
import risicammaraClient.Colore_t;
import risicammaraServer.messaggiManage.*;

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
    private boolean stop,lobby;
    
    public ServMsgProc(ConcurrentHashMap<Long,Player> connessi, ArrayBlockingQueue<Messaggio> lettura) {
        this.connessi = connessi;
        this.codasmp = lettura;
        this.stop = false;
        this.lobby = true;
        
    }

    @Override
    public void run() 
    {
        Messaggio tmp = null;
        while(!stop){
            try {
                tmp = codasmp.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, "Error messaggio smp", ex);
                tmp = null;
                continue;
            }
            if(tmp.getType() == messaggio_t.MODIFICANICKCOLORE) {
                MessaggioCambiaNickColore mes = (MessaggioCambiaNickColore) tmp;
                Player gi = connessi.get(mes.getSender());
                gi.setNome(mes.getNick());
                gi.setColoreArmate(mes.getColore());
                try {
                    sendMsgToAll(tmp);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, "ERROR, nickcolore non inviato", ex);
                    tmp = null;
                    continue;
                }
            } else this.parseCommand((MessaggioComandi)tmp);
            
        }
    }
    
//Interfaccia per essere gestito da fuori.    
    public void setLeader(long who)
    {
        try {
            sendMsg(MessaggioComandi.creaMsgLeader(who), who);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
        try {
            //sendMsgToAll(new MessaggioChat(-1, connessi.get(who).getNome()+" è stato espulso"));
            sendMsgToAll(MessaggioComandi.creaMsgKickplayer(-1, who));
        } catch (InterruptedException ex) {
            Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void remPlayer(long who) 
    {
        connessi.remove(who);
        try {
            sendMsgToAll(MessaggioComandi.creaMsgDisconnect(who));
        } catch (InterruptedException ex) {
            Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
        }
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
//                try {
//                    //Chi invia la notifica di kick è il server.
//                    notifiche.put(new MsgNotify(comando.getOptParameter(), Notify.ESPELLI));
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
//                }
                kickedPlayer(comando.getOptParameter());
                break;
            case DISCONNECT:
                try {
                    notifiche.put(new MsgNotify(comando.getOptParameter(), Notify.DISCONNESSIONE));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case SETPRONTO:
                boolean start = true;
                if(lobby){
                    Iterator itera_connessi = connessi.values().iterator();
                    Threadplayer threadpl = connessi.get(comando.getSender()).getThread();
                    try {
                        threadpl.setReady(!threadpl.isReady());
                        sendMsgToAll(comando);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(connessi.size() < 3) break;
                    while(itera_connessi.hasNext()){
                        Player gioc = (Player)itera_connessi.next();
                        if(!gioc.getThread().isReady()){
                            start = false;
                            break;
                        }
                    }
                    if(start){
                        this.lobby = false;
                        try {
                            sendMsgToAll(MessaggioComandi.creaMsgAvviaPartita(-1));
                            notifiche.put(new MsgNotify(-1, Notify.AVVIA));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServMsgProc.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    void setStop(boolean b) {
        this.stop = b;
    }
}
