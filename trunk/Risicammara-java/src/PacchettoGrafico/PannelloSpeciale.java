/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Line2D;
import javax.swing.JPanel;
import risicammarajava.turnManage.ListaPlayers;


/**
 *
 * @author matteo
 */
public class PannelloSpeciale extends JPanel{

    private Dimension dimensioni;
    private Elemento_2DGraphics barra;
    private Elemento_2DGraphics menuGiocatore;
    private OrologioTimer cronometro;
    private MillisecondiDiEsecuzione performance;
    private int durataFrame;
    boolean ridimensionata;

    public PannelloSpeciale(int durataFrame,ListaPlayers listagiocatori,int turno) {
        super();

        this.addComponentListener(new AscoltatorePannello(this));
        dimensioni = new Dimension();

        this.cronometro = new OrologioTimer();
        this.performance = new MillisecondiDiEsecuzione(this.dimensioni, cronometro);
        this.durataFrame = durataFrame;

        BarraSuperiore barraSuperriore = new BarraSuperiore(dimensioni, this, 60);
        MenuGiocatore menuGiocator = new MenuGiocatore(dimensioni,listagiocatori,turno);
        this.menuGiocatore = menuGiocator;
        this.barra = barraSuperriore;
        barraSuperriore.addCarteActionListener(null);
        barraSuperriore.addGiocatoreActionListener(menuGiocator);
        barraSuperriore.addProssimoMouseListener(menuGiocator);

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
            this.renderizzaScena(g, mouseX, mouseY);
            this.repaint();
            return;
        }

        if (this.ridimensionata == true) {
            this.renderizzaScena(g, mouseX, mouseY);
            this.ridimensionata=false;
            this.repaint();
            return;
        }

        
        try {
            Thread.sleep(90);
            this.repaint();
        }
        catch (InterruptedException e) { System.out.println("Errrore: "+e); }



    }

    private void renderizzaScena (Graphics g, int mouseX, int mouseY){
        long millisecondi = cronometro.tempoPassato();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        this.getSize(dimensioni);
        //System.out.println("Dimensioni: "+dimensioni);

        g2.draw(new Line2D.Double(mouseX, mouseY, millisecondi%500, 70));

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