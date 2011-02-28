/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.territori_t;

/**
 * SottoMenù carta. Elemento che compone tutte una singola carta o altro nel
 * menù carte.
 * @author matteo
 */
public class SottoMenuCarta extends Elemento_2DGraphicsCliccable {
    static final int MARGINE = 5;
    
    private int larghezzaCarta;
    private int altezzaCarta;
    private String testo;
    private boolean selezionato;

    /**
     * Costruttore.
     * @param testo Testo da visualizzare nel sottomenù.
     * @param altezzaCarta altezza che deve avere il sottomenù.
     * @param larghezzaCarta larghezza che deve avere il sottomenù.
     */
    public SottoMenuCarta(String testo, int altezzaCarta, int larghezzaCarta) {
        super(new Rectangle(larghezzaCarta, altezzaCarta));
        this.larghezzaCarta = larghezzaCarta;
        this.altezzaCarta = altezzaCarta;
        this.testo = testo;

        this.selezionato = false;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ag) {
        Color coloreSfondo = ag.getColoreGiocatore();
        if (selezionato)
            coloreSfondo = ag.getColoreGiocatoreEvidenziato();
        
        g2.setColor(coloreSfondo);
        g2.fill(posizione);
        g2.setColor(ag.getColoreScuro());
        g2.draw(posizione);


        //nome territorio
        int altezzaTesto = g2.getFontMetrics().getHeight();
        Point p = this.getPosizione();
        Point posizioneTesto = new Point(p.x+MARGINE, p.y+altezzaTesto+(altezzaCarta-altezzaTesto)/2);

        g2.setColor(ag.getTesto());
        g2.drawString(testo, posizioneTesto.x, posizioneTesto.y-g2.getFontMetrics().getDescent());


        //TOTO bonus
            //mancano le immagini di roberto
    }

    /**
     * Imposta nuova posizione dell'angolo in alto a sinistra del sottomenù.
     * @param p nuova posizione.
     */
    public void setPosizione(Point p) {
        getContorni().setLocation(p);
    }

    /**
     * Imposta il nuovo testo visualizzato dal sottoMenù.
     * @param testo testo nuovo da visualizzare.
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Imposta se il sottoMenù è selezionato o no. La selezione colora diversamente
     * la carta.
     * @param selezionato vero se selezionato.
     */
    public void setSelezionato(boolean selezionato) {
        this.selezionato = selezionato;
    }
    
    /**
     * Restituisce il riferimento a posizione e dimensioni del sottomenù.
     * @return riferimento ai bordi del sottomenù.
     */
    protected Rectangle getContorni(){
        return (Rectangle) posizione;
    }

    /**
     * Restituisce la posizione dell'angolo in alto a sinistra del sottomenù.
     * @return posizione del sottomenù.
     */
    public Point getPosizione(){
        return getContorni().getLocation();
    }
    
    /**
     * Restituisce la carta che rappresenta il sottomenù. Se non rappresenta
     * nessuna carta ritorna null.
     * @return carta rappresentata dal sottomenù.
     */
    public territori_t getCarta(){
        return null;
    }

    @Override
    public String toString() {
        return "SottoMenuCarta{" + "testo=" + testo + '}';
    }

}
