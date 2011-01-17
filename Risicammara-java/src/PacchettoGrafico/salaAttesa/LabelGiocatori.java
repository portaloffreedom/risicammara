/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import javax.swing.JLabel;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
import risicammaraClient.Colore_t;

/**
 *
 * @author matteo
 */
public class LabelGiocatori extends JLabel implements QuadratoGiocatori {

    public LabelGiocatori() {
        this.setHorizontalAlignment(CENTER);
        this.setBorder(new TextFieldBorder());
    }

    public LabelGiocatori(String text) {
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
