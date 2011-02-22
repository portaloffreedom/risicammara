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
     * getSender = Il giocatore che invia il messaggio
     * getOptParameter = Il numero di dadi con cui attaccare (Massimo 3 )
     */
    LANCIADADO,     //Lancia i dadi (usato anche per continuare ad attaccare)
    /**
     * Chi invia questo messaggio ha ritirato un proprio attacco prima
     * di lanciare i dadi.
     * getSender = Il giocatore che invia il messaggio
     */
    RITIRATI,       //Ritirati da un attacco
    //--------generale
    /**
     * Chi invia questo comando vuole passare alla fase successiva.
     * getSender = Il giocatore che invia il messaggio.
     * @see messaggio_t
     * @see MessaggioFase
     */
    PASSAFASE,      //Passa la fase corrente.

    //---------------- LOBBY
    /**
     * Il giocatore che riceve questo messaggio è stato eletto Leader della
     * lobby dal server.
     * getSender = Sempre il server
     * getOptParameter = Il giocatore che è leader
     */
    LEADER,         //Fa diventare leader della lobby un giocatore
    /**
     * Indica che il giocatore che ha inviato questo messaggio ha cambiato il
     * suo stato di pronto.
     * getSender = chi invia il messaggio.
     */
    SETPRONTO,      //Informa che il giocatore è pronto per partire
    /**
     * Esce dalla lobby e avvia la partita.
     * getSender  = chi invia il messaggio
     */
    AVVIAPARTITA,   // Ordina al server di iniziare una nuova partita
    /**
     * Il leader della lobby invia questo messaggio per forzare la disconnessione
     * di un preciso giocatore dalla lobby.
     * getSender = Chi invia il comando
     * getOptParameter = Chi viene espulso dalla lobby
     */
    KICKPLAYER,     // Espelli Giocatore
    /**
     * Indica che il server è stato resettato (indica a tutti di uscire).
     * getSender = il giocatore che invia il messaggio.
     */
    EXIT,           // il giocatore ha disconnesso la lobby
    //---------------- SERVER
    //--------partita
    /**
     * Chi riceve questo messaggio può iniziare a giocare il proprio turno.
     * getSender = Il giocatore che deve giocare
     * @deprecated si usa solo turnofplayer
     */
    STARTYOURTURN,  // Il giocatore a cui viene inviato questo messaggio inizia il suo turno
    /**
     * Questo messaggio informa su quale giocatore sta giocando adesso il turno.
     * getSender = il giocatore che deve giocare.
     */
    TURNOFPLAYER,   // Indica che il giocatore ricevente è quello di turno.
    /**
     * Indica che una sessione di attacco è terminata.
     * getSender = Il giocatore che effettuava l'attacco
     */
    ATTACCOTERMINATO, // Indica che l'attacco in corso è terminato
    /**
     * Indica che una sessione di attacco è iniziata.
     * getSender = Il giocatore che sta attaccando
     * getOptParameter = Il giocatore che si deve difendere.
     */
    INIZIAATTACCO,      //Indica un attacco.
    //--------generale
    /**
     * Giocatore connesso al server
     * getSender = L'indice del giocatore connesso
     */
    CONNECTED,      // Giocatore Connesso
    /**
     * Giocatore disconnesso dal server.
     * getSender = L'indice del giocatore disconnesso.
     */
    DISCONNECT;     // Il giocatore si è disconnesso dal server
}