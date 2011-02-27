/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author matteo
 */
public class Dado extends Elemento_2DGraphicsCliccable {
    private int bombaturaDado;
    private boolean inverti;
    private String valoreDado;
    private Color coloreDado;

    public Dado(Rectangle dimensioni, int bombaturaDado, boolean inverti, String valoreDado, Color coloreDado) {
        this(bombaturaDado);
        this.setImpostazioniDado(coloreDado, valoreDado, dimensioni, inverti);
    }

    public Dado(int bombaturaDado) {
        this.bombaturaDado = bombaturaDado;
    }

    public RoundRectangle2D getBordi(){
        return (RoundRectangle2D) posizione;
    }

    final public void setImpostazioniDado(Color coloreDado, String valoreDado, Rectangle bordiDado, boolean inverti) {
        this.coloreDado = coloreDado;
        this.valoreDado = valoreDado;
        this.posizione = new java.awt.geom.RoundRectangle2D.Float(bordiDado.x, bordiDado.y, bordiDado.width, bordiDado.height, bombaturaDado, bombaturaDado);
        this.inverti = inverti;
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced graphicsAdvanced) {
        disegnaDado(graphics2D, coloreDado, valoreDado, getBordi(), graphicsAdvanced, inverti);
    }

    /**
     * Disegna un singolo dado.
     * @param g2 contesto grafico
     * @param coloreDado colore del dado
     * @param valoreDado numero da visualizzare sul dado
     * @param bordiDado dimensioni e posizione del dado
     * @param ga colori aggiuntivi
     * @param inverti true se invertire i colori
     */
    private void disegnaDado(Graphics2D g2, Color coloreDado, String valoreDado, RoundRectangle2D bordiDado, GraphicsAdvanced ga, boolean inverti) {
        Color bordo = ga.getColoreScuro();
        Color testo = Color.BLACK;
        Color sfondo = coloreDado;
        if (inverti){
            bordo = coloreDado;
            testo = coloreDado;
            sfondo = ga.getColoreScuro();
        }

        //disegna sfondo dado
        g2.setColor(sfondo);
        g2.fill(posizione);
        g2.setColor(bordo);
        g2.draw(posizione);
        //disegna valore dado
        g2.setColor(testo);
        //disegna testo centrato
        FontMetrics metrica = g2.getFontMetrics();
        Rectangle rettangoloTesto = new Rectangle();
        rettangoloTesto.width  = metrica.stringWidth(valoreDado);
        rettangoloTesto.height = metrica.getHeight();
        Rectangle bordiDadoNetti = bordiDado.getBounds();
        rettangoloTesto.x = bordiDadoNetti.x + (bordiDadoNetti.width -rettangoloTesto.width )/2;
        rettangoloTesto.y = bordiDadoNetti.y + (bordiDadoNetti.height-rettangoloTesto.height)/2 + rettangoloTesto.height;
        g2.drawString(valoreDado, rettangoloTesto.x, rettangoloTesto.y);
    }


}
