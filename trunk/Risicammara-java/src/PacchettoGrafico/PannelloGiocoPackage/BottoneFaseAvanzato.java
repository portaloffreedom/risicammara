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
    private boolean richiestaSpostamento;
    private String testoSupSpostamento;
    private String testoInfSpostamento;

    public BottoneFaseAvanzato(Dimension dimPannello, AttivatoreGrafica ag, Point p, int larghezza, int altezza, String testoSupSpostamento) {
        super(dimPannello, ag, p, larghezza, altezza);
        this.richiestaSpostamento = false;
        this.testoSupSpostamento = testoSupSpostamento;
        this.testoInfSpostamento = "Quante armate vuoi spostare?";
    }

}
