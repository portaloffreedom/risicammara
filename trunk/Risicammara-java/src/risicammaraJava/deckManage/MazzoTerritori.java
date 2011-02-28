package risicammaraJava.deckManage;

import risicammaraClient.territori_t;

/**
 * Rappresenta il mazzo delle carte territorio e il corrispettivo mazzo
 * degli scarti. Si occupa di gestire lo scarto delle carte e la preparazione
 * del mazzo quando le carte finiscono e vanno usate quelle degli scarti.
 * @author Sten_Gun
 */
public final class MazzoTerritori extends Mazzo {
    private Carta scarti[];
    private int inizio_scarti;

    /**
     * Inizializza tutto il necessario per il mazzo di carte.
     */
    public MazzoTerritori(){
        super(territori_t.values());
        inizio_scarti = 0;
        scarti = null;
        Mischia();
    }


/**
 * Consente di prelevare la carta in cima al mazzo.
 * La funzione preleva la carta dalla cima del mazzo e la elimina dal
 * contenitore delle carte.
 * @return riferimento alla carta presente in cima al mazzo
 */
    @Override
    public Carta Pesca(){
        if(isEmpty()){
            if(scarti == null) return null;
            ScambiaMazzi();
        }
        Carta temp = deck[--inizio_mazzo];
        return temp;
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

    /**
     * Restituisce una carta a partire da un preciso indice.
     * @param index l'indice dove prelevare la carta
     * @return la carta prelevata
     */
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
