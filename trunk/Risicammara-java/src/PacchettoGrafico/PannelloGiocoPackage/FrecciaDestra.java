/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * Classe che sfruttando i Poligoni di AWT implementa una "freccia" con la punta
 * orientata a destra.
 * @author matteo
 * @see Polygon
 */
public final class FrecciaDestra extends Polygon {
    private int freccia;

    /**
     * Costruisce una freccia con i parametri di posizione, altezza, larghezza e
     * lunghezza della parte accuminata (freccia).
     * @param p Posizione dell'angolo in alto a destra.
     * @param height Altezza massima che raggiunge la freccia.
     * @param width Larghezza massima che raggiunge la freccia.
     * @param freccia Larghezza della parte accuminata della freccia.
     */
    public FrecciaDestra(Point p, int height, int width, int freccia) {
        super();
        this.freccia = freccia;
        addPoint(p.x, p.y);
        addPoint(p.x+width-freccia, p.y);
        addPoint(p.x+width, p.y+(height/2));
        addPoint(p.x+width-freccia, p.y+height);
        addPoint(p.x, p.y+height);
        this.bounds = getBounds();
    }

    /**
     * Funzione che permette di cambiare la larghezza totale della freccia.
     * @param larghezza Nuova larghezza che deve avere la freccia.
     */
    public void cambiaLarghezza(int larghezza){
        Point p = new Point(xpoints[0], ypoints[0]);
        xpoints[1] = p.x+larghezza-freccia;
        xpoints[2] = p.x+larghezza;
        xpoints[3] = p.x+larghezza-freccia;
        //this.bounds.width = larghezza;
        this.bounds = getBounds();
    }

    /**
     * Ritorna un rettangolo in cui si ha la certezza che è contenuta tutta la
     * freccia. (Ha un abbondanza di un pixel sulla dimensione di larghezza).
     * @return Rettangolo contenente la freccia
     */
    @Override
    public Rectangle getBounds() {
        Point pos = new Point(xpoints[0], ypoints[0]);
        Dimension dim = new Dimension(xpoints[2]-pos.x+1, ypoints[4]-pos.y);
        return new Rectangle(pos, dim);
    }

    /**
     * Ritorna un rettangolo in cui che contiene una freccia identica all'oggetto
     * ma con il valore di larghezza diverso. Attenzione! Questa funzione non
     * cambia il valore della freccia memorizzata, ma ne crea una nuova (fittizzia)
     * di dimensioni diverse che non influirà sull'oggetto.
     * @param larghezza Larghezza che dovrebbe avere la freccia.
     * @return Rettangolo contenente la freccia fittizzia
     */
    public Rectangle getBounds(int larghezza){
        Point pos = new Point(xpoints[0], ypoints[0]);
        Dimension dim = new Dimension(larghezza+1, ypoints[4]-pos.y);
        return new Rectangle(pos, dim);
    }

    /**
     * Funzione che ritorna un rettangolo le cui dimensioni sono certe che contengono
     * la punta della freccia. La larghezza del rettangolo è stata maggiorata sia
     * a destra che a sinistra della punta di BottoneFase.OFFSET/2
     * @return Rettangolo contenente la punta della freccia.
     * @see BottoneFase.OFFSET
     */
    public Rectangle getPuntaBounds() {
        Point pos = new Point(xpoints[1]-(BottoneFase.OFFSET/2), ypoints[1]);
        Dimension dim = new Dimension(xpoints[2]-pos.x+BottoneFase.OFFSET, ypoints[4]-pos.y);
        Rectangle rect = new Rectangle(pos, dim);
        return rect;
    }

    @Override
    public String toString() {
        return "FrecciaDestra{xpoints=" + this.xpoints + "ypoints=" + this.ypoints + '}';
    }

}
