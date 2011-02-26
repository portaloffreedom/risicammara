/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author matteo
 */
public class BottoneFaseAvanzato extends BottoneFase {
    static private final int MARGINE = 3;
    private RisultatoDadi risultatoDadi;

    public BottoneFaseAvanzato(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza) {
        super(dimPannello, ag, p, larghezza, altezza);
        this.risultatoDadi = new RisultatoDadi(ag, altezza-(MARGINE*2), new Point(), MARGINE);
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        super.disegna(g2, colori);
        if (smosciato()) {
            //TODO posziona in basso a destra
            this.risultatoDadi.risposiziona(new Point(dimPannello.width, dimPannello.height-getFrecciaDestra().getBounds().height-(MARGINE*2)));
        }
        else {
            //TODO posiziona sulla barra
            this.risultatoDadi.risposiziona(new Point(getFrecciaDestra().getPuntaBounds().x, getFrecciaDestra().getBounds().y+MARGINE));
        }

        this.risultatoDadi.disegna(g2, colori);
    }

    public void setRisultato(int[] dadiAttacco, int[] dadiDifesa) {
        risultatoDadi.setRisultato(dadiAttacco, dadiDifesa);
    }

    public void disattivaDadi() {
        risultatoDadi.disattiva();
    }
}
