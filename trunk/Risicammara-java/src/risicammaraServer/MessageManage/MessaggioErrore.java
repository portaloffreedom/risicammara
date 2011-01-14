/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioErrore implements Messaggio{
    private errori_t errore;
    private int sender;

    public MessaggioErrore(errori_t err,int sender){
        this.errore = err;
        this.sender = sender;
    }

    public errori_t getError(){
        return this.errore;
    }

    public messaggio_t getType() {
        return messaggio_t.ERROR;
    }

    public int getSender() {
        return sender;
    }
}
