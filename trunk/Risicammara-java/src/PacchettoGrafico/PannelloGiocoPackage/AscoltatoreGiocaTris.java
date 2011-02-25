/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;

/**
 *
 * @author matteo
 */
public class AscoltatoreGiocaTris implements RisicammaraEventListener {

    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        System.out.println("Tasto premuto");
    }

}
