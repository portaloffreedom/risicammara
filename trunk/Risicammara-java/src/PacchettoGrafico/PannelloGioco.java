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
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.turnManage.PartitaClient;


/**
 * Pannello principale del Client.
 * @author matteo
 */
final public class PannelloGioco extends JPanel{
    static public int ALTEZZAPANNELLO = 60;

    private ListaGiocatoriClient listaGiocatori;
    private PlanciaClient plancia;

    private Dimension dimensioniPannello;
    private BarraSuperiore barra;
    private PlanciaImmagine planciaImmagine;
    private OrologioTimer cronometro;
    private MillisecondiDiEsecuzione performance;
    private int durataFrame;

    private AttivatoreGrafica attivatoreGrafica;
    private MatricePannello gestionePulsanti;
    private GraphicsAdvanced colori;

    public PannelloGioco(int frameRateMassimo, PartitaClient partita) {
        super();
        this.listaGiocatori = partita.getListaGiocatori();
        this.plancia = partita.getPlancia();
        this.attivatoreGrafica = new AttivatoreGrafica(this);
        this.gestionePulsanti = new MatricePannello();

        dimensioniPannello = new Dimension();

        this.cronometro = new OrologioTimer();
        this.performance = new MillisecondiDiEsecuzione(this.dimensioniPannello, cronometro);
        this.durataFrame = (int) ((1.0/frameRateMassimo)*1000);

        this.barra = new BarraSuperiore(dimensioniPannello, ALTEZZAPANNELLO, this, listaGiocatori, attivatoreGrafica);
        this.addMouseListener(new MouseListenerImpl(this));
        this.planciaImmagine = new PlanciaImmagine(new Point(0, ALTEZZAPANNELLO), partita, dimensioniPannello, attivatoreGrafica);
        this.addPulsante(planciaImmagine);
        AscoltatorePlanciaEvidenziatore ascoltatorePlancia = new AscoltatorePlanciaEvidenziatore(planciaImmagine, attivatoreGrafica);
        planciaImmagine.setActionListener(ascoltatorePlancia);

        this.impostaColori(listaGiocatori.meStesso());

        Dimension dimensioniMinime = this.getDimensioniMinime();
        this.setMinimumSize(dimensioniMinime);
        this.setSize(dimensioniMinime);
    }

    
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
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

        this.planciaImmagine.disegna(g2, colori);
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
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
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
        Colore_t armateGiocatore = meStesso.getArmyColour();
        Color coloreGiocatore =armateGiocatore.getColor();
        Color testo = GraphicsAdvanced.getTextColor(armateGiocatore);

        this.colori = new GraphicsAdvanced(sfondoScuro, sfondoChiaro, testo, coloreGiocatore);
    }

    public Dimension getDimensioniMinime (){
        Dimension dim = this.planciaImmagine.getDimension();
        dim.height += ALTEZZAPANNELLO;
        return dim;
    }

    public BarraFasi getBarraFasi(){
        return barra.getBarraFasi();
    }

    public AttivatoreGrafica getAttivatoreGrafica() {
        return attivatoreGrafica;
    }

    public PlanciaImmagine getPlanciaImmagine() {
        return planciaImmagine;
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