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
    private List <Giocatore> listaplayers;
    public ListaPlayers(){
        listaplayers = new ArrayList<Giocatore>();
    }
    public void addPlayer(String nome,Colore_t colore_armate){
        for(int i = 0;i<listaplayers.size();i++){
            if(listaplayers.get(i).getArmyColour() == colore_armate){
                return; 
            }
        }
        listaplayers.add(new Giocatore(nome,colore_armate));
    }

        public void addPlayer(Giocatore player){
        for(int i = 0;i<listaplayers.size();i++){
            if(listaplayers.get(i).getArmyColour() == player.getArmyColour()){
                return;
            }
        }
        listaplayers.add(player);
    }

    public void setArmate(int num_player,int num_armate){
        listaplayers.get(num_player).setArmatedisponibili(num_armate);
    }

    public int getSize(){
        return listaplayers.size();
    }
    public Giocatore get(int index){
        return listaplayers.get(index);
    }
}
