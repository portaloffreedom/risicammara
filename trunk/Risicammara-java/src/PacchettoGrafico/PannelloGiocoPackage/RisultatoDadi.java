/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Oggetto che visualizza il risultato dei dadi. La posizione non è dipendente
 * dalle dimensioni del pannello, ma è da impostare a mano ogni volta che si
 * deve spostare.
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

    private AnimatoreGraficaPannelli attivatoreGrafica;

    /**
     * Oggetto che visualizza il risultato dei dadi. La posizione deve essere
     * impostata dall'esterno.
     * @param attivatoreGrafica riferimento all'attivatore grafica
     * @param altezza altezza in pixel dei dadi (essendo i dadi quadrati, questa
     * diventa anche la larghezza dei singoli dadi).
     * @param posizione posizione dell'angolo in alto a destra di questa figura
     * @param spaziaturaDadi spaziatura che ci deve essere tra un dado e l'altro.
     * Questa spaziatura viene anche inserita all'inizio e alla fine (destra e
     * sinistra) dell'intero disegno.
     */
    public RisultatoDadi(AnimatoreGraficaPannelli attivatoreGrafica, int altezza, Point posizione, int spaziaturaDadi) {
        this.posizione = new Rectangle(posizione.x, posizione.y, spaziaturaDadi, altezza+1);
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

    /**
     * Nasconde questo disegno
     */
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
        Dado dado = new Dado(BOMBATURA_DADO);

        
        for (int i=dadiDifesa.length-1; i>=0; i--) {
            boolean inverti = false;
            Color coloreDado = Color.BLUE;
            bordiDado.x -= (dimensioneDado+spaziaturaDadi);
            if (i >= dadiAttacco.length)
                inverti = true;
            else
                if (dadiDifesa[i] < dadiAttacco[i])
                    coloreDado = coloreDado.darker().darker();
            //disegnaDado(g2, coloreDado, dadiDifesa[i], bordiDado, ga, inverti);
            dado.setImpostazioniDado(coloreDado, Integer.toString(dadiDifesa[i]), bordiDado, inverti);
            dado.disegna(g2, ga);
        }

        for (int i=dadiAttacco.length-1; i>=0; i--) {
            boolean inverti = false;
            Color coloreDado = Color.red;
            bordiDado.x -= (dimensioneDado+spaziaturaDadi);
            if (i >= dadiDifesa.length)
                inverti = true;
            else
                if (dadiAttacco[i] <= dadiDifesa[i])
                    coloreDado = coloreDado.darker().darker();
            //disegnaDado(g2, coloreDado, dadiAttacco[i], bordiDado, ga, inverti);
            dado.setImpostazioniDado(coloreDado, Integer.toString(dadiAttacco[i]), bordiDado, inverti);
            dado.disegna(g2, ga);
        }
    }

}
