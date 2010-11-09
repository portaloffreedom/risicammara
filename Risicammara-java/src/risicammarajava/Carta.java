package risicammarajava;

/**
 * Classe che rappresenta la carta del mazzo.
 * Questa classe è usata nel mazzo e nel deck dei giocatori.
 * @author stengun
 */
public class Carta {
    private territori_t territorio = null;

    /**
     * Genera la carta in base al territorio passato al costruttore.
     * @param territorio Il territorio la cui carta rappresenta.
     */
    public Carta(territori_t territorio){
        this.territorio = territorio;
    }

    /**
     * Restituire il valore del territorio della carta.
     * @return Territorio che la carta rappresenta.
     */
    public territori_t getTerritorio(){
        return this.territorio;
    }

    /**
     * Interfaccia per restituire il Bonus che la carta rappresenta
     * @return Il bonus del territorio che la carta rappresenta.
     */
    public Bonus_t getBonus(){
        return this.territorio.getBonus();
    }
}
