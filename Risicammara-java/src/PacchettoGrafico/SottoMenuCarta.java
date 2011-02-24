/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import risicammaraClient.Client;

/**
 *
 * @author matteo
 */
public class SottoMenuCarta extends Elemento_2DGraphicsCliccable {
    static private int LARGHEZZA_CARTA = MenuCarte.LARGHEZZA_CARTA;
    static private int ALTEZZA_CARTA   = MenuCarte.ALTEZZA_CARTA;
    static private int MARGINE = 5;

    private String testo;

    public SottoMenuCarta(String testo) {
        super(new Rectangle(LARGHEZZA_CARTA, ALTEZZA_CARTA));
        this.testo = testo;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        g2.setColor(ga.getColoreGiocatore());
        g2.fill(posizione);
        g2.setColor(ga.getTesto());
        g2.draw(posizione);


        //nome territorio
        int altezzaTesto = g2.getFontMetrics().getHeight();
        Point p = this.getPosizione();
        Point posizioneTesto = new Point(p.x+MARGINE, p.y+altezzaTesto+(ALTEZZA_CARTA-altezzaTesto)/2);


        g2.drawString(testo, posizioneTesto.x, posizioneTesto.y);


        //TOTO bonus
            //mancano le immagini di roberto
    }

    public void setPosizione(Point p) {
        ((Rectangle)super.posizione).setLocation(p);
    }

    public Point getPosizione(){
        return ((Rectangle)super.posizione).getLocation();
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        //mi serve per debuggare il codice delle carte premute :D
        if (Client.DEBUG)
            System.out.println("Sotto menu \""+this.testo+"\" premuto");
        super.actionPressed(e);
    }

}
