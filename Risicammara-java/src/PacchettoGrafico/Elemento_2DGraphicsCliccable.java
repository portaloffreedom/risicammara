/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 *
 * @author matteo
 */
public abstract class Elemento_2DGraphicsCliccable implements Elemento_2DGraphics {
    protected Shape posizione;
    private ActionListener ascoltatore;

    public Elemento_2DGraphicsCliccable(){
        this.posizione = new Rectangle();
    }

    public Elemento_2DGraphicsCliccable(Shape posizione) {
        this.posizione = posizione;
    }

    public void setShape(Shape posizione) {
        this.posizione = posizione;
    }

    public boolean isClicked (Point punto){
        return (posizione.contains(punto));
    }

    public boolean doClicked (MouseEvent e){
        if (isClicked(e.getPoint())) {
            this.actionPressed(e);
            return true;
        }
        else
            return false;
    }

    public void setActionListener(ActionListener ascoltatore){
        this.ascoltatore = ascoltatore;
    }

    public void actionPressed(MouseEvent e){
        if (this.ascoltatore != null) {
            ActionEvent actionEvent = new ActionEvent(this, e.getID(), "cliccato");
            this.ascoltatore.actionPerformed(actionEvent);
        }
    }

    //abstract public void actionRollOver(MouseEvent e);
    
}
