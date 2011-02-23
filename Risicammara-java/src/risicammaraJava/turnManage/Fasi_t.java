package risicammaraJava.turnManage;

/**
 * Enumerato delle fasi con informazioni sulla possibilità o meno di poter
 * passare una fase.
 * @author Sten_Gun
 */
public enum Fasi_t {
    /**
     * Fase dove tutti i giocatori mettono le loro armate preliminari finché non
     * terminano.
     */
    PREPARTITA  (false,false),
    /**
     * Fase di rinforzo. Obbligatoria. Il giocatore di turno mette le armate
     * in base a quanti territori possiede, eventuali tris giocati e bonus
     * derivati dal possesso di interi continenti. Questa fase è Obbligatoria.
     */
    RINFORZO    (false,false),
    /**
     * Fase di attacco. Il giocatore sceglie se attaccare o meno uno o più
     * territori. Questa fase non è obbligatoria
     */
    ATTACCO     (true, false),
    /**
     * Questa fase sancisce sempre la fine del turno. è opzionale, ma se viene
     * effettuato lo spostamento la fase passa sempre a FINETURNO.
     * Se nell'attacco viene eliminato un giocatore si verifica se sono
     * soddisfatte le condizioni di vittoria per eliminazione e se è il caso si
     * modificano gli obbiettivi.
     * @see risicammaraClient.Obbiettivi_t
     */
    SPOSTAMENTO(true, true),
    /**
     * Fine del turno del giocatore. Se il giocatore ha conquistato uno o più
     * territori può prelevare una sola carta dal mazzo delle carta.
     * Se la fine del turno del giocatore coincide con la fine del turno di
     * tutti gli altri gicoatori vengono verificate le condizioni di vittoria
     * di tipo Territoriale.
     */
    FINETURNO   (false, true);
    private boolean isoptional;
    private boolean mustend;
    /**
     * Costruttore dell'oggetto Fase
     * @param is se la fase è opzionale
     * @param mustend se la fase sancisce la fine del turno
     */
    Fasi_t(boolean is ,boolean mustend){
        this.isoptional = is;
        this.mustend = mustend;
    }

    /**
     * Informa se la fase è opzionale
     * @return true se lo è, false altrimenti
     */
    public boolean isOptional(){
        return isoptional;
    }

    /**
     * informa se la fase sancisce la fine del turno
     * @return true se si finsice, false altrimenti.
     */
    public boolean mustEnd(){
        return mustend;
    }
   
    /**
     * Calcola il numero di passi necessari per passare dalla fase1 alla fase2
     * @param fase1 fase di partenza
     * @param fase2 fase di arrivo
     * @return numero di passi necessari
     */
    public static int getDistanzaFasi(Fasi_t fase1, Fasi_t fase2){
        int fase1I = getNumeroFase(fase1);
        int fase2I = getNumeroFase(fase2);
        
        int numeroPassi = fase2I - fase1I;
        
        if (numeroPassi < 0)
            numeroPassi += 4;
        
        return numeroPassi;
        
    }
    
    private static int getNumeroFase(Fasi_t fase){
        switch (fase){
            default:
            case FINETURNO:
                return 0;

            case RINFORZO:
                return 1;

            case ATTACCO:
                return 2;

            case SPOSTAMENTO:
                return 3;
        }
        
    }

}
