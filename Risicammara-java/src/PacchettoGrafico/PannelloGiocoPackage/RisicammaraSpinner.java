/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * Uno spinner implementato per essere inserito nella plancia di gioco. Supporta
 * solo valori interi e non valori reale (per amore di brevità - non servivano
 * i valori interi).<br>
 * Se non vine premuto lo spinner, ma nessuna delle due frecce, allora lo spinner
 * interpreta la cosa come se fosse stata premuta freccia su.<p>
 * 
 * Eredita da Dado il disegno: infatti la sua forma sarà similare a quella di un
 * dado.
 * @author matteo
 */
public class RisicammaraSpinner extends Dado {
    static private int LARGHEZZA_FRECCE = 10;

    private int valoreMinimo;
    private int valoreMassimo;
    private int valoreAttuale;
    private int salto;
    private Rectangle frecciaSu;
    private Rectangle frecciaGiu;

    private AnimatoreGraficaPannelli ag;


    /**
     * Costruttore del RisicammaraSpinner.<p>
     * 
     * Oltre ai parametri passati al costruttore, i parametri di valore minimo,
     * massimo e attuale sono impostati a 0. Il salto è impostato a 1.
     * @param dimensioni dimensioni che deve avere lo spinner.
     * @param bombaturaDado quanto arrotondamento deve avere il dado.
     * @param inverti true se i colori devono essere invertiti.
     * @param coloreDado colore che deve avere il dado di sfondo (se i colori
     * sono invertiti, questo sarà il colore dei bordi e del testo).
     * @param ag Attivatore Grafico
     */
    public RisicammaraSpinner(Rectangle dimensioni, int bombaturaDado, boolean inverti, Color coloreDado, AnimatoreGraficaPannelli ag) {
        super(dimensioni, bombaturaDado, inverti, "", coloreDado);
        setValori(0, 0, 0, 1);
        this.ag = ag;
        //dimensionaFrecce(); //dovrebbe essere inutile perché già nel costruttore
                                   //quando imposta le dimensioni
    }

    /**
     * Ridimensiona e riposiziona le frecce nella maniera giusta.
     */
    private void dimensionaFrecce(){
        Rectangle dimDado = getBordiNetti();
        Rectangle dimFreccia = new Rectangle(dimDado.x+dimDado.width-LARGHEZZA_FRECCE, dimDado.y, LARGHEZZA_FRECCE, dimDado.height/2);
        frecciaSu  = new Rectangle(dimFreccia);
        dimFreccia.y += (dimDado.height/2);
        frecciaGiu = dimFreccia;
    }

    /**
     * Imposta i limiti e le guide dello spinner.
     * @param valoreMinimo valore minimo che può raggiungere
     * @param valoreMassimo valore massimo che può raggiungere
     * @param valoreIniziale valore iniziale con cui deve partire lo spinner button
     * @param salto
     */
    public final void setValori(int valoreMinimo, int valoreMassimo, int valoreIniziale, int salto){
        setValoreMinimo(valoreMinimo);
        setValoreMassimo(valoreMassimo);
        setValoreAttuale(valoreIniziale);
        setSalto(salto);
    }

    /**
     * Imposta i valori Massimo e iniziale (minimo è settato a 0, salto rimane
     * invariato - di default 1)
     * @param valoreMassimo valore massimo dello spinner
     * @param valoreIniziale valore iniziale dello spinner
     */
    public final void setValori(int valoreMassimo, int valoreIniziale) {
        setValori(0, valoreMassimo, valoreIniziale, salto);
    }

    /**
     * Imposta il salto che ci deve essere ogni volta che si aumenta o diminuisce
     * il valore dello spinner.
     * @param salto L'offset di quanto avanza lo spinner
     */
    public void setSalto(int salto) {
        this.salto = salto;
    }

