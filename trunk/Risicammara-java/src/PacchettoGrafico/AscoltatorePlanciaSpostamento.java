/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioNonValido;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioSpostaArmate;

/**
 *
 * @author matteo
 */
public class AscoltatorePlanciaSpostamento implements RisicammaraEventListener, ActionListener, WindowListener {

    public static final Color Sorgente = Color.WHITE;
    public static final Color Destinazione  = Color.PINK;
    public static final double pesantezzaSfumatura = 0.86;

    private PlanciaImmagine plancia;
    private GestoreFasi fasi;
    private Connessione server;
    private PartitaClient partita;
    private RichiestaNumeroArmate numeroArmate;

    private TerritorioPlanciaClient territorioSorgente;
    private TerritorioPlanciaClient territorioDesitanzione;
    private boolean spostamentoInCorso;

    public AscoltatorePlanciaSpostamento(PlanciaImmagine plancia, GestoreFasi fasi, Connessione server, PartitaClient partita) {
        this.plancia = plancia;
        this.fasi = fasi;
        this.server = server;
        this.partita = partita;
        
        this.territorioDesitanzione = null;
        this.territorioSorgente = null;
        this.numeroArmate = null;
        this.spostamentoInCorso = false;
    }
    
    /**
     * Metodo che viene chiamato dopo che viene premuta la plancia
     */
    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        int idTerritorio = plancia.getidTerritorio(e.getPoint());
        if (!PlanciaImmagine.eTerritorio(idTerritorio))
            return;

        if (territorioSorgente == null)
            selezionaTerritorioSorgente(idTerritorio);
        else
            if (!spostamentoInCorso)
                selezionaTerritorioDestinazione(idTerritorio);

    }
    
    private void selezionaTerritorioSorgente(int idTerritorio) {
        try {
            territorioSorgente = plancia.plancia.getTerritorio(idTerritorio);
            if (territorioSorgente.getProprietario() != partita.getMeStessoIndex())
                throw new TerritorioNonValido("Non possiedi questo territorio!");
            if (territorioSorgente.getArmate() <= 1){
                throw new TerritorioNonValido("Non possiedi abbastanza armate"
                        + " in questo territorio");
            }
        } catch (TerritorioNonValido ex) {
            System.err.println("Punto non valido:\n"+ex);
            territorioSorgente =  null;
            return;
        }
        
        plancia.coloraSfumato(territorioSorgente, Sorgente, pesantezzaSfumatura);
        fasi.setAscoltatore(false, false);
    }
    
    private void selezionaTerritorioDestinazione(int idTerritorio) {
        territori_t difensore = null;
        try {
            difensore = territori_t.GetTerritorio(idTerritorio);
            
            //controllo di deselezione del territorio
            if (difensore == territorioSorgente.getTerritorio()) {
                spostamentoInCorso(false);
                plancia.ripristinaTerritorio(territorioSorgente);
                territorioSorgente = null;
                return;
            }
            
            territorioDesitanzione = plancia.plancia.getTerritorio(difensore);
            
            //controllo che il giocatore non sposti in un territorio dell'avversario le proprie armate
            if (territorioDesitanzione.getProprietario() != partita.getMeStessoIndex())
                throw new TerritorioNonValido("Non puoi attaccare te stesso!");
            
            //controllo che il territorio stia nelle adiacenze di quello attaccante
            if (!territorioSorgente.isAdiacent(difensore))
                throw new TerritorioNonValido("Non puoi attaccare un territorio troppo distante!");
            
        } catch (TerritorioNonValido ex) {
            System.err.println("Punto non valido:\n"+ex);
            return;
        }

        //plancia.colora(idTerritorio, Difesa);
        plancia.coloraSfumato(difensore, Destinazione, pesantezzaSfumatura);

        partita.setTerritorioAttaccante(territorioSorgente.getTerritorio());
        partita.setTerritorioAttaccato(difensore);

        //TODO chiedere con quante armate lanciare
        int armateSpostabili = territorioSorgente.getArmate()-1;
        numeroArmate = new RichiestaNumeroArmate("Quante armate vuoi spostare?", armateSpostabili);
        numeroArmate.addOKActionListener(this);
        spostamentoInCorso(true);
    }

    /**
     * Metodo che viene azionato dopo che viene premuto ok nella finestra di 
     * richiesta armate
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        int armateDaSpostare = numeroArmate.getNumArmate();
        try {
            
            server.spedisci(
                    new MessaggioSpostaArmate(
                            partita.getMeStessoIndex(),
                            territorioSorgente.getTerritorio(),
                            territorioDesitanzione.getTerritorio(),
                            armateDaSpostare
                    ));
            
        } catch (IOException ex) {
            System.err.println("Messaggio sposamento armate non riuscito!: "+ex);
            return;
        }

        //ripristina i territori selezionati selezionato
        plancia.ripristinaTerritorio(territorioSorgente);
        plancia.ripristinaTerritorio(territorioDesitanzione);
        territorioSorgente = null;
        territorioDesitanzione = null;
            //il colore territori viene ripristinato solo quando arriva il
            //messaggio fine attacco    
        spostamentoInCorso(false);
        
        numeroArmate.setVisible(false);
        numeroArmate.dispose();
        numeroArmate = null;
    }
    
    /**
     * Imposta le variabili di controllo se c'è uno spostamento in corso o no. 
     * Imposta anche gli ascoltatori.
     * @param attacco true se uno spostamento è in corso.
     */
    private void spostamentoInCorso(boolean attacco){
        this.spostamentoInCorso = attacco;
        this.fasi.setAscoltatore(!attacco, false);
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosing(WindowEvent we) {
        spostamentoInCorso(false);
        plancia.ripristinaTerritorio(territorioSorgente);
        plancia.ripristinaTerritorio(territorioDesitanzione);
        territorioSorgente = null;
        territorioDesitanzione = null;
        numeroArmate.setVisible(false);
        numeroArmate.dispose();
        numeroArmate = null;
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }
}
