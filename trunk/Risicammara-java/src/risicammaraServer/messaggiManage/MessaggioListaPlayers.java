/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import risicammaraJava.playerManage.ListaPlayers;

/**
 * Messaggio che invia l'intera lista dei giocatori.
 * @deprecated Questo messaggio non viene usato in quanto la lista dei
 * giocatori viene mandata soltanto all'atto della connessione con il server
 * e non viene più inviata dopo l'inizio della partita in quanto non si può
 * connettere nessuno mentre si sta giocando.
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
