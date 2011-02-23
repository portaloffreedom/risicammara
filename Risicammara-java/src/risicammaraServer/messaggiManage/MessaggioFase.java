/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraJava.turnManage.Fasi_t;

/**
 * Questo messaggio serve per indicare il passaggio ad una specifica fase di
 * gioco.
 * @author stengun
 */
public class MessaggioFase implements Messaggio {
    private Fasi_t fase;
    private int sender;

    public MessaggioFase(Fasi_t fase, int sender) {
        this.fase = fase;
        this.sender = sender;
    }

    public Fasi_t getFase() {
        return fase;
    }
    
    public messaggio_t getType() {
        return messaggio_t.FASE;
    }

    public int getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "MessaggioFase: "+fase+" (sender="+sender+")";
    }

}
