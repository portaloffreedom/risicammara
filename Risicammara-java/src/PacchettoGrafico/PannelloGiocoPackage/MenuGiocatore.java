/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import risicammaraClient.Client;
import risicammaraJava.turnManage.PartitaClient;

/**
 *
 * @author matteo
 */
public class MenuGiocatore extends Elemento_2DGraphicsCliccable implements RisicammaraEventListener {
    private Dimension dimePanel;
    private boolean visibile;
    private boolean cambiato;
    private RiquadroTesto obbiettivo;
    private AttivatoreGrafica attivatoreGrafica;

    private PartitaClient partita;


    public MenuGiocatore(Dimension dimPanel, PartitaClient partita, AttivatoreGrafica attivatoreGrafica) {
        this.dimePanel=dimPanel;
        this.visibile=false;
        this.attivatoreGrafica = attivatoreGrafica;
        this.partita = partita;
        Rectangle rettangoloTesto = new Rectangle(5, 55, 200, 1000); //TODO togliere l'hardcode
        this.obbiettivo = new RiquadroTesto(rettangoloTesto,
                                            partita.getMeStesso().getObbiettivo().toString(),
                                            partita.getMeStesso().getArmyColour());
        super.posizione = obbiettivo.getRettangoloRiquadro();
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        if (visibile) {
            graphics2D.setColor(colori.getColoreScuro());
            this.obbiettivo.disegna(graphics2D,colori);
            super.posizione = obbiettivo.getRettangoloRiquadro();
        }
    }

    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        this.visibile = !this.visibile;
        this.attivatoreGrafica.panelRepaint(obbiettivo.getRettangoloRiquadro());
        //attivatoreGrafica.attiva();
        //cambiato = true;
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        if (Client.DEBUG)
            System.out.println("Menù giocatore premuto");
        this.attivatoreGrafica.panelRepaint();
        super.actionPressed(e);
    }

    @Override
    public boolean isClicked(Point punto) {
        //se non è visibile non è cliccabile
        return (visibile && super.isClicked(punto));
    }

}

