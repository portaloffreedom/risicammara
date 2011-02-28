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
 * Crea un dado con del testo (centrato). Può essere spostato dove si vuole.
 * Non si ridisegna da solo.
 * @author matteo
 */
public class Dado extends Elemento_2DGraphicsCliccable {
    private int bombaturaDado;
    private boolean inverti;
    private String valoreDado;
    protected Color coloreDado;

    public Dado(Rectangle dimensioni, int bombaturaDado, boolean inverti, String valoreDado, Color coloreDado) {
        this.bombaturaDado = bombaturaDado;
        this.setImpostazioniDado(coloreDado, valoreDado, dimensioni, inverti);
    }

    /**
     * Crea un dado con la bordatura (quanto i bordi devono essere arrotondati)
     * impostata e con le altre impostazioni al minimo:<br>
     * - dimesioni   = 0,0,0,0<br>
     * - interti     = false<br>
     * - valore Dado = ""<br>
     * - colore Dado = Color.WHITE
     * @param bombaturaDado
     */
    public Dado(int bombaturaDado) {
        this(new Rectangle(), bombaturaDado, false, "", Color.WHITE);
    }

    /**
     * Ritorna i bordi (posizione e dimensioni) del dado.
     * @return bordi (posizione e dimensioni) del dado.
     */
    public RoundRectangle2D getBordi(){
        return (RoundRectangle2D) posizione;
    }

    /**
     * Ritorna i bordi (posizione e dimensioni) del dado non considerando
     * l'arrotondamento degli angoli.
     * @return bordi (posizione e dimensioni) del dado non arrotondati.
     */
    public Rectangle getBordiNetti(){
        return getBordi().getBounds();
    }

    public void setBordiDado(Rectangle bordiDado) {
        this.posizione = new java.awt.geom.RoundRectangle2D.Float(bordiDado.x, bordiDado.y, bordiDado.width, bordiDado.height, bombaturaDado, bombaturaDado);
    }

    /**
     * Imposta cosa visualizzare sopra al dado.
     * @param valoreDado Stringa da visualizzare sopra al dado.
     */
    public void setValoreDado(String valoreDado) {
        this.valoreDado = valoreDado;
    }

    /**
     * Imposta di nuovo i parametri del dado.
     * @param coloreDado colore che deve avere il dado (se non invertito, è il
     * colore di fondo).
     * @param valoreDado Valore che deve essere visualizzato sopra al dado.
     * @param bordiDado Nuova posizione e dimensioni del dado.
     * @param inverti true se i colori del dado devono essere invertiti.
     */
    final public void setImpostazioniDado(Color coloreDado, String valoreDado, Rectangle bordiDado, boolean inverti) {
        this.coloreDado = coloreDado;
        setValoreDado(valoreDado);
        setBordiDado(bordiDado);
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
