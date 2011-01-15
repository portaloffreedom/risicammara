/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.playerManage;
import java.io.Serializable;
import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import risicammaraClient.Colore_t;
/**
 *
 * @author stengun
 */
public class ListaPlayers implements Serializable {
    private ArrayList <Giocatore> listaPlayers;
    private int nullnumber;
    public static int MAXPLAYERS = 6;

    public ListaPlayers(){
        listaPlayers = new ArrayList<Giocatore>();
        for(int i=0;i<MAXPLAYERS;i++) listaPlayers.add(null);
        listaPlayers.trimToSize();
        this.nullnumber = MAXPLAYERS;
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
        if(index < 0) {
            throw new IndexOutOfBoundsException();
        }
        nullnumber--;
        listaPlayers.set(index,player);
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
        return listaPlayers.size()-nullnumber;
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
    /**
     * Rimuove un giocatore dalla lista
     * @param index l'indice assegnato al giocatore
     */
    public void remPlayer(int index){
        this.listaPlayers.set(index, null);
        this.nullnumber++;
    }
/**
 * Il nickname del giocatore con un dato indice
 * @param index l'indice del giocatore
 * @return il nick del giocatore
 */
    public String getNomeByIndex(int index){
        if(index == -1  ) return "SERVER";
        if(listaPlayers.isEmpty() || index < 0 || listaPlayers.get(index) == null) return "NAME_ERROR";
        return listaPlayers.get(index).getNome();
    }

}
