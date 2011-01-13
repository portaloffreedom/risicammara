/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author matteo
 */
public class MessaggioConfermaNuovoGiocatore implements Messaggio {

    private int numeroGiocatore;

    public messaggio_t getType() {
        return messaggio_t.CONFERMAAGGIUNGIGIOCATORE;
    }

    public int getNumeroGiocatore() {
        return numeroGiocatore;
    }

}
