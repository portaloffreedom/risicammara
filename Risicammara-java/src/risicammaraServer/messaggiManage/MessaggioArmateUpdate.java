/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioArmateUpdate implements Messaggio {
    private territori_t territorio;
    private int num_armate;
    private int sender;

    public MessaggioArmateUpdate(territori_t terr, int arm,int sender){
        this.territorio = terr;
        this.num_armate = arm;
        this.sender = sender;
    }
    public messaggio_t getType() {
        return messaggio_t.CAMBIAARMATETERRITORIO;
    }
    public int getArmate(){
        return num_armate;
    }
    public territori_t getTerritorio(){
        return territorio;
    }

    public int getSender() {
        return sender;
    }
}
