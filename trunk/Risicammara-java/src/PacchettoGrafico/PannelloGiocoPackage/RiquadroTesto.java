/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import PacchettoGrafico.PannelloGiocoPackage.TestoACapo;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.Colore_t;

/**
 *
 * @author matteo
 */
public class RiquadroTesto extends TestoACapo {

    private Rectangle rettangoloRiquadro;

    public RiquadroTesto(Rectangle rettangolo, String testo, Colore_t coloreGiocatore) {
        super(RimpicciolisciPerTesto(rettangolo), testo);
        this.rettangoloRiquadro = rettangolo;
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        //prepara per il disegno
        super.preparaTesto(graphics2D);
        int altezza = super.getAltezzaTesto(graphics2D);
        this.rettangoloRiquadro.height = altezza+2;

        //disengna effettivamente
        graphics2D.setColor(colori.getSfondoTesto());
        graphics2D.fill(rettangoloRiquadro);
        super.disegnaTesto(graphics2D, colori.getTesto());
    }

    @Override
    public void setRettangolo(Rectangle rettangolo){
        super.setRettangolo(RimpicciolisciPerTesto(rettangolo));
        this.rettangoloRiquadro = rettangolo;
    }

    public void setPosizione(Point posizione){
        this.setRettangolo(new Rectangle(posizione, rettangoloRiquadro.getSize()));
    }

    public Rectangle getRettangoloRiquadro() {
        return rettangoloRiquadro;
    }

    static private Rectangle RimpicciolisciPerTesto(Rectangle rettangolo){
        return new Rectangle(rettangolo.x+4, rettangolo.y, rettangolo.width-6, 0);
    }
}
