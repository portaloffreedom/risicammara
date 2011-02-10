/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
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
    private Color coloreGiocatore;
    private Color coloreTesto;

    public RiquadroTesto(Rectangle rettangolo, String testo, Colore_t coloreGiocatore) {
        super(RimpicciolisciPerTesto(rettangolo), testo);
        this.rettangoloRiquadro = rettangolo;
        this.coloreGiocatore = coloreGiocatore.getColor();
        if (this.coloreGiocatore == Color.BLACK || this.coloreGiocatore == Color.BLUE)
            this.coloreTesto = Color.WHITE;
        else
            this.coloreTesto = Color.BLACK;
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        //prepara per il disegno
        super.preparaTesto(graphics2D);
        int altezza = super.getAltezzaTesto(graphics2D);
        this.rettangoloRiquadro.height = altezza+2;

        //disengna effettivamente
        graphics2D.setColor(coloreGiocatore);
        graphics2D.fill(rettangoloRiquadro);
        graphics2D.setColor(Color.BLACK);
        graphics2D.draw(rettangoloRiquadro);
        graphics2D.setColor(coloreTesto);
        super.disegnaTesto(graphics2D);
    }

    @Override
    public void setRettangolo(Rectangle rettangolo){
        super.setRettangolo(RimpicciolisciPerTesto(rettangolo));
        this.rettangoloRiquadro = rettangolo;
    }

    public void setPosizione(Point posizione){
        this.setRettangolo(new Rectangle(posizione, rettangoloRiquadro.getSize()));
    }

    static private Rectangle RimpicciolisciPerTesto(Rectangle rettangolo){
        return new Rectangle(rettangolo.x+4, rettangolo.y, rettangolo.width-6, 0);
    }
}
