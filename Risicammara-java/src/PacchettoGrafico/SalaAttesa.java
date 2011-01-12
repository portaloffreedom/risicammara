/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
import risicammaraServer.MessageManage.Messaggio_Comandi;
import risicammaraServer.MessageManage.comandi_t;
import risicammarajava.Colore_t;

/**
 *
 * @author matteo
 */
public class SalaAttesa extends JFrame implements WindowListener {

    private final static Rectangle finestraR = new Rectangle(100, 100, 600, 305);
    private final static int margine = 5;
    private final static int altezza = 40;
    private final static Rectangle giocatoriR = new Rectangle(margine, -1, 120, altezza);
    private final static Rectangle prontiR = new Rectangle(giocatoriR.x+giocatoriR.width, -1, altezza, altezza);
    private final static Rectangle nomeGiocatoreR = new Rectangle(prontiR.x+prontiR.width+margine, margine, 220, altezza);
    private final static Rectangle coloreR = new Rectangle(nomeGiocatoreR.x+nomeGiocatoreR.width+margine, margine+10, 80, altezza-20);
    private final static Rectangle confermaR = new Rectangle(coloreR.x+coloreR.width+margine, margine, finestraR.width -(coloreR.x+coloreR.width+margine*2) , altezza);
    private final static Rectangle invioR = new Rectangle(finestraR.width-(altezza+margine), finestraR.height-(30+altezza+margine), altezza, altezza);
    private final static Rectangle immissioneR = new Rectangle(nomeGiocatoreR.x, invioR.y, finestraR.width-(giocatoriR.width+prontiR.width+invioR.width+margine*4), altezza);
    private final static Rectangle cronologiaR = new Rectangle(nomeGiocatoreR.x, nomeGiocatoreR.y+altezza+margine*2, immissioneR.width+invioR.width, finestraR.height-(35+altezza*2+margine*4));
    private boolean leader;
    private Socket server;
    private QuadratoGiocatori giocatori[];
    private JToggleButton pronti[];
    private JTextField nomeGiocatore;
    private JComboBox colore;
    private JButton conferma;
    private JTextField immissioneChat;
    private JButton invioChat;
    private JTextArea cronologiaChat;

    public SalaAttesa(Socket server, boolean leader) {
        super("Sala d'Attesa");
        this.server=server;
        this.leader=leader;
        this.giocatori= new QuadratoGiocatori[6];
        this.pronti = new JToggleButton[6];

        this.addWindowListener(this);
        this.setBounds(finestraR);
        this.setResizable(false);

        JPanel pannello = new JPanel(new LayoutManager() {

            public void addLayoutComponent(String name, Component comp) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void removeLayoutComponent(Component comp) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public Dimension preferredLayoutSize(Container parent) {
                return new Dimension(80, 30);
            }

            public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(80, 30);
            }

            public void layoutContainer(Container parent) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        this.getContentPane().add(pannello);

        disegnaGiocatori(pannello);

        this.setVisible(true);
    }

    public SalaAttesa(Socket server) {
        this(server, false);
    }

    private void disegnaGiocatori(JPanel pannello) {
        for (int i=0; i<6; i++) {

            this.pronti[i] = new JToggleButton("Ω");
            pannello.add(this.pronti[i]);

            if (this.leader)
                    this.giocatori[i] = quadratoInterfacciaLeader(pannello, i);
            else 
                    this.giocatori[i] = quadratoInterfaccia(pannello, i);


            this.pronti[i].setBounds(prontiR.x, margine+(margine+altezza)*i, prontiR.width, prontiR.height);
            this.pronti[i].setEnabled(false);

            this.giocatori[i].setColore(Colore_t.NULLO);
            this.giocatori[i].setBounds(giocatoriR.x, margine+(margine+altezza)*i, giocatoriR.width, giocatoriR.height);
        }

        this.nomeGiocatore = new JTextField();
        nomeGiocatore.setBounds(nomeGiocatoreR);
        
        this.colore = new JComboBox(Colore_t.values());
        this.colore.setBounds(coloreR);
        
        this.conferma = new JButton("conferma");
        this.conferma.setBounds(confermaR);

        this.immissioneChat = new JTextField();
        immissioneChat.setBounds(immissioneR);
        
        this.invioChat = new JButton("Invia");
        this.invioChat.setBounds(invioR);

        this.cronologiaChat = new JTextArea();
        this.cronologiaChat.setBounds(cronologiaR);


        pannello.add(this.nomeGiocatore);
        pannello.add(this.colore);
        pannello.add(this.conferma);
        pannello.add(this.immissioneChat);
        pannello.add(this.invioChat);
        pannello.add(this.cronologiaChat);

    }


    private QuadratoGiocatori quadratoInterfacciaLeader(JPanel pannello, int i){
        BottoneGiocatori bottone = new BottoneGiocatori("Giocatore "+(i+1));
        pannello.add(bottone);
        return bottone;
    }

    private QuadratoGiocatori quadratoInterfaccia(JPanel pannello, int i) {
        labelGiocatori label = new labelGiocatori("Giocatore "+(i+1));
        label.setBorder(new TextFieldBorder());
        pannello.add(label);
        return label;
    }

    // <editor-fold defaultstate="collapsed" desc="Window Listener">
    public void windowOpened(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        try {
            new ObjectOutputStream(server.getOutputStream()).writeObject(new Messaggio_Comandi(comandi_t.DISCONNECT, "merdone"));
        } catch (IOException ex) {
            System.err.println("Errore nel mandare il messaggio di \"hang-up\": "+ex);
        }
        try {
            server.close();
        } catch (IOException ex) {
            System.err.println("Errore nel chiudere il collegamento col server: "+ex);
        }
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }// </editor-fold>

    private interface QuadratoGiocatori {
        public void setNome(String testo);
        public void setColore(Colore_t colore);
        public void setVisible(boolean visible);
        public void setBounds(int x, int y, int width, int height);
    }

    private class BottoneGiocatori extends JButton implements QuadratoGiocatori {

        public BottoneGiocatori(String text) {
            super(text);
        }

        public void setNome(String testo) {
            this.setName(testo);
        }

        public void setColore(Colore_t colore) {
            this.setForeground(colore.getColor());
        }
    }

    private class labelGiocatori extends JLabel implements QuadratoGiocatori {

        public labelGiocatori(String text) {
            super (text, CENTER);
        }

        public void setNome(String testo) {
            this.setText(testo);
        }

        public void setColore(Colore_t colore) {
            this.setForeground(colore.getColor());
        }

    }
}