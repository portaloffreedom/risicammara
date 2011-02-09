/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author matteo
 */
public class BottoneRisicammaraBis extends Elemento_2DGraphicsCliccable {
    private ImageIcon sfondo;
    private ImageIcon rollover;
    private ImageIcon pressione;
    private String testo;

    public BottoneRisicammaraBis(Point posizione, String testo) {
        super();
        this.testo = testo;

        this.sfondo = new ImageIcon("./risorse/sfondo_pulsante.png", "Descrizione");
        this.rollover = new ImageIcon("./risorse/mouse_pulsante.png",  "Descrizione");
        this.pressione = new ImageIcon("./risorse/premuto_pulsante.png",  "Descrizione");

        super.setShape(new Rectangle(posizione, new Dimension(sfondo.getIconWidth(), sfondo.getIconHeight())));
    }

    public void disegna(Graphics2D graphics2D){
        Rectangle rect =((Rectangle)this.posizione);
        //disegna immagine di sfondo del pulsante
        //TODO fare diverse immagini a seconda che il pulsante sia premuto o no
        graphics2D.drawImage(sfondo.getImage(), rect.x, rect.y, null);
        //disegna il testo sopra al pulsante
        FontMetrics metrica = graphics2D.getFontMetrics();
        Point puntoTesto = new Point();
        puntoTesto.x = ((rect.width-metrica.stringWidth(testo))/2)+rect.x;
        puntoTesto.y = ((rect.height-metrica.getHeight())/2)+rect.y+metrica.getHeight();
        graphics2D.drawString(testo, puntoTesto.x, puntoTesto.y);
    }


    public void setPosizione(Point posizione){
        ((Rectangle)this.posizione).setLocation(posizione);
    }

    @Override
    public void actionPressed(MouseEvent e) {
        System.out.println("Premuto BottoneRisicammara "+testo);
    }

    @Override
    public void actionRollOver(MouseEvent e) {
        System.out.println("Rollover BottoneRisicammara "+testo);
    }
}
