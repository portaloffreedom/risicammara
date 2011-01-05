/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava.deckManage;

import java.util.Random;
import risicammarajava.Obbiettivi_t;

/**
 *
 * @author Sten_Gun
 */
public final class MazzoObbiettivi extends Mazzo{
    private int puntomaz;
    public MazzoObbiettivi(){
        super(Obbiettivi_t.values());
        puntomaz = 0;
        this.Mischia();
    }

    protected void Mischia() {
        Random random = new Random();
        for(int i=0;i<deck.length;i++){
            Carta temp;
            int r = random.nextInt(deck.length);
            temp = deck[i];
            deck[i] = deck[r];
            deck[r] = temp;
        }
    }
    public Carta Pesca(){
        return deck[puntomaz++];
    }
}

