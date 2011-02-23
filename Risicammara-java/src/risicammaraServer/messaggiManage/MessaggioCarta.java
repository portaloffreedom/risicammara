/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraJava.deckManage.Carta;

/**
 * La carta che è stata pescata dal Giocatore.
 * Questo messaggio può essere inviato soltanto dal server.
 * @author stengun
 */
public class MessaggioCarta implements Messaggio {
    private Carta carta;
    private int sender;

    public MessaggioCarta(Carta carta,int sender) {
        this.carta = carta;
        this.sender = sender;
    }

    public Carta getCarta() {
        return carta;
    }

    public messaggio_t getType() {
        return messaggio_t.CARTA;
    }

    public int getSender() {
        return sender;
    }

}
