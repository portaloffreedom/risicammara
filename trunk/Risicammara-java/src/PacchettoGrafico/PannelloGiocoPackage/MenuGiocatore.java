/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import PacchettoGrafico.GraphicsAdvanced;
import risicammaraJava.playerManage.ListaGiocatoriClient;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author matteo
 */
public class MenuGiocatore implements RisicammaraEventListener, Elemento_2DGraphics {
    private Dimension dimensioni;
    private boolean visibile;
    private boolean cambiato;
    private RiquadroTesto Obbiettivo;
    private AttivatoreGrafica attivatoreGrafica;

    private ListaGiocatoriClient listaGiocatori;


    public MenuGiocatore(Dimension dimensioni, ListaGiocatoriClient listaGiocatori, AttivatoreGrafica attivatoreGrafica) {
        this.dimensioni=dimensioni;
        this.visibile=false;
        this.attivatoreGrafica = attivatoreGrafica;
        this.listaGiocatori = listaGiocatori;
        Rectangle rettangoloTesto = new Rectangle(5, 55, 200, 0);
        this.Obbiettivo = new RiquadroTesto(rettangoloTesto,
                                            listaGiocatori.meStesso().getObbiettivo().toString(),
                                            listaGiocatori.meStesso().getArmyColour());
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        if (visibile) {
            graphics2D.setColor(colori.getColoreScuro());
            this.Obbiettivo.disegna(graphics2D,colori);
        }
    }

    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        this.visibile = !this.visibile;
        this.attivatoreGrafica.panelRepaint();
        //attivatoreGrafica.attiva();
        //cambiato = true;
    }

}

