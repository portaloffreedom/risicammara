/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

import risicammaraJava.playerManage.Giocatore;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioAddPlayer implements Messaggio{
    private Giocatore player;

    public MessaggioAddPlayer(Giocatore player) {
        this.player = player;
    }

    public Giocatore getPlayer() {
        return player;
    }


    public messaggio_t getType() {
        return messaggio_t.AGGIUNGIGIOCATORE;
    }

    public int getSender() {
        return -1;
    }

}
