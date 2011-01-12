/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public enum comandi_t {
    NUOVAPARTITA,   // Ordina al server di iniziare una nuova partita
    KICKPLAYER,     // Espelli Giocatore
    EXIT,           // Giocatore Disconnesso
    CONNECTED,      // Giocatore Connesso
    DISCONNECT,     // Disconnette il server (crea una nuova lobby)
    PLYADDED;       // Giocatore aggiunto
}
