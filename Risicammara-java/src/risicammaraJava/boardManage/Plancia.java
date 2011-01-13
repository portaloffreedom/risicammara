/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.boardManage;

import risicammaraClient.territori_t;

/**
 * Plancia di gioco contenente tutti i territori e i relativi attributi
 * (proprietario, numero di armate presenti, i confini)
 * @author stengun
 */
public class Plancia {
    private Territorio_plancia[] tabellone;
    public Plancia(){
        tabellone = new Territorio_plancia[42];
        int i = 0;
        for(territori_t t:territori_t.values()){
            tabellone[i] = new Territorio_plancia(t);
            i++;
            if((t == territori_t.Jolly1)|(t == territori_t.Jolly2)|(i==42)) break;
        }
    }

    public Territorio_plancia getTerritorio(territori_t territorio){
        territori_t[] terr = territori_t.values();
        for(int i=0;i<terr.length;i++){
            if(terr[i] == territorio) return tabellone[i];
        }
        return null;
    }

}
