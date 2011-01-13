package risicammaraClient;

/**
 * Enumerato che rappresenta i continenti e il loro bonus armate.
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
