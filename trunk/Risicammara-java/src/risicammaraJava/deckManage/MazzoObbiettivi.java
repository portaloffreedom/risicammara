package risicammaraJava.deckManage;

import risicammaraClient.Obbiettivi_t;

/**
 * Rappresenta il mazzo degli obbiettivi.
 * @author Sten_Gun
 */
public final class MazzoObbiettivi extends Mazzo{

    /**
     * Costruisce il mazzo degli obbiettivi
     */
    public MazzoObbiettivi(){
        super(Obbiettivi_t.values());
        Mischia();
    }

    public Carta Pesca(){
        Carta temp = deck[--fine_mazzo];
        return temp;
    }
}

