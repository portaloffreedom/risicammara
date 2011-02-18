/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.boardManage;

import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
public class TerritorioPlanciaClient extends TerritorioPlancia {
    private Rectangle posizione;
    private Point posizioneCerchietto;

    public TerritorioPlanciaClient(TerritorioPlancia territorioPlancia){
        super(territorioPlancia);
        this.posizione = null;
        this.posizioneCerchietto = null;
    }

    public Rectangle getPosizione() {
        return posizione;
    }

    public void setPosizione(Rectangle posizione) {
        this.posizione = posizione;
    }

    public Point getPosizioneCerchietto() {
        return posizioneCerchietto;
    }

    public void setPosizioneCerchietto(Point posizioneCerchietto) {
        this.posizioneCerchietto = posizioneCerchietto;
    }
}
