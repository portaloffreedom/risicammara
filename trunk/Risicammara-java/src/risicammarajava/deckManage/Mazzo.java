package risicammarajava.deckManage;
import risicammarajava.territori_t;
import java.util.Random;
/**
 * Classe che rappresenta un oggetto di tipo Mazzo (contenitore di carte) e le
 * azioni che è possibile effettuare su di esso (Pescare una carta o mischiare
 * l'intero mazzo)
 * @author stengun
 */


public abstract class Mazzo {

    protected Carta[] deck;
/**
 * Costruttore Predefinito del mazzo. Inizializza tutte le variabili
 * e l'array che rappresenta il contenitore delle carte.
 * @param numcarte Numero delle carte che dovrà avere il mazzo
 */
    public Mazzo(Carta[] tipo){
        deck = tipo;
    }

/**
 * Miscela il mazzo di carte.
 */
    protected abstract void Mischia();

    public abstract Carta Pesca();
}
