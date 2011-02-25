/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import risicammaraClient.Client;
import risicammaraJava.turnManage.PartitaClient;

/**
 *
 * @author matteo
 */
public class MenuGiocatore extends MenuRisicammara {
    private RiquadroTesto obbiettivo;
    
    private PartitaClient partita;

    public MenuGiocatore(PartitaClient partita, AttivatoreGrafica attivatoreGrafica) {
        super(attivatoreGrafica);
        this.partita = partita;
        this.obbiettivo = new RiquadroTesto(new Rectangle(5, 55, 200, 1000), //TODO togliere l'hardcode,
                                            partita.getMeStesso().getObbiettivo().toString(),
                                            partita.getMeStesso().getArmyColour());
        
        this.setRectangle(obbiettivo.getRettangoloRiquadro());
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        if (aperto) {
            graphics2D.setColor(colori.getColoreScuro());
            this.obbiettivo.disegna(graphics2D,colori);
            setRectangle(obbiettivo.getRettangoloRiquadro());
        }
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        if (Client.DEBUG)
            System.out.println("Menù giocatore premuto");
        this.attivatoreGrafica.panelRepaint();
        super.actionPressed(e);
    }

}

