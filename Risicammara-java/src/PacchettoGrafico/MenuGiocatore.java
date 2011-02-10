/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
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


    public MenuGiocatore(Dimension dimensioni, ListaGiocatoriClient listaGiocatori, AttivatoreGrafica attivatoreGrafica) {
        this.dimensioni=dimensioni;
        this.visibile=false;
        this.attivatoreGrafica = attivatoreGrafica;
        this.listaGiocatori = listaGiocatori;
        Rectangle rettangoloTesto = new Rectangle(5, 55, 200, 0);
        this.Obbiettivo = new RiquadroTesto(rettangoloTesto, listaGiocatori.meStesso().getObbiettivo().toString());
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        if (visibile) {
            this.Obbiettivo.disegna(graphics2D);
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.visibile = !this.visibile;
        this.attivatoreGrafica.panelRepaint();
        //attivatoreGrafica.attiva();
        //cambiato = true;
    }

}

