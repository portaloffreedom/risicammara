package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Classe astratta che serve per accumunare tutti i pulsanti della interfaccia
 * di risicammara. Sfrutta gli ActionListener di AWT.<p>
 * Per essere cliccabile bisogna aggiungerlo alla MatricePannello.
 * @author matteo
 * @see ActionListener
 * @see MatricePannello
 */
public abstract class Elemento_2DGraphicsCliccable implements Elemento_2DGraphics {
    /** Posizione e dimensioni del pulsante */
    protected Shape posizione;
    private RisicammaraEventListener ascoltatore;

    /**
     * Costruttore che inizializza le dimensioni iniziali con un Rectangle(0,0,0,0)
     */
    public Elemento_2DGraphicsCliccable(){
        this.posizione = new Rectangle();
    }

    /**
     * Costruttore che inizializza la forma interne con uno "Shape"
     * @param forma Forma e posizione che deve avere il nuovo pulsante
     */
    public Elemento_2DGraphicsCliccable(Shape forma) {
        this.posizione = forma;
    }

    /**
     * Cambia la forma e la posizione interne del pulsante
     * @param posizione Nuova forma e posizione del pulsante interno
     */
    public void setShape(Shape posizione) {
        this.posizione = posizione;
    }

    /**
     * Verifica se il punto è dentro le forme del pulsante. Presumibilmente il
     * punto è derivante dalla posizione in cui è stato premuto il mouse.
     * @param punto Punto da verificare
     * @return booleano che identifica se c'è stata "collisione"
     */
    public boolean isClicked (Point punto){
        return (posizione.contains(punto));
    }

    /**
     * Verifica se il pulsante è premibile nel punto del MouseEvent e nel caso
     * lo sia attiva la funzione dell'ascoltatore ActionListener che va associato
     * al pulsante.
     * @param e MouseEvent che viene verificato sul pulsante
     * @return true se il pulsante è stato effettivamente cliccato
     */
    public boolean doClicked (MouseEvent e){
        boolean cliccato = isClicked(e.getPoint());
        if (cliccato)
            this.actionPressed(e);
        return cliccato;
    }

    /**
     * Imposta l'ascoltatore al pulsante. NOTA BENE è possibile impostare solo
     * un ascoltatore alla volta. Per cancellare l'ascoltatore impostarne uno null
     * @param ascoltatore riferimento alla classe utilizzata come ascoltatore
     */
    public void setActionListener(RisicammaraEventListener ascoltatore){
        this.ascoltatore = ascoltatore;
    }

    protected void actionPressed(MouseEvent e){
        if (this.ascoltatore != null) {
            EventoAzioneRisicammara risicEv = new EventoAzioneRisicammara(this, e.getID(), "cliccato",e.getPoint());
            this.ascoltatore.actionPerformed(risicEv);
        }
    }

    //TODO implementare la funzione di rollover per i pulsanti
    //abstract public void actionRollOver(MouseEvent e);
    
}
