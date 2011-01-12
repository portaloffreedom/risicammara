/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public class Messaggio_chat implements Messaggio {
    private String sender;
    private String messaggio;

    public Messaggio_chat(String nick, String msg){
        this.sender = nick;
        this.messaggio = msg;
    }
    
    public messaggio_t getType() {
        return messaggio_t.CHAT;
    }

    public String getNick(){
        return sender;
    }

    public String getMessaggio(){
        return messaggio;
    }
}
