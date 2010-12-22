/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 *
 * @author matteo
 */
public class PannelloSpeciale extends JPanel {

    Dimension dimensioni;
    Elemento_2DGraphics barra;
    OrologioTimer cronometro;
    MillisecondiDiEsecuzione performance;


    public PannelloSpeciale() {
        super();
        dimensioni = new Dimension();
        this.cronometro = new OrologioTimer();
        barra = new BarraSuperiore(dimensioni, 40);
        this.performance = new MillisecondiDiEsecuzione(this.dimensioni, cronometro);
        System.out.println("Dimensioni: "+dimensioni);

    }

    
    @Override
    public void paintComponent (Graphics g) {
        long millisecondi = cronometro.tempoPassato();
        Point posizioneMouse = super.getMousePosition();
        int mouseX = 0;
        int mouseY = 0;

        if (posizioneMouse != null) {
            mouseX=posizioneMouse.x;
            mouseY=posizioneMouse.y;
        }
        else { try {
                Thread.sleep(90);
                this.repaint();
                return;
            } catch (InterruptedException e) { System.out.println(e); }
        }

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        this.getSize(dimensioni);
        //System.out.println("Dimensioni: "+dimensioni);

        g2.draw(new Line2D.Double(mouseX, mouseY, millisecondi%500, 70));


        



        /*//
         * Funzioni di prova per vedere come disegnare
         * 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //g2.setFont(new Font("Arial", 1, 10));

        g2.drawString("Hello", 10, 10);
        g2.drawString("Alla Facciaccia di Narni", 10, 30);
        
        g2.draw(new Line2D.Double(5, 5, 50, 120));

        g2.setBackground(Color.yellow);
        g2.setPaint(new GradientPaint(0f,0f,Color.blue,0f,30f,Color.blue));
        g2.fillRect(5, 5, 50, 50);

        g2.setColor(Color.yellow);
        g2.fill3DRect(dimensioni.width-200, dimensioni.height-100, 200, 100, true);

        g2.setColor(Color.red);
        g2.drawLine(0, 0, dimensioni.width, dimensioni.height);
         */

        barra.disegna(g2);
        performance.disegna(g2);

        this.repaint();
    }

}
