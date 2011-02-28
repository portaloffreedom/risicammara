package risicammaraServer.messaggiManage;

import risicammaraJava.deckManage.Carta;

/**
 * Messaggio per giocare un tris.
 * @author stengun
 */
public class MessaggioGiocaTris implements Messaggio{
    private int sender;
    private Carta carta1,carta2,carta3;

    /**
     * Costruisce un messaggio per giocare i tris
     * @param sender chi invia il messaggio
     * @param carta1 la prima carta
     * @param carta2 la seconda carta
     * @param carta3 la terza carta
     */
    public MessaggioGiocaTris(int sender, Carta carta1, Carta carta2, Carta carta3) {
        this.sender = sender;
        this.carta1 = carta1;
        this.carta2 = carta2;
        this.carta3 = carta3;
    }

    /**
     * Restituisce la prima carta
     * @return la prima carta
     */
    public Carta getCarta1() {
        return carta1;
    }

    /**
     * restituisce la seconda carta
     * @return la seconda carta
     */
    public Carta getCarta2() {
        return carta2;
    }

    /**
     * Restituisce la terza carta
     * @return la terza carta
     */
    public Carta getCarta3() {
        return carta3;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.GIOCATRIS;
    }


    @Override
    public int getSender() {
        return sender;
    }

}
