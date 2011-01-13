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
    private String sender;
    private String messaggio;

    public MessaggioChat(String sender, String messaggio) {
        this.sender = sender;
        this.messaggio = messaggio;
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
