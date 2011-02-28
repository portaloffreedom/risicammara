package risicammaraServer.messaggiManage;

import risicammaraJava.deckManage.Carta;

/**
 * La carta che è stata pescata dal Giocatore.
 * Questo messaggio può essere inviato soltanto dal server.
 * @author stengun
 */
public class MessaggioCarta implements Messaggio {
    private Carta carta;
    private int sender;

    /**
     * Crea un messaggio Carta
     * @param carta la carta da inviare
     * @param sender chi ha preso la carta.
     */
    public MessaggioCarta(Carta carta,int sender) {
        this.carta = carta;
        this.sender = sender;
    }

    /**
     * Restituisce la carta
     * @return la carta
     */
    public Carta getCarta() {
        return carta;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.CARTA;
    }

    @Override
    public int getSender() {
        return sender;
    }

}
