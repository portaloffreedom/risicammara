/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.deckManage;

import java.util.Random;
import risicammaraClient.Obbiettivi_t;

/**
 *
 * @author Sten_Gun
 */
public final class MazzoObbiettivi extends Mazzo{

    public MazzoObbiettivi(){
        super(Obbiettivi_t.values());
        Mischia();
        Mischia();
        Mischia();
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
        Carta temp = deck[--inizio_mazzo];
        return temp;
    }
}

