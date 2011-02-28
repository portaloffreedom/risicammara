package PacchettoGrafico;


/**
 * Implementa un timer.
 * @author matteo
 */
public class OrologioTimer {

    private long firstTime;
    private long tempoPrima;
    private int millisecondiDiEsecuzione;

    /**
     * Inizializza tutti i dati utili della classe
     */
    public OrologioTimer(){
        //this.lastTime=Calendar.getInstance().getTimeInMillis();
        this.tempoPrima = this.firstTime=System.currentTimeMillis();
        this.millisecondiDiEsecuzione = 0;
    }

    /**
     * Restituisce il tempo passato
     * @return il tempo passato
     */
    public long tempoPassato(){
        long tempoDopo= System.currentTimeMillis();
        this.millisecondiDiEsecuzione= (int) (tempoDopo - this.tempoPrima);
        this.tempoPrima= tempoDopo;
        return tempoDopo-this.firstTime;
    }

    /**
     * Restituisce il tempo impiegato dalla chiamata di tempoPassato o dal Costruttore.
     * @return il tempo trascorso.
     */
    public long getEsecTime() {
        long tempoDopo= System.currentTimeMillis();
        this.millisecondiDiEsecuzione= (int) (tempoDopo - this.tempoPrima);
        return this.millisecondiDiEsecuzione;
    }

}
