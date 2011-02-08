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

    private Rectangle rettangolo;
    private Dimension dimensioni;
    private boolean visibile;

    private ListaGiocatoriClient listaGiocatori;


    public MenuGiocatore(Dimension dimensioni, ListaGiocatoriClient listaGiocatori) {
        this.dimensioni=dimensioni;
        this.visibile=false;
        this.listaGiocatori = listaGiocatori;

        this.rettangolo = new Rectangle(5, 55, 120, 70);
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        if (visibile) {

            //parte provvisoria
            graphics2D.setColor(Color.red);
            graphics2D.fill(rettangolo);
            graphics2D.setColor(Color.black);
            new TestoACapo(graphics2D, dimensioni, rettangolo, listaGiocatori.meStesso().getObbiettivo().toString())
                          .disegna(graphics2D);
            //graphics2D.drawString("Tutti gli uomini del\n presidente", this.bordo+5, this.distanzaSuperiore+15);
            
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.visibile = !this.visibile;
    }
    
}

