/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Point;
import java.awt.Polygon;

/**
 *
 * @author matteo
 */
public class FrecciaDestra extends Polygon {
    private int freccia;

    public FrecciaDestra(Point p, int height, int width, int freccia) {
        super();
        this.freccia = freccia;
        addPoint(p.x, p.y);
        addPoint(p.x+width-freccia, p.y);
        addPoint(p.x+width, p.y+(height/2));
        addPoint(p.x+width-freccia, p.y+height);
        addPoint(p.x, p.y+height);
    }

    public void cambiaLarghezza(int larghezza){
        Point p = new Point(xpoints[0], ypoints[0]);
        xpoints[1] = p.x+larghezza-freccia;
        xpoints[2] = p.x+larghezza;
        xpoints[3] = p.x+larghezza-freccia;
        //this.bounds.width = larghezza;
    }

}
