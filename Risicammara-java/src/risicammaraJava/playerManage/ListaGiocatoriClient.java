/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.playerManage;

import risicammaraClient.Obbiettivi_t;

/**
 *
 * @author matteo
 */
public class ListaGiocatoriClient extends ListaPlayers {
    private int indexGiocatore;

    public ListaGiocatoriClient(ListaPlayers listaGiocatori, int indexGiocatore, Obbiettivi_t mioObbiettivo) {
        super(listaGiocatori);
        this.indexGiocatore = indexGiocatore;
        this.aggiungiObbiettivoMio(mioObbiettivo);
    }

    public Giocatore meStesso(){
        return this.get(indexGiocatore);
    }

    public int meStessoIndex(){
        return indexGiocatore;
    }

    private void aggiungiObbiettivoMio(Obbiettivi_t obbiettivo){
        Giocatore catz = listaPlayers.get(indexGiocatore);
        if(catz == null){
            System.out.println("Giocatore nullo: "+indexGiocatore);
            System.exit(133);
        }
        catz.setObj(obbiettivo);
    }
}
