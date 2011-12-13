package risicammara2.global;

import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.messaggio_t;

/**
 * Invio oggetto Giocatore.
 * @author stengun
 */
public class MessaggioPlayer implements Messaggio{
    private long id;
    private Player gioc;

    public MessaggioPlayer(long id_player, Player gioc) {
        this.id = id_player;
        this.gioc = gioc;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.PLAYER;
    }

    @Override
    public long getSender() {
        return -1;
    }
    
    public long getId(){
        return id;
    }

    public Player getPlayer() {
        return gioc;
    }
    
}
