/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

/**
 *
 * @author stengun
 */
public class Carta {
    private territori_t territorio = null;
    
    public Carta(territori_t territorio){
        this.territorio = territorio;
    }

    public territori_t getTerritorio(){
        return this.territorio;
    }

    public Bonus_t getBonus(){
        return this.territorio.getBonus();
    }
}
