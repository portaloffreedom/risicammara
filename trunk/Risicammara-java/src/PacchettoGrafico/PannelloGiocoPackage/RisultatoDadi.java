/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
public class RisultatoDadi implements Elemento_2DGraphics {
    static private final int BOMBATURA_DADO = 15;
    private Rectangle posizione;
    private int dimensioneDado;
    private int spaziaturaDadi;

    private int dadiAttacco[];
    private int dadiDifesa[];
    private boolean attivo;

    private AttivatoreGrafica attivatoreGrafica;

    public RisultatoDadi(AttivatoreGrafica attivatoreGrafica, int altezza, Point posizione, int spaziaturaDadi) {
        this.posizione = new Rectangle(posizione.x, posizione.y, spaziaturaDadi, altezza);
        this.attivatoreGrafica = attivatoreGrafica;
        this.dimensioneDado = altezza;
        this.spaziaturaDadi = spaziaturaDadi;
        this.attivo = false;
        this.dadiAttacco = new int[0];
        this.dadiDifesa  = new int[0];
    }

    private Rectangle ridimensiona() {
        Rectangle bordi = getBordi();
        int larghezza = bordi.width;
        bordi.width = ((dadiAttacco.length+dadiDifesa.length)*(dimensioneDado+spaziaturaDadi))+spaziaturaDadi;
        bordi.x += (larghezza - bordi.width);
        return bordi;
    }

    private void ridisegna() {
        attivatoreGrafica.panelRepaint(getBordi());
    }

    private Rectangle getBordi() {
        return posizione;
    }

    private Rectangle getMaxBordi() {
        Rectangle bordi = new Rectangle(getBordi());
        int larghezza = bordi.width;
        bordi.width = ((6)*(dimensioneDado+spaziaturaDadi))+spaziaturaDadi;
        bordi.x += (larghezza - bordi.width);
        return bordi;
    }

    /**
     * Fa visualizzare un risultato.<br>
     * Nota bene, già questa funzione al suo interno chiama repaint.
     * @param dadiAttacco risultati del dado dell'attaccante.
     * @param dadiDifesa risultati del dado del difensore.
     */
    public void setRisultato(int dadiAttacco[], int dadiDifesa[]) {
        boolean ridimensionaPrima = false;
        if (dadiAttacco.length+dadiDifesa.length > this.dadiAttacco.length+this.dadiDifesa.length)
            ridimensionaPrima = true;

        this.dadiAttacco = dadiAttacco;
        this.dadiDifesa = dadiDifesa;
        this.attivo = true;
        
        if (ridimensionaPrima) {
            ridimensiona();
            ridisegna();
        }
        else {
            ridisegna();
            ridimensiona();
        }
    }

    public void disattiva() {
        this.attivo = false;
        ridisegna();
    }

    /**
     * imposta la posizione del lato destro della figura.
     * @param distanza distanza dal bordo sinistro del pannello al bordo destro
     * dei RisultatoDadi
     */
    public void setPosizioneDestra(int distanza) {
        Rectangle rect = getBordi();
        rect.x = distanza-rect.width;
    }

    /**
     * Riposiziona l'angolo in alto a destra di RisultatoDadi
     * @param p nuova posizione
     */
    public void risposiziona(Point p) {
        setPosizioneDestra(p.x);
        getBordi().y = p.y;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        if (!attivo)
            return; //se non sono stati attivati non disegna niente
        //TODO disegna i dadi
        Rectangle bordiFigura = getBordi();
        int margineDestro = bordiFigura.x + bordiFigura.width;
        Rectangle bordiDado = new Rectangle(dimensioneDado, dimensioneDado);
        bordiDado.y = bordiFigura.y;
        bordiDado.x = margineDestro;

        //g2.setColor(Color.blue);
        for (int i=dadiDifesa.length-1; i>=0; i--) {
            bordiDado.x -= (dimensioneDado+spaziaturaDadi);
            disegnaDado(g2, Color.blue, dadiDifesa[i], bordiDado);
        }
        //g2.setColor(Color.red);
        for (int i=dadiAttacco.length-1; i>=0; i--) {
            bordiDado.x -= (dimensioneDado+spaziaturaDadi);
            disegnaDado(g2, Color.red, dadiAttacco[i], bordiDado);
        }
    }

    private void disegnaDado(Graphics2D g2, Color coloreDado, int numeroDado, Rectangle bordiDado) {
        //disegna sfondo dado
        g2.setColor(coloreDado);
        g2.fillRoundRect(bordiDado.x, bordiDado.y, bordiDado.width, bordiDado.height, BOMBATURA_DADO, BOMBATURA_DADO);
        //disegna valore dado
        String valoreDaDisegnare = Integer.toString(numeroDado);
        g2.setColor(Color.black);
        //disegna testo centrato
        FontMetrics metrica = g2.getFontMetrics();
        Rectangle rettangoloTesto = new Rectangle();
        rettangoloTesto.width  = metrica.stringWidth(valoreDaDisegnare);
        rettangoloTesto.height = metrica.getHeight();
        rettangoloTesto.x = bordiDado.x + (bordiDado.width -rettangoloTesto.width )/2;
        rettangoloTesto.y = bordiDado.y + (bordiDado.height-rettangoloTesto.height)/2 + rettangoloTesto.height;
        g2.drawString(valoreDaDisegnare, rettangoloTesto.x, rettangoloTesto.y);
    }

}
