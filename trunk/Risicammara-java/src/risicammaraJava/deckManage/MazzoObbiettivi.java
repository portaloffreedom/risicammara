package risicammaraJava.deckManage;

import risicammaraClient.Obbiettivi_t;

/**
 * Rappresenta il mazzo degli obbiettivi.
 * @author Sten_Gun
 */
public final class MazzoObbiettivi extends Mazzo{

    public MazzoObbiettivi(){
        super(Obbiettivi_t.values());
        Mischia();
    }

    public Carta Pesca(){
        Carta temp = deck[--inizio_mazzo];
        return temp;
    }
}

