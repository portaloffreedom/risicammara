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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
import javax.swing.text.BadLocationException;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;

/**
 *
 * @author matteo
 */
public class PannelloSalaAttesa extends JPanel {

    // Rettangoli e variabili necessarie per disegnare pulsanti e roba sul pannello
    private final static int margine = 5;
    private final static int altezza = 40;
    private final static Rectangle giocatoriR = new Rectangle(margine, -1, 120, altezza);
    private final static Rectangle prontiR = new Rectangle(giocatoriR.x+giocatoriR.width, -1, altezza, altezza);
    private final static Rectangle nomeGiocatoreR = new Rectangle(prontiR.x+prontiR.width+margine, margine, 220, altezza);
    private final static Rectangle coloreR = new Rectangle(nomeGiocatoreR.x+nomeGiocatoreR.width+margine, margine+10, 80, altezza-20);
    private final static Rectangle confermaR = new Rectangle(coloreR.x+coloreR.width+margine, margine, SalaAttesa.finestraR.width -(coloreR.x+coloreR.width+margine*2) , altezza);
    private final static Rectangle invioR = new Rectangle(SalaAttesa.finestraR.width-(altezza+margine), SalaAttesa.finestraR.height-(30+altezza+margine), altezza, altezza);
    private final static Rectangle immissioneR = new Rectangle(nomeGiocatoreR.x, invioR.y, SalaAttesa.finestraR.width-(giocatoriR.width+prontiR.width+invioR.width+margine*4), altezza);
    private final static Rectangle cronologiaR = new Rectangle(nomeGiocatoreR.x, nomeGiocatoreR.y+altezza+margine*2, immissioneR.width+invioR.width, SalaAttesa.finestraR.height-(35+altezza*2+margine*4));


    private QuadratoGiocatori giocatori[];
    private JToggleButton pronti[];
    private JTextField nomeGiocatore;
    private JComboBox colore;
    private JButton conferma;
    private JTextField immissioneChat;
    private JButton invioChat;
    private CronologiaChat konsole;

    /** Indice del Giocatore che sta utilizzando l'attuale Client */
    private int indexGiocatore;
    /** Intera lista dei giocatori */
    private ListaPlayers listaGiocatori;
    /** Accesso alla memoria di SalaAttesa */
    private SalaAttesa salaAttesa;

    public PannelloSalaAttesa(int indexGiocatore, ListaPlayers listaGiocatori, SalaAttesa salaAttesa) {
        super(new LayoutManagerMatteo());
        this.indexGiocatore = indexGiocatore;
        this.listaGiocatori = listaGiocatori;
        this.salaAttesa = salaAttesa;
        
        
        this.giocatori= new QuadratoGiocatori[6];
        this.pronti = new JToggleButton[6];

        disegnaGiocatori();
        personalizza();
    }

    public void diventaLeader() {
        for(int i=0; i<ListaPlayers.MAXPLAYERS; i++) {
            //this.giocatori[i] = quadratoInterfacciaLeader(pannello, i);
            this.giocatori[i].setBounds(giocatoriR.x, margine+(margine+altezza)*i, giocatoriR.width, giocatoriR.height);
        }
    }

    public String nomeGiocatore_getText(){
        return this.nomeGiocatore.getText();
    }

    public Colore_t colore_getSelectedItem(){
        return (Colore_t) this.colore.getSelectedItem();
    }

    public void stampaMessaggioErrore(String messaggio, Exception ex) {
        konsole.stampaMessaggioErrore(messaggio, ex);
    }

    public void stampaMessaggioComando(String messaggio) {
        konsole.stampaMessaggioComando(messaggio);
    }

    public void stampaMessaggio(String messaggio) {
        konsole.stampaMessaggio(messaggio);
    }

    public void giocatoreVisible (int index, boolean visibile){
        this.giocatori[index].setVisible(visibile);
        this.pronti[index].setVisible(visibile);
    }

    public void setInfoGiocatore (int index, String nome, Colore_t colore){
        //ATTENZIONE index prende anche valori che puntano a giocatori 'null'
        setNomeGiocatore(index, nome);
        setColoreGiocatore(index, colore);
    }

