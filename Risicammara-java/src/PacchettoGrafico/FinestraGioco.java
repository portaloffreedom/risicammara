/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
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

    private PannelloSpeciale pannello;

    public FinestraGioco(Connessione server, ListaGiocatoriClient listaGiocatori, Plancia plancia) {
        super("Risicammara");
        this.server = server;
        this.listaGiocatori = listaGiocatori;
        this.plancia = plancia;
        //TODO mettere l'icona della finestra (e capire come minchia si fa -.-)

        this.setMinimumSize(new Dimension(800, 400));

        Container contestoFinestra = this.getContentPane();
        this.pannello = new PannelloSpeciale(60, plancia, listaGiocatori);

        this.setBounds(200, 180, 200, 180);
        contestoFinestra.add(pannello);


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
