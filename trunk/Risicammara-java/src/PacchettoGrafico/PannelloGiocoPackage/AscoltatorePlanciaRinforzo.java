/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraJava.boardManage.TerritorioNonValido;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;

/**
 * Ascoltatore da collegare alla plancia di gioco per la fase di rinforzo.
 * @author matteo
 */
public class AscoltatorePlanciaRinforzo implements RisicammaraEventListener {
    private PlanciaImmagine planciaImmagine;
    private GestoreFasi gestoreFasi;
    private Connessione server;
    private int meStesso;

    /**
     * Costruttore.
     * @param planciaImmagine riferimento a planciaImmagine, per comandarne i
     * metodi atti a modificarla (colora, ridisegna, ecc..).
     * @param server riferimento alla connessione al server.
     * @param meStesso intero che identifica la posizione del proprietario del
     * client nella listaGiocatori.
     */
    public AscoltatorePlanciaRinforzo(PlanciaImmagine planciaImmagine, Connessione server, int meStesso) {
        this.planciaImmagine = planciaImmagine;
        this.server = server;
        this.meStesso = meStesso;
    }

    /**
     * Azione che viene chiamata ogni volta che viene premuta la plancia.
     * @param e evento.
     */
    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        //mettiArmata nel territorio
        int idTerritorio = planciaImmagine.getidTerritorio(e.getPoint());

        if (!PlanciaImmagine.eTerritorio(idTerritorio))
            return;

        territori_t territorioT;
        try {
            territorioT = territori_t.GetTerritorio(idTerritorio);
        } catch (TerritorioNonValido ex) {
                System.err.println("Punto non selezionabile:\n"+ex);
                return;
        }
        TerritorioPlanciaClient territorio = planciaImmagine.plancia.getTerritorio(territorioT);
        
        if (territorio.getProprietario() == meStesso) {
            MessaggioCambiaArmateTerritorio msg = new MessaggioCambiaArmateTerritorio(meStesso, 1, territorioT);

            try {
                server.spedisci(msg);
            } catch (IOException ex) {
                System.err.println("Errore di connessione con il server. Armata non spedita. Motivo: "+ex);
            }
        }
        else
            return;
    }

}
