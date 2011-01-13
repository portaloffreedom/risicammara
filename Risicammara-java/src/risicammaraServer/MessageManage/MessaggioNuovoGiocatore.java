/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

import java.net.Socket;

/**
 *
 * @author matteo
 */
public class MessaggioNuovoGiocatore implements Messaggio {

    private Socket connessioneGiocatore;

    public MessaggioNuovoGiocatore(Socket connessioneGiocatore) {
        this.connessioneGiocatore = connessioneGiocatore;
    }


    public messaggio_t getType() {
        return messaggio_t.AGGIUNGIGIOCATORE;
    }

    public Socket getConnessioneGiocatore() {
        return connessioneGiocatore;
    }

    

}
