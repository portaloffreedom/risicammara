/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioChat implements Messaggio {
    private int sender;
    private String messaggio;

    public MessaggioChat(int sender, String msg){
        this.sender = sender;
        this.messaggio = msg;
    }
    
    public messaggio_t getType() {
        return messaggio_t.CHAT;
    }

    public int getSender(){
        return sender;
    }

    public String getMessaggio(){
        return messaggio;
    }
}