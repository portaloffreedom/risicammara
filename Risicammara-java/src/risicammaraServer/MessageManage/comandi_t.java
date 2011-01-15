package risicammaraServer.MessageManage;

/**
 * Enumerato con tutti i comandi client/server
 * @author Sten_Gun
 */
public enum comandi_t {


    NUOVAPARTITA,   // Ordina al server di iniziare una nuova partita
    KICKPLAYER,     // Espelli Giocatore
    EXIT,           // il giocatore ha disconnesso la lobby
    
    CONNECTED,      // Giocatore Connesso
    DISCONNECT;     // Il giocatore si è disconnesso dal server
}
