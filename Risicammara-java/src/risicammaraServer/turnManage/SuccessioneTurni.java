/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.turnManage;

import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.Partita;
import risicammaraServer.CodaMsg;
import risicammaraServer.Giocatore_Net;

/**
 *
 * @author Sten_Gun
 */
public class SuccessioneTurni {
    private CodaMsg coda;
    private ListaPlayers listaGiocatori;

    public SuccessioneTurni(ListaPlayers listaGiocatori,CodaMsg coda){
        this.coda = coda;
        this.listaGiocatori = listaGiocatori;
    };
    public Giocatore_Net start(){
        Partita partita = new Partita(listaGiocatori);
        
        return null;
    }
}
