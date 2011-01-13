package risicammaraServer.MessageManage;

/**
 * Enumerato con tutti i comandi client/server
 * @author Sten_Gun
 */
public enum comandi_t {

    //TODO Completare il codice di NUOVAPARTITA
    NUOVAPARTITA,   // Ordina al server di iniziare una nuova partita
    //TODO Completare il codice di KICKPLAYER
    KICKPLAYER,     // Espelli Giocatore

    //TODO Completare il codice di Exit.
    EXIT,           // Giocatore Disconnesso
    
    CONNECTED,      // Giocatore Connesso
    DISCONNECT,     // Disconnette il server (crea una nuova lobby)

    //TODO Completare il codice di Plyadded
    PLYADDED;       // Giocatore aggiunto
}
