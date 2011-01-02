package risicammarajava.playerManage;

import java.util.ArrayList;
import java.util.List;
import risicammarajava.Colore_t;
import risicammarajava.territori_t;

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
    public int getNumTerritori(){
        return listaterr.size();
    }
    public int numCarte(){
        return carte;
    }

    private List<territori_t> listaterr;
    public void addTerr(territori_t territorio){
        listaterr.add(territorio);
    }
    public void incCarte(){
        carte +=1;
    }
}
