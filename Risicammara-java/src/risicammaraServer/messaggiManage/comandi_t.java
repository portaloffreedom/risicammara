package risicammaraServer.messaggiManage;

/**
 * Enumerato con tutti i comandi client/server
 * @author Sten_Gun
 */
public enum comandi_t {

    //---------------- PARTITA
    //--------attacco
    /**
     * Lancia i dadi dell'attacco.
     */
    LANCIADADO,     //Lancia i dadi (usato anche per continuare ad attaccare)
    /**
     * Chi invia questo messaggio ha ritirato un proprio attacco prima
     * di lanciare i dadi.
     */
    RITIRATI,       //Ritirati da un attacco
    //--------generale
    /**
     * Chi invia questo comando vuole passare alla fase successiva.
     */
    PASSAFASE,      //Passa la fase corrente.

    //---------------- LOBBY
    /**
     * Il giocatore che riceve questo messaggio è stato eletto Leader della
     * lobby dal server.
     */
    LEADER,         //Fa diventare leader della lobby un giocatore
    /**
     * Indica che il giocatore che ha inviato questo messaggio ha cambiato il
     * suo stato di pronto.
     */
    SETPRONTO,      //Informa che il giocatore è pronto per partire
    /**
     * Esce dalla lobby e avvia la partita.
     * @deprecated  non più utile in quanto il server parte appena sono tutti pronti.
     */
    AVVIAPARTITA,   // Ordina al server di iniziare una nuova partita
    /**
     * Il leader della lobby invia questo messaggio per forzare la disconnessione
     * di un preciso giocatore dalla lobby.
     */
    KICKPLAYER,     // Espelli Giocatore
    /**
     * Indica che il leader ha dismesso la lobby.
     */
    EXIT,           // il giocatore ha disconnesso la lobby
    //---------------- SERVER
    //--------partita
    /**
     * Chi riceve questo messaggio può iniziare a giocare il proprio turno.
     */
    STARTYOURTURN,  // Il giocatore a cui viene inviato questo messaggio inizia il suo turno
    /**
     * Questo messaggio informa su quale giocatore sta giocando adesso il turno.
     */
    TURNOFPLAYER,   // Indica che il giocatore ricevente è quello di turno.
    /**
     * Indica che una sessione di attacco è terminata.
     */
    ATTACCOTERMINATO, // Indica che l'attacco in corso è terminato
    /**
     * Indica che una sessione di attacco è iniziata.
     */
    INIZIAATTACCO,      //Indica un attacco.
    //--------generale
    /**
     * Giocatore connesso al server
     */
    CONNECTED,      // Giocatore Connesso
    /**
     * Giocatore disconnesso dal server.
     */
    DISCONNECT;     // Il giocatore si è disconnesso dal server
}
