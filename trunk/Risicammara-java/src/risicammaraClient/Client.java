package risicammaraClient;

import PacchettoGrafico.CollegatiPartita;
import PacchettoGrafico.FinestraGioco;
import PacchettoGrafico.PannelloGiocoPackage.PannelloGioco;
import PacchettoGrafico.salaAttesa.SalaAttesa;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.playerManage.ListaGiocatoriClient;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioObbiettivo;
import risicammaraServer.messaggiManage.MessaggioPlancia;
import risicammaraServer.messaggiManage.MessaggioSequenzaGioco;

//TODO Stampare un messaggio: "ho cambiato nick"
//TODO provare ad usare OpenGL

/**
 * Classe che rappresenta l'intera parte client del programma (Possiede anche il
 * Main per farlo partire)
 * 
 * @author matteo
 */
public class Client implements Runnable {
    /**
     * Percorso del logo del programma. (icona per la barra ecc.)
     */
    public static final String RISICAMLOGO = "/Images/risicamlogo.png";
    /**
     * Percorso dell'immagine Plancia usata nel client.
     */
    public static final String RISICAMMARA_PLANCIA = "/Images/risicammara_plancia.png";
    /**
     * Percorso dell'immagine Negativo usata dal client per identificare graficamente
     * i territori sulla mappa originale.
     */
    public static final String RISICAMMARA_NEGATIVO = "/Images/risicammara_plancia.bmp";
    /** Rappresenta la Porta di default che deve utilizzare il programma. Viene
     * utlizzata anche dal lato server come porta di DEFAULT */
    public static int PORT = 12345;
    /** Stringa che codifica il look and feel da utilizzare. Nel caso si voglia
     * utilizzare il look and feel di sistema, lasciare una stringa vuota ("") */
    private static String laf = "metal";
    /** boolenano che identifica se si sta utilizzando il programma in modalità
     * di DEBUG */
    public static boolean DEBUG = false;

    /**
     * Livello qualità del client Bassa
     */
    public static final int QUALITA_BASSA = 0;
    /**
     * Livello qualità del client Media
     */
    public static final int QUALITA_MEDIA = 1;
    /**
     * Livello qualità del client Alta
     */
    public static final int QUALITA_ALTA = 2;

    /**
     * Livello qualità del client
     */
    public static int QUALITA = 0;


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
     * Parser degli argomenti di DEBUG
     * @param args argomenti dal main
     */
    private static void Parser(String args[]) {
        for (int i = 0; i < args.length; i++) {

            //DEBUG
            if (args[i].equals("-d") || args[i].equals("--debug")) {
                Debug(true);
                continue;
            }


            //LOOK AND FEEL
            if (args[i].equals("-laf") || args[i].equals("--lookandfeel")) {
                i++;
                Laf(args[i]);
                continue;
            }

            //PORTA DA USARE
            if (args[i].equals("-p") || args[i].equals("--port")) {
                i++;
                Port(Integer.getInteger(args[i]));
                continue;
            }

            //QUALITÀ DEL RENDERING
            if (args[i].equals("-q") || args[i].equals("--quality")) {
                i++;
                Quality(args[i]);
            }

        }
    }

