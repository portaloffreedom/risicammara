package risicammaraServer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import risicammaraClient.Client;

/**
 *
 * @author stengun
 */
public class ServerGUI implements ActionListener{

    public static String VERSION = "beta 2";
    public static String ICON = "/Images/banana.png";

    private JPanel pannello;
    private JFrame finestra;
    private JButton bottonestart,bottonestop;
    private JLabel etichettaStato;

    private Server server;
    private Thread serverThread;
    private boolean gui;

    public static void main(String[] args){
        ServerGUI servergui = new ServerGUI(args);
        servergui.open();
    }

    public ServerGUI(String[] args){
        gui = true;
        if(args.length > 1) parseArgs(args);
        server = new Server(Client.PORT);
        serverThread = null;
    }

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

    private boolean requireOption(String str){
        if(str.contentEquals("--gui")) return true;
        return false;
    }
    
    private void open(){
        if(gui){
            creaFinestra();
        }
        else{
            serverThread = new Thread(server);
            serverThread.start();
        }
    }

    private void creaFinestra(){

        pannello = new JPanel();
        finestra = new JFrame();
        bottonestart = new JButton("Start Server");
        bottonestop = new JButton("Stop Server");
        etichettaStato = new JLabel("Server STOPPATO.");

        Dimension dimensioniStandard = new Dimension(250, 100);
        ImageIcon ic = new ImageIcon(this.getClass().getResource(ServerGUI.ICON));
        finestra.setIconImage(ic.getImage());
        finestra.setTitle("Risicammara SERVER version: "+ServerGUI.VERSION);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //finestra.setMinimumSize(dimensioniStandard);
        finestra.getContentPane().add(pannello);
        bottonestart.addActionListener(this);
        bottonestop.addActionListener(this);
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
            if(serverThread != null) serverThread.interrupt();
            serverThread = new Thread(server);
            serverThread.start();
            etichettaStato.setText("Server ATTIVO.");
            etichettaStato.repaint();
        }
        if(e.getSource() == bottonestop && serverThread != null){

            serverThread.interrupt();
            etichettaStato.setText("Server STOPPATO.");
            etichettaStato.repaint();
        }
    }

}