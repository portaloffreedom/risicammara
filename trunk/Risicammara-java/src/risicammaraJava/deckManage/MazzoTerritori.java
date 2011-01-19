/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.deckManage;

import java.util.Random;
import risicammaraClient.territori_t;

/**
 *
 * @author Sten_Gun
 */
public final class MazzoTerritori extends Mazzo {
    private Carta scarti[];
    private int inizio_scarti;

    public MazzoTerritori(){
        super(territori_t.values());
        inizio_scarti = 0;
        scarti = null;
        Mischia();
        Mischia();
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
 * e il mazzo degli scarti viene "distrutto" (null pointer)
 */
    private void ScambiaMazzi(){
        deck = scarti;
        scarti = null;
        inizio_mazzo = inizio_scarti;
        inizio_scarti = 0;
        this.Mischia();
        this.Mischia();
    }

    public territori_t getCard(int index){
        if((index > deck.length) | (index < 1)) return null;
        index -=1;
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
