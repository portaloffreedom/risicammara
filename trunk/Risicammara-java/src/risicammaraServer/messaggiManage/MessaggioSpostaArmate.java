package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 * Messaggio che indica lo spostamento di armate.
 * @author stengun
 */
public class MessaggioSpostaArmate implements Messaggio {
    private long sender;
    private territori_t sorgente,arrivo;
    private int numarmate;

    /**
     * Costruisce un messaggio di spostamento.
     * @param sender chi invia il messaggio
     * @param sorgente il territorio da cui parte lo spostamento
     * @param arrivo il territorio dove spostare le armate
     * @param numarmate il numero di armate da spostare.
     */
    public MessaggioSpostaArmate(long sender, territori_t sorgente, territori_t arrivo, int numarmate) {
        this.sender = sender;
        this.sorgente = sorgente;
        this.arrivo = arrivo;
        this.numarmate = numarmate;
    }

    /**
     * Il territorio di arrivo
     * @return territorio dove spostare le armate
     */
    public territori_t getArrivo() {
        return arrivo;
    }

    /**
     * Numero di armate spostate
     * @return il numero di armate spostate
     */
    public int getNumarmate() {
        return numarmate;
    }

    /**
     * Richiede Il territorio da cui parte lo spostamento
     * @return il territorio da cui parte lo spostamento
     */
    public territori_t getSorgente() {
        return sorgente;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.SPOSTAARMATE;
    }

    @Override
    public long getSender() {
        return sender;
    }

}
