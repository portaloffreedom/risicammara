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
 * Menù che visualizza le informazioni del giocatore (l'obbiettivo). Si può
 * rendere visibile o invisibile. Premere su questo menù farà ridisegnare
 * l'intero pannello.
 * @author matteo
 */
public class MenuGiocatore extends MenuRisicammara {
    private RiquadroTesto obbiettivo;

    //TODO usare questo riferimento per stampare più informazioni sul giocatore attuale
    private PartitaClient partita;

    /**
     * Costruttore.
     * @param partita riferimento all'oggetto partita.
     * @param attivatoreGrafica riferimento all'attivatore grafica.
     */
    public MenuGiocatore(PartitaClient partita, AnimatoreGraficaPannelli attivatoreGrafica) {
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

    /**
     * Prima di azionare l'ascoltatore mette in coda un ridisegno totale
     * del pannello.
     * @param e evento
     */
    @Override
    protected void actionPressed(MouseEvent e) {
        if (Client.DEBUG)
            System.out.println("Menù giocatore premuto");
        this.attivatoreGrafica.panelRepaint();
        super.actionPressed(e);
    }

}

