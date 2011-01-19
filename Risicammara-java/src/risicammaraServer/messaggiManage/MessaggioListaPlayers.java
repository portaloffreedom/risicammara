/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraJava.playerManage.ListaPlayers;

/**
 *
 * @author stengun
 */
public class MessaggioListaPlayers implements Messaggio{
    private ListaPlayers listagiocatori;

    public MessaggioListaPlayers(ListaPlayers listagiocatori) {
        this.listagiocatori = listagiocatori;
    }

    public ListaPlayers getListagiocatori() {
        return listagiocatori;
    }

    public messaggio_t getType() {
        return messaggio_t.PLAYERLIST;
    }

    public int getSender() {
        return -1;
    }

}
