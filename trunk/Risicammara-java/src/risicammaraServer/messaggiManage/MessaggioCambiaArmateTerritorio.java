
package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 *
 * @author stengun
 */
public class MessaggioCambiaArmateTerritorio implements Messaggio{
    private int armate;
    private territori_t territorio;
    private int sender;

    public MessaggioCambiaArmateTerritorio(int sender,int armate, territori_t territorio) {
        this.armate = armate;
        this.territorio = territorio;
        this.sender = sender;
    }

    public int getArmate() {
        return armate;
    }

    public territori_t getTerritorio() {
        return territorio;
    }

    public messaggio_t getType() {
        return messaggio_t.CAMBIAARMATETERRITORIO;
    }

    public int getSender() {
        return sender;
    }

}
