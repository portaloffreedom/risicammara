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
public class TestoACapo implements  Elemento_2DGraphics {
    
    private String testo;
    private FontMetrics fontMetrics;
    private Rectangle rettangolo;
    protected Dimension dimensioni;
    
    public TestoACapo (Graphics2D graphics2D, Dimension dimensioni, Rectangle rectangolo, String testo){
        this.dimensioni=dimensioni;
        this.testo=testo;

        this.rettangolo = new Rectangle(rectangolo.x+4, rectangolo.y, rectangolo.width-6, 0);
        
        this.fontMetrics = graphics2D.getFontMetrics();
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        this.disegna(graphics2D, this.testo);
    }

    public void disegna(Graphics2D graphics2D, String testo) {
        boolean finito = false;
        String restante = testo;
        for (int i=1; !finito; i++){
            String sottoStringa = new String(restante);
            int spazio_precedente = restante.length();
            while(fontMetrics.stringWidth(sottoStringa)>rettangolo.width) {
                spazio_precedente = sottoStringa.lastIndexOf(' ', spazio_precedente);
                if (spazio_precedente == -1)
                    spazio_precedente = 0;
                sottoStringa = sottoStringa.substring(0, spazio_precedente);
            }
            graphics2D.drawString(sottoStringa, (float)rettangolo.getX(), (float)rettangolo.getY()+((fontMetrics.getMaxAscent()+2)*i));
            restante = restante.substring(spazio_precedente);
            if (restante.isEmpty()) finito=true;
            else restante = restante.substring(1);


            /*
            spazio_precedente = testo.lastIndexOf(' ', spazio_precedente);
            String sottoStringa = testo.substring(0, spazio_precedente);
            testo = testo.substring(spazio_precedente+1);
            System.out.println(sottoStringa+":"+testo);
             */


            //System.out.println("Stringa: "+sottoStringa);
            //System.out.println("Dimensioni: "+fontMetrics.stringWidth(sottoStringa));
        }
        //graphics2D.drawString(testo, (float)rettangolo.getX(), (float)rettangolo.getY()+fontMetrics.getMaxAscent());
    }

}
