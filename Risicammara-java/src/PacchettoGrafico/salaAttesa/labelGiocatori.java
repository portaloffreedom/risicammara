/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import javax.swing.JLabel;
import risicammaraClient.Colore_t;

/**
 *
 * @author matteo
 */
public class labelGiocatori extends JLabel implements QuadratoGiocatori {

    public labelGiocatori() {
        this.setHorizontalAlignment(CENTER);
    }

    public labelGiocatori(String text) {
        super(text);
        this.setHorizontalAlignment(CENTER);
    }

    public void setNome(String testo) {
        this.setText(testo);
    }

    public void setColore(Colore_t colore) {
        this.setForeground(colore.getColor());
    }
}
