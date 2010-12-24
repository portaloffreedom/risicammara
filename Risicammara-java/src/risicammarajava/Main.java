/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

import PacchettoGrafico.PannelloSpeciale;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;


/**
 *
 * @author stengun
 */
public class Main implements WindowListener {
    
    private PannelloSpeciale pannello;

    public Main(PannelloSpeciale pannello) {
        super();
        this.pannello=pannello;
    }


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


        finestra.addWindowListener(new Main(pannello));
        finestra.setVisible(true);
    }

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
        System.out.println("windowClosed");
    }

    public void windowIconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("windowIconified");
    }

    public void windowDeiconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("windowDeiconified");
    }

    public void windowActivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("windowActivated");
        //this.pannello.repaint();
    }

    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("windowDeactivated");
    }

}
