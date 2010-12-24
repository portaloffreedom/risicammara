/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author matteo
 */
public class MenuGiocatore extends Elemento_2DGraphics implements ActionListener {

    private int numeroTerritori;
    private int numeroArmate;
    private String obbiettivo;

    private boolean visibile;

    public MenuGiocatore(Dimension dimensioni) {
        super(dimensioni);
        this.visibile=false;

        this.numeroTerritori = 13;
        this.numeroArmate = 42;

    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        if (visibile) {
            graphics2D.setColor(Color.red);
            graphics2D.fill3DRect(5, 55, 100, 70, true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.visibile = !this.visibile;
    }

}
