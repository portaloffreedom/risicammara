/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 *
 * @author matteo
 */
public class MatricePannello {

    private LinkedList<Elemento_2DGraphicsCliccable> lista;

    public MatricePannello (){
        lista = new LinkedList<Elemento_2DGraphicsCliccable>();
    }

    public void add(Elemento_2DGraphicsCliccable elemento) {
        lista.add(elemento);
    }

    public void aziona(MouseEvent e){
        for (Elemento_2DGraphicsCliccable elemento : lista) {
            elemento.doClicked(e);
        }
    }
}
