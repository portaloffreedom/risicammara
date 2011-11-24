package risicammaraServer.messaggiManage;

import risicammaraJava.playerManage.ListaPlayers;

/**
 * Rappresenta un messaggio di chat.
 * @author Sten_Gun
 */
public class MessaggioChat implements Messaggio {
    private long sender;
    private String messaggio;

    /**
     * Costruisce un messaggioChat
     * @param sender chi invia il messaggio
     * @param msg il messaggio.
     */
    public MessaggioChat(long sender, String msg){
        this.sender = sender;
        this.messaggio = msg;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.CHAT;
    }


    @Override
    public long getSender(){
        return sender;
    }

    /**
     * Restituisce il messaggio di chat come una stringa con il nick incorporato.
     * @param list la lista giocatori
     * @return il messaggio di chat nella forma nick: messaggio
     */
    public String toString(ListaPlayers list){
        return list.getNomeByIndex((int)sender)+": "+messaggio;
    }

    @Override
    public String toString() {
        return getType()+"|giocatore"+sender+":"+messaggio;
    }

    /**
     * Restitusice la stringa del messaggio di chat.
     * @return il messaggio di chat
     */
    public String getMessaggio(){
        return messaggio;
    }
}
