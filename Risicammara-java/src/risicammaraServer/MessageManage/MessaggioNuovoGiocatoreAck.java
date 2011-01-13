/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

import risicammaraJava.playerManage.ListaPlayers;

/**
 *
 * @author Sten_Gun
 */
public class MessaggioNuovoGiocatoreAck implements Messaggio{

    private ListaPlayers listgioc;
    private int indexply;

    public MessaggioNuovoGiocatoreAck(ListaPlayers listgioc, int indexply) {
        this.listgioc = listgioc;
        this.indexply = indexply;
    }


    public messaggio_t getType() {
        return messaggio_t.OKADD;
    }

    public ListaPlayers getPlyList(){
        return listgioc;
    }

    public int getPlyIndex(){
        return indexply;
    }

    public int getSender() {
        return -1;
    }

}
