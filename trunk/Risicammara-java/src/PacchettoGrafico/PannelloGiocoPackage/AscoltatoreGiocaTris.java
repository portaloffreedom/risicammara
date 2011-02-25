/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import risicammaraClient.Connessione;
import risicammaraJava.turnManage.PartitaClient;

/**
 *
 * @author matteo
 */
public class AscoltatoreGiocaTris implements RisicammaraEventListener {
    private MenuCarte menuCarte;
    private PartitaClient partita;
    private Connessione server;

    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        SottoMenuCarta carta = (SottoMenuCarta) e.getSource();
        System.out.println("Tasto premuto: "+carta);
    }

}
