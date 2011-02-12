/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author matteo
 */
public class BottoneFase extends Elemento_2DGraphicsCliccable implements ActionListener {
    static final private double TempoAnimazioneMillSec = 2000;
    static final int OFFSET = 7;
    private Dimension dimPannello;
    private AttivatoreGrafica attivatoreGrafica;
    private boolean smosciato;
    private boolean cambiato;
    private boolean animazione;
    private long inizioAnim;

    public BottoneFase(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza) {
        super();
        FrecciaDestra freccia = new FrecciaDestra(p, altezza, larghezza, 27);
        super.posizione = freccia;
        this.dimPannello = dimPannello;
        this.attivatoreGrafica = ag;
        this.smosciato = false;
        this.animazione = false;
    }

    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        
        //fase di ridimensionamento (dipendente da smoscia)
        this.ridimensiona();
        
        //fase di disegno
        g2.setColor(colori.getColoreGiocatore());
        g2.fill(posizione);
        
        g2.setColor(colori.getSfondoScuro());
        g2.draw(posizione);

        if (animazione)
            attivatoreGrafica.panelRepaint(((FrecciaDestra)posizione).getPuntaBounds());
    }

    public void actionPerformed(ActionEvent ae) {
        this.setSmoscia(!this.smosciato);
    }

    public void cambiaLarghezza(int larghezza){
        ((FrecciaDestra)posizione).cambiaLarghezza(larghezza);
    }

    public void setSmoscia(boolean smosciato){
        this.smosciato = smosciato;
        this.attivatoreGrafica.panelRepaint();
        this.inizioAnim = System.currentTimeMillis();
        this.animazione = true;
    }

    private void ridimensiona(){
        if (animazione){
            if (smosciato){
                long tempo = System.currentTimeMillis();
                double animComletamento = (tempo-inizioAnim)/TempoAnimazioneMillSec; //variabile che va da 0 a 1
                if (animComletamento >= 1){
                    //attivatoreGrafica.panelRepaint();
                    this.animazione = false;
                    this.cambiaLarghezza(50);
                    return;
                }
                int larghezzaPOP = BarraFasi.LarghezzaBottoni(dimPannello.width-250);
                larghezzaPOP = (int) (((larghezzaPOP - 50) * (1-animComletamento)) + 50);
                this.cambiaLarghezza(larghezzaPOP);
            }
            else {
                long tempo = System.currentTimeMillis();
                double animComletamento = (tempo-inizioAnim)/TempoAnimazioneMillSec; //variabile che va da 0 a 1
                if (animComletamento >= 1){
                    //attivatoreGrafica.panelRepaint();
                    this.animazione = false;
                    this.cambiaLarghezza(BarraFasi.LarghezzaBottoni(dimPannello.width-250));
                    this.attivatoreGrafica.panelRepaint(posizione.getBounds());
                    return;
                }
                int larghezzaPOP = BarraFasi.LarghezzaBottoni(dimPannello.width-250);
                larghezzaPOP = (int) (((larghezzaPOP - 50) * (animComletamento)) + 50);
                this.cambiaLarghezza(larghezzaPOP);
            }
            return;
        }
        if (smosciato){
            this.cambiaLarghezza(50);
        }
        else {
            int larghezza = BarraFasi.LarghezzaBottoni(dimPannello.width-250);
            this.cambiaLarghezza(larghezza);
        }
    }

    public Rectangle getMinBounds() {
        return ((FrecciaDestra)posizione).getBounds(BarraFasi.LARGHEZZABORDO);
    }

}
