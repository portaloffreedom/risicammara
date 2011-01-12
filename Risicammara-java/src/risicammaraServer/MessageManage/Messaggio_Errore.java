/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public class Messaggio_Errore implements Messaggio{
    private errori_t errore;

    public Messaggio_Errore(errori_t err){
        this.errore = err;
    }

    public errori_t getError(){
        return this.errore;
    }

    public messaggio_t getType() {
        return messaggio_t.ERROR;
    }
}
