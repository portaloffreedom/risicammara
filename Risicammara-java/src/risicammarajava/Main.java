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
import risicammarajava.playerManage.ListaPlayers;
import risicammarajava.turnManage.Partita;


/**
 *
 * @author stengun
 */
public class Main implements WindowListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean debug = false;
        if (args.length>= 1 && (args[0].equals("--debug") || args[0].equals("-d"))) debug=true;

        Main main = new Main(debug);

    }
    
    private PannelloSpeciale pannello;
    private Partita partita;
    private boolean debug;

    public Main(boolean debug) {
        super();
        this.debug= debug;

        //TODO dialogo di "crea partita"
        ListaPlayers listaGiocatori = new ListaPlayers();
        listaGiocatori.addPlayer("Roberto", Colore_t.BLU);
        listaGiocatori.addPlayer("Matteo", Colore_t.GIALLO);
        listaGiocatori.addPlayer("Mandingo", Colore_t.NERO);

        this.inizializzaPartita(new Partita(listaGiocatori));

    }

    private void inizializzaPartita(Partita partita) {

        this.partita=partita;

        JFrame finestra = new JFrame("Risicammara");
        finestra.setMinimumSize(new Dimension(800, 400));
        Container contestoFinestra = finestra.getContentPane();
        this.pannello = new PannelloSpeciale(60, this.partita);

        finestra.setBounds(200, 180, 200, 180);
        contestoFinestra.add(pannello);


        finestra.addWindowListener(this);
        finestra.setVisible(true);

        if (this.debug == true) {
            System.out.println("Poteri della SuperMucca attivati ;)");
            PoteriDellaSuperMucca dio = new PoteriDellaSuperMucca(this.partita);
        }

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
    }

    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("windowDeactivated");
    }

}
