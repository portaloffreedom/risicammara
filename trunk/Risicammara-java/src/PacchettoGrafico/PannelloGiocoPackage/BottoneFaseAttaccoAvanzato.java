package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * Classe che gestisce il Bottone Fasi.
 * @author matteo
 */
public class BottoneFaseAttaccoAvanzato extends BottoneFaseAvanzato {
    static private final int MARGINE = 3;
    private RisultatoDadi risultatoDadi;

    private boolean richiestaNumeroArmateAttaccanti;
    private RichiestaArmateAttaccanti armateAttaccanti;

    /**
     * Inizializza tutti i dadi per i Bottoni fase avanzati
     * @param dimPannello Dimensioni del pannello
     * @param ag Attivatore Grafico
     * @param p Punto della posizione dell'angolo in alto a sinistra del
     * pulsante
     * @param larghezza La larghezza
     * @param altezza L'altezza
     */
    public BottoneFaseAttaccoAvanzato(Dimension dimPannello, AnimatoreGraficaPannelli ag, Point p, int larghezza, int altezza) {
        super(dimPannello, ag, p, larghezza, altezza, "HAI VINTO!");
        this.risultatoDadi = new RisultatoDadi(ag, altezza-(MARGINE*2), new Point(), MARGINE);
        this.armateAttaccanti = new RichiestaArmateAttaccanti(MARGINE*2,MARGINE, new Rectangle(getBounds().x, getBounds().y+MARGINE, 0, getBounds().height-MARGINE*2));
        this.richiestaNumeroArmateAttaccanti = false;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        super.disegna(g2, colori);
        if (smosciato())
            this.risultatoDadi.risposiziona(new Point(dimPannello.width, dimPannello.height-getFrecciaDestra().getBounds().height-(MARGINE*2)));
        else
            this.risultatoDadi.risposiziona(new Point(getFrecciaDestra().getPuntaBounds().x, getFrecciaDestra().getBounds().y+MARGINE));

        this.risultatoDadi.disegna(g2, colori);

        //TODO disegna la richiesta numero armate
        if (richiestaNumeroArmateAttaccanti) {
            armateAttaccanti.disegna(g2, colori);
        }

    }

    /**
     * Imposta il risultato dei dadi.
     * @param dadiAttacco Lanci dell'attaccante
     * @param dadiDifesa Lanci del difensore
     */
    public void setRisultatoDadi(int[] dadiAttacco, int[] dadiDifesa) {
        risultatoDadi.setRisultato(dadiAttacco, dadiDifesa);
    }

    /**
     * Disattiva i dadi
     */
    public void disattivaDadi() {
        risultatoDadi.disattiva();
    }

    /**
     * Restituisce il numero di armate che attaccano.
     * @return Il numero di armate che attaccano.
     */
    public int getNumeroArmateAttaccanti() {
        return armateAttaccanti.getNumeroArmateAttaccanti();
    }

    /**
     * Imposta lo stato di richeista per le armate che attaccano
     * @param richiestaNumeroArmateAttaccanti true per impsotare la richiesta, false altrimenti
     */
    public void setRichiestaNumeroArmateAttaccanti(boolean richiestaNumeroArmateAttaccanti) {
        this.richiestaNumeroArmateAttaccanti = richiestaNumeroArmateAttaccanti;
        attivatoreGrafica.panelRepaint(this.getBounds());
    }

    /**
     * Imposta quanti dadi al massimo puoi tirare
     * @param maxLancio dadi massimi.
     */
    public void setMaxLancioDadi(int maxLancio) {
        armateAttaccanti.setMaxLancio(maxLancio);
    }

    /**
     * Imposta l'ascoltatore delle armate D'attacco.
     * @param ascoltatore L'ascoltatore da allacciare.
     */
    public void setActionListenerArmateAttaccanti(RisicammaraEventListener ascoltatore) {
        armateAttaccanti.setActionListener(ascoltatore);
    }

    /**
     * Azione da fare se viene Premuto.
     * @param e L'evento
     */
    @Override
    protected void actionPressed(MouseEvent e) {
        super.actionPressed(e);
        if (richiestaNumeroArmateAttaccanti){
            armateAttaccanti.doClicked(e);
        }
    }
}
