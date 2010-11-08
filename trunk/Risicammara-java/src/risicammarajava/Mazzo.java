/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;
/**
 *
 * @author stengun
 */
import java.util.Random;

public class Mazzo {
    private Carta[] deck;
    private int inizio_mazzo;

    public Mazzo(){
        inizio_mazzo = 44;
        deck = new Carta[44];
        for(territori_t t:territori_t.values()){
            int i = 0;
            deck[i] = new Carta(t);
            i++;
        }
    }

    public Carta Pesca(){
        Carta temp = deck[inizio_mazzo--];
        return temp;
    }

    public void Mischia(){
        Random random = new Random();
        for(int i=0;i<deck.length;i++){
            Carta temp;
            int r = random.nextInt()%deck.length;
            temp = deck[i];
            deck[i] = deck[r];
            deck[r] = temp;
        }
    }

}
