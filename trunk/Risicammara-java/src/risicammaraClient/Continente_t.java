package risicammaraClient;

/**
 * Enumerato che rappresenta i continenti e il loro bonus armate.
 * @author stengun
 */
public enum Continente_t {
    /** Continente Asia, che da diritto a 7 armate in più */
    ASIA            (7,12,1),
    /** Continente Africano, che da diritto a 3 armate in più */
    AFRICA          (3,6 ,2),
    /** Coninente Europeo, che da diritto a 5 armate in più */
    EUROPA          (5,7 ,3),
    /** Continente NordAmerica, che da diritto a 5 armate in più */
    NORDAMERICA     (5,9 ,4),
    /** Continente Sud America, che da diritto a 2 armate in più */
    SUDAMERICA      (2,4 ,5),
    /** Continente Oceania, che da diritto a 2 armate in più */
    OCEANIA         (2,4 ,6),
    /** Nessun continente. */
    NESSUNO         (0,0, 0);
    /** Contenitore per il numero di armate */
    private int numeroarmate;
    private int numterritori;
    private int id;
/**
 * Costruttore dell'enumerato.
 * @param armate Armate che il continente da al giocatore.
 * @param numterritori il numero di territori che appartengono al continente.
 * @param id id del continente (vedere TabellaTerritori in risorse)
 */
    Continente_t(int armate,int numterritori, int id){
        this.numeroarmate = armate;
        this.numterritori = numterritori;
        this.id = id;
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

    /**
     * Interfiacia per leggere l'id del continente. (Vedi TabellaTerritori nelle
     * risorse.
     * @return id id del continente.
     */
    public int getId(){
        return id;
    }
}
