/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
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
public class AscoltatorePlanciaAttacco implements RisicammaraEventListener {
    public static final Color Attacco = Color.RED;
    public static final Color Difesa  = Color.BLUE;
    public static final double pesantezzaSfumatura = 0.86;

    private PlanciaImmagine plancia;
    private Connessione server;
    private PartitaClient partita;

    private TerritorioPlanciaClient territorioAttaccante;

    public AscoltatorePlanciaAttacco(PlanciaImmagine plancia, Connessione server, PartitaClient partita) {
        this.plancia = plancia;
        this.server = server;
        this.partita = partita;
        this.territorioAttaccante = null;
    }

    public void actionPerformed(EventoAzioneRisicammara e) {
        int idTerritorio = plancia.getidTerritorio(e.getPoint());
        if (!PlanciaImmagine.eTerritorio(idTerritorio))
            return;

        if (territorioAttaccante == null){
            try {

                territorioAttaccante = plancia.plancia.getTerritorio(idTerritorio);
                if (territorioAttaccante.getProprietario() != partita.getMeStessoIndex())
                    throw new TerritorioNonValido("Non possiedi questo territorio!");
                if (territorioAttaccante.getArmate() <= 1){
                    throw new TerritorioNonValido("Non possiedi abbastanza armate"
                            + " in questo territorio");
                }

                //plancia.colora(idTerritorio, Attacco);
                plancia.coloraSfumato(idTerritorio, Attacco, pesantezzaSfumatura);
                plancia.aggiornaTerritorio(idTerritorio);

            } catch (TerritorioNonValido ex) {
                System.err.println("Punto non valido:\n"+ex);
                territorioAttaccante =  null;
                return;
            }
        }
        else {
            territori_t territorioDifensore = null;
            try {
                territorioDifensore = territori_t.GetTerritorio(idTerritorio);
                if (plancia.plancia.getTerritorio(territorioDifensore).getProprietario() == partita.getMeStessoIndex())
                    throw new TerritorioNonValido("Non puoi attaccare te stesso!");
                server.spedisci(new MessaggioDichiaraAttacco(territorioAttaccante.getTerritorio(), territorioDifensore, partita.getMeStessoIndex()));
            } catch (TerritorioNonValido ex) {
                System.err.println("Punto non valido:\n"+ex);
                return;
            } catch (IOException ex) {
                System.err.println("Errore nello spedire il messaggio:"+ex+". Azione annullata");
                return;
            }

            try {
                //plancia.colora(idTerritorio, Difesa);
                plancia.coloraSfumato(idTerritorio, Difesa, pesantezzaSfumatura);
                plancia.aggiornaTerritorio(idTerritorio);
            } catch (TerritorioNonValido ex) {
                System.err.println("Impossibile trovare il territorio da colorare:\n"+ex);
            }

            partita.setTerritorioAttaccante(territorioAttaccante.getTerritorio());
            partita.setTerritorioAttaccato(territorioDifensore);

            //TODO chiedere con quante armate lanciare
            int armateAttaccanti = territorioAttaccante.getArmate()-1;
            if (armateAttaccanti > 3){
                armateAttaccanti = 3;
            }
            try {
                server.spedisci(MessaggioComandi.creaMsgLanciadado(partita.getMeStessoIndex(), armateAttaccanti));
            } catch (IOException ex) {
                System.err.println("Messaggio lancio dadi non riuscito!: "+ex);
            }

            //ripristina il territorio attacco selezionato
            territorioAttaccante = null;
                //il colore territori viene ripristinato solo quando arriva il
                //messaggio fine attacco
        }
    }
}
