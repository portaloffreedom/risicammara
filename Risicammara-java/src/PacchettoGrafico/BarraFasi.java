/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * Barra costituita dai necessari bottoni delle fasi
 * @see BottoneFase
 * @author matteo
 */
public class BarraFasi extends Elemento_2DGraphicsCliccable {
    private Dimension dimPannello;
    private int inizio;
    private int fine;
    private BottoneFase rinforzi;
    private BottoneFase attacco;
    private BottoneFase spostamento;

    final static int LARGHEZZABORDO = 50;

    /**
     * Costruttore
     * @param dimPannello Dimensioni del pannello variabili al ridimensionamento.
     * @param ag AttivatoreGrafica che serve ai pulsanti per fare le animazioni.
     * @param inizio Distanza dal bordo sinistro.
     * @param fine Distanza dal bordo destro.
     * @param altezza Altezza della barra.
     * @param bordoSup Distanza da bordo superiore.
     */
    public BarraFasi(Dimension dimPannello, AttivatoreGrafica ag, int inizio, int fine, int altezza, int bordoSup) {
        this.dimPannello = dimPannello;
        this.inizio = inizio;
        this.fine = fine;
        Rectangle ret = this.ridimensiona(inizio, fine, altezza, bordoSup);

        //TODO posizionare per bene i bottone fasi(uno sopra all'altro, poi vengono rimpiccioliti
        int larghezzaB = LarghezzaBottoni(ret.width);
        this.rinforzi    = new BottoneFase(dimPannello, ag, new Point(inizio,     bordoSup),larghezzaB, altezza);
        this.attacco     = new BottoneFase(dimPannello, ag, new Point(inizio+50,  bordoSup),larghezzaB, altezza);
        this.spostamento = new BottoneFase(dimPannello, ag, new Point(inizio+100, bordoSup),larghezzaB, altezza);

        this.rinforzi.setActionListener(rinforzi);
        this.attacco.setActionListener(attacco);
        this.spostamento.setActionListener(spostamento);
    }

    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        this.ridimensiona(); //questa riga rende dinamiche le dimensioni

        //disegna lo sfondo magenta (e quindi il pulsante finale)
        graphics2D.setColor(Color.red);
        graphics2D.fill(posizione);

        //disegna i tre bottonifase nella giusta sequenza
        this.spostamento.disegna(graphics2D, colori);
        graphics2D.setColor(colori.getColoreGiocatore());
        graphics2D.fill(attacco.getMinBounds());

        this.attacco.disegna(graphics2D, colori);
        graphics2D.setColor(colori.getColoreGiocatore());
        graphics2D.fill(rinforzi.getMinBounds());
        
        this.rinforzi.disegna(graphics2D, colori);

        //disegna il contorno
        graphics2D.setColor(colori.getColoreScuro());
        graphics2D.draw(posizione);
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        if (rinforzi.doClicked(e))
            return;
        if (attacco.doClicked(e))
            return;
        if (spostamento.doClicked(e))
            return;
        else
            this.fineturno();
    }

    private void fineturno(){
        //TODO codice per fare "fine turno"
    }

    /**
     * Cambia la posizione del rettangolo e si occupa di cambiare la posizione
     * anche in riferimento al pannello principale che ascolta i MouseEvent
     * @param inizio distanza dal bordo sinistro
     * @param fine distanza dal bordo destro
     * @param altezza altezza della barra
     * @param bordoSup distanza da bordo superiore
     * @return ritorna il rettangolo che rappresenta la dimensione della barra
     */
    final public Rectangle ridimensiona(int inizio, int fine, int altezza, int bordoSup){
        int larghezza = larghezza(inizio, fine);
        this.inizio = inizio;
        this.fine = fine;

        this.posizione = new Rectangle(inizio, bordoSup, larghezza, altezza);
        return (Rectangle) this.posizione;
    }

    /**
     * Cambia la posizione del rettangolo e si occupa di cambiare la posizione
     * anche in riferimento al pannello principale che ascolta i MouseEvent
     * @param inizio distanza dal bordo sinistro
     * @param fine distanza dal bordo destro
     */
    final public void ridimensiona(int inizio, int fine){
        Rectangle ret = (Rectangle) this.posizione;
        this.ridimensiona(inizio, fine, ret.height, ret.y);
    }

    /**
     * Cambia la posizione del rettangolo e si occupa di cambiare la posizione
     * anche in riferimento al pannello principale che ascolta i MouseEvent
     */
    private void ridimensiona(){
        this.ridimensiona(inizio, fine);
        int larghezzaB = LarghezzaBottoni(larghezza(inizio, fine));
        //this.rinforzi.cambiaLarghezza(larghezzaB); //tolti perché gestiti internamente dalle frecce
        //this.attacco.cambiaLarghezza(larghezzaB);
        //this.spostamento.cambiaLarghezza(larghezzaB);
    }

    private int larghezza(int inizio, int fine){
        return dimPannello.width-(inizio+fine);
    }

    static int LarghezzaBottoni(int larghezza){
        return larghezza-LARGHEZZABORDO*3;
        //TODO è questa la funzione che non fa ridimensionare i pulsanti "dinamicamente": toglierla e sostituirla con un'altra idea:
        //idea 1 lasciare il ridimensionamento a carico di ciascun pulsanto, la barra indica solo quando e di quanto
        //idea 2 memorizzare le 3 dimensioni dei tre bottoni all'interno della Barra
    }

}
