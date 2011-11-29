package risicammaraTest;

import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.messaggio_t;

/**
 * Messaggio che sta dentro la coda delle notifiche per il Server.
 * La coda delle notifiche informa il server sulle azioni da intraprendere
 * verso gli altri processi in base agli eventi.
 * @author stengun
 */
public class MsgNotify implements Messaggio {
    
    private long sender;
    private Notify notifica;
    private Object dato;

    public MsgNotify(long sender,Notify notifica) {
        this.sender = sender;
        this.notifica = notifica;
        this.dato = null;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.GIOCO;
    }

    @Override
    public long getSender() {
        return this.sender;
    }

    public Notify getNotifica() {
        return notifica;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }
    
    
    
}