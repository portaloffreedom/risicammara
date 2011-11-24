package risicammaraServer.messaggiManage;

/**
 * Messaggio che imposta le armate disponibili per il giocatore che lo riceve.
 * Può essere inviato soltanto dal server.
 * @author Sten_Gun
 */
public class MessaggioArmateDisponibili implements Messaggio {
    private int numarm;
    private long sender;

    /**
     * Costruisce un messaggio ArmateDisponibili
     * @param numarm numero di armate disponibili
     * @param sender chi invia il messaggio
     */
    public MessaggioArmateDisponibili(int numarm,long sender) {
        this.numarm = numarm;
        this.sender = sender;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.ARMATEDISPONIBILI;
    }

    /**
     * Restituisce il numero di armate disponibili
     * @return il numero di armate disponibili
     */
    public int getNumarm() {
        return numarm;
    }


    @Override
    public long getSender() {
        return sender;
    }

}
