/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import risicammaraJava.boardManage.Plancia;


/**
 *
 * @author matteo
 */
public class PannelloGioco extends JPanel{

    private ListaGiocatoriClient listaGiocatori;
    private Plancia plancia;

    private Dimension dimensioniPannello;
    private BarraSuperiore barra;
    private MenuGiocatore menuGiocatore;
    private OrologioTimer cronometro;
    private MillisecondiDiEsecuzione performance;
    private int durataFrame;

    private AttivatoreGrafica attivatoreGrafica;
    private MatricePannello gestionePulsanti;

    public PannelloGioco(int frameRateMassimo, Plancia plancia, ListaGiocatoriClient listaGiocatori) {
        super();
        this.listaGiocatori = listaGiocatori;
        this.plancia = plancia;
        this.attivatoreGrafica = new AttivatoreGrafica(this);
        this.gestionePulsanti = new MatricePannello();

        dimensioniPannello = new Dimension();

        this.cronometro = new OrologioTimer();
        this.performance = new MillisecondiDiEsecuzione(this.dimensioniPannello, cronometro);
        this.durataFrame = (int) ((1.0/frameRateMassimo)*1000);

        this.barra = new BarraSuperiore(dimensioniPannello, 60, this);
        this.menuGiocatore = new MenuGiocatore(dimensioniPannello, this.listaGiocatori, attivatoreGrafica);
        //this.barra.addCarteActionListener(null);
        //this.barra.addGiocatoreActionListener(menuGiocatore);
        this.addMouseListener(new MouseListenerImpl(this));

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

        if (this.attivatoreGrafica.ridisegna())
            this.repaint();

        return;


    }

    private void renderizzaScena (Graphics g, int mouseX, int mouseY){
        long millisecondi = cronometro.tempoPassato();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        this.getSize(dimensioniPannello);
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

    // <editor-fold defaultstate="collapsed" desc="mouseListener">
    private static class MouseListenerImpl implements MouseListener {

        private PannelloGioco pannello;

        public MouseListenerImpl(PannelloGioco pannello) {
            this.pannello = pannello;
        }

        public void mouseClicked(MouseEvent e) {
            pannello.mouseCliccato(e);
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }// </editor-fold>


    public void mouseCliccato(MouseEvent e) {
        this.gestionePulsanti.aziona(e);
    }

    public void addPulsante(Elemento_2DGraphicsCliccable elemento) {
        gestionePulsanti.add(elemento);
    }
}