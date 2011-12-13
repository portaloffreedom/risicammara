package risicammaraServer.messaggiManage;

/**
 * Messaggio di connessione preliminare.
 * @author stengun
 */
public class MessaggioHelo implements Messaggio
{
    private Object[] data;
    private long sender;

    public MessaggioHelo(long sender,Object[] data) {
        this.sender = sender;
        this.data = data;
    }
    /**
     * 
     * @return 
     */
    public Object[] getData() {
        return data;
    }
    /**
     * Imopsta il campo dati del messaggio di HELO.
     * La convenzione da usare è la seguente:
     * data[0] = nickname, formato String;
     * data[1] = client version, formanto: Double;
     * data[2] = os_type, formato Integer;
     * @param data 
     * @deprecated
     */
    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.HELO;
    }

    @Override
    public long getSender() {
        return sender;
    }
    
     
    
}
