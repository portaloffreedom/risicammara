
package risicammaraServer.messaggiManage;

/**
 * La sequenza di gioco
 * @author stengun
 */
public class MessaggioSequenzaGioco implements Messaggio{
    private Object[] sequenza;
    private int sender;

    public Object[] getSequenza() {
        return sequenza;
    }

    public MessaggioSequenzaGioco(Object[] sequenza, int sender) {
        this.sequenza = sequenza;
        this.sender = sender;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.SEQUENZAGIOCO;
    }

    @Override
    public int getSender() {
        return sender;
    }


}
