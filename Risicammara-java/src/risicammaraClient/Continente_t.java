package risicammaraClient;

/**
 * Enumerato che rappresenta i continenti e il loro bonus armate.
 * @author stengun
 */
public enum Continente_t {
    /** Continente NordAmerica, che da diritto a 5 armate in più */
    NORDAMERICA     (5,9),
    /** Continente Sud America, che da diritto a 2 armate in più */
    SUDAMERICA      (2,4),
    /** Coninente Europeo, che da diritto a 5 armate in più */
    EUROPA          (5,7),
    /** Continente Africano, che da diritto a 3 armate in più */
    AFRICA          (3,6),
    /** Continente Asia, che da diritto a 7 armate in più */
    ASIA            (7,12),
    /** Continente Oceania, che da diritto a 2 armate in più */
    OCEANIA         (2,4),
    /** Nessun continente. */
    NESSUNO         (0,0);
    /** Contenitore per il numero di armate */
    private int numeroarmate;
    private int numterritori;
/**
 * Costruttore dell'enumerato.
 * @param armate Armate che il continente da al giocatore.
 * @param numterritori il numero di territori che appartengono al continente.
 */
    Continente_t(int armate,int numterritori){
        this.numeroarmate = armate;
        this.numterritori = numterritori;
    }
    /**
     * Metodo per richiedere il numero di territori di un dato continente.
     * @return numero dei territori di quel continente.
     */
    public int getNumterritori(){
        return numterritori;
    }
    /**
     * Interfaccia per leggere il numero di armate bonus per il dato continente.
     * @return Numero di armate bonus che il continente fornisce.
     */
    public int getArmate(){
        return numeroarmate;
    }
}
