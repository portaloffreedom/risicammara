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
    private int inizio_mazzo;
/**
 * Costruttore Predefinito del mazzo. Inizializza tutte le variabili
 * e l'array che rappresenta il contenitore delle carte.
 */
    public Mazzo(){
        inizio_mazzo = 44;
        deck = new Carta[44];
        for(territori_t t:territori_t.values()){
            int i = 0;
            deck[i] = new Carta(t);
            i++;
        }
    }
/**
 * Consente di prelevare la carta in cima al mazzo.
 * La funzione preleva la carta dalla cima del mazzo e la elimina dal
 * contenitore delle carte.
 * @return riferimento alla carta presente in cima al mazzo
 */
    public Carta Pesca(){
        Carta temp = deck[inizio_mazzo--];
        return temp;
    }
/**
 * Miscela il mazzo di carte.
 */
    public void Mischia(){
        Random random = new Random();
        for(int i=0;i<deck.length;i++){
            Carta temp;
            int r = random.nextInt()%deck.length;
            temp = deck[i];
            deck[i] = deck[r];
            deck[r] = temp;
        }
    }

}
