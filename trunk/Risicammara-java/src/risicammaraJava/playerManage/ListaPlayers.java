package risicammaraJava.playerManage;

import java.io.Serializable;
import java.util.ArrayList;
import risicammaraClient.Colore_t;

/**
 * Classe che implementa la lista dei giocatori che partecipano alla partita.
 * Questa classe implementa metodi specifici per gestire una lista che è sempre
 * piena di oggetti di tipo null.
 * @author stengun
 */
public class ListaPlayers implements Serializable {
/** Oggetto che contiene la lista giocatori*/
    protected ArrayList <Giocatore> listaPlayers;
    private int nullnumber;

/**
 * Indicai l massimo numero possibile di giocatori.
 */
    public static final int MAXPLAYERS = 6;

/**
 * Costruttore che inizializza tutti i componenti della lista come null.
 */
    public ListaPlayers(){
        listaPlayers = new ArrayList<Giocatore>();
        for(int i=0;i<MAXPLAYERS;i++) listaPlayers.add(null);
        listaPlayers.trimToSize();
        this.nullnumber = MAXPLAYERS;
    }

/**
 * Crea una lista copia della lista passata come parametro
 * @param listaGiocatori lista sorgente
 */
    protected ListaPlayers (ListaPlayers listaGiocatori) {
        this();
        for (int i=0;i<ListaPlayers.MAXPLAYERS;i++) {
            this.addPlayer(listaGiocatori.get(i));
        }
    }


/**
 * Richiede il primo giocatore non nullo partendo dall'inizio.
 * @return il Giocatore corrispondente
 */
    public Giocatore getFirst(){
        return this.getFirst(-1);
    }

/**
 * Richiede il primo giocatore non nullo partendo da un indice
 * @param _from_index L'indice da cui partire per cercare il primo non nullo
 * @return il giocatore trovato
 */
    public Giocatore getFirst(int _from_index){
        if(this.isEmpty()) return null;
        Giocatore gio = null;
        for(int i=_from_index+1;i<MAXPLAYERS;i++){
            gio = listaPlayers.get(i);
            if(gio==null)continue;
            break;
        }
        return gio;
    }

/**
 * Aggiunge un giocatore direttamente in lista.
 * @param nome Stringa contenente il nome giocatore
 * @param colore_armate Colore delle armate del giocatore
 * @return
 */
    public int addPlayer(String nome,Colore_t colore_armate){
        return this.addPlayer(new Giocatore(nome, colore_armate));
    }
/**
 * Aggiunge un oggetto giocatore alla lista.
 * @param player L'oggetto giocatore da inserire in lista
 * @return
 */
    public final int addPlayer(Giocatore player){
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
 * Controlla se la lista dei giocatori è vuota
 * @return True se è vuota, false altrimenti.
 */
    public boolean isEmpty(){
        if(this.getSize() == 0) return true;
        return false;
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
        if(listaPlayers.isEmpty() || index < 0 || listaPlayers.get(index) == null)
            return "NAME_ERROR";
        return listaPlayers.get(index).getNome();
    }

}
