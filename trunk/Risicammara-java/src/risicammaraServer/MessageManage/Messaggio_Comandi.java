/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public class Messaggio_Comandi implements Messaggio{
    private comandi_t comando;

    public Messaggio_Comandi(comandi_t cmd){
        this.comando = cmd;
    }

    public messaggio_t getType() {
        return messaggio_t.COMMAND;
    }

    public comandi_t getComando(){
        return comando;
    }
}
