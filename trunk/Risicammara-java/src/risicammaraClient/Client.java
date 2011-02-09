/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraClient;

import PacchettoGrafico.CollegatiPartita;
import PacchettoGrafico.FinestraGioco;
import PacchettoGrafico.ListaGiocatoriClient;
import PacchettoGrafico.PannelloGioco;
import PacchettoGrafico.salaAttesa.SalaAttesa;
import java.net.Socket;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import risicammaraJava.boardManage.Plancia;
import risicammaraJava.turnManage.Partita;

//TODO Stampare un messaggio: "ho cambiato nick"
//TODO provare ad usare OpenGL

/**
 * Classe che rappresenta l'intera parte client del programma (Possiede anche il
 * Main per farlo partire)
 * 
 * @author matteo
 */
public class Client implements Runnable {

    /** Rappresenta la Porta di default che deve utilizzare il programma. Viene
     * utlizzata anche dal lato server come porta di DEFAULT */
    public static int PORT = 12345;
    /** Stringa che codifica il look and feel da utilizzare. Nel caso si voglia
     * utilizzare il look and feel di sistema, lasciare una stringa vuota ("") */
    private static String laf = "";
    /** boolenano che identifica se si sta utilizzando il programma in modalità
     * di DEBUG */
    private static boolean debug = false;

    /**
     * Main per fare partire il programma lato client
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Parser(args);

        Client client = new Client(Client.PORT, laf);
        client.run(); //Attenzione non viene creato un Thread, viene solo
        //eseguito il metodo run
    }

    /**
     * Parser degli argomenti di debug
     * @param args argomenti dal main
     */
    private static void Parser(String args[]) {
        for (int i = 0; i < args.length; i++) {

            //DEBUG
            if (args[i].equals("-d")) {
                Debug(true);
                continue;
            }
            if (args[i].equals("--debug")) {
                Debug(true);
                continue;
            }


            //LOOK AND FEEL
            if (args[i].equals("-laf")) {
                i++;
                Laf(args[i]);
                continue;
            }

            //PORTA DA USARE
            if (args[i].equals("-p")) {
                i++;
                Port(Integer.getInteger(args[i]));
                continue;
            }
            if (args[i].equals("--port")) {
                i++;
                Port(Integer.getInteger(args[i]));
                continue;
            }

        }
    }

    /**
     * Cambia la modalità di debug (attiva o no). Si può utilizzare solo all'inizio
     * prima che parta la parte grafica, altrimenti non si garantisce il corretto
     * funzionamento.
     * @param debug da mettere true o false
     */
    private static void Debug(boolean debug) {
        Client.debug = debug;
    }

    /**
     * Imposta un look and feel personalizzato. 
     * @param laf Stringa che identifica il laf che si vuole impostare
     */
    private static void Laf(String laf) {
        Client.laf = laf;
    }

    /**
     * Imposta una porta personalizzata (default 12345). Da utlizzare all'inizio
     * dell'avvio del programma, altrimenti non si garantisce un corretto
     * funzionamento del programma stesso.
     * @param port nuova porta da utilizzare
     */
    private static void Port(int port) {
        Client.PORT = port;
    }

    private PannelloGioco pannello;
    private Partita partita;
    private Socket server;
    private int porta;

    /**
     * Costruttore del Client. Imposta la porta ed il look and feel.
     * @param porta Porta che deve utilizzare il client per collegarsi
     * @param laf look and feel che deve utilizzare il client.
     */
    public Client(int porta, String laf) {
        super();
        this.porta = porta;

        try {
            if (!laf.equals("")) {
                if (laf.equalsIgnoreCase("kde"))
                    System.err.println(laf+" non ancora implementato");
                else
                    UIManager.setLookAndFeel(laf);
                
            } else {
                if (System.getProperties().getProperty("os.name").equalsIgnoreCase("linux")) {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                } else {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("Look and feel error: " + ex);
        } catch (InstantiationException ex) {
            System.err.println("Look and feel error: " + ex);
        } catch (IllegalAccessException ex) {
            System.err.println("Look and feel error: " + ex);
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Look and feel di sistema non supportato: " + ex);
        }
    }

    /**
     * Fa partire il client costruito.
     */
    public void run() {
        //TODO dialogo di "crea inizializzaPartita"
        this.collegatiPartita();
    }

    public void collegatiPartita() {
        CollegatiPartita dialogo = new CollegatiPartita(this, this.porta);
    }

    public void salaAttesa(Socket server) {
        this.server = server;
        
        SalaAttesa finestraSalaAttesa = new SalaAttesa(server, this);
        Thread salaAttesa = new Thread(finestraSalaAttesa);
        salaAttesa.start();
    }

    public void inizializzaPartita (Connessione server, Plancia plancia, ListaGiocatoriClient listaGiocatori){
        
        FinestraGioco finestra =  new FinestraGioco(server, listaGiocatori, plancia);
        /*
        if (Client.debug == true) {
            System.out.println("Poteri della SuperMucca attivati ;)");
            PoteriDellaSuperMucca dio = new PoteriDellaSuperMucca(this.partita);
        }
         */
        
    }
}
