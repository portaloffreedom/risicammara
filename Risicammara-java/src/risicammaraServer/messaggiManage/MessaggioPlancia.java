/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraJava.boardManage.Plancia;

/**
 *
 * @author stengun
 */
public class MessaggioPlancia implements Messaggio{
    private Plancia plancia;

    public MessaggioPlancia(Plancia plancia) {
        this.plancia = plancia;
    }

    public Plancia getPlancia() {
        return plancia;
    }
    
    public messaggio_t getType() {
        return messaggio_t.PLANCIA;
    }

    public int getSender() {
        return -1;
    }

}
