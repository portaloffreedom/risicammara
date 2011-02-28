
package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 * Messaggio per Cambiare le armate in un dato territorio.
 * @author stengun
 */
public class MessaggioCambiaArmateTerritorio implements Messaggio{
    private int armate;
    private territori_t territorio;
    private int sender;

    /**
     * Costruisce un messaggio CambiaArmateTerritorio
     * @param sender chi invia il messaggio
     * @param armate le armate da inserire
     * @param territorio il territorio da modificare
     */
    public MessaggioCambiaArmateTerritorio(int sender,int armate, territori_t territorio) {
        this.armate = armate;
        this.territorio = territorio;
        this.sender = sender;
    }

    /**
     * Restituisce le armate
     * @return le armate
     */
    public int getArmate() {
        return armate;
    }

    /**
     * il territorio del quale cambiare le armate
     * @return il territorio da cambiare
     */
    public territori_t getTerritorio() {
        return territorio;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.CAMBIAARMATETERRITORIO;
    }

    @Override
    public int getSender() {
        return sender;
    }

}
