/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Elemento che gestisce la distribuzione degli eventi (del mouse) sul pannello;
 * @author matteo
 */
public class MatricePannello {

    private ArrayList<Elemento_2DGraphicsCliccable> lista;

    /**
     * Costruttore
     */
    public MatricePannello (){
        lista = new ArrayList<Elemento_2DGraphicsCliccable>();
    }

    /**
     * Aggiunge un elemento che la matrice deve controllate quando chiamato il
     * metodo "aziona".
     * @param elemento Elemento da aggiungere alla lista da controllare
     * @see aziona(MouseEvent)
     */
    public void add(Elemento_2DGraphicsCliccable elemento) {
        lista.add(elemento);
    }

    /**
     * Controlla e attiva tutti gli elementi della coda. Parte della gestione
     * dell'evento è lasciata a Elemento_2DGraphicsCliccable. Non c'è penetrazione
     * degli eventi: appena si trova un oggetto premuto, termina la scansione degli
     * elementi.
     * @param e MouseEvent che deve generare l'azionamento.
     * @see Elemento_2DGraphicsCliccable
     */
    public void aziona(MouseEvent e){
        for (Elemento_2DGraphicsCliccable elemento : lista) {
            if (elemento.doClicked(e))
                return;
        }
    }
}
