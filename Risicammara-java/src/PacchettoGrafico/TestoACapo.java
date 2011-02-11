/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author matteo
 */
public class TestoACapo implements  Elemento_2DGraphics {
    
    private String testo;
    private Rectangle rettangoloTesto;
    private LinkedList<String> listaRighe;
    
    public TestoACapo (Rectangle rettangolo, String testo){
        this.testo=testo;
        this.rettangoloTesto = rettangolo;
        this.listaRighe = new LinkedList<String>();
    }

    public TestoACapo (Point posizione, int larghezza, String testo){
        this(new Rectangle(posizione.x, posizione.y, larghezza, 0), testo);
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        this.preparaTesto(graphics2D);
        this.disegnaTesto(graphics2D, colori.getTesto());
    }

    public void preparaTesto(Graphics2D graphics2D) {
        this.listaRighe = new LinkedList<String>();
        boolean finito = false;
        String restante = testo;
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        for (int i=1; !finito; i++){
            String sottoStringa = new String(restante);
            int spazio_precedente = restante.length();
            while(fontMetrics.stringWidth(sottoStringa)>rettangoloTesto.width) {
                spazio_precedente = sottoStringa.lastIndexOf(' ', spazio_precedente);
                if (spazio_precedente == -1)
                    spazio_precedente = 0;
                sottoStringa = sottoStringa.substring(0, spazio_precedente);
            }
            //graphics2D.drawString(sottoStringa, (float)rettangoloTesto.getX(), (float)rettangoloTesto.getY()+((fontMetrics.getMaxAscent()+2)*i));
            listaRighe.add(sottoStringa);
            restante = restante.substring(spazio_precedente);
            if (restante.isEmpty()) finito=true;
            else restante = restante.substring(1);
        }
    }

    public void disegnaTesto(Graphics2D graphics2D, Color colore){
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int i=1;
        graphics2D.setColor(colore);
        for (String riga : listaRighe){
            graphics2D.drawString(riga, (float)rettangoloTesto.getX(), (float)rettangoloTesto.getY()+((fontMetrics.getMaxAscent()+2)*i));
            i++;
        }
    }

    public int getAltezzaTesto(Graphics2D graphics2D){
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        return fontMetrics.getHeight()*listaRighe.size();
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setRettangolo(Rectangle rettangolo) {
        this.rettangoloTesto = rettangolo;
    }
}
