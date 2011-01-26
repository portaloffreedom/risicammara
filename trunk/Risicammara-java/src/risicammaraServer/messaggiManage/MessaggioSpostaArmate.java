/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 *
 * @author stengun
 */
public class MessaggioSpostaArmate implements Messaggio {
    private int sender;
    private territori_t sorgente,arrivo;
    private int numarmate;

    public MessaggioSpostaArmate(int sender, territori_t sorgente, territori_t arrivo, int numarmate) {
        this.sender = sender;
        this.sorgente = sorgente;
        this.arrivo = arrivo;
        this.numarmate = numarmate;
    }

    public territori_t getArrivo() {
        return arrivo;
    }

    public int getNumarmate() {
        return numarmate;
    }

    public territori_t getSorgente() {
        return sorgente;
    }

    public messaggio_t getType() {
        return messaggio_t.SPOSTAARMATE;
    }

    public int getSender() {
        return sender;
    }

}
