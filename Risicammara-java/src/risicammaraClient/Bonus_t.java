package risicammaraClient;

/**
 * Enumerato che rappresenta tutti i bonus
 * @author stengun
 */
 public enum Bonus_t {
    /** Bonus cannone (tre cannoni uguali = 4 armate in più) */
    CANNONE (4, "/Images/cannone.png"),
    /** Bonus fanti (tre fanti = 6 armate in più)*/
    FANTE   (6, "/Images/fante.png"),
    /** Bonus cavallo (tre cavalli = 8 armate in più)*/
    CAVALLO (8, "/Images/cavallo.png"),
    /** Bonus Jolly. Un jolly e due carte uguale 12 armate in più*/
    JOLLY   (12, "/Images/cannone.png");

    private int numarm;
    private String percorsoImmagine;
    /** Inizializza il valore dell'enumerato in base al valore assegnato*/
    Bonus_t(int numarm, String percorsoImmagine){
        this.numarm=numarm;
        this.percorsoImmagine = percorsoImmagine;
    };
    /** Restituisce la quantità di armate del tris (di tipo uguale)
     * @param diversi Se le tre carte sono diverse o se sono uguali
     * @return Il valore del bonus corrispondente (territori esclusi)
     */
    public int TrisValue(boolean diversi){
        if(diversi) return 12;
        return numarm;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }
 }
