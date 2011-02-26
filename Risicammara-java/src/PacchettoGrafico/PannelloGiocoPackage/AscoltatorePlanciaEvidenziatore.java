/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.Client;
import risicammaraJava.boardManage.TerritorioNonValido;

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

    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        Rectangle rettangolo = null;
        Point p = e.getPoint();
        int idTerritorio = planciaImmagine.getidTerritorio(p);
        if (!PlanciaImmagine.eTerritorio(idTerritorio))
                return;
        
        if (Client.DEBUG == true) {
            System.out.println("Continente: "+PlanciaImmagine.GetContinente(idTerritorio));
            System.out.println("Territorio: "+PlanciaImmagine.GetTerritorio(idTerritorio));
        }

        try {
            if (selezionato)
                rettangolo = deseleziona();
            else 
                rettangolo = seleziona(idTerritorio);
        } 
        catch (TerritorioNonValido ex) {
            System.err.println("Punto non selezionabile:\n"+ex);
            return;
        }

        planciaImmagine.repaintPlancia(rettangolo);
    }
    
    private Rectangle deseleziona() throws TerritorioNonValido {
        Rectangle rect = planciaImmagine.ripristinaTerritorio(idTerritorioSelezionato);
        idTerritorioSelezionato = 0;
        selezionato = false;
        return rect;
    }
    
    private Rectangle seleziona(int idTerritorio) throws TerritorioNonValido {
        Rectangle rect = planciaImmagine.coloraSfumato(idTerritorio, Color.white, 0.5);
               
        idTerritorioSelezionato = idTerritorio;
        selezionato = true;
        return rect;
    }
    
    /**
     * Deseleziona il territorio se ce n'è uno selezionato.
     */
    public void pulisci() {
        if (selezionato) {
            try {
                
                Rectangle rect = deseleziona();
                attivatoreGrafica.panelRepaint(rect);
                
            } catch (TerritorioNonValido ex) {
                System.err.println("Impossibile rispristinare territorio!");
            } 
        }
    }

}
