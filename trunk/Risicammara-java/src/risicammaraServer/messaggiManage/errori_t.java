package risicammaraServer.messaggiManage;

/**
 * Gestione degli errori
 * @author Sten_Gun
 */
public enum errori_t {
    /**
     * Connessione Rifiutata
     */
    CONNECTIONREFUSED, // Connessione rifiutata
    /**
     * Nick già utilizzato
     */
    NICKUSED, // Nick già in uso (probabilmente inutile -> i conflitti si risolvono con rand)
    /**
     * Nick non valido
     */
    INVALIDNICK, //Nick riservato o non valido
    /**
     * Giocatore non valido
     */
    INVALIDPLAYER; //Giocatore non valido
}
