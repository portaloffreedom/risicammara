package risicammaraJava.boardManage;

import java.io.Serializable;
import risicammaraClient.territori_t;

/**
 * Plancia di gioco contenente tutti i territori e i relativi attributi
 * (proprietario, numero di armate presenti, i confini)
 * @author stengun
 */
public class Plancia implements Serializable{
    /** Un array di territorio_plancia che rappresenta tutti i territori.*/
    private Territorio_plancia[] tabellone;
    /**
     * Inizializza tutti i territori della plancia.
     */
    public Plancia(){
        tabellone = new Territorio_plancia[42];
        int i = 0;
        for(territori_t t:territori_t.values()){
            tabellone[i] = new Territorio_plancia(t);
            i++;
            if((t == territori_t.Jolly1)|(t == territori_t.Jolly2)|(i==42)) break;
        }
    }

    /**
     * Richiedi il corrispondente territorio della plancia di gioco a partire
     * dall'enumerato che rappresenta il territorio. (nome).
     * @param territorio Il nome del territorio richiesto.
     * @return il Territorio_plancia corrispondente
     */
    public Territorio_plancia getTerritorio(territori_t territorio){
        territori_t[] terr = territori_t.values();
        for(int i=0;i<terr.length;i++){
            if(terr[i] == territorio) return tabellone[i];
        }
        return null;
    }

}
