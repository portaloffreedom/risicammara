package risicammaraServer.messaggiManage;

import risicammaraJava.turnManage.Fasi_t;

/**
 * Questo messaggio serve per indicare il passaggio ad una specifica fase di
 * gioco.
 * @author stengun
 */
public class MessaggioFase implements Messaggio {
    private Fasi_t fase;
    private int sender;

    /**
     * Costruisce un messaggio di cambio fase
     * @param fase la nuova fase
     * @param sender di chi è il turno
     */
    public MessaggioFase(Fasi_t fase, int sender) {
        this.fase = fase;
        this.sender = sender;
    }

    /**
     * Restituisce la fase.
     * @return la fase.
     */
    public Fasi_t getFase() {
        return fase;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.FASE;
    }

    @Override
    public int getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "MessaggioFase: "+fase+" (sender="+sender+")";
    }

}
