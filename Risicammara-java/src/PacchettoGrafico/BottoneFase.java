/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author matteo
 */
public class BottoneFase extends Elemento_2DGraphicsCliccable implements ActionListener {


    public BottoneFase(Point p, int larghezza, int altezza) {
        super();
        FrecciaDestra freccia = new FrecciaDestra(p, altezza, larghezza, 27);
        super.posizione = freccia;
    }

    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        g2.setColor(colori.getColoreGiocatore());
        g2.fill(posizione);
        g2.setColor(colori.getSfondoScuro());
        g2.draw(posizione);
    }

    public void actionPerformed(ActionEvent ae) {
        this.cambiaLarghezza(50);
    }

    public void cambiaLarghezza(int larghezza){
        ((FrecciaDestra)posizione).cambiaLarghezza(larghezza);
    }

}
