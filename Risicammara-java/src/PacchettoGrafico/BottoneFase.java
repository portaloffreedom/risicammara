package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Bottone che viene disegnato a forma di freccia. Le sue dimensioni possono
 * essere smosciate oppure relative alla dimensione del pannello. Possiede una
 * animazione quando viene smosciato e il contrario. Smosciato significa che
 * le sue dimensioni sono ridotte e dipendenti solo dalla posizione a sinistra
 * (non dalla larghezza del pannello).
 * @author matteo
 */
public class BottoneFase extends Elemento_2DGraphicsCliccable implements RisicammaraEventListener {
    static final private double TempoAnimazioneMillSec = 2000;
    static final int OFFSET = 8;
    static final int OFFSET_TESTO = 20;
    static final int DIMENSIONI_PUNTA = 27;
    private Dimension dimPannello;
    private AttivatoreGrafica attivatoreGrafica;
    private boolean smosciato;
    private boolean animazione;
    private long inizioAnim;
    private double faseAnimPrec;
    private String testoDestra;
    private String testoSinistra;
    private String testoSmosciato;
    private boolean disegnaTestoSmosciato;

    /**
     * Costruttore.
     * @param dimPannello Riferimento alle dimensioni del pannello principale.
     * @param ag Riferimento all'attivatore grafica.
     * @param p Punto della posizione dell'angolo in alto a sinistra del
     * pulsante
     * @param larghezza Larghezza che deve avere il pulsante (ormai inutile).
     * @param altezza Altezza che deve avere il pulsante.
     */
    public BottoneFase(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza) {
        super();
        FrecciaDestra freccia = new FrecciaDestra(p, altezza, larghezza, DIMENSIONI_PUNTA);
        super.posizione = freccia;
        this.dimPannello = dimPannello;
        this.attivatoreGrafica = ag;
        this.smosciato = false;
        this.animazione = false;
        this.faseAnimPrec = 1;
        this.testoDestra = "";
        this.testoSinistra = "";
        this.testoSmosciato = "";
        this.disegnaTestoSmosciato = false;
    }

    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        
        //fase di ridimensionamento (dipendente da smoscia)
        this.ridimensiona();
        
        //fase di disegno
        g2.setColor(colori.getColoreGiocatore());
        g2.fill(posizione);

        disegnaTesto(g2, colori);
        
        g2.setColor(colori.getColoreScuro());
        g2.draw(posizione);

