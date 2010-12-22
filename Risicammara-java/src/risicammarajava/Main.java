/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import PacchettoGrafico.*;

/**
 *
 * @author stengun
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame finestra = new JFrame("Culo");
        finestra.setMinimumSize(new Dimension(800, 400));
        Container contestoFinestra = finestra.getContentPane();
        PannelloSpeciale pannello = new PannelloSpeciale(0);

        finestra.setBounds(500, 400, 200, 180);
        contestoFinestra.add(pannello);


        finestra.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                System.out.println("Ciao Mondo di merda!!");
            }

            public void windowClosing(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                System.out.println("Uscita in corso...");
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowIconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowDeiconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowActivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowDeactivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        finestra.setVisible(true);
    }

}
