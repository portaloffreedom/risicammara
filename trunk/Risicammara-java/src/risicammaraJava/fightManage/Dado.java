/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.fightManage;
import java.util.Random;

/**
 * Classe che implementa l'oggetto "dado" che può essere lanciato per ottenere
 * un numero compreso fra 1 e il numero delle facce del dado.
 * @author stengun
 */
public class Dado {
    private int facce;
    /**
     * Costruttore per creare un dado a n facce
     * @param facce Facce del dado
     */
    Dado(int facce){
        this.facce = facce;
    }
    public int RollDice(){
        Random random = new Random();
        return (1 + random.nextInt(facce));
    }
}