    /**
     * Cambia la modalità di DEBUG (attiva o no). Si può utilizzare solo all'inizio
     * prima che parta la parte grafica, altrimenti non si garantisce il corretto
     * funzionamento.
     * @param DEBUG da mettere true o false
     */
    private static void Debug(boolean debug) {
        Client.DEBUG = debug;
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

    private static void Quality(String quality){
        if (quality.equals("bassa") || quality.equals("0")) {
            QUALITA = QUALITA_BASSA;
            return;
        }
        if (quality.equals("media") || quality.equals("1")) {
            QUALITA = QUALITA_MEDIA;
            return;
        }
        if (quality.equals("alta") || quality.equals("2")) {
            QUALITA = QUALITA_ALTA;
            return;
        }

    }

    private PannelloGioco pannello;
    private PartitaClient partita;
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
                if (laf.equalsIgnoreCase("metal"))
                    UIManager.setLookAndFeel(new MetalLookAndFeel());
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
    @Override
    public void run() {
        //TODO dialogo di "crea inizializzaPartita"
        this.collegatiPartita();
    }

    /**
     * tu sei malato
     */
    public void collegatiPartita() {
        CollegatiPartita dialogo = new CollegatiPartita(this, this.porta);
    }

    /**
     * Crea una sala d'attesa.
     * @param server Il socket del server.
     */
    public void salaAttesa(Socket server) {
        this.server = server;
        
        SalaAttesa finestraSalaAttesa = new SalaAttesa(server, this);
        Thread salaAttesa = new Thread(finestraSalaAttesa);
        salaAttesa.start();
    }

    /**
     * Inizializza la partita lato client.
     * @param connessioneServer La connessione al server
     * @param listaGiocatori La lista dei giocatori
     * @param indexGiocatore L'indice del giocatore locale
     */
    public void inizializzaPartita(Connessione connessioneServer, ListaPlayers listaGiocatori, int indexGiocatore){
        MessaggioPlancia veicoloPlancia = null;
        Obbiettivi_t mioObbiettivo = null;
        MessaggioSequenzaGioco sequenzaGioco = null;
        try {
            veicoloPlancia = (MessaggioPlancia) connessioneServer.ricevi();
            sequenzaGioco = (MessaggioSequenzaGioco) connessioneServer.ricevi();
            mioObbiettivo = ((MessaggioObbiettivo) connessioneServer.ricevi()).getObj();
        } catch (IOException ex) {
            RiavviaClientErrore("Errore nel leggere la plancia (lettura da stream): "+ex);
        } catch (ClassNotFoundException ex) {
            RiavviaClientErrore("Errore nel leggere la plancia (interpretazione oggetto): "+ex);
        }
        ListaGiocatoriClient listaGiocatoriClient = new ListaGiocatoriClient(listaGiocatori, indexGiocatore, mioObbiettivo);
        PlanciaClient planciaClient = new PlanciaClient(veicoloPlancia.getPlancia());
        partita = new PartitaClient(connessioneServer, listaGiocatoriClient, planciaClient, sequenzaGioco.getSequenza());
        
        FinestraGioco finestra =  new FinestraGioco(connessioneServer, partita);

        //fa partire direttamente il ciclo di lettura dei messaggi
        //TODO implementare la cosa all'esterno di finestra, magari in partitaClient
        Thread finestraThread = new Thread(finestra);
        finestraThread.start();
    }

    /**
     * Riavvia il client in seguito ad un errore.
     * @param motivazione il testo dell'errore
     */
    static public void RiavviaClientErrore(String motivazione){
        //TODO implementare finestra di dialogo
        //TODO implementare la richiesta di una nuova connessione ad un'altro server
        JOptionPane.showMessageDialog(null, motivazione, "Riavvio Client", JOptionPane.ERROR_MESSAGE);
        System.err.println(motivazione);
        RiavviaClient();
    }

    /**
     * Riavvia il client.
     */
    static public void RiavviaClient(){
        Client client = new Client(Client.PORT, laf);
        client.run();
    }

    /**
     * Carica una immagine da un URL.
     * @param pad Il percorso dell'immagine in formato URL
     * @return L'oggetto BufferedImage corrispondente
     */
    static public BufferedImage loadImage(URL pad){
        //return Toolkit.getDefaultToolkit().getImage(getClass().getResource(pad));
        BufferedImage img = null;
        try {
            img = ImageIO.read(pad);
        } catch (IOException e) {
            System.err.println("Errore nel caricare \""+pad+"\":"+e);
        }
        return img;
    }

    /**
     * Carica una immagine da un percorso specifico come risorsa.
     * @param questo l'oggetto da cui viene chiamata
     * @param pad La stringa che identifica il percorso
     * @return Un oggetto BufferedImage corrispondente
     */
    static public BufferedImage loadImage(Object questo, String pad){
        return loadImage(questo.getClass().getResource(pad));
    }
}
