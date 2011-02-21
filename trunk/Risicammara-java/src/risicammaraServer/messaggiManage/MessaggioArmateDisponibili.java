package risicammaraServer.messaggiManage;

/**
 * Messaggio che imposta le armate disponibili per il giocatore che lo riceve.
 * Può essere inviato soltanto dal server.
 * @author Sten_Gun
 */
public class MessaggioArmateDisponibili implements Messaggio {
    private int numarm;
    private int sender;

    public MessaggioArmateDisponibili(int numarm,int sender) {
        this.numarm = numarm;
        this.sender = sender;
    }

    public messaggio_t getType() {
        return messaggio_t.ARMATEDISPONIBILI;
    }

    public int getNumarm() {
        return numarm;
    }

    public int getSender() {
        return sender;
    }

}
