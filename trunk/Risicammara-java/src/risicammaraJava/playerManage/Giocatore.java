package risicammaraJava.playerManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import risicammaraClient.*;
import risicammaraJava.deckManage.Carta;

/**
 * Oggetto di tipo giocatore che include il nome, il colore delle armate e
 * le armate che può inserire in quel dato turno.
 * @author stengun
 */
public class Giocatore implements Serializable {

    /** Nome del giocatore.*/
    protected String nome;
    /** Numero di armate disponibili per la fase rinforzo.*/
    private int armate_bonus;
    /** Il colore delle armate di questo giocatore.*/
    protected Colore_t colore_armate;
    /** Le carte in mano al giocatore */
    private ArrayList<Carta> carte;
    /** L'obbiettivo del giocatore */
    private Obbiettivi_t obbiettivo;
    /** Se il giocatore è pronto per giocare */
    private boolean ready;

/**
 * Inizializza con i valori di default tutti i campi del giocatore eccetto il
 * nome (inizializzato con nome ) e colore delle armate (inizializzato
 * con colore_armate )
 * @param nome Il nome del giocatore (String)
 * @param colore_armate Il colore delle armate (Color_t)
 */
    public Giocatore(String nome,Colore_t colore_armate){
        this.ready = false;
        this.nome = nome;
        this.colore_armate = colore_armate;
        this.armate_bonus = 0;
        this.carte = new ArrayList<Carta>();
        this.listaterr = new ArrayList<territori_t>();
        this.obbiettivo = null;
    }

    /**
     * Serve per leggere il nome del giocatore
     * @return Nome del giocatore (String)
     */
    public String getNome(){
        return nome;
    }

    /**
     * Serve per leggere il colore delle armate del giocatore
     * @return Colore delle armate (::Colore_t)
     */
    public Colore_t getArmyColour(){
        return colore_armate;
    }

    /**
     * Leggere le armate bonus senza toccarle
     * @return il numero di armate che il giocatore può inserire (int)
     */
    public int getArmateperturno(){
        return armate_bonus;
    }

    /**
     * Modifica il numero di armate disponibili per quel giocatore
     * @param armate Numero di armate disponibili per il giocatore
     */
    public void setArmatedisponibili(int armate){
        this.armate_bonus = armate;
    }

    /**
     * Restituisce il numero di territori posseduti dal giocatore
     * @return  Numero dei territori del giocatore
     */
    public int getNumTerritori(){
        return listaterr.size();
    }

    /**
     * Ritorna il numero di carte possedute dal giocatore
     * @return Numero di carte del giocatore
     */
    public int numCarte(){
        return carte.size();
    }

    /**
     * Restituisce l'oggetto Obbiettivo del giocatore
     * @return oggetto "obbiettivo" del giocatore
     */
    public Obbiettivi_t getObbiettivo(){
        return obbiettivo;
    }

    /** Lista dei territori posseduti dal giocatore */
    private List<territori_t> listaterr;

    /**
     * Richiedi la lista dei territori posseduti dal giocatore
     * @return lista dei territori del giocatore.
     */
    public List<territori_t> getListaterr(){
        return listaterr;
    }

    /**
     * Aggiunge un territorio alla lista di quelli posseduti dal giocatore
     * @param territorio Il territorio da aggiungere
     */
    public void addTerr(territori_t territorio){
        listaterr.add(territorio);
    }

    /**
     * Imposta l'obbiettivo al giocatore
     * @param obj L'obbiettivo da assegnare al giocatore
     */
    public void setObj(Obbiettivi_t obj){
        this.obbiettivo = obj;
    }

    /**
     * Aggiunge una carta al mazzo del gicoatore
     * @param card La carta da aggiungere
     */
    public void addCard(Carta card){
        carte.add(card);
    }

    @Override
    public String toString(){
        return this.nome;
    }

    /**
     * Imposta il nome al giocatore (in caso di cambio nome)
     * @param nom il nuovo nome
     */
    public void setNome(String nom){
        this.nome = nom;
    }

    /**
     * Cambia il colore delle armate del giocatore (in caso di cambio colore)
     * @param col Nuovo colore.
     */
    public void setArmyColour(Colore_t col){
        this.colore_armate = col;
    }
/**
 * Chiede se il giocatore è pronto per giocare.
 * @return True se è pronto, False altrimenti
 */
    public boolean isReady() {
        return ready;
    }
/**
 * Serve per impostare lo stato di Ready di un giocatore.
 * @param ready true per impostare a Ready, false per impostare non ready
 */
    public void setReady(boolean ready) {
        this.ready = ready;
    }


}
