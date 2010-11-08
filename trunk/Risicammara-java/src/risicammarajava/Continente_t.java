/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

/**
 *
 * @author stengun
 */
public enum Continente_t {
    NORDAMERICA     (5),
    SUDAMERICA      (2),
    EUROPA          (5),
    AFRICA          (3),
    ASIA            (7),
    OCEANIA         (2),
    NESSUNO         (0);
    
    private int numeroarmate;
    
    Continente_t(int armate){
        this.numeroarmate = armate;
    }
    public int getArmate(){
        return numeroarmate;
    }
}
