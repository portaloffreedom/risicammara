package PacchettoGrafico;

import risicammaraJava.turnManage.Fasi_t;

/**
 * Contatore delle fasi per la BarraFasi.
 * @author matteo
 */
public class ContatoreFasi {
    private int fase;

    static final private int FINETURNO   = 0;
    static final private int ATTESA      = 0;
    static final private int RINFORZO    = 1;
    static final private int ATTACCO     = 2;
    static final private int SPOSTAMENTI = 3;

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
    public Fasi_t getFase(){
        switch (fase){
            default:
                System.err.println("Errore nel prelevare la fase: fase prelevata"
                        + " come fineturno");
            case FINETURNO:
                return Fasi_t.FINETURNO;
                
            case RINFORZO:
                return Fasi_t.RINFORZO;
                
            case ATTACCO:
                return Fasi_t.ATTACCO;
                
            case SPOSTAMENTI:
                return Fasi_t.SPOSTAMENTO;
        }
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

    /**
     * Ritorna alla fase 0. La fase 0 è "turno degli altri giocatori".
     */
    public void resetta(){
        fase = 0;
    }

}
