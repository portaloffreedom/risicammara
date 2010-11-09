package risicammarajava;
import java.util.Random;

/**
 * Classe che rappresenta un oggetto di tipo Mazzo (contenitore di carte) e le
 * azioni che è possibile effettuare su di esso (Pescare una carta o mischiare
 * l'intero mazzo)
 * @author stengun
 */


public class Mazzo {

    private Carta[] deck;
    private Carta[] scarti;
    private int inizio_mazzo;
    private int inizio_scarti;
/**
 * Costruttore Predefinito del mazzo. Inizializza tutte le variabili
 * e l'array che rappresenta il contenitore delle carte.
 * @param numcarte Numero delle carte che dovrà avere il mazzo
 */
    public Mazzo(int numcarte){
        inizio_mazzo = numcarte;
        inizio_scarti = 0;
        scarti = null;
        deck = new Carta[numcarte];
        int i = 0;
        /*
         * Vengono inserite tante carte quante sono le costanti dell'enumerato
         * ::territori_t
         */
        for(territori_t t:territori_t.values()){
            deck[i] = new Carta(t);
            i++;
        }
        Mischia();
    }
    
/**
 * Consente di prelevare la carta in cima al mazzo.
 * La funzione preleva la carta dalla cima del mazzo e la elimina dal
 * contenitore delle carte.
 * @return riferimento alla carta presente in cima al mazzo
 */
    public Carta Pesca(){
        if(isEmpty()){
            if(scarti == null) return null;
            ScambiaMazzi();
        }
        Carta temp = deck[inizio_mazzo--];
        return temp;
    }
/**
 * Miscela il mazzo di carte.
 */
    private void Mischia(){
        Random random = new Random();
        for(int i=0;i<inizio_mazzo;i++){
            Carta temp;
            int r = random.nextInt()%inizio_mazzo;
            temp = deck[i];
            deck[i] = deck[r];
            deck[r] = temp;
        }
    }

/**
 * Aggiunge una carta al mazzo degli scarti
 * @param carta Il riferimento alla carta scartata
 */
    public void AddDiscardedCard(Carta carta){
        if(scarti == null){
            scarti = new Carta[deck.length];
        }
        scarti[inizio_scarti++] = carta;
    }

/**
 * Gli scarti diventano il mazzo da cui pescare le carte
 * e il mazzo degil scarti viene "distrutto" (null pointer)
 */
    private void ScambiaMazzi(){
        deck = scarti;
        scarti = null;
        inizio_mazzo = inizio_scarti;
        inizio_scarti = 0;
        Mischia();
    }
    
/**
 * Testa se il mazzo delle carte è vuoto
 * @return true se il mazzo non ha più carte, falso altrimenti.
 */
    private boolean isEmpty(){
        if(inizio_mazzo == 0)
            return true;
        else return false;
    }
}
