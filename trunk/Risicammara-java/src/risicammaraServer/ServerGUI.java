package risicammaraServer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import risicammaraClient.Client;

/**
 * Classe avviabile per il server. In base gli argomenti passati all'eseguibile
 * puoi far partire o meno l'interfaccia per comandare il server.
 * @author stengun
 */
public class ServerGUI implements ActionListener{

    public static String VERSION = "beta 2";
    public static String ICON = "/Images/banana.png";

    private JPanel pannello;
    private JFrame finestra;
    private JButton bottonestart,bottonestop;
    private JLabel etichettaStato;

    private Server serverThread;
    private boolean gui;

    public static void main(String[] args){
        ServerGUI servergui = new ServerGUI(args);
        servergui.open();
    }

    /**
     * Inizializza tutte le variabili necessarie al server per avviarsi.
     * @param args gli argomenti passati all'eseguibile dalla riga di comando.
     */
    public ServerGUI(String[] args){
        gui = true;
        if(args.length > 1) parseArgs(args);
        serverThread = new Server(Client.PORT);
    }
/**
 * Screma gli argomenti della riga di comando per determinare se sono corretti e
 * ne imposta il valore/opzioni.
 * @param args gli argomenti passati all'eseguibile dalla riga di comando.
 */
    private void parseArgs(String[] args){
        String prec = null;
        for(String s:args){
            if(s.contentEquals("--gui")){
                if(prec != null){
                    if(requireOption(prec)){
                        System.err.println("Sintassi errata dei parametri!");
                        System.exit(12);
                    }
                }
                prec = new String(s);
                continue;
            }
            else if(prec == null){
                System.err.println("Errore, comando sconosciuto: "+s);
                System.exit(13);
            }
            if(prec != null && prec.contentEquals("--gui")){
                if(s.contentEquals("true")){
                    gui = true;
                    prec = null;
                    continue;
                }
                if(s.contentEquals("false")){
                    gui = false;
                    prec = null;
                    continue;
                }

            }
            else{
                System.err.println("Errore, parametro sconosciuto: "+s);
                System.exit(13);
            }
        }
        if(prec!=null){
            System.err.println("Errore, sintassi errata!"+args);
            System.exit(12);
        }
    }
/**
 * Chiede se un dato comando ha bisogno di una sintassi particolari (esempio:
 * --gui option è corretto, --gui -d è errato.)
 * @param str il comando da controllare.
 * @return true se il comando ha bisogno di un'ulteriore opzione, false altrimenti.
 */
    private boolean requireOption(String str){
        if(str.contentEquals("--gui")) return true;
        return false;
    }
 /**
  * Avvia il server con i parametri impostati.
  */
    private void open(){
        if(gui){
            creaFinestra();
        }
        else{
            serverThread.start();
        }
    }
/**
 * Crea la finestra del server.
 */
    private void creaFinestra(){

        pannello = new JPanel();
        finestra = new JFrame();
        bottonestart = new JButton("Start Server");
        bottonestop = new JButton("Stop Server");
        etichettaStato = new JLabel("Server STOPPATO.");

        ImageIcon ic = new ImageIcon(this.getClass().getResource(ServerGUI.ICON));
        finestra.setIconImage(ic.getImage());
        finestra.setTitle("Risicammara SERVER version: "+ServerGUI.VERSION);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.getContentPane().add(pannello);
        bottonestart.addActionListener(this);
        bottonestop.addActionListener(this);
        bottonestop.setEnabled(false);
        
        pannello.add(bottonestart);
        pannello.add(bottonestop);
        pannello.add(etichettaStato);
        finestra.setLocationByPlatform(true);
        
        finestra.setVisible(true);
        finestra.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bottonestart){
            if(serverThread.isAlive()) serverThread.setStop(true);
            serverThread = new Server(Client.PORT);
            serverThread.start();
            etichettaStato.setText("Server ATTIVO.");
            etichettaStato.repaint();
            bottonestart.setEnabled(false);
            bottonestop.setEnabled(true);
        }
        if(e.getSource() == bottonestop && serverThread != null){

            if(serverThread.isAlive()) serverThread.setStop(true);
            etichettaStato.setText("Server STOPPATO.");
            etichettaStato.repaint();
            bottonestart.setEnabled(true);
            bottonestop.setEnabled(false);
        }
    }

}