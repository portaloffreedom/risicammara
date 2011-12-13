package risicammara2.global;

import java.util.concurrent.ConcurrentHashMap;
import risicammara2.global.Player;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.messaggio_t;

/**
 *
 * @author stengun
 */
public class MessaggioLista implements Messaggio {
    private long sender;
    private ConcurrentHashMap<Long,Player> connessi;
    public MessaggioLista(ConcurrentHashMap<Long, Player> connessi, long sender) {
        this.sender = sender;
        this.connessi = connessi;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.LISTA;
    }

    @Override
    public long getSender() {
        return sender;
    }
    public ConcurrentHashMap<Long,Player> getList()
    {
        return connessi;
    }
}
