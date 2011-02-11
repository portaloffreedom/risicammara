/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;

/**
 *
 * @author matteo
 */
public class GraphicsAdvanced {
    private Color sfondoScuro;
    private Color sfondoChiaro;
    private Color testo;
    private Color coloreGiocatore;

    public GraphicsAdvanced(Color sfondoScuro, Color sfondoChiaro, Color testo, Color coloreGiocatore) {
        this.sfondoScuro = sfondoScuro;
        this.sfondoChiaro = sfondoChiaro;
        this.testo = testo;
        this.coloreGiocatore = coloreGiocatore;
    }

    public Color getColoreGiocatore() {
        return coloreGiocatore;
    }

    public Color getSfondoChiaro() {
        return sfondoChiaro;
    }

    public Color getSfondoScuro() {
        return sfondoScuro;
    }

    public Color getTesto() {
        return testo;
    }

    public Color getSfondoTesto() {
        return coloreGiocatore;
    }
}
