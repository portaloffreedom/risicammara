/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

import risicammaraClient.Colore_t;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioCambiaNickColore implements Messaggio {
    private String nick;
    private Colore_t colore;
    private int sender;

    public MessaggioCambiaNickColore(String nick, Colore_t colore,int sender) {
        this.nick = nick;
        this.colore = colore;
        this.sender = sender;
    }

    public Colore_t getColore() {
        return colore;
    }

    public String getNick() {
        return nick;
    }



    public messaggio_t getType() {
        return messaggio_t.MODIFICANICKCOLORE;
    }

    public int getSender() {
        return sender;
    }

}
