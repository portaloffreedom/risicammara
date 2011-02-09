/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author matteo
 */
public class MatricePannello {

    private LinkedList<Elemento_2DGraphics> lista;

    public MatricePannello (){
        lista = new LinkedList<Elemento_2DGraphics>();
    }

    public void add(Elemento_2DGraphics elemento) {
        lista.add(elemento);
    }

    public void aziona(Point punto){
        for (Elemento_2DGraphics elemento : lista) {
            
        }
    }
}
