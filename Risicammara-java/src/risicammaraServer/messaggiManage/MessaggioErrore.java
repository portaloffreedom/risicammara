//TODO implementare tutti gli errori di errori_t nella classe MessaggioErrore
package risicammaraServer.messaggiManage;

/**
 * Classe che implementa i costruttori per dei messaggi di tipo "Errore"
 * @author Sten_Gun
 */
public class MessaggioErrore implements Messaggio{
    private errori_t errore;
    private long sender;

    
    public static MessaggioErrore creaMsgServerFull(long sender)
    {
        return new MessaggioErrore(errori_t.SERVERFULL, sender);
    }
    /**
     * Messaggio che informa un errore di Connessione Rifiutata
     * @param sender da chi parte questo errore
     * @return l'oggetto MessaggioErrore corrispondente a CONNECTIONREFUSED
     */
    public static MessaggioErrore creaMsgConnectionRefused(long sender){
        return new MessaggioErrore(errori_t.CONNECTIONREFUSED, sender);
    }

    /**
     * Costruttore di messaggi di tipo Errore.
     * @param err tipo di errore
     * @param sender da chi parte l'errore
     */
    private MessaggioErrore(errori_t err,long sender){
        this.errore = err;
        this.sender = sender;
    }
/**
 * Ritorna il tipo di errore.
 * @return tipo di errore
 */
    public errori_t getError(){
        return this.errore;
    }
/**
 * Ritorna il tipo di messaggio (override dall'interfaccia Messaggio)
 * @return
 */
    @Override
    public messaggio_t getType() {
        return messaggio_t.ERROR;
    }
/**
 * Ritorna chi ha inviato il messaggio (override dall'interfaccia Messaggio)
 * @return chi invia il messaggio
 */
    @Override
    public long getSender() {
        return sender;
    }
}
