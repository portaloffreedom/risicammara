/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import risicammaraClient.Connessione;

/**
 *
 * @author matteo
 */
public class GestoreFasi {
    private BarraFasi barraFasi;
    private PlanciaImmagine planciaImmagine;
    private AscoltatoreFineTurno   ascoltatoreFineTurno;
    //private AscoltatoreRinforzo    ascoltatoreRinforzo;
    //private AscoltatoreAttacco     ascoltatoreAttacco;
    private AscoltatoreSpostamento ascoltatoreSpostamento;
    private int armateRinforzoDisponibili;

    private Connessione server;
    private AttivatoreGrafica ag;
    private ListaGiocatoriClient listaGiocatori;

    public GestoreFasi(BarraFasi barraFasi, Connessione server, ListaGiocatoriClient listaGiocatori, PlanciaImmagine planciaImmagine, AttivatoreGrafica ag) {
        this.barraFasi = barraFasi;
        this.planciaImmagine = planciaImmagine;
        this.listaGiocatori = listaGiocatori;
        this.server = server;
        this.ag = ag;
        this.setArmateRinforzoDisponibili(0);

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
                planciaImmagine.setActionListener(null);
                return;

            case ContatoreFasi.RINFORZO:
                setAscoltatore(false, false);
                planciaImmagine.setActionListener(new AscoltatorePlanciaRinforzo(planciaImmagine, server, listaGiocatori.meStessoIndex()));
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

    final public void setArmateRinforzoDisponibili(int armate){
        this.armateRinforzoDisponibili = armate;
        if (barraFasi.getFase() == ContatoreFasi.RINFORZO)
            barraFasi.rinforzi.setTestoDestra("Armate disponibili: "+armateRinforzoDisponibili);
        ag.panelRepaint(barraFasi.rinforzi.getBounds());
    }

    final public void diminuisciArmateRinforzoDisponibili(){
        armateRinforzoDisponibili--;
        setArmateRinforzoDisponibili(armateRinforzoDisponibili);
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
