/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import javax.swing.JButton;
import risicammaraClient.Colore_t;

/**
 *
 * @author matteo
 */
public class BottoneGiocatori extends JButton implements QuadratoGiocatori {

    public BottoneGiocatori() {
    }

    public BottoneGiocatori(String text) {
        super(text);
    }

    public void setNome(String testo) {
        this.setText(testo);
    }

    public void setColore(Colore_t colore) {
        this.setForeground(colore.getColor());
    }
}
