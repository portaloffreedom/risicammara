/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraClient.Obbiettivi_t;

/**
 * Serve per assegnare al giocatore  che riceve questo messaggio un obbiettivo.
 * @author Sten_Gun
 */
public class MessaggioObbiettivo implements Messaggio{
    private Obbiettivi_t obj;
    private int sender;

    public MessaggioObbiettivo(Obbiettivi_t obj) {
        this.obj = obj;
        this.sender = -1;
    }

    public Obbiettivi_t getObj() {
        return obj;
    }
    
    public messaggio_t getType() {
        return messaggio_t.AGGIORNAOBJGIOCATORE;
    }

    public int getSender() {
        return sender;
    }

}
