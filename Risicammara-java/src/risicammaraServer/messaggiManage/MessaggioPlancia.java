package risicammaraServer.messaggiManage;

import risicammaraJava.boardManage.Plancia;

/**
 * Messaggio che serve per fornire la plancia di gioco ai giocatori.
 * @author stengun
 */
public class MessaggioPlancia implements Messaggio{
    private Plancia plancia;

    /**
     * Costruisce un messaggio Plancia
     * @param plancia la plancia da spedire
     */
    public MessaggioPlancia(Plancia plancia) {
        this.plancia = plancia;
    }

    /**
     * Richiede la plancia dal messaggio
     * @return la plancia.
     */
    public Plancia getPlancia() {
        return plancia;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.PLANCIA;
    }

    @Override
    public long getSender() {
        return -1;
    }

}
