/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraClient.Colore_t;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioAggiornaDatiGiocatore implements Messaggio {
    private String nick;
    private Colore_t color;
    private int who;

    public messaggio_t getType() {
        return messaggio_t.AGGIORNADATIGIOCATORE;
    }

    public int getSender() {
        return -1;
    }

    public MessaggioAggiornaDatiGiocatore(String nick, Colore_t color, int who) {
        this.nick = nick;
        this.color = color;
        this.who = who;
    }

    public Colore_t getColor() {
        return color;
    }

    public String getNick() {
        return nick;
    }

    public int getWho() {
        return who;
    }

    @Override
    public String toString(){
        return "PLAYER NUMBER"+who+" UPDATED: nick "+nick+", colore "+color+".";
    }
}
