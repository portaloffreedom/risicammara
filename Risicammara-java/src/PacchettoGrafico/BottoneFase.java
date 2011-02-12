/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author matteo
 */
public class BottoneFase extends Elemento_2DGraphicsCliccable implements ActionListener {
    private Dimension dimPannello;
    private AttivatoreGrafica attivatoreGrafica;
    private boolean smosciato;
    private boolean cambiato;

    public BottoneFase(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza) {
        super();
        FrecciaDestra freccia = new FrecciaDestra(p, altezza, larghezza, 27);
        super.posizione = freccia;
        this.dimPannello = dimPannello;
        this.attivatoreGrafica = ag;
        this.smosciato = false;
    }

    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        
        //fase di ridimensionamento (dipendente da smoscia)
        this.ridimensiona();
        
        //fase di disegno
        g2.setColor(colori.getColoreGiocatore());
        g2.fill(posizione);
        
        g2.setColor(colori.getSfondoScuro());
        g2.draw(posizione);
    }

    public void actionPerformed(ActionEvent ae) {
        this.setSmoscia(!this.smosciato);
    }

    public void cambiaLarghezza(int larghezza){
        ((FrecciaDestra)posizione).cambiaLarghezza(larghezza);
    }

    public void setSmoscia(boolean smosciato){
        this.smosciato = smosciato;
        this.attivatoreGrafica.panelRepaint();
    }

    private void ridimensiona(){
        if (smosciato){
            this.cambiaLarghezza(50);
        }
        else {
            int larghezza = BarraFasi.LarghezzaBottoni(dimPannello.width-250);
            this.cambiaLarghezza(larghezza);
        }
    }

    public Rectangle getMinBounds() {
        return ((FrecciaDestra)posizione).getBounds(BarraFasi.LARGHEZZABORDO);
    }

}
