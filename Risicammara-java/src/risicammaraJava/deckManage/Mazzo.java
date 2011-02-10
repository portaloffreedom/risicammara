package risicammaraJava.deckManage;

import java.util.Random;

/**
 * Classe che rappresenta un oggetto di tipo Mazzo (contenitore di carte) e le
 * azioni che è possibile effettuare su di esso (Pescare una carta o mischiare
 * l'intero mazzo)
 * @author stengun
 */


public abstract class Mazzo {

    protected Carta[] deck;
    protected int inizio_mazzo;
/**
 * Costruttore Predefinito del mazzo. Inizializza tutte le variabili
 * e l'array che rappresenta il contenitore delle carte.
 * @param numcarte Numero delle carte che dovrà avere il mazzo
 */
    protected Mazzo(Carta[] tipo){
        deck = tipo;
        inizio_mazzo = deck.length;
    }

/**
 * Testa se il mazzo delle carte è vuoto
 * @return true se il mazzo non ha più carte, falso altrimenti.
 */
    protected boolean isEmpty(){
        if(inizio_mazzo == 0)
            return true;
        else return false;
    }

/**
 * Miscela il mazzo di carte.
 */
    protected void Mischia(){
      int asd = 0;
      while(asd < 500){
          asd++;
          Random random = new Random();
          for(int i=0;i<inizio_mazzo;i++){
              Carta temp;
              int r = random.nextInt(inizio_mazzo);
              temp = deck[i];
              deck[i] = deck[r];
              deck[r] = temp;
          }
      }
    }
/** Preleva una carta dal mazzo*/
    public abstract Carta Pesca();
}
