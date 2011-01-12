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
    private String sender;
    private String receiver;

    public Messaggio_Comandi(comandi_t cmd, String inviante){
        this(cmd, inviante, null);
    }

    public Messaggio_Comandi(comandi_t cmd, String inviante, String who){
        this.comando = cmd;
        this.sender = inviante;
        this.receiver = who;
    }

    public messaggio_t getType() {
        return messaggio_t.COMMAND;
    }

    public comandi_t getComando(){
        return comando;
    }

    public String getSender(){
        return sender;
    }

    public String getReceiver(){
        return receiver;
    }
}