    /**
     * Cambia il valore attuale dello spinner.<br>
     * Attenzione a come si cambia in funzione del salto che è stato impostato
     * @param valoreAttuale Il valore da impostare allo spinner
     */
    public void setValoreAttuale(int valoreAttuale) {
        this.valoreAttuale = valoreAttuale;
        super.setValoreDado(Integer.toString(valoreAttuale)+" ");
    }

    /**
     * Imposta il valore massimo (incluso) che può essere raggiunto dallo spinner.
     * @param valoreMassimo Il valore massimo che può raggiungere
     */
    public void setValoreMassimo(int valoreMassimo) {
        this.valoreMassimo = valoreMassimo;
    }

    /**
     * Imposta il valore minimo (incluso) che lo spinner può raggiungere.
     * @param valoreMinimo Il valore minimo che lo spinner può raggiungere
     */
    public void setValoreMinimo(int valoreMinimo) {
        this.valoreMinimo = valoreMinimo;
    }

    /**
     * Prende il valore attuale dello spinner.
     * @return l'intero del valore attuale dello spinner.
     */
    public int getValoreAttuale() {
        return valoreAttuale;
    }

    /**
     * Quando viene effettuata una azione PRESSED
     * @param e L'evento mouse generato
     */
    @Override
    protected void actionPressed(MouseEvent e) {
        super.actionPressed(e);
        //vedi se ha premuto freccia su o giu (se nessuno dei due, come se avesse
            //premuto freccia su
        int valoreNuovo;
        if (frecciaGiu.contains(e.getPoint())) {
            valoreNuovo = valoreAttuale-salto;
            if (valoreNuovo < valoreMinimo)
                valoreNuovo = valoreMinimo;
        }
        else {
            valoreNuovo = valoreAttuale+salto;
            if (valoreNuovo > valoreMassimo)
                valoreNuovo = valoreMassimo;
        }

        setValoreAttuale(valoreNuovo);
        ag.panelRepaint(getBordi().getBounds());
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        super.disegna(g2, ga);
        g2.setColor(ga.getColoreScuro());
        //disegna la linea separatrice
        g2.drawLine(frecciaSu.y, frecciaSu.x, frecciaGiu.y+frecciaGiu.height, frecciaGiu.x);
        //disegna le frecce
        disegnaFreccia(g2, frecciaSu, true);
        disegnaFreccia(g2, frecciaGiu, false);
    }

    private void disegnaFreccia(Graphics2D g2, Rectangle bordiFreccia,boolean su) {
        final int NUMERO_PUNTI_FRECCIA = 3;
        int xBase1 = bordiFreccia.x;
        int xBase2 = bordiFreccia.x+bordiFreccia.width;
        int xPunta = bordiFreccia.x+(bordiFreccia.width/2);
        int altezza1 = bordiFreccia.y;
        int altezza2 = bordiFreccia.y+bordiFreccia.height;
        if (su)
            altezza2 = (int) (bordiFreccia.y + ((float) bordiFreccia.height / 2 + 0.5));
        else
            altezza1 = (int) (bordiFreccia.y + ((float) bordiFreccia.height / 2 + 0.5));

        int[] xpoints = {xBase1,xBase2,xPunta};//= new int[NUMERO_PUNTI_FRECCIA];
        int[] ypoints = {altezza2,altezza2,altezza2};//= new int[NUMERO_PUNTI_FRECCIA];
        
        if (su)
            ypoints[2] = altezza1;
        else {
            ypoints[0] = altezza1;
            ypoints[1] = altezza1;
            //ypoints = {altezza1,altezza1,altezza2}; esempio di idiozia del java -.- questo non lo compila
        }

        g2.fill(new Polygon(xpoints, ypoints, NUMERO_PUNTI_FRECCIA));
    }

    /**
     * Imposta i bordi del dado
     * @param bordiDado Il rettangolo che forma il dado
     */
    @Override
    public void setBordiDado(Rectangle bordiDado) {
        super.setBordiDado(bordiDado);
        this.dimensionaFrecce();
    }

}
