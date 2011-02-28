package risicammaraServer.messaggiManage;

import risicammaraClient.Obbiettivi_t;

/**
 * Serve per assegnare al giocatore  che riceve questo messaggio un obbiettivo.
 * @author Sten_Gun
 */
public class MessaggioObbiettivo implements Messaggio{
    private Obbiettivi_t obj;
    private int sender;

    /**
     * Costruisce un messaggio per assegnare un obbiettivo
     * @param obj l'obbiettivo da assegnare
     */
    public MessaggioObbiettivo(Obbiettivi_t obj) {
        this.obj = obj;
        this.sender = -1;
    }

    /**
     * Restituisce l'obbiettivo
     * @return l'obbiettivo
     */
    public Obbiettivi_t getObj() {
        return obj;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.AGGIORNAOBJGIOCATORE;
    }

    @Override
    public int getSender() {
        return sender;
    }

}
