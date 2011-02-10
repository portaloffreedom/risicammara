package risicammaraClient;

/**
 *  Enumerato che mostra il tipo di vittoria possibile.
 * @author Sten_Gun
 */
public enum tipovittoria_t{
    /** Vittoria per continenti (parente stretta di Territoriale)*/
    CONTINENTALE,
    /**
     * Vittoria per Territori. Come la vittoria continentale, viene verificata
     * alla fine del turno di tutti i giocatori
     */
    TERRITORIALE,
    /** Vittoria per eliminazione di un giocatore */
    DISTRUZIONE;
}
