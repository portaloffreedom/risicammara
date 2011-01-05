package risicammarajava.playerManage;

import java.util.ArrayList;
import java.util.List;
import risicammarajava.*;
import risicammarajava.deckManage.Carta;

/**
 * Oggetto di tipo giocatore che include il nome, il colore delle armate e
 * le armate che può inserire in quel dato turno.
 * @author stengun
 */
public class Giocatore {

    private String nome;
    private int armate_bonus;
    private Colore_t colore_armate;
    private ArrayList<Carta> carte;
    private Obbiettivi_t obbiettivo;

/**
 * Il costruttore dell'oggetto giocatore. Non viene inizializzato il campo per
 * le armate che il giocatore mette ad ogni turno, poiché si effettua tramite
 * una funzione dedicata.
 * @param nome Il nome del giocatore (String)
 * @param colore_armate Il colore delle armate (Color_t)
 */
    public Giocatore(String nome,Colore_t colore_armate){
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

    /**
     * Lista dei territori posseduti dal giocatore
     */
    private List<territori_t> listaterr;

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
}
