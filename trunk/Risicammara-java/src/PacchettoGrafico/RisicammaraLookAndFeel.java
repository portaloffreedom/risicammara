/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import javax.swing.LookAndFeel;

/**
 *
 * @author matteo
 */
public class RisicammaraLookAndFeel extends LookAndFeel{

    @Override
    public String getName() {
        return "Risicammara Look and Feel";
    }

    @Override
    public String getID() {
        return "checazzovuoi";
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