        if (animazione)
            attivatoreGrafica.panelRepaint(this.getExtraPuntaBounds());
    }

    private void disegnaTesto(Graphics2D g2, GraphicsAdvanced colori) {
        if (animazione || smosciato || disegnaTestoSmosciato){
            disegnaTestoSmosciato(g2, colori);
        }
        else {
            disegnaTestoDestra(g2, colori);
            disegnaTestoSinistra(g2, colori);
        }
    }

    private void disegnaTestoSmosciato(Graphics2D g2, GraphicsAdvanced colori){
        this.disegnaTestoDestra(g2, colori, testoSmosciato);
    }

    private void disegnaTestoDestra(Graphics2D g2, GraphicsAdvanced colori){
        this.disegnaTestoDestra(g2, colori, testoDestra);
    }

    private void disegnaTestoDestra(Graphics2D g2, GraphicsAdvanced colori, String testo){
        Rectangle rect = this.getBounds();
        FontMetrics metrics = g2.getFontMetrics();
        Dimension dimensioniTesto = new Dimension();
        Point posizioneTesto = new Point();

        dimensioniTesto.width = metrics.stringWidth(testo);
        dimensioniTesto.height = metrics.getHeight();
        posizioneTesto.x = rect.width+rect.x-dimensioniTesto.width-(OFFSET_TESTO);
        //posizioneTesto.y = rect.y + (rect.height-dimensioniTesto.height)/2;
        posizioneTesto.y = rect.y + (rect.height-dimensioniTesto.height)/2 + dimensioniTesto.height;

        g2.setColor(colori.getTesto());
        g2.drawString(testo, posizioneTesto.x, posizioneTesto.y);
    }

    private void disegnaTestoSinistra(Graphics2D g2, GraphicsAdvanced colori){
        Rectangle rect = this.getBounds();
        FontMetrics metrics = g2.getFontMetrics();
        Dimension dimensioniTesto = new Dimension();
        Point posizioneTesto = new Point();

        dimensioniTesto.width = metrics.stringWidth(testoSinistra);
        dimensioniTesto.height = metrics.getHeight();
        posizioneTesto.x = rect.x + (OFFSET_TESTO);
        posizioneTesto.y = rect.y + (rect.height-dimensioniTesto.height)/2 + dimensioniTesto.height;

        g2.setColor(colori.getTesto());
        g2.drawString(testoSinistra, posizioneTesto.x, posizioneTesto.y);
    }

    @Deprecated
    public void actionPerformed(EventoAzioneRisicammara ae) {
        this.setSmosciato(!this.smosciato);
    }

    private void cambiaLarghezza(int larghezza){
        ((FrecciaDestra)posizione).cambiaLarghezza(larghezza);
    }

    /**
     * Imposta se il pulsante si deve smosciare o no.
     * @param smosciato Booleano per lo stato "smosciato".
     */
    public void setSmosciato(boolean smosciato){
        this.smosciato = smosciato;
        this.attivatoreGrafica.panelRepaint(this.getBounds());
        this.inizioAnim = System.currentTimeMillis();
        this.animazione = true;
        this.disegnaTestoSmosciato = smosciato;
    }

    /**
     * Imposta il testo che deve essere visualizzato quando il pulsante è
     * smosciato. Impostare un testo corto.
     * @param testoSmosciato Stringa da visualizzare
     */
    public void setTestoSmosciato(String testoSmosciato){
        this.testoSmosciato = testoSmosciato;
    }

    /**
     * Imposta il testo da visualizzare sulla destra quando il pulsante non è
     * smosciato.
     * @param testoDestra Stringa da visualizzare
     */
    public void setTestoDestra(String testoDestra) {
        this.testoDestra = testoDestra;
    }

    /**
     * Imposta il testo da visualizzare sulla sinistra quando il pulsante non
     * è smosciato.
     * @param testoSinistra Stringa da visualizzare
     */
    public void setTestoSinistra(String testoSinistra) {
        this.testoSinistra = testoSinistra;
    }

    private void ridimensiona(){
        if (animazione){
            long tempo = System.currentTimeMillis();
            double animCompletamento = (tempo-inizioAnim)/TempoAnimazioneMillSec; //variabile che va da 0 a 1
            animCompletamento = (Math.sin((animCompletamento+Math.PI/2)*Math.PI)+1)/2;
            int larghezzaPOP = BarraFasi.LarghezzaBottoniMassima(dimPannello.width-250);
            if (smosciato){
                animCompletamento = 1-animCompletamento;
                if (animCompletamento > this.faseAnimPrec){
                    this.animazione = false;
                    this.faseAnimPrec = 0;
                    this.cambiaLarghezza(50);
                    this.attivatoreGrafica.panelRepaint(posizione.getBounds());
                    return;
                }
            }
            else {
                if (animCompletamento < this.faseAnimPrec){
                    this.animazione = false;
                    this.faseAnimPrec = 1;
                    this.cambiaLarghezza(BarraFasi.LarghezzaBottoniMassima(dimPannello.width-250));
                    this.attivatoreGrafica.panelRepaint(posizione.getBounds());
                    return;
                }
            }
            larghezzaPOP = (int) (((larghezzaPOP - 50) * (animCompletamento)) + 50);
            this.cambiaLarghezza(larghezzaPOP);
            this.faseAnimPrec = animCompletamento;
            return;
        }
        if (smosciato){
            this.cambiaLarghezza(50);
        }
        else {
            int larghezza = BarraFasi.LarghezzaBottoniMassima(dimPannello.width-250);
            this.cambiaLarghezza(larghezza);
        }
    }

    /**
     * Serve per ricevere le minime dimensioni che può prendere il pulsante.
     * @return Rectangle che rappresenta la minima dimensione (con posizione)
     * del pulsante.
     */
    public Rectangle getMinBounds() {
        return ((FrecciaDestra)posizione).getBounds(BarraFasi.LARGHEZZABORDO);
    }

    /**
     * Prende le dimensioni e la posizione della freccia.
     * @return rettangolo che racchiude la freccia.
     */
    public Rectangle getBounds(){
        return ((FrecciaDestra)posizione).getBounds();
    }

    /**
     * Imposta il testo da disegnare come smosciato anche se non è smosciato.
     * Serve per quando il pulsante non è smosciato ma coperto.
     * @param disegnaTestoSmosciato vero se disegnare il testo smosciato.
     */
    public void setDisegnaTestoSmosciato(boolean disegnaTestoSmosciato) {
        this.disegnaTestoSmosciato = disegnaTestoSmosciato;
    }

    /**
     * @return Ritorna un rettangolo che garantisce essere grande quanto la
     * freccia smosciata, ma la posizione comprende dentro la punta della freccia
     * sempre.
     */
    private Rectangle getExtraPuntaBounds() {
        Rectangle rect = (((FrecciaDestra)posizione).getPuntaBounds());
        rect.x-= BarraFasi.LARGHEZZABORDO;
        rect.width += BarraFasi.LARGHEZZABORDO+5;
        return rect;
    }

}
