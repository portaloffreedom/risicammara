/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import javax.swing.LookAndFeel;

/**
 * Prova per creare un look and feel personalizzato (fallita miseramente). Non
 * Utilizzare.
 * @author matteo
 */
@Deprecated
public class RisicammaraLookAndFeel extends LookAndFeel{

    @Override
    public String getName() {
        return "Risicammara Look and Feel";
    }

    @Override
    public String getID() {
        return "checosavuoi?";
    }

    @Override
    public String getDescription() {
        return "Sono un look and feel sperimentale";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return false;
    }

}
