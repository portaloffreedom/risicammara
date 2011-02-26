/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author matteo
 */
public class BottoneFaseAvanzato extends BottoneFase {

    public BottoneFaseAvanzato(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza) {
        super(dimPannello, ag, p, larghezza, altezza);
        setTestoDestra("");
    }

}
