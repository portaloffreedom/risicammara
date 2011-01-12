/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava.playerManage;
import java.util.ArrayList;
import java.util.List;
import risicammarajava.Colore_t;
/**
 *
 * @author stengun
 */
public class ListaPlayers {
    private List <Giocatore> listaPlayers;
    public ListaPlayers(){
        listaPlayers = new ArrayList<Giocatore>();
    }
    /**
     * Aggiunge un giocatore direttamente in lista.
     * @param nome Stringa contenente il nome giocatore
     * @param colore_armate Colore delle armate del giocatore
     */
    public void addPlayer(String nome,Colore_t colore_armate){
        this.addPlayer(new Giocatore(nome, colore_armate));
    }
    /**
     * Aggiunge un oggetto giocatore alla lista.
     * @param player L'oggetto giocatore da inserire in lista
     */
    public void addPlayer(Giocatore player){
        if(!player.getArmyColour().equals(Colore_t.NULLO))
        for(int i = 0;i<listaPlayers.size();i++){
            if(listaPlayers.get(i).getArmyColour() == player.getArmyColour()){
                System.err.println("Errore: Giocatore non inserito. Colore già preso da un'altro giocatore");
                return;
            }
        }
        listaPlayers.add(player);
    }

    /**
     * Imposta il numero delle armate disponibili da mettere nella plancia.
     * @param num_player L'indice del giocatore
     * @param num_armate Il numero di armate.
     */
    public void setArmate(int num_player,int num_armate){
        listaPlayers.get(num_player).setArmatedisponibili(num_armate);
    }

    /**
     * Fornisce il numero di giocatori
     * @return Numero di giocatori
     */
    public int getSize(){
        return listaPlayers.size();
    }

    /**
     * Preleva l'oggetto giocadore all'indice specificato
     * @param index L'indice dal quale prelevare il giocatore
     * @return L'oggetto Giocatore indicato dall'indice
     */
    public Giocatore get(int index){
        return listaPlayers.get(index);
    }
    
    public void remPlayer(int index) throws UnsupportedOperationException, IndexOutOfBoundsException{
        this.listaPlayers.remove(index);
    }

}
