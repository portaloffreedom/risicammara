
package risicammaraServer.messaggiManage;

import risicammaraClient.Colore_t;

/**
 * Messaggio che aggiorna i dati giocatore.
 * @author Sten_Gun
 * @deprecated 
 */
public class MessaggioAggiornaDatiGiocatore implements Messaggio {
    private String nick;
    private Colore_t color;
    private long who;

    @Override
    public messaggio_t getType() {
        return messaggio_t.AGGIORNADATIGIOCATORE;
    }


    @Override
    public long getSender() {
        return -1;
    }

    /**
     * Costruisce un nuovo messaggio Aggiorna Dati Giocatore
     * @param nick il nuovo nick
     * @param color il nuovo colore
     * @param who l'indice di chi cambia
     */
    public MessaggioAggiornaDatiGiocatore(String nick, Colore_t color, long who) {
        this.nick = nick;
        this.color = color;
        this.who = who;
    }

    /**
     * restituisce il colore delle nuove armate
     * @return il colore delle nuove armate
     */
    public Colore_t getColor() {
        return color;
    }

    /**
     * Restituisce il nuovo nick
     * @return il nuovo nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * Aggiorna i dati di un dato giocatore.
     * @return l'indice del gicoatore da aggiornare
     */
    public long getWho() {
        return who;
    }

    @Override
    public String toString(){
        return "PLAYER NUMBER"+who+" UPDATED: nick "+nick+", colore "+color+".";
    }
}
