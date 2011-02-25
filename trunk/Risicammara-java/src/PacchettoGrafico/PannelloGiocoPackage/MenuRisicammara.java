/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
abstract public class MenuRisicammara extends Elemento_2DGraphicsCliccable implements RisicammaraEventListener {
    private final int BORDO_OFFSET = 2;
    
    protected boolean aperto;
    protected AttivatoreGrafica attivatoreGrafica;

    public MenuRisicammara(AttivatoreGrafica attivatoreGrafica) {
        this(new Rectangle(), attivatoreGrafica);
    }

    public MenuRisicammara(Rectangle forma, AttivatoreGrafica attivatoreGrafica) {
        super(forma);
        this.attivatoreGrafica = attivatoreGrafica;
        this.aperto = false;
    }

    public void setAperto(boolean aperto) {
        this.aperto = aperto;
        ridisegna();
    }

    public Point getPosition(){
        return getRectangle().getLocation();
    }

    public Rectangle getRectangle(){
        return (Rectangle) posizione;
    }

    public void setLocation(Point p){
        getRectangle().setLocation(p);
    }

    public void setRectangle(Rectangle rect){
        super.setShape(rect);
    }

    /**
     * Chiamerà panel.repaint nelle dimensioni giuste
     */
    public void ridisegna(){
        attivatoreGrafica.panelRepaint(getRectangleLarge());
    }

    /**
     * Genera un Rettangolo nuovo con dimensioni maggiorate per fare un repaint
     * leggermente abbondante
     * @return
     */
    public Rectangle getRectangleLarge() {

        //rettangolo copia, per fare un repaint più ampio
        Rectangle rettangolo = new Rectangle(getRectangle());

        //allarga i contorni di un pixel e poi ingloba anche parte del pulsante
        rettangolo.y /= 2;
        rettangolo.height+= rettangolo.y + BORDO_OFFSET;
        rettangolo.x -= BORDO_OFFSET;
        rettangolo.width += BORDO_OFFSET*2;

        return rettangolo;
    }

    @Override  //l'azione che deve fare quando si preme il pulsante menu
    public void actionPerformed(EventoAzioneRisicammara e) {
        //attiva questo menu :D
        setAperto(!aperto);
        this.attivatoreGrafica.panelRepaint(getRectangleLarge());
    }

    @Override
    public boolean isClicked(Point punto) {
        //se non è visibile non è cliccabile
        return (aperto && super.isClicked(punto));
    }

}
