/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import risicammaraJava.playerManage.Giocatore;

/**
 *
 * @author matteo
 */
public class MenuCarte extends Elemento_2DGraphicsCliccable {
    private Giocatore meStesso;

    public MenuCarte(Dimension forma, Giocatore giocatore) {
        meStesso = giocatore;
    }

    public void disegna(Graphics2D graphics2D, GraphicsAdvanced graphicsAdvanced) {
        
    }

}
