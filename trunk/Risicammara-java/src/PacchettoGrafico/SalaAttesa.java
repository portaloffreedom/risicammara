/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author matteo
 */
public class SalaAttesa extends JFrame implements WindowListener {
    
    private boolean leader;
    private Socket server;

    public SalaAttesa(Socket server, boolean leader) {
        super("Sala d'Attesa");
        this.server=server;
        this.leader=leader;

        this.addWindowListener(this);
        this.setBounds(100, 100, 500, 300);
        this.setResizable(false);

        JPanel pannello = new JPanel();
        this.getContentPane().add(pannello);pannello.setBorder(new EmptyBorder(5, 5, 5, 5));
        //pannello.setBorder(new EmptyBorder(5, 5, 5, 5));

        disegnaInterfaccia(pannello);




        this.setVisible(true);
    }

    public SalaAttesa(Socket server) {
        this(server, false);
    }

    private void disegnaInterfaccia(JPanel pannello){

    }

    // <editor-fold defaultstate="collapsed" desc="Window Listener">
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }// </editor-fold>
}
