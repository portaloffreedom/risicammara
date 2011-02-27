/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
public class BottoneFaseAvanzato extends BottoneFase {
    private boolean richiestaSpostamento;
    private String testoSupSpostamento;
    private String testoInfSpostamento;

    public BottoneFaseAvanzato(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza, String testoSupSpostamento) {
        super(dimPannello, ag, p, larghezza, altezza);
        this.richiestaSpostamento = true;
        this.testoSupSpostamento = testoSupSpostamento;
        this.testoInfSpostamento = "Quante armate vuoi spostare?";
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        super.disegna(g2, colori);
        if (richiestaSpostamento && !smosciato()) {
            Rectangle rect = getBounds();
            FontMetrics metrica = g2.getFontMetrics();

            Rectangle testoSup = new Rectangle();
            Rectangle testoInf = new Rectangle();

            testoInf.height = metrica.getHeight();
            testoSup.height = metrica.getHeight();
            testoInf.width = metrica.stringWidth(testoInfSpostamento);
            testoSup.width = metrica.stringWidth(testoSupSpostamento);

            int larghezzaMax = Math.max(testoInf.width, testoSup.width);

            testoInf.x = rect.x + (larghezzaMax - testoInf.width)/2;
            testoSup.x = rect.x + (larghezzaMax - testoSup.width)/2;

            testoSup.y = rect.y + (rect.height - testoInf.height - testoSup.height)/2;
            testoInf.y = testoSup.y + testoSup.height;

            //pulisce lo sfondo
            g2.setColor(colori.getColoreGiocatore());
            g2.fill(testoInf.createUnion(testoSup));

            testoSup.y += testoSup.height - metrica.getDescent();
            testoInf.y += testoInf.height - metrica.getDescent();

            //disegna il testo vero
            g2.setColor(Color.BLACK);
            g2.drawString(testoSupSpostamento, testoSup.x, testoSup.y);
            g2.drawString(testoInfSpostamento, testoInf.x, testoInf.y);

            //TODO disegna lo spin
        }
    }

}
