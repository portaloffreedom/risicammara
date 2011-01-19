/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraClient;

import PacchettoGrafico.CollegatiPartita;
import PacchettoGrafico.PannelloSpeciale;
import PacchettoGrafico.RisicammaraLookAndFeel;
import PacchettoGrafico.salaAttesa.SalaAttesa;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import risicammaraJava.turnManage.Partita;

//TODO Stampare un messaggio: "ho cambiato nick"

/**
 *
 * @author stengun
 */
public class Client implements WindowListener, Runnable {

    public static int PORT = 12345;
    private static String laf;
    private static boolean lafPersonalizzato = false;
    private static boolean debug = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Parser(args);

        Client client = new Client(Client.PORT);
        client.run(); //Attenzione non viene creato un Thread, viene solo
                      //eseguito il metodo run
    }

    /**
     * Parser degli argomenti di debug
     * @param args argomenti dal main
     */
    private static void Parser (String args[]){
        for (int i=0; i<args.length; i++) {

            //DEBUG
            if (args[i].equals("-d"))
                Debug(true);
            if (args[i].equals("--debug"))
                Debug(true);

            //LOOK AND FEEL
            if (args[i].equals("-laf")){
                i++;
                Laf(args[i]);
            }

            //PORTA DA USARE
            if (args[i].equals("-p")){
                i++;
                Port(Integer.getInteger(args[i]));
            }
            if (args[i].equals("--port")){
                i++;
                Port(Integer.getInteger(args[i]));
            }
            
        }
    }

    private static void Debug (boolean debug){
        Client.debug=debug;
    }

    private static void Laf(String laf){
        Client.lafPersonalizzato=true;
        Client.laf=laf;
    }

    private static void Port(int port){
        Client.PORT=port;
    }

    private PannelloSpeciale pannello;
    private Partita partita;
    private Socket server;
    private int porta;

    public Client(int porta) {
        super();
        this.porta = porta;

        try {
            if (lafPersonalizzato){
                if (laf.equals("personal")){
                    UIManager.setLookAndFeel(new RisicammaraLookAndFeel());
                }
                else {
                    UIManager.setLookAndFeel(laf);
                }
            }
            else {
                if (System.getProperties().getProperty("os.name").equalsIgnoreCase("linux")) {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                }
                else {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            }
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


        SalaAttesa finestraSalaAttesa = new SalaAttesa(server);
        Thread salaAttesa = new Thread(finestraSalaAttesa);
        salaAttesa.start();


        /*
         //prova per vedere se funziona la parte vera del programma "risiko"
        ListaPlayers listaGiocatori = new ListaPlayers();
        listaGiocatori.addPlayer("Roberto", Colore_t.BLU);
        listaGiocatori.addPlayer("Matteo", Colore_t.GIALLO);
        listaGiocatori.addPlayer("Mandingo", Colore_t.NERO);

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

    private static class LookAndFeelImpl extends LookAndFeel {

        public LookAndFeelImpl() {
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getID() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isNativeLookAndFeel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isSupportedLookAndFeel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
