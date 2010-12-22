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
public class MillisecondiDiEsecuzione extends Elemento_2DGraphics{
    
    private OrologioTimer Cronometro;

    public MillisecondiDiEsecuzione (Dimension dimensioni, OrologioTimer Cronometro) {
        super(dimensioni);
        this.Cronometro = Cronometro;
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        graphics2D.drawString(""+Cronometro.getEsecTime(), 5, dimensioni.height-15);
    }

}
