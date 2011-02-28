package risicammaraJava.boardManage;

import java.io.Serializable;
import risicammaraClient.territori_t;

/**
 * Plancia di gioco contenente tutti i territori e i relativi attributi
 * (proprietario, numero di armate presenti, i confini)
 * @author stengun
 */
public class Plancia implements Serializable {
    /** Un array di territorio_plancia che rappresenta tutti i territori.*/
    protected TerritorioPlancia[] tabellone;
    /**
     * Inizializza tutti i territori della plancia.
     */
    public Plancia(){
        tabellone = new TerritorioPlancia[42];
        int i = 0;
        for(territori_t t:territori_t.values()){
            tabellone[i] = new TerritorioPlancia(t);
            i++;
            if((t == territori_t.Jolly1)|(t == territori_t.Jolly2)|(i==42)) break;
        }
    }
/**
 * Costruisce la plancia assegnando il tabellone di un altro oggetto Plancia.
 * @param plancia L'oggetto plancia di cui copiare il tabellone.
 */
    protected Plancia(Plancia plancia){
        this.tabellone = plancia.tabellone;
    }

    /**
     * Richiedi il corrispondente territorio della plancia di gioco a partire
     * dall'enumerato che rappresenta il territorio. (nome).
     * @param territorio Il nome del territorio richiesto.
     * @return il TerritorioPlancia corrispondente
     */
    public TerritorioPlancia getTerritorio(territori_t territorio){
        if(territorio == territori_t.Jolly1 || territorio == territori_t.Jolly2)
            return null;
        territori_t[] terr = territori_t.values();
        return getTerritorio(territorio, 0,terr.length,terr);
    }
/**
 * Metodo per la discesa ricorsiva per la ricerca del territorio
 * controllando ogni volta se il territorio è più grande o piccolo di quello
 * che si trova nella metà dell'array contenente i territori.
 * @param territorio il territorio da cercare
 * @param inizar dove iniziare a contare
 * @param finear dove finire di contare
 * @param terr array di territori
 * @return il territorio plancia corrispondente
 */
    private TerritorioPlancia getTerritorio(territori_t territorio,
                                       int inizar,int finear,territori_t[] terr)
    {
        int meta = ((finear-inizar)/2)+inizar;
        if ( territorio.compareTo(terr[meta]) == 0 )return tabellone[meta];
        else if(territorio.compareTo(terr[meta]) > 0) return getTerritorio(territorio,
                                                     meta+1, finear, terr);
        else if ( territorio.compareTo(terr[meta]) < 0) return getTerritorio(territorio, inizar, meta-1, terr);
        return null;
    }
/**
 * Restituisce un TerritorioPlancia partendo dal suo intero identificativo.
 * @param idTerritorio l'identificativo del territorio.
 * @return il TerritorioPlancia corrispondente.
 * @throws TerritorioNonValido Se l'id non è valido viene sollevata questa eccezione.
 */
    public TerritorioPlancia getTerritorio(int idTerritorio) throws TerritorioNonValido {
        return this.getTerritorio(territori_t.GetTerritorio(idTerritorio));
    }
}
