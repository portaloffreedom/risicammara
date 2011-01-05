/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava.deckManage;

import java.util.Random;
import risicammarajava.territori_t;

/**
 *
 * @author Sten_Gun
 */
public final class MazzoTerritori extends Mazzo {
    private Carta[] scarti;
    private int inizio_mazzo;
    private int inizio_scarti;

    public MazzoTerritori(){
        super(territori_t.values());
        inizio_mazzo = deck.length;
        inizio_scarti = 0;
        scarti = null;
        this.Mischia();
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
        Carta temp = deck[--inizio_mazzo];
        return temp;
    }

    
    protected void Mischia(){
        Random random = new Random();
        for(int i=0;i<inizio_mazzo;i++){
            Carta temp;
            int r = random.nextInt(inizio_mazzo);
            temp = deck[i];
            deck[i] = deck[r];
            deck[r] = temp;
        }
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
        this.Mischia();
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

    public territori_t getCard(int index){
        if(index >0) index = index-1;
        if(index > deck.length) return null;
        return (territori_t)deck[index];
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

}