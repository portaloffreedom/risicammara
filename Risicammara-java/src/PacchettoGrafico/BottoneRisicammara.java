/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author matteo
 */
public class BottoneRisicammara extends JButton implements Elemento_2DGraphics {

    private Dimension dimensioni_muro;

    public BottoneRisicammara (String testo, /*Rectangle dimensioni,*/ Dimension dimensioni_muro) {
        super();
        ImageIcon sfondo = new ImageIcon("./risorse/sfondo_pulsante.png", "Descrizione");
        ImageIcon rollover = new ImageIcon("./risorse/mouse_pulsante.png",  "Descrizione");
        ImageIcon pressione = new ImageIcon("./risorse/premuto_pulsante.png",  "Descrizione");

        this.setIcon(sfondo);
        this.setRolloverIcon(rollover);
        this.setRolloverSelectedIcon(rollover);
        this.setSelectedIcon(pressione);

        this.setRolloverEnabled(true);

        this.dimensioni_muro=dimensioni_muro;
        //this.setBounds(dimensioni);

    }

    public void disegna(Graphics2D graphics2D) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
