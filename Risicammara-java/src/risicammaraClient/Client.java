/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraClient;

import PacchettoGrafico.CollegatiPartita;
import PacchettoGrafico.PannelloSpeciale;
import PacchettoGrafico.SalaAttesa;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import risicammaraServer.MessageManage.MessaggioComandi;
import risicammaraServer.MessageManage.MessaggioChat;
import risicammaraServer.MessageManage.comandi_t;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.Partita;


/**
 *
 * @author stengun
 */
public class Client implements WindowListener, Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean debug = false;
        if (args.length>= 1 && (args[0].equals("--debug") || args[0].equals("-d"))) debug=true;

        Client client = new Client(debug, Client.PORT);
        client.run(); //Attenzione non viene creato un Thread, viene solo
                      //eseguito il metodo run
    }

    public static final int PORT = 12345;
    private PannelloSpeciale pannello;
    private Partita partita;
    private boolean debug;
    private Socket server;
    private int porta;

    public Client(boolean debug, int porta) {
        super();
        this.debug= debug;
        this.porta = porta;
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            System.err.println("Look and feel error: "+ex);
        } catch (InstantiationException ex) {
            System.err.println("Look and feel error: "+ex);
        } catch (IllegalAccessException ex) {
            System.err.println("Look and feel error: "+ex);
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Look and feel di sistema non supportato: "+ex);
        }
    }

    public void run() {
        //TODO dialogo di "crea partita"
        this.collegatiPartita();
    }

    public void collegatiPartita(){
        CollegatiPartita dialogo = new CollegatiPartita(this, this.porta);
        //System.out.println("8===D");
        }

    public void salaAttesa(Socket server){
        this.server=server;


        SalaAttesa finestraSalaAttesa = new SalaAttesa(server, true);


        /*
         //prova per vedere se funziona la parte vera del programma "risiko"
        ListaPlayers listaGiocatori = new ListaPlayers();
        listaGiocatori.addPlayer("Roberto", Colore_t.BLU);
        listaGiocatori.addPlayer("Matteo", Colore_t.GIALLO);
        listaGiocatori.addPlayer("Mandingo", Colore_t.NERO);
        /*try {
            new ObjectOutputStream(server.getOutputStream()).writeObject(new MessaggioComandi(comandi_t.CONNECTED, "Giocatore"));
            new ObjectOutputStream(server.getOutputStream()).writeObject(new MessaggioChat("culo", "messaggiopluffete"));
            new ObjectOutputStream(server.getOutputStream()).writeObject(new MessaggioChat("culo", "messaggiopluffete2"));
        } catch (IOException ex) {
            System.err.println(ex);
        }

        this.inizializzaPartita(new Partita(listaGiocatori));
         /*
         */

    }

    public void inizializzaPartita(Partita partita) {

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

    // <editor-fold defaultstate="collapsed" desc="ascoltatore finestre">
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
    }// </editor-fold>
}
