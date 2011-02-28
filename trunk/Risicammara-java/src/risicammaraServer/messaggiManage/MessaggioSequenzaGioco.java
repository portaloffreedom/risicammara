
package risicammaraServer.messaggiManage;

/**
 * La sequenza di gioco
 * @author stengun
 */
public class MessaggioSequenzaGioco implements Messaggio{
    private Integer[] sequenza;
    private int sender;

    /**
     * Restituisce la sequenza di gioco.
     * @return la sequenza di gioco
     */
    public Integer[] getSequenza() {
        return sequenza;
    }

    /**
     * Crea un messaggio Sequenza di Gioco
     * @param sequenza la sequenza di gioco
     * @param sender chi invia la sequenza.
     */
    public MessaggioSequenzaGioco(Integer[] sequenza, int sender) {
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
