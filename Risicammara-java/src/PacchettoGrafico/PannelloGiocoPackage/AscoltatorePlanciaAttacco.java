/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioNonValido;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioDichiaraAttacco;

/**
 * Ascoltatore che gestisce la fase di attacco reagendo agli eventi sulla plancia
 * di gioco.
 * @author matteo
 */
public class AscoltatorePlanciaAttacco implements RisicammaraEventListener {
    /**
     * Colore con qui sfumare il territorio selezionato come attaccante.
     */
    public static final Color Attacco = Color.RED;
    /**
     * Colore con cui sfumare il territorio selezionato come difensore.
     */
    public static final Color Difesa  = Color.BLUE;
    /**
     * Valore che imposta quanto deve essere visibile la colorazione di attacco
     * e difesa.
     */
    public static final double pesantezzaSfumatura = 0.86;

    private PlanciaImmagine plancia;
    private GestoreFasi fasi;
    private Connessione server;
    private PartitaClient partita;
    //private RichiestaNumeroArmate numeroArmate;
    private BottoneFaseAttaccoAvanzato bottoneFaseAttacco;

    private TerritorioPlanciaClient territorioAttaccante;
    private TerritorioPlanciaClient territorioDifensore;
    private boolean attaccoInCorso;

    /**
     * Costruttore
     * @param plancia riferimento alla plancia che deve gestire
     * @param fasi riferimento al gestore delle fasi
     * @param server riferimento alla connessione col server
     * @param partita riferimento all'oggetto partita
     * @param bottoneFaseAttacco riferimento al bottone fase attacco (per gestire
     * la visualizzazzione dei dialoghi)
     */
    public AscoltatorePlanciaAttacco(PlanciaImmagine plancia, GestoreFasi fasi, Connessione server, PartitaClient partita, BottoneFaseAttaccoAvanzato bottoneFaseAttacco) {
        this.plancia = plancia;
        this.fasi = fasi;
        this.server = server;
        this.partita = partita;
        this.bottoneFaseAttacco = bottoneFaseAttacco;
        this.territorioAttaccante = null;
        this.territorioDifensore = null;
        bottoneFaseAttacco.setActionListenerArmateAttaccanti(new AscoltatoreNumeroArmateAttaccanti(this));
    }

    /**
     * Metodo che viene chiamato dopo che viene premuta la plancia
     * @param e
     */
    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        int idTerritorio = plancia.getidTerritorio(e.getPoint());
        if (!PlanciaImmagine.eTerritorio(idTerritorio))
            return;

