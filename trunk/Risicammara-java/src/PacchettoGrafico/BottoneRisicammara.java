/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.Giocatore;

/**
 *
 * @author matteo
 */
public class BottoneRisicammara extends Elemento_2DGraphicsCliccable {
    private Giocatore mestesso;
    private boolean pressed;

    private ImageIcon sfondo;
    private ImageIcon rollover;
    private ImageIcon pressione;
    private String testo;

    public BottoneRisicammara(Point posizione, String testo, Giocatore mestesso) {
        super();
        this.testo = testo;
        this.mestesso = mestesso;
        this.pressed = false;

        this.sfondo = new ImageIcon("./risorse/sfondo_pulsante.png", "Descrizione");
        this.rollover = new ImageIcon("./risorse/mouse_pulsante.png",  "Descrizione");
        this.pressione = new ImageIcon("./risorse/premuto_pulsante.png",  "Descrizione");

        super.setShape(new Rectangle(posizione, new Dimension(sfondo.getIconWidth(), sfondo.getIconHeight())));

    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori){
        Rectangle rect =((Rectangle)this.posizione);

        //disegna immagine di sfondo del pulsante
        //TODO fare diverse immagini a seconda che il pulsante sia premuto o no
        //graphics2D.drawImage(sfondo.getImage(), rect.x, rect.y, null); //disegnava l'immagine
        graphics2D.setColor(colori.getColoreGiocatore());
        graphics2D.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 11, 11);
        if (pressed){
            graphics2D.fillRect(rect.x, rect.y+(rect.height/2), rect.width, rect.height/2);
        }

        //disegna il testo sopra al pulsante
        graphics2D.setColor(colori.getTesto());
        FontMetrics metrica = graphics2D.getFontMetrics();
        Point puntoTesto = new Point();
        puntoTesto.x = ((rect.width-metrica.stringWidth(testo))/2)+rect.x;
        puntoTesto.y = ((rect.height-metrica.getHeight())/2)+rect.y+metrica.getHeight();
        graphics2D.drawString(testo, puntoTesto.x, puntoTesto.y);
    }


    public void setPosizione(Point posizione){
        ((Rectangle)this.posizione).setLocation(posizione);
    }

    public void setPressed(boolean pressed){
        this.pressed = pressed;
    }
}
