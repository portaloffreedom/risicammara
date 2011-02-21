/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;

/**
 *
 * @author matteo
 */
public class AscoltatorePlanciaRinforzo implements RisicammaraEventListener {
    private PlanciaImmagine planciaImmagine;
    private GestoreFasi gestoreFasi;
    private Connessione server;
    private int meStesso;

    public AscoltatorePlanciaRinforzo(PlanciaImmagine planciaImmagine, Connessione server, int meStesso) {
        this.planciaImmagine = planciaImmagine;
        this.server = server;
        this.meStesso = meStesso;
    }

    public void actionPerformed(EventoAzioneRisicammara e) {
        //mettiArmata nel territorio
        int idTerritorio = planciaImmagine.getidTerritorio(e.getPoint());
        territori_t territorioT = territori_t.GetTerritorio(idTerritorio);
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