        if (territorioAttaccante == null)
            selezionaTerritorioAttaccante(idTerritorio);
        else
            if (!attaccoInCorso)
                selezionaTerritorioDifensore(idTerritorio);

    }
    
    private void selezionaTerritorioAttaccante(int idTerritorio) {
        try {
            territorioAttaccante = plancia.plancia.getTerritorio(idTerritorio);
            if (territorioAttaccante.getProprietario() != partita.getMeStessoIndex())
                throw new TerritorioNonValido("Non possiedi questo territorio!");
            if (territorioAttaccante.getArmate() <= 1){
                throw new TerritorioNonValido("Non possiedi abbastanza armate"
                        + " in questo territorio");
            }
        } catch (TerritorioNonValido ex) {
            System.err.println("Punto non valido:\n"+ex);
            territorioAttaccante =  null;
            return;
        }
        
        plancia.coloraSfumato(territorioAttaccante, Attacco, pesantezzaSfumatura);
        fasi.setAscoltatoreFineTurno(false);
        fasi.setAscoltatoreSpostamenti(false);
    }
    
    private void selezionaTerritorioDifensore(int idTerritorio) {
        territori_t difensore = null;
        try {
            difensore = territori_t.GetTerritorio(idTerritorio);
            
            //controllo di deselezione del territorio
            if (difensore == territorioAttaccante.getTerritorio()) {
                attaccoInCorso(false);
                plancia.ripristinaTerritorio(territorioAttaccante);
                territorioAttaccante = null;
                return;
            }
            
            territorioDifensore = plancia.plancia.getTerritorio(difensore);
            
            //controllo che il giocatore non cerchi di attaccare se stesso
            if (territorioDifensore.getProprietario() == partita.getMeStessoIndex())
                throw new TerritorioNonValido("Non puoi attaccare te stesso!");
            
            //controllo che il territorio stia nelle adiacenze di quello attaccante
            if (!territorioAttaccante.isAdiacent(difensore))
                throw new TerritorioNonValido("Non puoi attaccare un territorio troppo distante!");
            
            server.spedisci(new MessaggioDichiaraAttacco(territorioAttaccante.getTerritorio(), difensore, partita.getMeStessoIndex()));
            
        } catch (TerritorioNonValido ex) {
            System.err.println("Punto non valido:\n"+ex);
            return;
        } catch (IOException ex) {
            System.err.println("Errore nello spedire il messaggio:"+ex+". Azione annullata");
            return;
        }

        //plancia.colora(idTerritorio, Difesa);
        plancia.coloraSfumato(difensore, Difesa, pesantezzaSfumatura);

        partita.setTerritorioAttaccante(territorioAttaccante.getTerritorio());
        partita.setTerritorioAttaccato(difensore);

        //TODO chiedere con quante armate lanciare
        int maxArmateAttaccanti = territorioAttaccante.getArmate()-1;
        if (maxArmateAttaccanti > 3){
            maxArmateAttaccanti = 3;
        }
        bottoneFaseAttacco.setMaxLancioDadi(maxArmateAttaccanti);
        bottoneFaseAttacco.setRichiestaNumeroArmateAttaccanti(true);
        attaccoInCorso(true);
    }

    private class AscoltatoreNumeroArmateAttaccanti implements RisicammaraEventListener {
        private AscoltatorePlanciaAttacco apa;

        public AscoltatoreNumeroArmateAttaccanti(AscoltatorePlanciaAttacco apa) {
            this.apa = apa;
        }

        @Override
        public void actionPerformed(EventoAzioneRisicammara e) {
            apa.rispostaNumeroArmate(e);
        }

    }

    /**
     * Metodo che viene azionato dopo che viene scelto il numero di armate da
     * scegliere
     * @param ae
     */
    public void rispostaNumeroArmate(ActionEvent ae) {
        int armateAttaccanti = bottoneFaseAttacco.getNumeroArmateAttaccanti();
        try {
            if (armateAttaccanti == 0){
                ritiratiAttacco();
                disattivaRichiestaArmate();
                return;
            }
            server.spedisci(MessaggioComandi.creaMsgLanciadado(partita.getMeStessoIndex(), armateAttaccanti));
        } catch (IOException ex) {
            System.err.println("Messaggio lancio dadi o ritirati non riuscito!: "+ex);
            return;
        }

        disattivaRichiestaArmate();
    }
    
    /**
     * Imposta le variabili di controllo se c'è un'attacco in corso o no. 
     * Imposta anche gli ascoltatori.
     * @param attacco true se un'attacco è in corso.
     */
    private void attaccoInCorso(boolean attacco){
        this.attaccoInCorso = attacco;
        fasi.setAscoltatoreSpostamenti(!attacco);
        fasi.setAscoltatoreFineTurno(!attacco);
    }

    private void ritiratiAttacco() throws IOException {
        server.spedisci(MessaggioComandi.creaMsgRitirati(partita.getMeStessoIndex()));
    }

    /**
     * Termina l'attacco in corso, ripristinando tutte le variabili (e i colori
     * sulla plancia) per potere passare o effettuare un nuovo attacco.
     */
    public void terminaAttacco() {
        attaccoInCorso(false);
        plancia.ripristinaTerritorio(territorioAttaccante);
        plancia.ripristinaTerritorio(territorioDifensore);
        territorioAttaccante = null;
        territorioDifensore = null;
        disattivaRichiestaArmate();
    }

    /**
     * Disattiva il dialogo di richiesta armate da spostare (dopo un'eventuale
     * vincita). 
     */
    public void disattivaRichiestaArmate() {
        bottoneFaseAttacco.setRichiestaNumeroArmateAttaccanti(false);
    }
}
