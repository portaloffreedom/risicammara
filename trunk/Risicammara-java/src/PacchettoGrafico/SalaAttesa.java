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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import risicammaraServer.MessageManage.Messaggio_Comandi;
import risicammaraServer.MessageManage.comandi_t;
import risicammarajava.Colore_t;

/**
 *
 * @author matteo
 */
public class SalaAttesa extends JFrame implements WindowListener {
    
    private boolean leader;
    private Socket server;
    private QuadratoGiocatori giocatori[];

    public SalaAttesa(Socket server, boolean leader) {
        super("Sala d'Attesa");
        this.server=server;
        this.leader=leader;
        this.giocatori= new QuadratoGiocatori[6];

        this.addWindowListener(this);
        this.setBounds(100, 100, 500, 300);
        //this.setResizable(false);

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
        //pannello.setBorder(new EmptyBorder(5, 5, 5, 5));

        if (leader) disegnaInterfacciaLeader(pannello);
        else disegnaInterfaccia(pannello);
        

        disegnaRimanente(pannello);

       
        this.setVisible(true);


        for (QuadratoGiocatori quadratoGiocatori : giocatori) {
            BottoneGiocatori cacca = (BottoneGiocatori) quadratoGiocatori;
            System.out.println(cacca.getBounds());
        }
    }

    public SalaAttesa(Socket server) {
        this(server, false);
    }

    private void disegnaInterfacciaLeader(JPanel pannello){
        for (int i=0; i<6; i++) {
            BottoneGiocatori bottone = new BottoneGiocatori("Giocatore "+(i+1));
            pannello.add(bottone);
            this.giocatori[i]= bottone;
            bottone.setColore(Colore_t.GIALLO);
            bottone.setBounds(new Rectangle(5, 5+(30*i), 80, 30));
        }
    }

    private void disegnaInterfaccia(JPanel pannello) {
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void disegnaRimanente(JPanel pannello) {
        //throw new UnsupportedOperationException("Not yet implemented");
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