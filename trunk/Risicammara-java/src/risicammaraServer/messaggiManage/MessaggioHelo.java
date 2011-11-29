package risicammaraServer.messaggiManage;

/**
 * Messaggio di connessione preliminare.
 * @author stengun
 */
public class MessaggioHelo implements Messaggio
{
    private String[] data;
    private long sender;
    private messaggio_t type;

    public MessaggioHelo(long sender, messaggio_t type) {
        this.sender = sender;
        this.type = type;
        this.data = null;
    }
    /**
     * 
     * @return 
     */
    public String[] getData() {
        return data;
    }
    /**
     * Imopsta il campo dati del messaggio di HELO.
     * La convenzione da usare è la seguente:
     * data[0] = nickname, formato String;
     * data[1] = client version, formanto: Double;
     * data[2] = os_type, formato Integer;
     * @param data 
     */
    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public messaggio_t getType() {
        return type;
    }

    @Override
    public long getSender() {
        return sender;
    }
    
     
    
}
