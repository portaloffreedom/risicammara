package risicammaraServer.messaggiManage;

import java.io.Serializable;

/**
 * Interfaccia per definire un generico messaggio via rete.
 * @author Sten_Gun
 */
public interface Messaggio extends Serializable {

    /**
     * Metodo che ritorna il tipo di messaggio per aiutare con la deserializzazione completa.
     * @return tipo di messaggio ::messaggio_t
     */
    public messaggio_t getType();
    /**
     * Restituisce l'inviante di un messaggio.
     * @return
     */
    public int getSender();

}
