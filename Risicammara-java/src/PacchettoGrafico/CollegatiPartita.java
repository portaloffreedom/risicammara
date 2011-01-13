/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import risicammaraClient.Main;

/**
 *
 * @author matteo
 */
public class CollegatiPartita extends JFrame implements WindowListener {

    private TextField stringaIndirizzo;
    private JButton collegati;
    private JCheckBox locale;
    private Main main;

    private Socket server;

    public Socket getServer() {
        return server;
    }

    public CollegatiPartita(Main main) {
        super("Collegati al server");
        this.addWindowListener(this);
        this.setBounds(100, 100, 300, 100);
        this.setResizable(false);
        this.main=main;

        JPanel pannello = new JPanel(new BorderLayout(5, 5));
        this.getContentPane().add(pannello);
        pannello.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.stringaIndirizzo = new TextField();
        pannello.add(stringaIndirizzo, BorderLayout.NORTH);

        this.locale = new JCheckBox("Locale");
        pannello.add(locale, BorderLayout.WEST);
        this.locale.addActionListener(new ActionLocalHost(stringaIndirizzo));

        this.collegati = new JButton("Collegati");
        pannello.add(collegati, BorderLayout.EAST);
        this.collegati.addActionListener(new ActionCollegati(this));

        this.setVisible(true);
    }


    // <editor-fold defaultstate="collapsed" desc="WindowListener">
    public void windowOpened(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
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
    }// </editor-fold>

    private static class ActionLocalHost implements ActionListener {

        private TextField stringaIndirizzo;

        public ActionLocalHost(TextField stringaIndirizzo) {
            this.stringaIndirizzo=stringaIndirizzo;
        }

        public void actionPerformed(ActionEvent e) {
            JCheckBox culo = (JCheckBox) e.getSource();
            this.stringaIndirizzo.setEnabled(!culo.isSelected());
        }
    }

    private static class ActionCollegati implements ActionListener {

        private CollegatiPartita memoria;

        public ActionCollegati(CollegatiPartita memoria) {
            this.memoria=memoria;
        }

        public void actionPerformed(ActionEvent e) {

            InetAddress ip = null;
            try {
                if (memoria.locale.isSelected()) {
                    ip = InetAddress.getLocalHost();
                }
                else {
                    ip = InetAddress.getByName(memoria.stringaIndirizzo.getText());
                }
            } catch (UnknownHostException ex) {
                    System.err.println("UnknowHostException: "+ex);
                    return;
            }

            try {
                memoria.server = new Socket(ip, Main.PORT);
            } catch (IOException ex) {
                System.err.println("Errore col server "+ip+": "+ex.getLocalizedMessage());
                return;
            }

            memoria.setVisible(false);
            memoria.main.salaAttesa(memoria.getServer());
        }
    }

}
