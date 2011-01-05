/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava.playerManage;
import java.util.ArrayList;
import java.util.List;
import risicammarajava.Colore_t;
import risicammarajava.playerManage.Giocatore;
/**
 *
 * @author stengun
 */
public class ListaPlayers {
    private List <Giocatore> listaPlayers;
    public ListaPlayers(){
        listaPlayers = new ArrayList<Giocatore>();
    }
    public void addPlayer(String nome,Colore_t colore_armate){
        this.addPlayer(new Giocatore(nome, colore_armate));
    }

        public void addPlayer(Giocatore player){
        for(int i = 0;i<listaPlayers.size();i++){
            if(listaPlayers.get(i).getArmyColour() == player.getArmyColour()){
                System.err.println("Errore: Giocatore non inserito. Colore già preso da un'altro giocatore");
                return;
            }
        }
        listaPlayers.add(player);
    }

    public void setArmate(int num_player,int num_armate){
        listaPlayers.get(num_player).setArmatedisponibili(num_armate);
    }

    public int getSize(){
        return listaPlayers.size();
    }
    public Giocatore get(int index){
        return listaPlayers.get(index);
    }
}
