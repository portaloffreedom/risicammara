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

    private Dimension dimensioni;
    private Elemento_2DGraphics barra;
    private OrologioTimer cronometro;
    private MillisecondiDiEsecuzione performance;
    private int durataFrame;


    public PannelloSpeciale(int durataFrame) {
        super();

        dimensioni = new Dimension();
        this.cronometro = new OrologioTimer();
        barra = new BarraSuperiore(dimensioni, this, 60);
        this.performance = new MillisecondiDiEsecuzione(this.dimensioni, cronometro);
        this.durataFrame = durataFrame;
        //System.out.println("Dimensioni: "+dimensioni);

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
            } catch (InterruptedException e) { System.out.println("Errrore: "+e); }
        }

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        this.getSize(dimensioni);
        //System.out.println("Dimensioni: "+dimensioni);

        g2.draw(new Line2D.Double(mouseX, mouseY, millisecondi%500, 70));

        this.barra.disegna(g2);
        this.performance.disegna(g2);
        



        long tempoSleep = this.durataFrame-cronometro.getEsecTime();
        if (tempoSleep > 0) {
            try {
                Thread.sleep(tempoSleep);
            } catch (InterruptedException ex) {
                System.err.println("Errore: "+ex);
            }
        }

        this.repaint();
    }

}
