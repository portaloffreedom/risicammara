package PacchettoGrafico;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import risicammaraClient.Client;

/**
 * Classe che implementa i metodi per effettuare una connessione alla partita.
 * @author matteo
 */
public class CollegatiPartita extends JFrame {

    private TextField stringaIndirizzo;
    private JButton collegati;
    private JCheckBox locale;
    private Client main;

    private Socket server;
    private int porta;

    /**
     * Restituisce il socket al server.
     * @return il socket del server
     */
    public Socket getServer() {
        return server;
    }

    /**
     * Si collega ad un server di gioco tramite una porta specificata.
     * @param main l'oggetto Client che si connette
     * @param porta la porta a cui connettersi
     */
    public CollegatiPartita(Client main, int porta) {
        super("Collegati al server");
        //this.addWindowListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(300, 100);
        super.setLocationByPlatform(true);
        this.setResizable(false);
        this.main = main;
        this.porta = porta;
        URL url = this.getClass().getResource(Client.RISICAMLOGO);
        ImageIcon icona = new ImageIcon(url);
        this.setIconImage(icona.getImage());

        JPanel pannello = new JPanel(new BorderLayout(5, 5));
        this.getContentPane().add(pannello);
        pannello.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.stringaIndirizzo = new TextField();
        pannello.add(stringaIndirizzo, BorderLayout.NORTH);

        this.locale = new JCheckBox("Locale");
        pannello.add(locale, BorderLayout.WEST);
        this.locale.addActionListener(new ActionLocalHost(stringaIndirizzo));

        this.collegati = new JButton("Collegati");
        pannello.add(collegati, BorderLayout.EAST);
        this.collegati.addActionListener(new ActionCollegati(this, this.porta));

        this.setVisible(true);
    }

    private static class ActionLocalHost implements ActionListener {

        private TextField stringaIndirizzo;

        public ActionLocalHost(TextField stringaIndirizzo) {
            this.stringaIndirizzo=stringaIndirizzo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox culo = (JCheckBox) e.getSource();
            this.stringaIndirizzo.setEnabled(!culo.isSelected());
        }
    }

    private static class ActionCollegati implements ActionListener {

        private CollegatiPartita memoria;
        private int porta;

        public ActionCollegati(CollegatiPartita memoria, int porta) {
            this.memoria = memoria;
            this.porta = porta;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setWorking(true);

            InetAddress ip = null;
            try {
                if (memoria.locale.isSelected()) {
                    ip = InetAddress.getLocalHost();
                }
                else {
                    ip = InetAddress.getByName(memoria.stringaIndirizzo.getText());
                }
            } catch (UnknownHostException ex) {
                    System.err.println("UnknowHostException: "+ex);
                    setWorking(false);
                    return;
            }

            try {
                memoria.server = new Socket(ip, this.porta);
            } catch (IOException ex) {
                System.err.println("Errore col server "+ip+": "+ex.getLocalizedMessage());
                setWorking(false);
                return;
            }

            memoria.setVisible(false);
            memoria.main.salaAttesa(memoria.getServer());

            //libera la memoria della finestra di "collegati partita"
            memoria.dispose();
        }

        private void setWorking (boolean lavorante){
            //preparazione
            Cursor cursore = null;
            if (lavorante)
                cursore = new Cursor(Cursor.WAIT_CURSOR);
            else
                cursore = new Cursor(Cursor.DEFAULT_CURSOR);

            //imposta gli oggetti grafici
            memoria.stringaIndirizzo.setEnabled(!lavorante);
            memoria.stringaIndirizzo.setCursor(cursore);
            memoria.setCursor(cursore);


        }
    }

}
