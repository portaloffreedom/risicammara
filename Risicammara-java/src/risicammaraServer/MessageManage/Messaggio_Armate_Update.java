/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

import risicammaraClient.territori_t;

/**
 *
 * @author Sten_Gun
 */
public class Messaggio_Armate_Update implements Messaggio {
    private territori_t territorio;
    private int num_armate;

    public Messaggio_Armate_Update(territori_t terr, int arm){
        this.territorio = terr;
        this.num_armate = arm;
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
}
