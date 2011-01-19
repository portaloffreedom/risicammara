/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraClient.territori_t;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioDichiaraAttacco implements Messaggio {
    private territori_t territorio_attaccante,territorio_difensore;
    private int sender;

    public MessaggioDichiaraAttacco(territori_t territorio_attaccante, territori_t territorio_difensore, int sender) {
        this.territorio_attaccante = territorio_attaccante;
        this.territorio_difensore = territorio_difensore;
        this.sender = sender;
    }

    public territori_t getTerritorio_attaccante() {
        return territorio_attaccante;
    }

    public territori_t getTerritorio_difensore() {
        return territorio_difensore;
    }


    public messaggio_t getType() {
        return messaggio_t.DICHIARAATTACCO;
    }

    public int getSender() {
        return sender;
    }

}
