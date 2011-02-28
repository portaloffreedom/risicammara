/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 *
 * @author matteo
 */
public class RichiestaArmateAttaccanti extends Elemento_2DGraphicsCliccable {
    private int numeroArmateAttaccanti;
    private Dado dado1;
    private Dado dado2;
    private Dado dado3;
    private Dado dadoAnnulla;
    private String TestoSinistra;
    private String TestoDadoAnnulla;
    private int margine;
    private int maxLancio;

    public RichiestaArmateAttaccanti(int bordaturaDadi, int margine, Rectangle bordi) {
        this.margine = margine;
        this.posizione = bordi;
        this.TestoDadoAnnulla = "Annulla";
        this.TestoSinistra = "Lancia i Dadi";
        this.numeroArmateAttaccanti = 3;
        this.maxLancio = 3;

        this.dado1 = new Dado(bordaturaDadi);
        this.dado2 = new Dado(bordaturaDadi);
        this.dado3 = new Dado(bordaturaDadi);
        this.dadoAnnulla = new Dado(bordaturaDadi);

        this.dado1.setValoreDado("1");
        this.dado2.setValoreDado("2");
        this.dado3.setValoreDado("3");
        this.dadoAnnulla.setValoreDado(TestoDadoAnnulla);

        this.dado1.setActionListener(new AscoltatoreDadi(this, 1));
        this.dado2.setActionListener(new AscoltatoreDadi(this, 2));
        this.dado3.setActionListener(new AscoltatoreDadi(this, 3));
        this.dadoAnnulla.setActionListener(new AscoltatoreDadi(this, 0));

    }

    private Rectangle getBordi() {
        return (Rectangle) posizione;
    }

    public void setMaxLancio(int maxLancio) {
        if (maxLancio > 3)
            maxLancio = 3;
        if (maxLancio < 1)
            maxLancio = 1;
        this.maxLancio = maxLancio;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        Rectangle bordiTotali = getBordi();

        //posiziona testo
        FontMetrics metrica = g2.getFontMetrics();
        Rectangle testo = new Rectangle();
        testo.width = metrica.stringWidth(TestoSinistra);
        testo.height = metrica.getHeight();
        testo.x = bordiTotali.x+margine;
        testo.y = bordiTotali.y + (bordiTotali.height-testo.height) + metrica.getAscent();
        testo.y -= metrica.getDescent();

        //riposiziona dadi
        Rectangle posizioneDado = new Rectangle(bordiTotali.height, bordiTotali.height);
        posizioneDado.y = bordiTotali.y;

        posizioneDado.x = testo.x+testo.width+margine;
        this.dado1.setBordiDado(new Rectangle(posizioneDado));

        if (maxLancio >=2) {
            posizioneDado.x += posizioneDado.width + margine;
            this.dado2.setBordiDado(new Rectangle(posizioneDado));

            if (maxLancio >=3) {
                posizioneDado.x += posizioneDado.width + margine;
                this.dado3.setBordiDado(new Rectangle(posizioneDado));
            }
        }

        posizioneDado.x += posizioneDado.width + margine;
        posizioneDado.width = metrica.stringWidth(TestoDadoAnnulla)+(2*margine);
        this.dadoAnnulla.setBordiDado(posizioneDado);

        //disegna --------------------------------------------------------------
        bordiTotali.width = posizioneDado.width + posizioneDado.x - bordiTotali.x;
        //cancella dietro
        g2.setColor(ga.getColoreGiocatore());
        g2.fill(bordiTotali);
        //disegna testo
        g2.setColor(ga.getTesto());
        g2.drawString(TestoSinistra, testo.x, testo.y);

        //disegna i 4 dadi
        dado1.disegna(g2, ga);
        if (maxLancio >=2) {
            dado2.disegna(g2, ga);
            if (maxLancio >=3)
                dado3.disegna(g2, ga);
        }
        dadoAnnulla.disegna(g2, ga);
    }

    public int getNumeroArmateAttaccanti() {
        return numeroArmateAttaccanti;
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        if (dado1.doClicked(e))
            return;
        if (dado2.doClicked(e))
            return;
        if (dado3.doClicked(e))
            return;
        if (dadoAnnulla.doClicked(e))
            return;
    }

    private void azionaAscoltatore(MouseEvent e){
        super.actionPressed(e);
    }

    private class AscoltatoreDadi extends Component implements RisicammaraEventListener {
        private RichiestaArmateAttaccanti armateAttaccanti;
        private int numeroArmate;

        public AscoltatoreDadi(RichiestaArmateAttaccanti armateAttaccanti, int numeroArmate) {
            this.armateAttaccanti = armateAttaccanti;
            this.numeroArmate = numeroArmate;
        }

        @Override
        public void actionPerformed(EventoAzioneRisicammara e) {
            this.armateAttaccanti.numeroArmateAttaccanti = this.numeroArmate;
            this.armateAttaccanti.azionaAscoltatore(new MouseEvent(this, 0, 0, 0, 0, 0, 0, false));
        }

    }

}
