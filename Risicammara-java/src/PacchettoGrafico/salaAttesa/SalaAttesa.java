/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.MessageManage.*;

/**
 *
 * @author matteo
 */
public class SalaAttesa extends JFrame implements WindowListener, Runnable {

    // Rettangoli e variabili necessarie per disegnare pulsanti e roba sul pannello
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

    // variabili locali per il funzionamento interno
    private boolean leader;
    private Socket server;
    private ObjectOutputStream scriviServer;
    private ObjectInputStream  leggiServer;
    /** Variabile che serve per decidere quando deve smettere la sala d'attesa e
     * cominciare la partita */
    private boolean lobby;

    //roba ricevuta dal sever
    /** Indice del Giocatore che sta utilizzando l'attuale Client */
    private int indexGiocatore;
    /** Intera lista dei giocatori */
    private ListaPlayers listaGiocatori;

    //Oggetti disegnati sul pannello
    private QuadratoGiocatori giocatori[];
    private JToggleButton pronti[];
    private JTextField nomeGiocatore;
    private JComboBox colore;
    private JButton conferma;
    private JTextField immissioneChat;
    private JButton invioChat;
    private CronologiaChat cronologiaChat;

    public SalaAttesa(Socket server, boolean leader) {
        super("Sala d'Attesa");
        this.server=server;
        this.leader=leader;
        this.giocatori= new QuadratoGiocatori[6];
        this.pronti = new JToggleButton[6];
        this.lobby = true; //imposta lo "stato" come lobby

        this.indexGiocatore = -2;
        this.listaGiocatori = null;


        try {
            creaConnessione();
        } catch (IOException ex) {
            System.err.println("Il sistema non è riuscito ad aprire gli stream"
                    + "di output o di input dal server oppure non è riuscito a"
                    + "ricevere il messaggio dal server. Errore: "+ex);
            System.exit(3);
        } catch (ClassNotFoundException ex) {
            System.err.println("Errore di lettura nel messaggio di conferma del"
                    + " server: "+ex);
            System.exit(4);
        }


        this.addWindowListener(this);
        this.setBounds(finestraR);
        this.setResizable(false);

        JPanel pannello = new JPanel(new LayoutManagerMatteo());
        this.getContentPane().add(pannello);

        disegnaGiocatori(pannello);

        personalizza();
    }

    public SalaAttesa(Socket server) {
        this(server, false);
    }

    public void run() {
        this.setVisible(true);
        
        Messaggio arrivo = null;

        while (lobby) {
            try {
                arrivo = (Messaggio) leggiServer.readObject();
            } catch (IOException ex) {
                System.err.println("Errore! Client disconnesso :"+ex);
                System.err.println(ex.getStackTrace());
                System.exit(15);
            } catch (ClassNotFoundException ex) {
                System.err.println("Attenzione! messaggio arrivato irriconoscibile: "+ex);
            }

            System.out.println("Nuovo messaggio arrivato: "+arrivo);

            switch (arrivo.getType()) {
                case CHAT:
                    MessaggioChat msgChat = (MessaggioChat) arrivo;
                    cronologiaChat.stampaMessaggio(msgChat.toString(listaGiocatori));
                    System.out.println("Messaggio Chat| "+msgChat.toString(listaGiocatori));
                    break;

                case AGGIORNADATIGIOCATORE:
                    MessaggioAggiornaDatiGiocatore msgUpdateGiocatore = (MessaggioAggiornaDatiGiocatore) arrivo;
                    Giocatore tmp = listaGiocatori.get(msgUpdateGiocatore.getSender());
                    tmp.setNome(msgUpdateGiocatore.getNick());
                    tmp.setArmyColour(msgUpdateGiocatore.getColor());
                    aggiornaQuadratoGiocatori(tmp, msgUpdateGiocatore.getSender());
                    break;

                case AGGIUNGIGIOCATORE:
                    MessaggioAddPlayer msgAddPlayer = (MessaggioAddPlayer) arrivo;
                    int indexNewG = listaGiocatori.addPlayer(msgAddPlayer.getPlayer());
                    aggiornaQuadratoGiocatori(msgAddPlayer.getPlayer(), indexNewG);
                    break;

                case COMMAND:
                    MessaggioComandi msgComando = (MessaggioComandi) arrivo;
                    switch (msgComando.getComando()) {
                        case DISCONNECT:
                            int indexRimozione = msgComando.getSender();
                            listaGiocatori.remPlayer(indexRimozione);
                            aggiornaQuadratoGiocatori(new Giocatore("disconnesso", Colore_t.NERO), indexRimozione);
                            break;

                        default:
                            System.err.println("Comando non riconosciuto: "+msgComando.getComando());
                            //lasciare continuare così ricade nel caso default dello swich sopra
                    }
                    //Non aggiungere casi qua
                default:
                    System.err.println("Messaggio ignorato (il programma potrebbe non funzionare più bene)\n"
                                      +"Il messaggio ignorato era "+arrivo.getType()+":\""+arrivo+"\"");
            }

        }
    }

