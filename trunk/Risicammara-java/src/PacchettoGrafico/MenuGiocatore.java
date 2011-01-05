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
import risicammarajava.playerManage.ListaPlayers;

/**
 *
 * @author matteo
 */
public class MenuGiocatore extends Elemento_2DGraphics implements ActionListener {

    private int numeroTerritori;
    private int numeroArmate;
    private String obbiettivo;
    private String nomegiocatore;

    private int larghezza;
    private int altezza;
    private int bordo;
    private int distanzaSuperiore;
    private boolean visibile;

    public MenuGiocatore(Dimension dimensioni) {
        super(dimensioni);
        this.visibile=false;

        this.larghezza = 120;
        this.altezza = 70;
        this.bordo = 5;
        this.distanzaSuperiore = 55;
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        if (visibile) {

            //parte provvisoria
            graphics2D.setColor(Color.red);
            graphics2D.fill3DRect(this.bordo, this.distanzaSuperiore, this.larghezza, this.altezza, true);
            graphics2D.setColor(Color.black);
            graphics2D.drawString("Tutti gli uomini del presidente", this.bordo+5, this.distanzaSuperiore+15);
        }
    }

    public void actionPerformed(ActionEvent e) {
        this.visibile = !this.visibile;
    }
}