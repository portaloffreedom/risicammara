/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import PacchettoGrafico.OrologioTimer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import risicammaraClient.Client;
import risicammaraClient.Colore_t;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.turnManage.PartitaClient;


/**
 * Pannello principale del Client.
 * @author matteo
 */
final public class PannelloGioco extends JPanel{
    static public int ALTEZZAPANNELLO = 60;

    private PartitaClient partita;

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
        this.partita = partita;
        this.attivatoreGrafica = new AttivatoreGrafica(this);
        this.gestionePulsanti = new MatricePannello();

        dimensioniPannello = new Dimension();

        this.cronometro = new OrologioTimer();
        this.performance = new MillisecondiDiEsecuzione(this.dimensioniPannello, cronometro);
        this.durataFrame = (int) ((1.0/frameRateMassimo)*1000);

        this.barra = new BarraSuperiore(dimensioniPannello, ALTEZZAPANNELLO, this, partita, attivatoreGrafica);
        partita.setMenuCarte(barra.getMenuCarte());
        
        //prova carte
        if (Client.DEBUG) {
            partita.aggiungiCartaMeStesso(territori_t.Cina);
            partita.aggiungiCartaMeStesso(territori_t.Jolly1);
            partita.aggiungiCartaMeStesso(territori_t.Afghanistan);
            partita.aggiungiCartaMeStesso(territori_t.Kamchatka);
            partita.aggiungiCartaMeStesso(territori_t.Medio_Oriente);
        }

        this.addMouseListener(new MouseListenerImpl(this));
        this.planciaImmagine = new PlanciaImmagine(new Point(0, ALTEZZAPANNELLO), partita, dimensioniPannello, attivatoreGrafica);
        AscoltatorePlanciaEvidenziatore ascoltatorePlancia = new AscoltatorePlanciaEvidenziatore(planciaImmagine, attivatoreGrafica);
        planciaImmagine.setActionListener(ascoltatorePlancia);

        this.impostaColori(partita.getMeStesso());

        Dimension dimensioniMinime = this.getDimensioniMinime();
        this.setMinimumSize(dimensioniMinime);
        this.setSize(dimensioniMinime);

        //mette a posto i pulsanti (l'ordine con cui vengono inseriti importa (invertito))
        this.addCliccabile(barra.getGiocatoreButton());
        this.addCliccabile(barra.getCarteButton());
        this.addCliccabile(barra.getBarraFasi());
        this.addCliccabile(barra.getMenuCarte());
        this.addCliccabile(barra.getMenuGiocatore());
        this.addCliccabile(planciaImmagine);
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
        Graphics2D g2 = (Graphics2D) g;
        switch (Client.QUALITA) {
            default:
            case Client.QUALITA_BASSA:
                impostaQualitàDisegno(g2,
                        RenderingHints.VALUE_ANTIALIAS_OFF,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
                        RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED,
                        RenderingHints.VALUE_COLOR_RENDER_SPEED,
                        RenderingHints.VALUE_DITHER_DISABLE,
                        RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
                        RenderingHints.VALUE_RENDER_SPEED,
                        RenderingHints.VALUE_STROKE_NORMALIZE);
                return;
            case Client.QUALITA_MEDIA:
                impostaQualitàDisegno(g2,
                        RenderingHints.VALUE_ANTIALIAS_DEFAULT,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP,
                        RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT,
                        RenderingHints.VALUE_COLOR_RENDER_DEFAULT,
                        RenderingHints.VALUE_DITHER_DEFAULT,
                        RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                        RenderingHints.VALUE_RENDER_DEFAULT,
                        RenderingHints.VALUE_STROKE_DEFAULT);
                return;
            case Client.QUALITA_ALTA:
                impostaQualitàDisegno(g2,
                        RenderingHints.VALUE_ANTIALIAS_ON,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB,
                        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
                        RenderingHints.VALUE_COLOR_RENDER_SPEED,
                        RenderingHints.VALUE_DITHER_ENABLE,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                        RenderingHints.VALUE_RENDER_QUALITY,
                        RenderingHints.VALUE_STROKE_PURE);

        }
    }

    private void impostaQualitàDisegno(Graphics2D g,
                                       Object antialias,
                                       Object text_antialias,
                                       Object alpha_interpolation,
                                       Object color_rendering,
                                       Object dithering,
                                       Object fractionalmetrics,
                                       Object interpolation,
                                       Object rendering,
                                       Object stroke_control){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, text_antialias);
        //g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, alpha_interpolation);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, color_rendering);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, dithering);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalmetrics);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, rendering);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, stroke_control);

    }
    
    public void mouseCliccato(MouseEvent e) {
        this.gestionePulsanti.aziona(e);
    }

    private void addCliccabile(Elemento_2DGraphicsCliccable elemento) {
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

    public MenuCarte getMenuCarte() {
        return barra.getMenuCarte();
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