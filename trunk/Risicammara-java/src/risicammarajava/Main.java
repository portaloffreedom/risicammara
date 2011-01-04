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
import risicammarajava.playerManage.Giocatore;
import risicammarajava.playerManage.ListaPlayers;


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


        ListaPlayers listagiocatori = new ListaPlayers();
        Giocatore gioc = new Giocatore("Roberto", Colore_t.BLU);
        gioc.setObj(Obbiettivi_t.ASIASUDAMERICA);
        listagiocatori.addPlayer(gioc);
        gioc = new Giocatore("matteo",Colore_t.NERO);
        gioc.setObj(Obbiettivi_t.BLU);
        listagiocatori.addPlayer(gioc);
        int turno=0;
        
        JFrame finestra = new JFrame("Risicammara");
        finestra.setMinimumSize(new Dimension(800, 400));
        Container contestoFinestra = finestra.getContentPane();
        PannelloSpeciale pannello = new PannelloSpeciale(0,listagiocatori,turno);

        finestra.setBounds(200, 180, 200, 180);
        contestoFinestra.add(pannello);


        finestra.addWindowListener(new Main(pannello));
        finestra.setVisible(true);

        if (args[0].equals("--debug")) {
            System.out.println("Poteri della SuperMucca attivati ;)");
            PoteriDellaSuperMucca dio = new PoteriDellaSuperMucca();
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
        //this.pannello.repaint();
    }

    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("windowDeactivated");
    }

}
