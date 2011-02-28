/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * Disegna del testo su più righe entro i bordi impostati. Viene ignorata la
 * dimensioni di altezza.
 * @author matteo
 */
public class TestoACapo implements  Elemento_2DGraphics {
    
    private String testo;
    private Rectangle rettangoloTesto;
    private LinkedList<String> listaRighe;
    
    /**
     * Costruttore.
     * @param rettangolo Dimensioni e posizione del testo da disegnare a capo.
     * la dimensione di altezza viene ignorata.
     * @param testo testo da disegnare.
     */
    public TestoACapo(Rectangle rettangolo, String testo){
        this.testo=testo;
        this.rettangoloTesto = rettangolo;
        this.listaRighe = new LinkedList<String>();
    }

    /**
     * Costruttore.
     * @param posizione posizione dell'angolo in alto a sinistra del testo.
     * @param larghezza larghezza che deve avere ogni riga prima di andare a
     * capo.
     * @param testo testo da visualizzare.
     */
    public TestoACapo(Point posizione, int larghezza, String testo){
        this(new Rectangle(posizione.x, posizione.y, larghezza, 0), testo);
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        this.preparaTesto(graphics2D);
        this.disegnaTesto(graphics2D, colori.getTesto());
    }

    /**
     * Prepara il testo: divide il testo in tante stringhe ognuna delle dimensioni
     * orizzontali minori della larghezza massima.
     * @param graphics2D riferimento al contesto grafico.
     */
    public void preparaTesto(Graphics2D graphics2D) {
        this.listaRighe = new LinkedList<String>();
        boolean finito = false;
        String restante = testo;
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        for (int i=1; !finito; i++){
            String sottoStringa = new String(restante); //porco cane non trovo altro modo
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

    /**
     * Disegna il testo prima preparato.
     * @param graphics2D riferimento al contesto grafico.
     * @param colore colore con cui disegnare il testo.
     */
    public void disegnaTesto(Graphics2D graphics2D, Color colore){
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int i=1;
        graphics2D.setColor(colore);
        for (String riga : listaRighe){
            graphics2D.drawString(riga, (float)rettangoloTesto.getX(), (float)rettangoloTesto.getY()+((fontMetrics.getMaxAscent()+2)*i));
            i++;
        }
    }

    /**
     * Ritorna l'altezza del testo su più righe. Ricordarsi di chiamare questa
     * funzione dopo avere preparato il testo @see preparaTesto(Graphics2D)
     * @param graphics2D riferimento al contesto grafico.
     * @return altezza del testo in pixel.
     */
    public int getAltezzaTesto(Graphics2D graphics2D){
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        return fontMetrics.getHeight()*listaRighe.size();
    }

    /**
     * Imposta un nuovo testo da disegnare su più righe. Ricordarsi di prepararlo.
     * @param testo nuovo testo.
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Imposta nuova posizione e nuove dimensioni del'oggetto. Ricordarsi di
     * riprepararlo.
     * @param rettangolo nuovi bordi.
     */
    public void setRettangolo(Rectangle rettangolo) {
        this.rettangoloTesto = rettangolo;
    }
}
