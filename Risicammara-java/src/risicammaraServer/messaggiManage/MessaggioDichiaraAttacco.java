package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 * Questo messaggio serve per indicare a tutti i giocatori una dichiarazione di attacco.
 * @author Sten_Gun
 */
public class MessaggioDichiaraAttacco implements Messaggio {
    private territori_t territorio_attaccante,territorio_difensore;
    private long sender;

    /**
     * Costruisce un messaggio per dichiarare un attacco.
     * @param territorio_attaccante il territorio da cui parte l'attacco
     * @param territorio_difensore il territorio attaccato
     * @param sender chi invia il messaggio
     */
    public MessaggioDichiaraAttacco(territori_t territorio_attaccante, territori_t territorio_difensore, long sender) {
        this.territorio_attaccante = territorio_attaccante;
        this.territorio_difensore = territorio_difensore;
        this.sender = sender;
    }

    /**
     * Restituisce il territorio dell'attaccante..
     * @return il territorio dell'attaccante
     */
    public territori_t getTerritorio_attaccante() {
        return territorio_attaccante;
    }

    /**
     * Restituisce il territorio del difensore
     * @return il territorio del difensore.
     */
    public territori_t getTerritorio_difensore() {
        return territorio_difensore;
    }


    @Override
    public messaggio_t getType() {
        return messaggio_t.DICHIARAATTACCO;
    }

    @Override
    public long getSender() {
        return sender;
    }

}
