/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author matteo
 */
public class MillisecondiDiEsecuzione implements Elemento_2DGraphics{

    private Dimension dimensioni;
    private OrologioTimer Cronometro;

    public MillisecondiDiEsecuzione (Dimension dimensioni, OrologioTimer Cronometro) {
        this.dimensioni=dimensioni;
        this.Cronometro = Cronometro;
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        graphics2D.setColor(colori.getSfondoScuro());
        graphics2D.drawString(""+Cronometro.getEsecTime(), 5, dimensioni.height-15);
    }

}
