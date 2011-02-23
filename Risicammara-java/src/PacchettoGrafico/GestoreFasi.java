/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioFase;

/**
 * Classe che gestisce le fasi sia come parte grafica che come parte della
 * attivazione dei vari ascoltatori.
 * @author matteo
 */
final public class GestoreFasi {
    private BarraFasi barraFasi;
    private PlanciaImmagine planciaImmagine;
    private AscoltatoreFineTurno   ascoltatoreFineTurno;
    //private AscoltatoreRinforzo    ascoltatoreRinforzo;
    //private AscoltatoreAttacco     ascoltatoreAttacco;
    private AscoltatoreSpostamento ascoltatoreSpostamento;
    private int armateRinforzoDisponibili;
    private AscoltatorePlanciaRinforzo      ascoltatorePlanciaRinforzo;
    private AscoltatorePlanciaEvidenziatore ascoltatorePlanciaEvidenziatore;
    private AscoltatorePlanciaAttacco       ascoltatorePlanciaAttacco;
    private AscoltatorePlanciaSpostamento   ascoltatorePlanciaSpostamento;

    private Connessione server;
    private AttivatoreGrafica ag;
    private ListaGiocatoriClient listaGiocatori;

    /**
     * Costruttore
     * @param barraFasi riferimento alla barra fasi che deve gestire
     * @param server insieme di connessione che servono a mandare i messaggi al
     * server
     * @param listaGiocatori riferimento alla lista dei giocatori (versione Client)
     * @param planciaImmagine riferimento alla planciaImmagine (che gestisce la
     * visualizzazione della plancia). Serve per agganciarci gli ascoltatori.
     * @param ag riferimento all'Attivatore Grafica (per ridisegnare il pannello)
     */
    public GestoreFasi(BarraFasi barraFasi, Connessione server, PartitaClient partita, PlanciaImmagine planciaImmagine, AttivatoreGrafica ag) {
        this.barraFasi = barraFasi;
        this.planciaImmagine = planciaImmagine;
        this.listaGiocatori = partita.getListaGiocatori();
        this.server = server;
        this.ag = ag;
        this.setArmateRinforzoDisponibili(0);

        this.ascoltatoreFineTurno = new AscoltatoreFineTurno(this, listaGiocatori.meStessoIndex());
        //this.ascoltatoreRinforzo = new AscoltatoreRinforzo();
        //this.ascoltatoreAttacco = new AscoltatoreAttacco();
        this.ascoltatoreSpostamento = new AscoltatoreSpostamento(this, listaGiocatori.meStessoIndex());
        
        this.ascoltatorePlanciaEvidenziatore = new AscoltatorePlanciaEvidenziatore(planciaImmagine, ag);
        this.ascoltatorePlanciaRinforzo = new AscoltatorePlanciaRinforzo(planciaImmagine, server, listaGiocatori.meStessoIndex());
        this.ascoltatorePlanciaAttacco = new AscoltatorePlanciaAttacco(planciaImmagine, this, server, partita);
        this.ascoltatorePlanciaSpostamento = new AscoltatorePlanciaSpostamento(planciaImmagine, this, server, partita);
        faseToAttesa();
    }

    /**
     * Riporta la fase alla fase di attesa. Si occupa di parte grafica e di
     * tutto quanto
     */
    public void faseToAttesa(){
        barraFasi.resetFase();
        setAscoltatore(true, false);
    }

    /**
     * Passa alla fase successiva. Le fasi sono cicliche. Questa funzione si
     * occupa di tutta la parte grafica e di tutta la parte degli ascoltatori.
     */
    public void avanzaFase(){
        barraFasi.avanzaFase();
        switch(barraFasi.getFase()){
            case FINETURNO:
                setAscoltatore(false, false);
                planciaImmagine.setActionListener(ascoltatorePlanciaEvidenziatore);
                return;

            case RINFORZO:
                setAscoltatore(false, false);
                planciaImmagine.setActionListener(ascoltatorePlanciaRinforzo);
                return;

            case ATTACCO:
                setAscoltatore(true, true);
                barraFasi.rinforzi.setDisegnaTestoSmosciato(true);
                planciaImmagine.setActionListener(ascoltatorePlanciaAttacco);
                return;

            case SPOSTAMENTO:
                setAscoltatore(true, false);
                barraFasi.attacco.setDisegnaTestoSmosciato(true);
                planciaImmagine.setActionListener(ascoltatorePlanciaSpostamento);
                return;
        }
    }
    
    public void setFase(Fasi_t fase) {
        int avanzamento = Fasi_t.getDistanzaFasi(getFaseCorrente(), fase);
        for (int i=0; i<avanzamento; i++){
            this.avanzaFase();
        }
    }

    public void setAscoltatore(boolean fineTurno, boolean spostamenti){
        //FINE TURNO
        if (fineTurno)
            barraFasi.setAscoltatoreFineTurno(ascoltatoreFineTurno);
        else
            barraFasi.setAscoltatoreFineTurno(null);

        //SPOSTAMENTI
        if (spostamenti)
            barraFasi.setAscoltatoreSpostamento(ascoltatoreSpostamento);
        else
            barraFasi.setAscoltatoreSpostamento(null);
    }

    final public void setArmateRinforzoDisponibili(int armate){
        this.armateRinforzoDisponibili = armate;
        barraFasi.rinforzi.setTestoDestra("Armate disponibili: "+armateRinforzoDisponibili);
        barraFasi.rinforzi.setTestoSmosciato(""+armateRinforzoDisponibili);
        ag.panelRepaint(barraFasi.rinforzi.getBounds());
    }

    final public void diminuisciArmateRinforzoDisponibili(){
        armateRinforzoDisponibili--;
        setArmateRinforzoDisponibili(armateRinforzoDisponibili);
    }

    public Fasi_t getFaseCorrente(){
        return barraFasi.getFase();
    }

    // <editor-fold defaultstate="collapsed" desc="AscoltatoriPulsantiFase">
    private class AscoltatoreFineTurno implements RisicammaraEventListener {
        private GestoreFasi fasi;
        private int meStesso;

        public AscoltatoreFineTurno(GestoreFasi fasi, int meStesso) {
            this.fasi = fasi;
            this.meStesso = meStesso;
        }

        public void actionPerformed(EventoAzioneRisicammara e) {
            try {
                server.spedisci(new MessaggioFase(Fasi_t.FINETURNO, meStesso));
            } catch (IOException ex) {
                System.err.println("Messaggio fine turno non inviato: "+ex);
                return;
            }
            fasi.faseToAttesa();
        }
    }

    private class AscoltatoreSpostamento implements RisicammaraEventListener {
        private GestoreFasi gestoreFasi;
        private int meStesso;

        public AscoltatoreSpostamento(GestoreFasi gestoreFasi, int meStesso) {
            this.gestoreFasi = gestoreFasi;
            this.meStesso = meStesso;
        }
        
        public void actionPerformed(EventoAzioneRisicammara e) {
            //da attacco passa a spostamenti
            try {
                server.spedisci(new MessaggioFase(Fasi_t.SPOSTAMENTO, meStesso));
            } catch (IOException ex) {
                System.err.println("Messaggio passaggio a turno spostamento non inviato: "+ex);
                return;
            }
            gestoreFasi.avanzaFase();
        }
    }
    // </editor-fold>
}