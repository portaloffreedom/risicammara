package risicammaraServer.messaggiManage;

import risicammaraJava.playerManage.Giocatore;

/**
 * Messaggio per indicare un giocatore appena aggiunto
 * @author Sten_Gun
 */
public class MessaggioAddPlayer implements Messaggio{
    private Giocatore player;

    /**
     * Costruttore del messaggioAddPlayer
     * @param player L'oggetto Giocatore appena aggiunto
     */
    public MessaggioAddPlayer(Giocatore player) {
        this.player = player;
    }

    /**
     * Restituisce L'oggetto giocatore associato al nuovo giocatore.
     * @return L'oggetto Giocatore.
     */
    public Giocatore getPlayer() {
        return player;
    }


    @Override
    public messaggio_t getType() {
        return messaggio_t.AGGIUNGIGIOCATORE;
    }


    @Override
    public int getSender() {
        return -1;
    }

}
