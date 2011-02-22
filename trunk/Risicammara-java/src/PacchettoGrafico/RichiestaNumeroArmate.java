/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author matteo
 */
public class RichiestaNumeroArmate extends JFrame{
    private JSpinner spinner;
    private JButton bottoneOK;

    public RichiestaNumeroArmate(String motivazione, int max) throws HeadlessException {
        super("Spostamento Armate");
        JPanel pannello = new JPanel();
        super.getContentPane().add(pannello);
        
        pannello.add(new JLabel(motivazione));
        
        pannello.add(new JLabel("Armate:"));
        
        this.spinner = new JSpinner(new SpinnerNumberModel(0, 0, max, 1));
        pannello.add(spinner);
        
        bottoneOK = new JButton("OK");
        pannello.add(bottoneOK);
        
        super.pack();
        super.setVisible(true);
    }
    
    public void addOKActionListener(ActionListener ascoltatore){
        bottoneOK.addActionListener(ascoltatore);
    }
    
    public int getNumArmate(){
        return ((Integer) spinner.getValue()).intValue();
    }
}
