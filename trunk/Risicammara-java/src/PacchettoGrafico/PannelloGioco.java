/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import risicammaraClient.Colore_t;
import risicammaraJava.boardManage.Plancia;
import risicammaraJava.playerManage.Giocatore;


/**
 *
 * @author matteo
 */
public class PannelloGioco extends JPanel{

    private ListaGiocatoriClient listaGiocatori;
    private Plancia plancia;

    private Dimension dimensioniPannello;
    private BarraSuperiore barra;
    private OrologioTimer cronometro;
    private MillisecondiDiEsecuzione performance;
    private int durataFrame;

    private AttivatoreGrafica attivatoreGrafica;
    private MatricePannello gestionePulsanti;
    private GraphicsAdvanced colori;

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

        this.barra = new BarraSuperiore(dimensioniPannello, 60, this, listaGiocatori, attivatoreGrafica);
        this.addMouseListener(new MouseListenerImpl(this));

        this.impostaColori(listaGiocatori.meStesso());

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

        this.impostaContestoDisegno(g);

        this.renderizzaScena(g, mouseX, mouseY);

        if (this.attivatoreGrafica.continuaRidisegna())
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

        this.barra.disegna(g2, colori);
        this.performance.disegna(g2, colori);




        long tempoSleep = this.durataFrame-cronometro.getEsecTime();
        if (tempoSleep > 0) {
            try {
                Thread.sleep(tempoSleep);
            } catch (InterruptedException ex) {
                System.err.println("Errore: "+ex);
            }
        }
    }

    private void impostaContestoDisegno(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //TODO impostazione per permettere le altre versioni dell'antialiasing
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
    }
    
    public void mouseCliccato(MouseEvent e) {
        this.gestionePulsanti.aziona(e);
    }

    public void addPulsante(Elemento_2DGraphicsCliccable elemento) {
        gestionePulsanti.add(elemento);
    }

    private void impostaColori(Giocatore meStesso){
        Color sfondoScuro = new Color(51, 51, 51);
        Color sfondoChiaro = Color.white;
        Color testo = Color.black;
        Colore_t armateGiocatore = meStesso.getArmyColour();
        Color coloreGiocatore = meStesso.getArmyColour().getColor();

        if (armateGiocatore == Colore_t.BLU || armateGiocatore == Colore_t.NERO)
            testo = Color.white;

        this.colori = new GraphicsAdvanced(sfondoScuro, sfondoChiaro, testo, coloreGiocatore);
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

}