/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
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
    private MenuCarte menuCarte;
    private PannelloGioco pannello;
    private int armateRinforzoDisponibili;
    private RichiestaNumeroArmate richiestaNumeroArmateSpostamento;

    private boolean preFase;

    private AscoltatoreFasi ascoltatoreFineTurno;
    //private AscoltatoreRinforzo    ascoltatoreRinforzo;
    private AscoltatoreFasi ascoltatoreAttacco;
    private AscoltatoreFasi ascoltatoreSpostamento;

    private AscoltatorePlanciaRinforzo      ascoltatorePlanciaRinforzo;
    private AscoltatorePlanciaEvidenziatore ascoltatorePlanciaEvidenziatore;
    private AscoltatorePlanciaAttacco       ascoltatorePlanciaAttacco;
    private AscoltatorePlanciaSpostamento   ascoltatorePlanciaSpostamento;

    private PartitaClient partita;
    private Connessione server;
    private AttivatoreGrafica ag;

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
    public GestoreFasi(PartitaClient partita, PlanciaImmagine planciaImmagine, PannelloGioco pannello) {
        this.pannello = pannello;
        this.menuCarte = pannello.getMenuCarte();
        this.barraFasi = pannello.getBarraFasi();
        this.planciaImmagine = planciaImmagine;
        this.server = partita.getConnessione();
        this.ag = pannello.getAttivatoreGrafica();
        this.partita = partita;
        this.setArmateRinforzoDisponibili(0);

        this.preFase = true;

        this.ascoltatoreFineTurno   = new AscoltatoreFasi(this, server, partita.getMeStessoIndex(), Fasi_t.FINETURNO);
        //this.ascoltatoreRinforzo  = new AscoltatoreFasi(this, server, partita.getMeStessoIndex(), Fasi_t.RINFORZO);
        this.ascoltatoreAttacco     = new AscoltatoreFasi(this, server, partita.getMeStessoIndex(), Fasi_t.ATTACCO);
        this.ascoltatoreSpostamento = new AscoltatoreFasi(this, server, partita.getMeStessoIndex(), Fasi_t.SPOSTAMENTO);
        
        this.ascoltatorePlanciaEvidenziatore = new AscoltatorePlanciaEvidenziatore(planciaImmagine, ag);
        this.ascoltatorePlanciaRinforzo = new AscoltatorePlanciaRinforzo(planciaImmagine, server, partita.getMeStessoIndex());
        this.ascoltatorePlanciaAttacco = new AscoltatorePlanciaAttacco(planciaImmagine, this, server, partita,pannello.getBottoneFaseAttacco());
        this.ascoltatorePlanciaSpostamento = new AscoltatorePlanciaSpostamento(planciaImmagine, this, server, partita,pannello);

        this.richiestaNumeroArmateSpostamento = new RichiestaNumeroArmate(barraFasi.getBottoneFaseSpostamento(), server, partita);

        faseToAttesa();
    }

    /**
     * Riporta la fase alla fase di attesa. Si occupa di parte grafica e di
     * tutto quanto
     */
    public void faseToAttesa(){
        barraFasi.resetFase();
        setAscoltatoreFineTurno(false);
        setAscoltatoreSpostamenti(false);
        setAscoltatoreAttacco(false);
        menuCarte.setFaseRinforzo(false);
        pannello.disattivaRisultatoDadi();
        planciaImmagine.setActionListener(ascoltatorePlanciaEvidenziatore);
    }

    public void finePreFase(){
        this.preFase = false;
        this.faseToAttesa();
    }

    /**
     * Passa alla fase successiva. Le fasi sono cicliche. Questa funzione si
     * occupa di tutta la parte grafica e di tutta la parte degli ascoltatori.
     */
    public void avanzaFase(){
        barraFasi.avanzaFase();
        switch(barraFasi.getFase()){
            case FINETURNO: // - 
                setAscoltatoreAttacco(false);
                setAscoltatoreSpostamenti(false);
                setAscoltatoreFineTurno(false);
                pannello.disattivaRisultatoDadi();
                planciaImmagine.setActionListener(ascoltatorePlanciaEvidenziatore);
                return;

            case RINFORZO: // (attacco)
                if (!preFase) //non attiva la possibilità di giocare le carte se si è in prefase
                    menuCarte.setFaseRinforzo(true);
                if (armateRinforzoDisponibili == 0) //attiva la possibilità di passare il turno non si hanno armate (si potrebbe giocare un tris)
                    setAscoltatoreFineTurno(true);
                ascoltatorePlanciaEvidenziatore.pulisci(); //deseleziona i possibili rimasugli di ascoltatorePlanciaEvidenzazione
                pannello.disattivaRisultatoDadi();
                planciaImmagine.setActionListener(ascoltatorePlanciaRinforzo);
                return;

            case ATTACCO: //fineturno spostamenti
                setAscoltatoreFineTurno(true);
                setAscoltatoreSpostamenti(true);
                menuCarte.setFaseRinforzo(false);
                barraFasi.rinforzi.setDisegnaTestoSmosciato(true);
                planciaImmagine.setActionListener(ascoltatorePlanciaAttacco);
                return;

            case SPOSTAMENTO: //fineturno
                setAscoltatoreSpostamenti(false);
                barraFasi.attacco.setDisegnaTestoSmosciato(true);
                pannello.disattivaRisultatoDadi();
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

    public void setAscoltatoreFineTurno(boolean fineTurno) {
        //FINE TURNO
        if (fineTurno)
            barraFasi.setAscoltatoreFineTurno(ascoltatoreFineTurno);
        else
            barraFasi.setAscoltatoreFineTurno(null);
    }

    public void setAscoltatoreSpostamenti (boolean spostamenti){
        //SPOSTAMENTI
        if (spostamenti)
            barraFasi.setAscoltatoreSpostamento(ascoltatoreSpostamento);
        else
            barraFasi.setAscoltatoreSpostamento(null);
    }

    public void setAscoltatoreAttacco (boolean attacco){
        //SPOSTAMENTI
        if (attacco)
            barraFasi.setAscoltatoreAttacco(ascoltatoreAttacco);
        else
            barraFasi.setAscoltatoreAttacco(null);
    }

    final public void setArmateRinforzoDisponibili(int armate){
        this.armateRinforzoDisponibili = armate;
        if (armateRinforzoDisponibili > 0) {
            setAscoltatoreFineTurno(false);
            setAscoltatoreAttacco(false);
        }
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

    public void terminaAttaccoInCorso() {
        ascoltatorePlanciaAttacco.terminaAttacco();
    }

    // <editor-fold defaultstate="collapsed" desc="AscoltatoriPulsantiFase">
    private class AscoltatoreFasi implements RisicammaraEventListener {
        private GestoreFasi gestoreFasi;
        private Connessione server;
        private int meStesso;
        private Fasi_t fase;

        public AscoltatoreFasi(GestoreFasi gestoreFasi, Connessione server, int meStesso, Fasi_t fase) {
            this.gestoreFasi = gestoreFasi;
            this.server = server;
            this.meStesso = meStesso;
            this.fase = fase;
        }

        @Override
        public void actionPerformed(EventoAzioneRisicammara e) {
            try {
                server.spedisci(new MessaggioFase(fase, meStesso));
            } catch (IOException ex) {
                System.err.println("Messaggio "+fase+" non inviato: "+ex);
                return;
            }
            gestoreFasi.setFase(fase);
        }
    }
    // </editor-fold>
}
