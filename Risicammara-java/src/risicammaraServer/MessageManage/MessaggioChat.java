/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

import risicammaraJava.playerManage.ListaPlayers;

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

    public String toString(ListaPlayers list){
        return list.getNomeByIndex(sender)+": "+messaggio;
    }

    @Override
    public String toString() {
        return getType()+"|giocatore"+sender+":"+messaggio;
    }

    public String getMessaggio(){
        return messaggio;
    }
}
