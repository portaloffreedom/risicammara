package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.Colore_t;

/**
 * Riquadro che disegna un testo con uno sfondo. Se il testo è troppo lungo
 * disegna il testo andando a capo (vengono moficate le dimensioni di altezza
 * di questo oggetto).
 * @author matteo
 */
public class RiquadroTesto extends TestoACapo {

    private Rectangle rettangoloRiquadro;

    /**
     * Costruttore
     * @param rettangolo Dimensioni che deve avere questo Riquadro testo (l'altezza
     * viene ignorata).
     * @param testo testo da disegnare.
     * @param coloreGiocatore colore di sfondo del testo.
     */
    public RiquadroTesto(Rectangle rettangolo, String testo, Colore_t coloreGiocatore) {
        super(RimpicciolisciPerTesto(rettangolo), testo);
        this.rettangoloRiquadro = rettangolo;
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        //prepara per il disegno
        super.preparaTesto(graphics2D);
        int altezza = super.getAltezzaTesto(graphics2D);
        this.rettangoloRiquadro.height = altezza+2;

        //disengna effettivamente
        graphics2D.setColor(colori.getSfondoTesto());
        graphics2D.fill(rettangoloRiquadro);
        super.disegnaTesto(graphics2D, colori.getTesto());
    }

    /**
     * Imposta le nuove dimensioni e posizione del rettangolo. La dimensione
     * di altezza viene ignorata.
     * @param rettangolo nuove dimensione e posizione.
     */
    @Override
    public void setRettangolo(Rectangle rettangolo){
        super.setRettangolo(RimpicciolisciPerTesto(rettangolo));
        this.rettangoloRiquadro = rettangolo;
    }

    /**
     * Imposta la nuova posizione dell'oggetto.
     * @param posizione nuova posizione dell'oggetto.
     */
    public void setPosizione(Point posizione){
        this.setRettangolo(new Rectangle(posizione, rettangoloRiquadro.getSize()));
    }

    /**
     * Restituisce un riferimento alle dimensione e alla posizione attuale del
     * riquadro testo.
     * @return riferimento ai bordi della figura.
     */
    public Rectangle getRettangoloRiquadro() {
        return rettangoloRiquadro;
    }

    static private Rectangle RimpicciolisciPerTesto(Rectangle rettangolo){
        return new Rectangle(rettangolo.x+4, rettangolo.y, rettangolo.width-6, 0);
    }
}