    public String immissioneChat_getText(){
        return immissioneChat.getText();
    }

    public boolean immissioneChat_requestFocus() {
        return immissioneChat.requestFocusInWindow();
    }

    public void immissioneChat_resetText() {
        immissioneChat.setText("");
    }

    //#############PARTE PRIVATA#############################################################################################################
     private void disegnaGiocatori() {
        for (int i=0; i<ListaPlayers.MAXPLAYERS; i++) {

            this.pronti[i] = new JToggleButton("Ω");
            this.add(this.pronti[i]);

            //if (this.leader)
            //        this.giocatori[i] = quadratoInterfacciaLeader(pannello, i);
            //else
                    this.giocatori[i] = quadratoInterfaccia(this, i);


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
        this.conferma.addActionListener(new CambiaNomeColore());

        this.immissioneChat = new JTextField();
        this.immissioneChat.setBounds(immissioneR);
        ActionListener mandaChat = new MandaChat();
        this.immissioneChat.addActionListener(mandaChat);

        this.invioChat = new JButton("Invia");
        this.invioChat.setBounds(invioR);
        this.invioChat.addActionListener(mandaChat);

        this.konsole = new CronologiaChat(20);
        JScrollPane konsoleScorrimento = konsole.inscatolaInScrollPane(cronologiaR);

        this.add(this.nomeGiocatore);
        this.add(this.colore);
        this.add(this.conferma);
        this.add(this.immissioneChat);
        this.add(this.invioChat);
        //pannello.add(this.konsole);
        this.add(konsoleScorrimento);

    }


    private void personalizza() {

        Giocatore giocatore = null;
        for (int i=0; i<ListaPlayers.MAXPLAYERS; i++){
            giocatore = listaGiocatori.get(i);
            if ( giocatore == null){
                giocatoreVisible(i, false);
            }
            else {
                setInfoGiocatore(i, giocatore.getNome(), giocatore.getArmyColour());
            }
        }

        this.pronti[this.indexGiocatore].setEnabled(true);
        this.nomeGiocatore.setText(this.listaGiocatori.getNomeByIndex(this.indexGiocatore));
    }

    private QuadratoGiocatori quadratoInterfacciaLeader(int i){
        BottoneGiocatori bottone = new BottoneGiocatori();
        this.add(bottone);
        return bottone;
    }

    private QuadratoGiocatori quadratoInterfaccia(JPanel pannello, int i) {
        labelGiocatori label = new labelGiocatori();
        label.setBorder(new TextFieldBorder());
        this.add(label);
        return label;
    }

    private void setNomeGiocatore(int index, String nuovoNome){
        Giocatore giocatore = this.listaGiocatori.get(index);

        //memoria
        if (giocatore != null) {
            giocatore.setNome(nuovoNome);
        }

        //grafica
        this.giocatori[index].setNome(nuovoNome);
    }

    private void setColoreGiocatore(int index, Colore_t nuovoColore){
        Giocatore giocatore = this.listaGiocatori.get(index);
        Colore_t vecchioColore = Colore_t.NULLO;

        //memoria
        if (giocatore != null ) {
            vecchioColore = giocatore.getArmyColour();
            giocatore.setArmyColour(nuovoColore);
        }

        //grafica
        if (index != indexGiocatore) {
            if (!(vecchioColore.equals(Colore_t.NULLO)) && !nuovoColore.equals(vecchioColore))
                    this.colore.addItem(vecchioColore);
            if (!(nuovoColore.equals(Colore_t.NULLO)))
                    this.colore.removeItem(nuovoColore);
        }

        this.giocatori[index].setColore(nuovoColore);
    }


    // <editor-fold defaultstate="collapsed" desc="Ascoltatori dei pulsanti">
    private class MandaChat implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            salaAttesa.mandaMessaggioChat();
        }
    }

    private class CambiaNomeColore implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            salaAttesa.aggiornaNomeColore();
        }
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
