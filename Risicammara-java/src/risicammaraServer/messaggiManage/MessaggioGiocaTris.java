/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraJava.deckManage.Carta;

/**
 *
 * @author stengun
 */
public class MessaggioGiocaTris implements Messaggio{
    private int sender;
    private Carta carta1,carta2,carta3;

    public MessaggioGiocaTris(int sender, Carta carta1, Carta carta2, Carta carta3) {
        this.sender = sender;
        this.carta1 = carta1;
        this.carta2 = carta2;
        this.carta3 = carta3;
    }

    public Carta getCarta1() {
        return carta1;
    }

    public Carta getCarta2() {
        return carta2;
    }

    public Carta getCarta3() {
        return carta3;
    }

    public messaggio_t getType() {
        return messaggio_t.GIOCATRIS;
    }

    public int getSender() {
        return sender;
    }

}
