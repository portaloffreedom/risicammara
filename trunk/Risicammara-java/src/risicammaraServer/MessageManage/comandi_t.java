package risicammaraServer.MessageManage;

/**
 * Enumerato con tutti i comandi client/server
 * @author Sten_Gun
 */
public enum comandi_t {

    @Deprecated
    SETNOPRONTO,     // Informa che il giocatore non è pronto per partire
            
    SETPRONTO,      //Informa che il giocatore è pronto per partire
    NUOVAPARTITA,   // Ordina al server di iniziare una nuova partita
    KICKPLAYER,     // Espelli Giocatore
    EXIT,           // il giocatore ha disconnesso la lobby
    
    CONNECTED,      // Giocatore Connesso
    DISCONNECT;     // Il giocatore si è disconnesso dal server
}
