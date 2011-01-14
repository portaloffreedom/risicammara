/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public enum errori_t {
    CONNECTIONREFUSED, // Connessione rifiutata
    NICKUSED, // Nick già in uso (probabilmente inutile -> i conflitti si risolvono con rand)
    INVALIDNICK, //Nick riservato o non valido
    INVALIDPLAYER; //Giocatore non valido
}
