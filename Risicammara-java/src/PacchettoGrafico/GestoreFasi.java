/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

/**
 *
 * @author matteo
 */
public class GestoreFasi {
    private BarraFasi barraFasi;
    private AscoltatoreFineTurno   ascoltatoreFineTurno;
    //private AscoltatoreRinforzo    ascoltatoreRinforzo;
    //private AscoltatoreAttacco     ascoltatoreAttacco;
    private AscoltatoreSpostamento ascoltatoreSpostamento;

    public GestoreFasi(BarraFasi barraFasi) {
        this.barraFasi = barraFasi;

        this.ascoltatoreFineTurno = new AscoltatoreFineTurno(this);
        //this.ascoltatoreRinforzo = new AscoltatoreRinforzo();
        //this.ascoltatoreAttacco = new AscoltatoreAttacco();
        this.ascoltatoreSpostamento = new AscoltatoreSpostamento(this);
        faseToAttesa();
    }

    final public void faseToAttesa(){
        barraFasi.resetFase();
        setAscoltatore(true, false);
    }

    public void avanzaFase(){
        barraFasi.avanzaFase();
        switch(barraFasi.getFase()){
            case ContatoreFasi.FINETURNO:
                setAscoltatore(true, false);
                return;

            case ContatoreFasi.RINFORZO:
                setAscoltatore(false, false);
                return;

            case ContatoreFasi.ATTACCO:
                setAscoltatore(true, true);
                return;

            case ContatoreFasi.SPOSTAMENTI:
                setAscoltatore(true, false);
                return;
        }
    }

    private void setAscoltatore(boolean fineTurno, boolean spostamenti){
        //FINE TURNO
        if (fineTurno)
            barraFasi.setAscoltatoreFineTurno(ascoltatoreFineTurno);
        else
            barraFasi.setAscoltatoreFineTurno(null);
        
        //RINFONZO
        /*if (rinforzo)
            barraFasi.setAscoltatoreRinforzi(ascoltatoreRinforzo);
        else
            barraFasi.setAscoltatoreRinforzi(null);*/

        //ATTACCO
        /*if (attacco)
            barraFasi.setAscoltatoreAttacco(ascoltatoreAttacco);
        else
            barraFasi.setAscoltatoreAttacco(null);*/

        //SPOSTAMENTI
        if (spostamenti)
            barraFasi.setAscoltatoreSpostamento(ascoltatoreSpostamento);
        else
            barraFasi.setAscoltatoreSpostamento(null);
    }

    private void setAscoltatore(int fase, boolean attivo){
        switch (fase) {
            case ContatoreFasi.FINETURNO:
                if (attivo)
                    barraFasi.setAscoltatoreFineTurno(ascoltatoreFineTurno);
                else
                    barraFasi.setAscoltatoreFineTurno(null);
                return;

            case ContatoreFasi.RINFORZO:/*
                if (attivo)
                    barraFasi.setAscoltatoreRinforzi(ascoltatoreRinforzo);
                else
                    barraFasi.setAscoltatoreRinforzi(null);*/
                return;

            case ContatoreFasi.ATTACCO:/*
                if (attivo)
                    barraFasi.setAscoltatoreAttacco(ascoltatoreAttacco);
                else
                    barraFasi.setAscoltatoreAttacco(null);*/
                return;

            case ContatoreFasi.SPOSTAMENTI:
                if (attivo)
                    barraFasi.setAscoltatoreSpostamento(ascoltatoreSpostamento);
                else
                    barraFasi.setAscoltatoreSpostamento(null);
                return;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="AscoltatoriPulsantiFase">
    private class AscoltatoreFineTurno implements RisicammaraEventListener {
        private GestoreFasi fasi;

        public AscoltatoreFineTurno(GestoreFasi fasi) {
            this.fasi = fasi;
        }

        public void actionPerformed(EventoAzioneRisicammara e) {
            //TODO Messaggio vai a fine turno
            fasi.faseToAttesa();
        }
    }

    private class AscoltatoreSpostamento implements RisicammaraEventListener {
        private GestoreFasi gestoreFasi;

        public AscoltatoreSpostamento(GestoreFasi gestoreFasi) {
            this.gestoreFasi = gestoreFasi;
        }
        
        public void actionPerformed(EventoAzioneRisicammara e) {
            //da attacco passa a spostamenti
            //TODO messaggio di fine fase attacco e inizio fase spostamento
            gestoreFasi.avanzaFase();
        }
    }
    // </editor-fold>
}
