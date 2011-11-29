package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

/**
 * Bottoni rettangolari normali (Arrotondati). Si colorano del colore del giocatore.
 * Hanno due stati: premuto o no. Quando vengono premuti tolgono i bordi arrotondati
 * nei due angoli inferiori.
 * @author matteo
 */
public class BottoneRisicammara extends Elemento_2DGraphicsCliccable {
    /**
     * Altezza del bottone
     */
    static public int ALTEZZA = 50;
    /**
     * Larghezza del bottone
     */
    static public int LARGHEZZA = 100;
    static private int ARROTONDAMENTO = 11;

    private boolean pressed;

    private ImageIcon sfondo;
    private ImageIcon rollover;
    private ImageIcon pressione;
    private String testo;

    /**
     * Costruttore
     * @param posizione posizione dell'angolo in alto a sinistra.
     * @param testo Testo che deve essere disegnato sopra al pulsante
     */
    public BottoneRisicammara(Point posizione, String testo) {
        super();
        this.testo = testo;
        this.pressed = false;

        //TODO togliere queste immagini
        //this.sfondo = new ImageIcon("./risorse/sfondo_pulsante.png", "Descrizione");
        //this.rollover = new ImageIcon("./risorse/mouse_pulsante.png",  "Descrizione");
        //this.pressione = new ImageIcon("./risorse/premuto_pulsante.png",  "Descrizione");

        super.setShape(new Rectangle(posizione, new Dimension(100,50)));

    }

    /**
     * Funzione che disegna il bottone su un contesto grafico (pannello).
     * @param graphics2D contesto grafico su cui disegnare
     * @param ga informazioni aggiuntive sul contesto grafico (colori)
     */
    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced ga ){
        Rectangle rect =((Rectangle)this.posizione);

        //disegna immagine di sfondo del pulsante
        //TODO fare diverse immagini a seconda che il pulsante sia premuto o no
        //graphics2D.drawImage(sfondo.getImage(), rect.x, rect.y, null); //disegnava l'immagine
        graphics2D.setColor(ga .getColoreGiocatore());
        graphics2D.fillRoundRect(rect.x, rect.y, rect.width, rect.height, ARROTONDAMENTO, ARROTONDAMENTO);
        if (pressed){
            graphics2D.fillRect(rect.x, rect.y+(rect.height/2), rect.width, rect.height/2);
        }

        //disegna il testo sopra al pulsante
        graphics2D.setColor(ga .getTesto());
        FontMetrics metrica = graphics2D.getFontMetrics();
        Point puntoTesto = new Point();
        puntoTesto.x = ((rect.width-metrica.stringWidth(testo))/2)+rect.x;
        puntoTesto.y = ((rect.height-metrica.getHeight())/2)+rect.y+metrica.getAscent();
        graphics2D.drawString(testo, puntoTesto.x, puntoTesto.y);
    }


    /**
     * Cambia la posizione del pulsante (al prossimo "disegna" sarà ridisegnato correttamente)
     * @param posizione Nuova posizione dell'angolo in alto a sinistra
     */
    public void setPosizione(Point posizione){
        ((Rectangle)this.posizione).setLocation(posizione);
    }

    /**
     * Cambia lo stato del pulsante: se vero fa disegnare i due spigoli in
     * basso non arrotondati.
     * @param pressed Nuovo stato del pulsante
     */
    public void setPressed(boolean pressed){
        this.pressed = pressed;
    }

    /**
     * Quando viene premuto il bottone, prima di chiamare l'eventuale ascoltatore,
     * inverte il proprio stato di premuto o no.
     * @param e
     */
    @Override
    protected void actionPressed(MouseEvent e) {
        setPressed(!pressed);
        super.actionPressed(e);
    }
}