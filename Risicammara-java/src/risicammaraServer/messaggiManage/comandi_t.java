package risicammaraServer.messaggiManage;

/**
 * Enumerato con tutti i comandi client/server
 * @author Sten_Gun
 */
//TODO Documentazione dei comandi
public enum comandi_t {

    //---------------- PARTITA
    //--------attacco
    LANCIADADO,     //Lancia i dadi (usato anche per continuare ad attaccare)
    RITIRATI,       //Ritirati da un attacco
    //--------generale
    PASSAFASE,      //Passa la fase corrente.

    //---------------- LOBBY
    LEADER,         //Fa diventare leader della lobby un giocatore
    @Deprecated
    SETNOPRONTO,     // Informa che il giocatore non è pronto per partire            
    SETPRONTO,      //Informa che il giocatore è pronto per partire
    AVVIAPARTITA,   // Ordina al server di iniziare una nuova partita
    KICKPLAYER,     // Espelli Giocatore
    EXIT,           // il giocatore ha disconnesso la lobby
    //---------------- SERVER
    //--------partita
    STARTYOURTURN,  // Il giocatore a cui viene inviato questo messaggio inizia il suo turno
    TURNOFPLAYER,   // Indica che il giocatore ricevente è quello di turno.
    ATTACCOTERMINATO, // Indica che l'attacco in corso è terminato
    INIZIAATTACCO,      //Indica un attacco.
    //--------generale
    CONNECTED,      // Giocatore Connesso
    DISCONNECT;     // Il giocatore si è disconnesso dal server
}
