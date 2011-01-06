/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
public class TestoACapo extends Elemento_2DGraphics {
    
    private String testo;
    private FontMetrics fontMetrics;
    private Rectangle rettangolo;
    
    public TestoACapo (Graphics2D graphics2D, Dimension dimensioni, Rectangle rectangolo, String testo){
        super(dimensioni);
        this.testo=testo;
        
        this.rettangolo= rectangolo;
        this.rettangolo.x +=2;
        this.rettangolo.y +=1;
        this.rettangolo.height -=2;
        this.rettangolo.width  -=4;

        this.fontMetrics = graphics2D.getFontMetrics();
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        this.disegna(graphics2D, this.testo);
    }

    public void disegna(Graphics2D graphics2D, String testo) {
        boolean finito = false;
        int spazio_precedente = testo.length();
        while(!finito){


            spazio_precedente = testo.lastIndexOf(' ', spazio_precedente);
            String sottoStringa = testo.substring(0, spazio_precedente);
            testo = testo.substring(spazio_precedente+1);
            System.out.println(sottoStringa+":"+testo);


            //System.out.println("Stringa: "+sottoStringa);
            //System.out.println("Dimensioni: "+fontMetrics.stringWidth(sottoStringa));

            finito = true;
        }
        graphics2D.drawString(testo, (float)rettangolo.getX(), (float)rettangolo.getY()+fontMetrics.getMaxAscent());
    }

}
