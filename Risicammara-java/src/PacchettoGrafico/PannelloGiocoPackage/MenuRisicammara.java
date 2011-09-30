package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Classe astratta che accomuna tutti i menù del pannello risicammara.
 * @author matteo
 */
abstract public class MenuRisicammara extends Elemento_2DGraphicsCliccable implements RisicammaraEventListener {
    private final int BORDO_OFFSET = 2;
    
    /**
     * booleano che identifica se il menù è aperto (mostrato) o no.
     */
    protected boolean aperto;
    /**
     * riferimento ad attivatore grafica per ridisegnare quando il menù è mostrato
     * o no.
     */
    protected AnimatoreGraficaPannelli attivatoreGrafica;

    /**
     * Costruttore semplice. Ricordarsi poi di impostargli delle dimensioni.
     * @param attivatoreGrafica riferimento all'attivatore grafica.
     */
    public MenuRisicammara(AnimatoreGraficaPannelli attivatoreGrafica) {
        this(new Rectangle(), attivatoreGrafica);
    }

    /**
     * Costruttore completo.
     * @param forma dimensioni e posizione che deve avere il menù.
     * @param attivatoreGrafica riferimento all'attivatore grafica.
     */
    public MenuRisicammara(Rectangle forma, AnimatoreGraficaPannelli attivatoreGrafica) {
        super(forma);
        this.attivatoreGrafica = attivatoreGrafica;
        this.aperto = false;
    }

    /**
     * Cambia lo stato di aperto o non del menù e lo ridisegna sul pannello.
     * @param aperto
     */
    public void setAperto(boolean aperto) {
        this.aperto = aperto;
        ridisegna();
    }

    /**
     * Restituisce la posizione dell'angolo in alto a sinistra del menù.
     * @return posizione del menù.
     */
    public Point getPosition(){
        return getRectangle().getLocation();
    }

    /**
     * Restituisce il riferimento alla posizione e dimensioni del menù.
     * @return riferimento ai bordi del menù.
     */
    public Rectangle getRectangle(){
        return (Rectangle) posizione;
    }

    /**
     * Imposta la posizione dell'angolo in alto a destra del  menù.
     * @param p nuova posizione.
     */
    public void setLocation(Point p){
        getRectangle().setLocation(p);
    }

    /**
     * Imposta nuova posizione e dimensioni al menù.
     * @param rect nuovi bordi del menù.
     */
    public void setRectangle(Rectangle rect){
        super.setShape(rect);
    }

    /**
     * Chiamerà panel.repaint nelle dimensioni giuste
     */
    public void ridisegna(){
        attivatoreGrafica.panelRepaint(getRectangleLarge());
    }

    /**
     * Genera un Rettangolo nuovo con dimensioni maggiorate per fare un repaint
     * leggermente abbondante
     * @return
     */
    public Rectangle getRectangleLarge() {

        //rettangolo copia, per fare un repaint più ampio
        Rectangle rettangolo = new Rectangle(getRectangle());

        //allarga i contorni di un pixel e poi ingloba anche parte del pulsante
        rettangolo.y /= 2;
        rettangolo.height+= rettangolo.y + BORDO_OFFSET;
        rettangolo.x -= BORDO_OFFSET;
        rettangolo.width += BORDO_OFFSET*2;

        return rettangolo;
    }

    /**
     * Azione che deve essere fatta per visualizzare il menù (tipicamente
     * collegato al pulsante che attiva il menù).
     * @param e evento.
     */
    @Override  //l'azione che deve fare quando si preme il pulsante menu
    public void actionPerformed(EventoAzioneRisicammara e) {
        //attiva questo menu :D
        setAperto(!aperto);
        this.attivatoreGrafica.panelRepaint(getRectangleLarge());
    }

    @Override
    public boolean isClicked(Point punto) {
        //se non è visibile non è cliccabile
        return (aperto && super.isClicked(punto));
    }

}
