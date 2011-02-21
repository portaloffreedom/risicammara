/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.Client;

/**
 *
 * @author matteo
 */
public class AscoltatorePlanciaEvidenziatore implements RisicammaraEventListener {
    private PlanciaImmagine planciaImmagine;
    private AttivatoreGrafica attivatoreGrafica;
    private int idTerritorioSelezionato;
    private boolean selezionato;

    public AscoltatorePlanciaEvidenziatore(PlanciaImmagine planciaImmagine, AttivatoreGrafica ag) {
        this.planciaImmagine = planciaImmagine;
        this.attivatoreGrafica = ag;
        //planciaImmagine.setActionListener(this);
    }

    public void actionPerformed(EventoAzioneRisicammara e) {
        Rectangle rettangolo = null;
        Point p = e.getPoint();
        int idTerritorio = planciaImmagine.getidTerritorio(p);

        int continente = PlanciaImmagine.GetContinente(idTerritorio);
        int territorio = PlanciaImmagine.GetTerritorio(idTerritorio);
        if (Client.DEBUG == true) {
            System.out.println("Continente: "+continente);
            System.out.println("Territorio: "+territorio);
        }

        if (selezionato){
            rettangolo = planciaImmagine.ripristinaTerritorio(idTerritorioSelezionato);
            idTerritorioSelezionato = 0;
            selezionato = false;
        }
        else {
            if (!PlanciaImmagine.eTerritorio(idTerritorio))
                return;
            rettangolo = planciaImmagine.coloraSfumato(idTerritorio, Color.white, 0.5);
            idTerritorioSelezionato = idTerritorio;
            selezionato = true;
        }

        planciaImmagine.repaintPlancia(rettangolo);
    }

}
