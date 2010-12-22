/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;


/**
 *
 * @author matteo
 */
public class OrologioTimer {

    private long firstTime;
    private long tempoPrima;
    private long millisecondiDiEsecuzione;

    public OrologioTimer (){
        //this.lastTime=Calendar.getInstance().getTimeInMillis();
        this.tempoPrima = this.firstTime=System.currentTimeMillis();
        this.millisecondiDiEsecuzione = 0;
    }

    public long tempoPassato (){
        long tempoDopo= System.currentTimeMillis();
        this.millisecondiDiEsecuzione= tempoDopo - this.tempoPrima;
        this.tempoPrima= tempoDopo;
        return tempoDopo-this.firstTime;
    }

    public long getEsecTime() {
        return this.millisecondiDiEsecuzione;
    }

}
