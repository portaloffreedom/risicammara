package risicammarajava.playerManage;

import java.util.ArrayList;
import java.util.List;
import risicammarajava.*;

/**
 * Oggetto di tipo giocatore che include il nome, il colore delle armate e
 * le armate che può inserire in quel dato turno.
 * @author stengun
 */
public class Giocatore {
    private String nome;
    private int armate_bonus;
    private Colore_t colore_armate;
    private int numero_territori;
    private int carte;
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
        this.carte = 0;
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
        return carte;
    }
    /**
     * Restituisce l'oggetto Obbiettivo del giocatore
     * @return oggetto "obbiettivo" del giocatore
     */
    public Obbiettivi_t getObbiettivo(){
        return obbiettivo;
    }
    /**
     * Restituisce l'obbiettivo del giocatore sottoforma di Stringa
     * @return la stringa che descrive l'obbiettivo
     */
    public String getObbiettivo_String(){
        return obbiettivo.getTesto();
    }
    /**
     * Lista dei territori posseduti dal giocatore
     */
    private List<territori_t> listaterr;

    /**
     * Aggiunge un territorio alla lista di quelli posseduti dal giocatore
     * @param territorio Il territorio da aggiungere
     */
    public void addTerr(territori_t territorio){
        listaterr.add(territorio);
    }
    /**
     * Incrementa il numero di carte possedute dal giocatore
     */
    public void incCarte(){
        carte +=1;
    }
    /**
     * Decrementa il numero di carte possedute dal giocatore
     */
    private void decCarte(){
        if(carte<1) return;
        carte -=1;
    }
    /**
     * Incrementa il numero di territori poseduti dal giocatore
     */
    public void incTerritori(){
        numero_territori += 1;
    }
    /**
     * Decrementa il numero di territori posseduti dal giocatore
     */
    public void decTerritori(){
        if(numero_territori < 1) return;
        numero_territori -=1;
    }
    public void setObj(Obbiettivi_t obj){
        this.obbiettivo = obj;
    }
}
