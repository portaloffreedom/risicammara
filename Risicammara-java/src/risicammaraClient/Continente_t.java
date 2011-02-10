package risicammaraClient;

/**
 * Enumerato che rappresenta i continenti e il loro bonus armate.
 * @author stengun
 */
public enum Continente_t {
    /** Continente NordAmerica, che da diritto a 5 armate in più */
    NORDAMERICA     (5),
    /** Continente Sud America, che da diritto a 2 armate in più */
    SUDAMERICA      (2),
    /** Coninente Europeo, che da diritto a 5 armate in più */
    EUROPA          (5),
    /** Continente Africano, che da diritto a 3 armate in più */
    AFRICA          (3),
    /** Continente Asia, che da diritto a 7 armate in più */
    ASIA            (7),
    /** Continente Oceania, che da diritto a 2 armate in più */
    OCEANIA         (2),
    /** Nessun continente. */
    NESSUNO         (0);
    /** Contenitore per il numero di armate */
    private int numeroarmate;
/**
 * Costruttore dell'enumerato
 * @param armate Armate che il continente da al giocatore
 */
    Continente_t(int armate){
        this.numeroarmate = armate;
    }
    /**
     * Interfaccia per leggere il numero di armate bonus per il dato continente.
     * @return Numero di armate bonus che il continente fornisce.
     */
    public int getArmate(){
        return numeroarmate;
    }
}
