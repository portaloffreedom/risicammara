/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 * @author matteo
 */
public class BottoneFaseAttaccoAvanzato extends BottoneFaseAvanzato {
    static private final int MARGINE = 3;
    private RisultatoDadi risultatoDadi;

    private boolean richiestaNumeroArmateAttaccanti;
    private RichiestaArmateAttaccanti armateAttaccanti;

    public BottoneFaseAttaccoAvanzato(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza) {
        super(dimPannello, ag, p, larghezza, altezza, "HAI VINTO!");
        this.risultatoDadi = new RisultatoDadi(ag, altezza-(MARGINE*2), new Point(), MARGINE);
        this.armateAttaccanti = new RichiestaArmateAttaccanti(MARGINE*2,MARGINE, new Rectangle(getBounds().x, getBounds().y+MARGINE, 0, getBounds().height-MARGINE*2));
        this.richiestaNumeroArmateAttaccanti = false;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        super.disegna(g2, colori);
        if (smosciato())
            this.risultatoDadi.risposiziona(new Point(dimPannello.width, dimPannello.height-getFrecciaDestra().getBounds().height-(MARGINE*2)));
        else
            this.risultatoDadi.risposiziona(new Point(getFrecciaDestra().getPuntaBounds().x, getFrecciaDestra().getBounds().y+MARGINE));

        this.risultatoDadi.disegna(g2, colori);

        //TODO disegna la richiesta numero armate
        if (richiestaNumeroArmateAttaccanti) {
            armateAttaccanti.disegna(g2, colori);
        }

    }

    public void setRisultatoDadi(int[] dadiAttacco, int[] dadiDifesa) {
        risultatoDadi.setRisultato(dadiAttacco, dadiDifesa);
    }

    public void disattivaDadi() {
        risultatoDadi.disattiva();
    }

    public int getNumeroArmateAttaccanti() {
        return armateAttaccanti.getNumeroArmateAttaccanti();
    }

    public void setRichiestaNumeroArmateAttaccanti(boolean richiestaNumeroArmateAttaccanti) {
        this.richiestaNumeroArmateAttaccanti = richiestaNumeroArmateAttaccanti;
        attivatoreGrafica.panelRepaint(this.getBounds());
    }

    public void setMaxLancioDadi(int maxLancio) {
        armateAttaccanti.setMaxLancio(maxLancio);
    }

    public void setActionListenerArmateAttaccanti(RisicammaraEventListener ascoltatore) {
        armateAttaccanti.setActionListener(ascoltatore);
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        super.actionPressed(e);
        if (richiestaNumeroArmateAttaccanti){
            armateAttaccanti.doClicked(e);
        }
    }
}
