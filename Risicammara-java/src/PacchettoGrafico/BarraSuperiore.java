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
    private BottoneRisicammara giocatoreButton;
    private BottoneRisicammara carteButton;
    private Point posizioneGiocatore;
    private Point posizioneCarte;
    private MenuGiocatore menuGiocatore;
    private BarraFasi barraFasi;


    public BarraSuperiore(Dimension dimensioniPannello, int altezza,PannelloGioco pannello, ListaGiocatoriClient listaGiocatori, AttivatoreGrafica attivatoreGrafica){
        this.dimensioniPannello = dimensioniPannello;
        this.dimensioni= new Rectangle(dimensioniPannello);
        this.dimensioni.height = altezza;
        this.posizioneGiocatore = new Point(5, 5);
        this.posizioneCarte     = new Point(dimensioni.width-5-100, 5);
        this.giocatoreButton = new BottoneRisicammara(posizioneGiocatore, listaGiocatori.meStesso().getNome(), listaGiocatori.meStesso());
        this.carteButton     = new BottoneRisicammara(posizioneCarte, "Carte", listaGiocatori.meStesso());
        this.menuGiocatore = new MenuGiocatore(dimensioniPannello, listaGiocatori, attivatoreGrafica, giocatoreButton);
        this.barraFasi = new BarraFasi(dimensioniPannello, attivatoreGrafica, 125, 125, altezza-20, 10);

        
        this.giocatoreButton.setActionListener(menuGiocatore);
        pannello.addPulsante(giocatoreButton);
        pannello.addPulsante(carteButton);
        pannello.addPulsante(barraFasi);
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        //Corregge le posizioni per il ridimensionamento
        if (true) { //TODO implementare questa fase solo se il pannello è stato ridimensionato
            this.dimensioni.width = this.dimensioniPannello.width;
            this.posizioneCarte.x = dimensioni.width-5-100;
            this.carteButton.setPosizione(posizioneCarte);
        }

        //Disegna lo sfondo nero della barra
        graphics2D.setColor(colori.getSfondoScuro());
        graphics2D.fillRect(dimensioni.x, dimensioni.y, dimensioni.width, dimensioni.height);

        //Disegna i pulsanti
        this.barraFasi.disegna(graphics2D, colori);
        this.giocatoreButton.disegna(graphics2D, colori);
        this.carteButton.disegna(graphics2D, colori);

        //disegna i menù (da tenere per ultimi)
        this.menuGiocatore.disegna(graphics2D, colori);
    }
}
