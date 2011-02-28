package risicammaraServer.messaggiManage;

import risicammaraClient.Colore_t;

/**
 * Messaggio che indica un cambiamento di nick e di colore armate.
 * @author Sten_Gun
 */
public class MessaggioCambiaNickColore implements Messaggio {
    private String nick;
    private Colore_t colore;
    private int sender;

    /**
     * Messaggio per indicare il cambiamento del nick o del colore delle armate
     * di un gicoatore.
     * @param nick il nuovo nick
     * @param colore il nuovo colore armate
     * @param sender chi invia il messaggio
     */
    public MessaggioCambiaNickColore(String nick, Colore_t colore,int sender) {
        this.nick = nick;
        this.colore = colore;
        this.sender = sender;
    }

    /**
     * Restituisci il nuovo colore
     * @return il nuovo colore
     */
    public Colore_t getColore() {
        return colore;
    }

    /**
     * Richiedi nuovo nickname
     * @return il nuovo nickname.
     */
    public String getNick() {
        return nick;
    }



    @Override
    public messaggio_t getType() {
        return messaggio_t.MODIFICANICKCOLORE;
    }

    @Override
    public int getSender() {
        return sender;
    }

}
