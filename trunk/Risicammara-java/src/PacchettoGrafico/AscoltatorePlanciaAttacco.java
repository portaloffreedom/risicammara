/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioNonValido;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioDichiaraAttacco;

/**
 *
 * @author matteo
 */
public class AscoltatorePlanciaAttacco implements RisicammaraEventListener, ActionListener {
    public static final Color Attacco = Color.RED;
    public static final Color Difesa  = Color.BLUE;
    public static final double pesantezzaSfumatura = 0.86;

    private PlanciaImmagine plancia;
    private GestoreFasi fasi;
    private Connessione server;
    private PartitaClient partita;
    private RichiestaNumeroArmate numeroArmate;

    private TerritorioPlanciaClient territorioAttaccante;
    private boolean attaccoInCorso;

    public AscoltatorePlanciaAttacco(PlanciaImmagine plancia, GestoreFasi fasi, Connessione server, PartitaClient partita) {
        this.plancia = plancia;
        this.fasi = fasi;
        this.server = server;
        this.partita = partita;
        this.territorioAttaccante = null;
    }

    /**
     * Metodo che viene chiamato dopo che viene premuta la plancia
     */
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
        fasi.setAscoltatore(false, false);
    }
    
    private void selezionaTerritorioDifensore(int idTerritorio) {
        territori_t difensore = null;
        TerritorioPlanciaClient territorioDifensore = null;
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
        numeroArmate = new RichiestaNumeroArmate("Con quante armate vuoi attaccare?", maxArmateAttaccanti);
        numeroArmate.addOKActionListener(this);
        attaccoInCorso(true);
    }

    /**
     * Metodo che viene azionato dopo che viene premuto ok nella finestra di 
     * richiesta armate
     */
    public void actionPerformed(ActionEvent ae) {
        int armateAttaccanti = numeroArmate.getNumArmate();
        try {
            server.spedisci(MessaggioComandi.creaMsgLanciadado(partita.getMeStessoIndex(), armateAttaccanti));
        } catch (IOException ex) {
            System.err.println("Messaggio lancio dadi non riuscito!: "+ex);
            return;
        }

        //ripristina il territorio attacco selezionato
        territorioAttaccante = null;
            //il colore territori viene ripristinato solo quando arriva il
            //messaggio fine attacco    
        attaccoInCorso(false);
        
        numeroArmate.setVisible(false);
        numeroArmate.dispose();
        numeroArmate = null;
    }
    
    /**
     * Imposta le variabili di controllo se c'è un'attacco in corso o no. 
     * Imposta anche gli ascoltatori.
     * @param attacco true se un'attacco è in corso.
     */
    private void attaccoInCorso(boolean attacco){
        this.attaccoInCorso = attacco;
        this.fasi.setAscoltatore(!attacco, !attacco);
    }
}
