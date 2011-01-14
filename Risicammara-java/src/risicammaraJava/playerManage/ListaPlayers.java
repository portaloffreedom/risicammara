/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.playerManage;
import java.io.Serializable;
import java.util.ArrayList;
import risicammaraClient.Colore_t;
/**
 *
 * @author stengun
 */
public class ListaPlayers implements Serializable {
    private ArrayList <Giocatore> listaPlayers;
    public ListaPlayers(){
        listaPlayers = new ArrayList<Giocatore>();
    }
    /**
     * Aggiunge un giocatore direttamente in lista.
     * @param nome Stringa contenente il nome giocatore
     * @param colore_armate Colore delle armate del giocatore
     */
    public int addPlayer(String nome,Colore_t colore_armate){
        return this.addPlayer(new Giocatore(nome, colore_armate));
    }
    /**
     * Aggiunge un oggetto giocatore alla lista.
     * @param player L'oggetto giocatore da inserire in lista
     */
    public int addPlayer(Giocatore player){
        int index = listaPlayers.indexOf(null);
        if(index < 0) index = listaPlayers.size();
        listaPlayers.add(index,player);
        return index;
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
        // (listaPlayers.isEmpty() || index < 0) return null;
        return listaPlayers.get(index);
    }
    
    public void remPlayer(int index){
        this.listaPlayers.add(index,null);
    }

    public String getNomeByIndex(int index){
        if(index == -1  ) return "SERVER";
        if(listaPlayers.isEmpty() || index < 0 || listaPlayers.get(index) == null) return "NAME_ERROR";
        return listaPlayers.get(index).getNome();
    }

}
