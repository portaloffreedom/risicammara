/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import risicammaraClient.Connessione;
import risicammaraJava.boardManage.Plancia;

/**
 *
 * @author matteo
 */
public class FinestraGioco extends JFrame {
    private Connessione server;
    private ListaGiocatoriClient listaGiocatori;
    private Plancia plancia;

    private PannelloGioco pannello;

    public FinestraGioco(Connessione server, ListaGiocatoriClient listaGiocatori, Plancia plancia) {
        super("Risicammara");
        this.server = server;
        this.listaGiocatori = listaGiocatori;
        this.plancia = plancia;

        this.setIconImage(new ImageIcon("./risorse/risicamlogo.png").getImage());

        Container contestoFinestra = this.getContentPane();
        this.pannello = new PannelloGioco(240, plancia, listaGiocatori);
        contestoFinestra.add(pannello);

        Rectangle rect = new Rectangle(this.pannello.getDimensioniMinime());
        //rect.width+=20;
        //rect.height+=60;
        contestoFinestra.setMinimumSize(rect.getSize());
        contestoFinestra.setSize(rect.getSize());
        rect.setLocation(50, 50);
        this.setBounds(rect);

        this.addWindowListener(new WindowListenerImpl(server));
        this.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="ascoltatore della finestra">
    private static class WindowListenerImpl implements WindowListener {

        Connessione server;

        public WindowListenerImpl(Connessione server) {
            this.server = server;
        }

        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
            //TODO casella di conferma uscita
            try {
                server.chiudiConnessione();
            } catch (IOException ex) {
                System.err.println("Connessione al server non chiusa nella maniera corretta");
                System.exit(1234);
            }
            System.exit(0);
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }
    }// </editor-fold>



}
