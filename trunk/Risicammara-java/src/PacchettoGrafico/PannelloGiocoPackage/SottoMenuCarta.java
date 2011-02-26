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
 *
 * @author matteo
 */
public class SottoMenuCarta extends Elemento_2DGraphicsCliccable {
    static final int MARGINE = 5;
    
    private int larghezzaCarta;
    private int altezzaCarta;
    private String testo;
    private boolean selezionato;

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
        g2.drawString(testo, posizioneTesto.x, posizioneTesto.y);


        //TOTO bonus
            //mancano le immagini di roberto
    }

    public void setPosizione(Point p) {
        getContorni().setLocation(p);
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setSelezionato(boolean selezionato) {
        this.selezionato = selezionato;
    }
    
    protected Rectangle getContorni(){
        return (Rectangle) posizione;
    }

    public Point getPosizione(){
        return getContorni().getLocation();
    }
    
    public territori_t getCarta(){
        return null;
    }

    @Override
    public String toString() {
        return "SottoMenuCarta{" + "testo=" + testo + '}';
    }

}
