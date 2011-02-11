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
        this.oceania = -Continente_t.OCEANIA.getNumterritori();
        this.europa = -Continente_t.EUROPA.getNumterritori();
        this.nordamerica = -Continente_t.NORDAMERICA.getNumterritori();
        this.africa = -Continente_t.AFRICA.getNumterritori();
        this.asia = -Continente_t.ASIA.getNumterritori();
        this.sudamerica = -Continente_t.SUDAMERICA.getNumterritori();
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

    //----- Metodi e variabili per il trattamento dei territori.
    private int oceania;
    private int europa;
    private int nordamerica;
    private int africa;
    private int asia;
    private int sudamerica;

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
        switch(territorio.getContinente()){
            case AFRICA:
                this.africa++;
                break;
            case ASIA:
                this.asia++;
                break;
            case EUROPA:
                this.europa++;
                break;
            case NORDAMERICA:
                this.nordamerica++;
                break;
            case OCEANIA:
                this.oceania++;
                break;
            case SUDAMERICA:
                this.sudamerica++;
            default:
                break;
        }

        listaterr.add(territorio);
    }
    /**
     * Rimuove un dato territorio dalla lista dei territori del giocatore.
     * @param territorio il territorio da rimuovere.
     */
    public void remTerr(territori_t territorio){
        switch(territorio.getContinente()){
            case AFRICA:
                this.africa--;
                break;
            case ASIA:
                this.asia--;
                break;
            case EUROPA:
                this.europa--;
                break;
            case NORDAMERICA:
                this.nordamerica--;
                break;
            case OCEANIA:
                this.oceania--;
                break;
            case SUDAMERICA:
                this.sudamerica--;
            default:
                break;
        }
        listaterr.remove(territorio);
    }
    /**
     * Chiede il numero di continenti posseduto dal giocatore.
     * @return il numero di continenti posseduti dal giocatore.
     */
    public int numContinenti(){
        int numcon =0;
        for(Continente_t c : Continente_t.values()){
            if(hasContinente(c)) numcon++;
        }
        return numcon;
    }
/**
 * Indica se il giocatore possiede un preciso continente.
 * @param continente il continente da verificare
 * @return True se il giocatore lo possiede, false altrimenti.
 */
    public boolean hasContinente(Continente_t continente){
        switch(continente){
            case AFRICA:
                if(africa == 0) return true;
                return false;
            case ASIA:
                if(asia == 0) return true;
                return false;
            case EUROPA:
                if(europa == 0) return true;
                return false;
            case NORDAMERICA:
                if(nordamerica == 0) return true;
                return false;
            case OCEANIA:
                if(oceania == 0 ) return true;
                return false;
            case SUDAMERICA:
                if(sudamerica == 0) return true;
            default:
                return false;
        }
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
