/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author matteo
 */
public class BarraSuperiore extends Elemento_2DGraphics {
    
    protected int altezza;

    public BarraSuperiore(Dimension dimensioni, int altezza){
        super(dimensioni);
        this.altezza=altezza;
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        graphics2D.fillRect(0, 0, dimensioni.width, altezza);
    }


}
