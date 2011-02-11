/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author matteo
 */
public class MenuGiocatore implements ActionListener, Elemento_2DGraphics {
    private Dimension dimensioni;
    private boolean visibile;
    private boolean cambiato;
    private RiquadroTesto Obbiettivo;
    private AttivatoreGrafica attivatoreGrafica;

    private ListaGiocatoriClient listaGiocatori;
    private BottoneRisicammara bottone;


    public MenuGiocatore(Dimension dimensioni, ListaGiocatoriClient listaGiocatori, AttivatoreGrafica attivatoreGrafica, BottoneRisicammara giocatoreButton) {
        this.dimensioni=dimensioni;
        this.visibile=false;
        this.attivatoreGrafica = attivatoreGrafica;
        this.listaGiocatori = listaGiocatori;
        this.bottone = giocatoreButton;
        Rectangle rettangoloTesto = new Rectangle(5, 55, 200, 0);
        this.Obbiettivo = new RiquadroTesto(rettangoloTesto,
                                            listaGiocatori.meStesso().getObbiettivo().toString(),
                                            listaGiocatori.meStesso().getArmyColour());
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        if (visibile) {
            graphics2D.setColor(colori.getSfondoScuro());
            this.Obbiettivo.disegna(graphics2D,colori);
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.visibile = !this.visibile;
        this.attivatoreGrafica.panelRepaint();
        this.bottone.setPressed(this.visibile);
        //attivatoreGrafica.attiva();
        //cambiato = true;
    }

}

