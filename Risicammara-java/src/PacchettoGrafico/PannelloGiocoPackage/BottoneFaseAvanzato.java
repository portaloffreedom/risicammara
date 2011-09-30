/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import risicammaraClient.Client;

/**
 * Classe che gestisce il Bottone Fase Avanzato.
 * @author matteo
 */
public class BottoneFaseAvanzato extends BottoneFase {
    private static final int DIMENSIONI_DADO_ALTEZZA = 35;
    private static final int DIMENSIONI_DADO_LARGHEZZA = 30;
    private boolean richiestaSpostamento;
    private RisicammaraSpinner spinner;
    private Dado bottoneOK;
    private String testoSupSpostamento;
    private String testoInfSpostamento;

    /**
     * Inizializza tutti i dati necessari.
     * @param dimPannello Dimensioni del pannello
     * @param ag Attivatore Grafico
     * @param p Punto della posizione dell'angolo in alto a sinistra del
     * pulsante
     * @param larghezza Larghezza
     * @param altezza Altezza
     * @param testoSupSpostamento Testo che viene visualizzato superiore sulle righe.
     */
    public BottoneFaseAvanzato(Dimension dimPannello, AnimatoreGraficaPannelli ag, Point p, int larghezza, int altezza, String testoSupSpostamento) {
        super(dimPannello, ag, p, larghezza, altezza);
        this.spinner = new RisicammaraSpinner(new Rectangle(100, 100, 300, 300), 5, false, Color.green, ag);
        this.spinner.setValori(15, 5);
        this.bottoneOK = new Dado(5);
        this.bottoneOK.setValoreDado("     OK");
        this.setRichiestaVisible(false);
        this.testoSupSpostamento = testoSupSpostamento;
        this.testoInfSpostamento = "Quante armate vuoi spostare?";
    }

    /**
     * Imposta la visibilità dei pulsanti di quante armate spostare.
     * @param visibile true se visibile, false altrimenti
     */
    public final void setRichiestaVisible(boolean visibile){
        this.richiestaSpostamento = visibile;
        super.attivatoreGrafica.panelRepaint(super.getBounds());
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        super.disegna(g2, colori);
        if (richiestaSpostamento && !smosciato()) {
            Rectangle rect = getBounds();
            FontMetrics metrica = g2.getFontMetrics();

            Rectangle testoSup = new Rectangle();
            Rectangle testoInf = new Rectangle();

            testoInf.height = metrica.getHeight();
            testoSup.height = metrica.getHeight();
            testoInf.width = metrica.stringWidth(testoInfSpostamento);
            testoSup.width = metrica.stringWidth(testoSupSpostamento);

            int larghezzaMax = Math.max(testoInf.width, testoSup.width);

            testoInf.x = rect.x + (larghezzaMax - testoInf.width)/2;
            testoSup.x = rect.x + (larghezzaMax - testoSup.width)/2;

            testoSup.y = rect.y + (rect.height - testoInf.height - testoSup.height)/2;
            testoInf.y = testoSup.y + testoSup.height;

            //pulisce lo sfondo
            g2.setColor(colori.getColoreGiocatore());
            Rectangle2D spazioTesto2D = testoInf.createUnion(testoSup);
            g2.fill(spazioTesto2D);
            Rectangle spazioTesto = spazioTesto2D.getBounds();
            spazioTesto.width += 5;

            testoSup.y += testoSup.height - metrica.getDescent();
            testoInf.y += testoInf.height - metrica.getDescent();

            //disegna il testo vero
            g2.setColor(Color.BLACK);
            g2.drawString(testoSupSpostamento, testoSup.x, testoSup.y);
            g2.drawString(testoInfSpostamento, testoInf.x, testoInf.y);

            //disegna il pulsante OK e spin
            Rectangle dimensioniSpinner = new Rectangle(
                    spazioTesto.width+spazioTesto.x,
                    spazioTesto.y +(spazioTesto.height-DIMENSIONI_DADO_ALTEZZA)/2,
                    DIMENSIONI_DADO_LARGHEZZA,
                    DIMENSIONI_DADO_ALTEZZA);
            Rectangle dimensioniPulsanteOK = new Rectangle(dimensioniSpinner);
            dimensioniPulsanteOK.width *= 2;

            bottoneOK.setBordiDado(dimensioniPulsanteOK);
            spinner.setBordiDado(dimensioniSpinner);

            bottoneOK.disegna(g2, colori);
            spinner.disegna(g2, colori);
        }
    }

    /**
     * Imposta l'ascoltatore per il Bottone Fase
     * @param listener L'ascoltatore
     */
    public void setOkActionListener(RisicammaraEventListener listener) {
        bottoneOK.setActionListener(listener);
    }

    /**
     * Restitusice il numero di armate da spostare
     * @return numero di armate da spostare
     */
    public int getNumeroArmateSpostamento() {
        return spinner.getValoreAttuale();
    }

    /**
     * Imposta i valori dello spinner del Bottone fase.
     * @param valoreMassimo Valore Max raggiungibile
     * @param valoreIniziale Valore minimo
     */
    public final void setValori(int valoreMassimo, int valoreIniziale) {
        spinner.setValori(valoreMassimo, valoreIniziale);
    }

    /**
     * Imposta i valori allo spinner
     * @param valoreMinimo Valore minimo
     * @param valoreMassimo Valore massimo raggiungibile
     * @param valoreIniziale Valore iniziale dello spinner
     * @param salto di quanto andare avanti ad ogni click
     */
    public final void setValori(int valoreMinimo, int valoreMassimo, int valoreIniziale, int salto) {
        spinner.setValori(valoreMinimo, valoreMassimo, valoreIniziale, salto);
    }

    @Override
    public boolean isClicked(Point punto) {
        boolean cliccato = super.isClicked(punto);
        System.out.println(this+" Cliccato!"+cliccato);
        System.out.println("Cliccato: "+posizione);
        return cliccato;
    }

    /**
     * Quando viene catturata una azione "pressed"
     * @param e L'evento.
     */
    @Override
    protected void actionPressed(MouseEvent e) {
        super.actionPressed(e);
        if (Client.DEBUG)
            System.out.println("bottone "+this+" premuto");
        if (richiestaSpostamento) {
            if (spinner.doClicked(e)) {
                if (Client.DEBUG)
                    System.out.println("spinner premuto");
                return;
            }
            if (bottoneOK.doClicked(e)) {
                if (Client.DEBUG)
                    System.out.println("bottoneOK premuto");
                return;
            }
        }
    }

}
