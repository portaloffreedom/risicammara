package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 * L'attacco che si stava svolgendo si è concluso con la vittoria dell'attaccante
 * e per tanto il territorio è stato conquistato.
 * @author stengun
 */
public class MessaggioAttaccoVinto implements Messaggio{
    private int armspost;
    private territori_t territorio_conquistato;

    public MessaggioAttaccoVinto(int armspost, territori_t territorio_conquistato) {
        this.armspost = armspost;
        this.territorio_conquistato = territorio_conquistato;
    }

    public int getArmSpost() {
        return armspost;
    }

    public territori_t getTerritorio_conquistato() {
        return territorio_conquistato;
    }

    public messaggio_t getType() {
        return messaggio_t.ATTACCOVINTO;
    }

    public int getSender() {
        return -1;
    }

}
