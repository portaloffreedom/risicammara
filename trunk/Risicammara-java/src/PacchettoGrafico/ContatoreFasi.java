/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

/**
 * Contatore delle fasi per la BarraFasi.<p>
 * Fase 0 = turno di altri giocatori.<br>
 * Fase 1 = rinforzo - posiziona armate.<br>
 * Fase 2 = attacco.<br>
 * Fase 3 = spostamenti.<br>
 * @author matteo
 */
public class ContatoreFasi {
    private int fase;
    static final public int FINETURNO   = 0;
    static final public int ATTESA      = 0;
    static final public int RINFORZO    = 1;
    static final public int ATTACCO     = 2;
    static final public int SPOSTAMENTI = 3;

    /**
     * Costruttore. Inizializza la fase a 0.
     */
    public ContatoreFasi() {
        fase = 0;
    }

    /**
     * Ritorna la fase in cui sei arrivato.<p>
     * Fase ATTESA      (0) = turno di altri giocatori.<br>
     * Fase RINFORZO    (1) = rinforzo - posiziona armate.<br>
     * Fase ATTACCO     (2) = attacco.<br>
     * Fase SPOSTAMENTI (3) = spostamenti.<br>
     * @return
     */
    public int getFase(){
        return fase;
    }

    /**
     * Passa alla fase successiva. È ciclico, invece che raggiungere la fase 5,
     * raggiunge la fase 0.
     */
    public void avanzaFase(){
        fase = (fase+1)%4;
    }

    /**
     * Avanza alla fase successiva. È ciclico: dalla fase 4 ritorna alla fase 0.
     * @param avanzamento
     */
    public void avanzaFase(int avanzamento){
        fase = (fase+avanzamento)%4;
    }

    public void setFase(int fase){
        this.fase = fase;
    }

    /**
     * Ritorna alla fase 0. La fase 0 è "turno degli altri giocatori".
     */
    public void resetta(){
        fase = 0;
    }

}
