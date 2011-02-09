/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
public class BarraSuperiore implements Elemento_2DGraphics {
    private Dimension dimensioniPannello;
    private Rectangle dimensioni;
    private PannelloGioco pannello;
    private BottoneRisicammara giocatoreButton;
    private BottoneRisicammara carteButton;
    private Point posizioneGiocatore;
    private Point posizioneCarte;


    public BarraSuperiore(Dimension dimensioniPannello, int altezza,PannelloGioco pannello){
        this.dimensioniPannello = dimensioniPannello;
        this.dimensioni= new Rectangle(dimensioniPannello);
        this.dimensioni.height = altezza;
        this.pannello=pannello;

        this.posizioneGiocatore = new Point(5, 5);
        this.posizioneCarte     = new Point(dimensioni.width-5-100, 5);

        this.giocatoreButton = new BottoneRisicammara(posizioneGiocatore, "Giocatore");
        pannello.addPulsante(giocatoreButton);
        this.carteButton     = new BottoneRisicammara(posizioneCarte, "Carte");
        pannello.addPulsante(carteButton);
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        //Corregge le posizioni per il ridimensionamento
        if (true) { //TODO implementare questa fase solo se il pannello è stato ridimensionato
            this.dimensioni.width = this.dimensioniPannello.width;
            this.posizioneCarte.x = dimensioni.width-5-100;
            this.carteButton.setPosizione(posizioneCarte);
        }

        //Disegna lo sfondo nero della barra
        graphics2D.fillRect(dimensioni.x, dimensioni.y, dimensioni.width, dimensioni.height);

        //Disegna i due pulsanti
        this.giocatoreButton.disegna(graphics2D);
        this.carteButton.disegna(graphics2D);
    }
}