    private void creaConnessione() throws IOException, ClassNotFoundException {
        if (server.isInputShutdown()) {
            System.err.println("Il server non permette di leggere da lui");
        }
        if (server.isOutputShutdown()) {
            System.err.println("Il server non permette di scrivere su di lui");
        }

        this.scriviServer = new ObjectOutputStream(new BufferedOutputStream(this.server.getOutputStream()));
        this.scriviServer.flush();
        this.leggiServer  = new ObjectInputStream(new BufferedInputStream(this.server.getInputStream()));
        MessaggioConfermaNuovoGiocatore msg = (MessaggioConfermaNuovoGiocatore) leggiServer.readObject();
        this.indexGiocatore = msg.getPlyIndex();
        this.listaGiocatori = msg.getPlyList();
    }

    private void disegnaGiocatori(JPanel pannello) {
        for (int i=0; i<ListaPlayers.MAXPLAYERS; i++) {

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
        this.invioChat.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    mandaMessaggioChat();
                } catch (IOException ex) {
                    System.err.println("Errore! Messaggio non inviato: "+ex);
                }
            }
        });

        this.cronologiaChat = new CronologiaChat();
        this.cronologiaChat.setBounds(cronologiaR);
        this.cronologiaChat.setEditable(false);


        pannello.add(this.nomeGiocatore);
        pannello.add(this.colore);
        pannello.add(this.conferma);
        pannello.add(this.immissioneChat);
        pannello.add(this.invioChat);
        pannello.add(this.cronologiaChat);

    }

    private void personalizza() {

        QuadratoGiocatori quadratoGiocatori = null;
        Giocatore giocatore = null;
        for (int i=0; i<listaGiocatori.MAXPLAYERS; i++){
            quadratoGiocatori = this.giocatori[i];
            giocatore = listaGiocatori.get(i);
            if ( giocatore == null){
                aggiornaQuadratoGiocatori(quadratoGiocatori, "disconnesso", Colore_t.NERO);
            }
            else {
                aggiornaQuadratoGiocatori(quadratoGiocatori, giocatore.getNome(), giocatore.getArmyColour());
            }
        }

        this.pronti[this.indexGiocatore].setEnabled(true);
    }

    private void aggiornaQuadratoGiocatori (Giocatore giocatore, int index){
        this.aggiornaQuadratoGiocatori(this.giocatori[index], giocatore.getNome(), giocatore.getArmyColour());
    }

    private void aggiornaQuadratoGiocatori (QuadratoGiocatori quadratoGiocatori, String nome, Colore_t colore){
        quadratoGiocatori.setNome(nome);
        quadratoGiocatori.setColore(colore);
    }

    public void mandaMessaggioChat () throws IOException {
        //TODO prendi il messaggio dal riquadro apposta
        //TODO resetta il riquadro del messaggio
        //TODO scrivi il messaggio sulla cronologia principale
        scriviServer.writeObject(new MessaggioChat(this.indexGiocatore, "Prova_ciccia"));
        scriviServer.flush();
    }

    private QuadratoGiocatori quadratoInterfacciaLeader(JPanel pannello, int i){
        BottoneGiocatori bottone = new BottoneGiocatori();
        pannello.add(bottone);
        return bottone;
    }

    private QuadratoGiocatori quadratoInterfaccia(JPanel pannello, int i) {
        labelGiocatori label = new labelGiocatori();
        label.setBorder(new TextFieldBorder());
        pannello.add(label);
        return label;
    }

    // <editor-fold defaultstate="collapsed" desc="Window Listener">
    public void windowOpened(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        //try {
        //    new ObjectOutputStream(server.getOutputStream()).writeObject(new MessaggioComandi(comandi_t.DISCONNECT, indexGiocatore));
        //} catch (IOException ex) {
        //    System.err.println("Errore nel mandare il messaggio di \"hang-up\": "+ex);
        //}
        try {
            scriviServer.flush();
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

    // <editor-fold defaultstate="collapsed" desc="Classi e interfacce interne al funzionamento della Sala d'Attesa">
    static class LayoutManagerMatteo implements LayoutManager {

        public LayoutManagerMatteo() {
        }

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
    }// </editor-fold>
}