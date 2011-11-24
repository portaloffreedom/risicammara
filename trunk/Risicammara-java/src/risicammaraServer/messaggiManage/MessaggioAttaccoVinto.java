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

    /**
     * Costruisce un messaggioattaccoVinto
     * @param armspost le armate da spostare
     * @param territorio_conquistato il territorio conquistato
     */
    public MessaggioAttaccoVinto(int armspost, territori_t territorio_conquistato) {
        this.armspost = armspost;
        this.territorio_conquistato = territorio_conquistato;
    }

    /**
     * Restituisce le armate spostate in modo obbligatorio dal server
     * @return le armate spostate in modo obbligatorio dal server.
     */
    public int getArmSpost() {
        return armspost;
    }

    /**
     * Restituisce il territorio appena conquistato
     * @return il territorio conquistato.
     */
    public territori_t getTerritorio_conquistato() {
        return territorio_conquistato;
    }

    @Override
    public messaggio_t getType() {
        return messaggio_t.ATTACCOVINTO;
    }

    @Override
    public long getSender() {
        return -1;
    }

}
