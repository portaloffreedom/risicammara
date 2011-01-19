/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import risicammaraJava.turnManage.Partita;


/**
 *
 * @author matteo
 */
public class PannelloSpeciale extends JPanel{

    private Partita partita;

    private Dimension dimensioni;
    private BarraSuperiore barra;
    private MenuGiocatore menuGiocatore;
    private OrologioTimer cronometro;
    private MillisecondiDiEsecuzione performance;
    private int durataFrame;
    boolean ridimensionata;

    public PannelloSpeciale(int frameRateMassimo, Partita partita) {
        super();
        this.partita= partita;

        this.addComponentListener(new AscoltatorePannello(this));
        dimensioni = new Dimension();

        this.cronometro = new OrologioTimer();
        this.performance = new MillisecondiDiEsecuzione(this.dimensioni, cronometro);
        this.durataFrame = (int) ((1.0/frameRateMassimo)*1000);

        this.barra = new BarraSuperiore(dimensioni, this, 60);
        this.menuGiocatore = new MenuGiocatore(dimensioni, partita);
        this.barra.addCarteActionListener(null);
        this.barra.addGiocatoreActionListener(menuGiocatore);

        //System.out.println("Dimensioni: "+dimensioni);

    }

    
    @Override
    public void paintComponent (Graphics g) {
        Point posizioneMouse = super.getMousePosition();
        int mouseX = 0;
        int mouseY = 0;


        if (posizioneMouse != null) {
            mouseX=posizioneMouse.x;
            mouseY=posizioneMouse.y;
        }

        this.renderizzaScena(g, mouseX, mouseY);
        this.repaint();
        return;


    }

    private void renderizzaScena (Graphics g, int mouseX, int mouseY){
        long millisecondi = cronometro.tempoPassato();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        this.getSize(dimensioni);
        //System.out.println("Dimensioni: "+dimensioni);

        //g2.draw(new Line2D.Double(mouseX, mouseY, millisecondi%500, 70));

        this.barra.disegna(g2);
        this.performance.disegna(g2);
        this.menuGiocatore.disegna(g2);




        long tempoSleep = this.durataFrame-cronometro.getEsecTime();
        if (tempoSleep > 0) {
            try {
                Thread.sleep(tempoSleep);
            } catch (InterruptedException ex) {
                System.err.println("Errore: "+ex);
            }
        }
    }
}




/**
 * Classe che serve come sempice ascoltatore del Pannello speciale.
 * @author matteo
 */
class AscoltatorePannello implements ComponentListener {
    
    private PannelloSpeciale pannello;

    public AscoltatorePannello(PannelloSpeciale pannello) {
        this.pannello=pannello;
    }

    public void componentResized(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        //System.out.println("componentResized");
        this.pannello.ridimensionata=true;
    }

    public void componentMoved(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentShown(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentHidden(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}