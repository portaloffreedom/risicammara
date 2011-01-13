/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

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
    public int getSender();

}
