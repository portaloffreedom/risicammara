package PacchettoGrafico.salaAttesa;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
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
    private final static Rectangle nomeGiocatoreR = new Rectangle(prontiR.x+prontiR.width+margine, margine, 120, altezza);
    private final static Rectangle coloreR = new Rectangle(nomeGiocatoreR.x+nomeGiocatoreR.width+margine, margine+10, 120, altezza-20);
    private final static Rectangle confermaR = new Rectangle(coloreR.x+coloreR.width+margine, margine, SalaAttesa.finestraR.width -(coloreR.x+coloreR.width+margine*2) , altezza);
    private final static Rectangle invioR = new Rectangle(SalaAttesa.finestraR.width-(altezza+margine), SalaAttesa.finestraR.height-(30+altezza+margine), altezza, altezza);
    private final static Rectangle immissioneR = new Rectangle(nomeGiocatoreR.x, invioR.y, SalaAttesa.finestraR.width-(giocatoriR.width+prontiR.width+invioR.width+margine*4), altezza);
    private final static Rectangle cronologiaR = new Rectangle(nomeGiocatoreR.x, nomeGiocatoreR.y+altezza+margine*2, immissioneR.width+invioR.width, SalaAttesa.finestraR.height-(35+altezza*2+margine*4));


    private QuadratoGiocatori giocatori[];
    private LabelGiocatori giocatoriLabel[];
    private BottoneGiocatori giocatoriBottoni[];
    private JToggleButton pronti[];
    private JTextField nomeGiocatore;
    private JComboBox colore;
    private JButton conferma;
    private JTextField immissioneChat;
    private JButton invioChat;
    private CronologiaChat konsole;

    private AscoltatoreKickGiocatore ascoltatoreKick;

    /** Indice del Giocatore che sta utilizzando l'attuale Client */
    private int indexGiocatore;
    /** Intera lista dei giocatori */
    private ListaPlayers listaGiocatori;
    /** Accesso alla memoria di SalaAttesa */
    private SalaAttesa salaAttesa;

    public PannelloSalaAttesa(int indexGiocatore, ListaPlayers listaGiocatori, SalaAttesa salaAttesa) {
        //super(new LayoutManagerMatteo());
        super();
        this.indexGiocatore = indexGiocatore;
        this.listaGiocatori = listaGiocatori;
        this.salaAttesa = salaAttesa;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.pronti = new JToggleButton[6];
        this.giocatoriBottoni = new BottoneGiocatori[ListaPlayers.MAXPLAYERS];
        this.giocatoriLabel = new LabelGiocatori[ListaPlayers.MAXPLAYERS];
        this.giocatori = giocatoriLabel;

        disegnaGiocatori();
        personalizza();
    }

    public void diventaLeader() {
        this.ascoltatoreKick = new AscoltatoreKickGiocatore(salaAttesa,this);

        for(int i=0; i<ListaPlayers.MAXPLAYERS; i++) {
            if (this.giocatoriLabel[i].isVisible()) {
                this.giocatoriLabel[i].setVisible(false);
                this.giocatoriBottoni[i].setVisible(true);
                this.giocatoriBottoni[i].setColore(listaGiocatori.get(i).getArmyColour());
                this.giocatoriBottoni[i].setText(listaGiocatori.getNomeByIndex(i));
            }
                this.giocatoriBottoni[i].addActionListener(ascoltatoreKick);
        }

        this.giocatori = this.giocatoriBottoni;
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
        if (!visibile)
            setInfoGiocatore(index, "sconnesso", Colore_t.NULLO, false);
    }

    public void setInfoGiocatore (int index, String nome, Colore_t colore){
        //ATTENZIONE index prende anche valori che puntano a giocatori 'null'
        setNomeGiocatore(index, nome);
        setColoreGiocatore(index, colore);
    }

    public void setInfoGiocatore (int index, String nome, Colore_t colore, boolean pronto){
        //ATTENZIONE index prende anche valori che puntano a giocatori 'null'
        setNomeGiocatore(index, nome);
        setColoreGiocatore(index, colore);
        setPronto(index, pronto);
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

    public void invertiPronto(int indexGiocatore){
        //pronti[indexGiocatore].setSelected(!pronti[indexGiocatore].isSelected());
        setPronto(indexGiocatore, !pronto(indexGiocatore));
    }

    public void setPronto(int indexGiocatore, boolean pronto){
        pronti[indexGiocatore].setSelected(pronto);
    }

    public boolean pronto(int indexGiocatore) {
        return pronti[indexGiocatore].isSelected();
    }

    ///////////////PARTE PRIVATA/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void disegnaGiocatori (){

        JPanel giocatoriPanel = new JPanel(new GridBagLayout());
        this.add(giocatoriPanel);
        {/* giocatoriPanel */
            GridBagConstraints comportamentoGiocatori = new GridBagConstraints();
            comportamentoGiocatori.fill = GridBagConstraints.HORIZONTAL;
            comportamentoGiocatori.weighty = 0;
            comportamentoGiocatori.weightx = 1;
            comportamentoGiocatori.insets = new Insets(3, 5, 3, 0);
            comportamentoGiocatori.gridy = 1;
            GridBagConstraints comportamentoPronti = (GridBagConstraints) comportamentoGiocatori.clone();
            comportamentoPronti.weightx = 0;
            comportamentoPronti.insets = new Insets(3, 0, 3, 5);
            comportamentoPronti.gridy = 2;

            GridBagConstraints comportamento = new GridBagConstraints();
            comportamento.gridy = ListaPlayers.MAXPLAYERS+1;
            comportamento.weighty = 1;
            giocatoriPanel.add(Box.createRigidArea(new Dimension(0, 0)), comportamento);

            for (int i=0; i<ListaPlayers.MAXPLAYERS; i++) {
                comportamentoGiocatori.gridy = i;
                comportamentoPronti.gridy = i;

                this.giocatoriBottoni[i] = new BottoneGiocatori(i);
                this.giocatoriBottoni[i].setVisible(false);
                this.giocatoriBottoni[i].setPreferredSize(giocatoriR.getSize());
                giocatoriPanel.add(this.giocatoriBottoni[i], comportamentoGiocatori);
                this.giocatoriBottoni[i].setAlignmentY(Component.TOP_ALIGNMENT);

                this.giocatoriLabel[i] = new LabelGiocatori();
                this.giocatoriLabel[i].setVisible(true);
                this.giocatoriLabel[i].setPreferredSize(giocatoriR.getSize());
                giocatoriPanel.add(this.giocatoriLabel[i], comportamentoGiocatori);
                this.giocatoriLabel[i].setAlignmentY(Component.TOP_ALIGNMENT);

                this.pronti[i] = new JToggleButton("Ω");
                this.pronti[i].setEnabled(false);
                this.pronti[i].setPreferredSize(prontiR.getSize());
                giocatoriPanel.add(this.pronti[i], comportamentoPronti);
                this.pronti[i].setAlignmentY(Component.TOP_ALIGNMENT);
            }
        }/* giocatoriPanel */

        JPanel destraPanel = new JPanel();
        destraPanel.setLayout(new BoxLayout(destraPanel, BoxLayout.Y_AXIS));
        this.add(destraPanel);
        {/* destraPanel */
            //pannello della barra in alto che contiene le impostazioni per cambiare colore e nome
            JPanel impostazioniPanel = new JPanel();
            impostazioniPanel.setLayout(new BoxLayout(impostazioniPanel, BoxLayout.X_AXIS));
            destraPanel.add(impostazioniPanel);
            {/* impostazioniPanel */
                this.nomeGiocatore = new JTextField();
                this.setPreferredSize(nomeGiocatoreR.getSize());
                impostazioniPanel.add(this.nomeGiocatore);

                this.colore = new JComboBox(Colore_t.values());
                this.colore.setPreferredSize(coloreR.getSize());
                impostazioniPanel.add(this.colore);

                this.conferma = new JButton("conferma");
                this.conferma.setPreferredSize(coloreR.getSize());
                impostazioniPanel.add(this.conferma);
                this.conferma.addActionListener(new AscoltatoreCambiaNomeColore(salaAttesa, this));//new CambiaNomeColore();
            }/* impostazioniPanel */

            this.konsole = new CronologiaChat(20);
            JScrollPane konsoleScorrimento = konsole.inscatolaInScrollPane(cronologiaR);
            destraPanel.add(konsoleScorrimento);

            
            JPanel immissioneChatPanel = new JPanel();
            immissioneChatPanel.setLayout(new BoxLayout(immissioneChatPanel, BoxLayout.X_AXIS));
            destraPanel.add(immissioneChatPanel);
            {/* immissioneChatPanel */
                this.immissioneChat = new JTextField();
                this.immissioneChat.setPreferredSize(immissioneR.getSize());
                ActionListener mandaChat = new AscoltatoreMandaChat(salaAttesa, this);
                this.immissioneChat.addActionListener(mandaChat);
                immissioneChatPanel.add(immissioneChat);

                this.invioChat = new JButton("Invia");
                this.invioChat.setPreferredSize(invioR.getSize());
                this.invioChat.addActionListener(mandaChat);
                immissioneChatPanel.add(invioChat);
            }/* immissioneChatPanel */
        }/* destraPanel */

    }


    private void personalizza() {

        Giocatore giocatore = null;
        for (int i=0; i<ListaPlayers.MAXPLAYERS; i++){
            giocatore = listaGiocatori.get(i);
            if (giocatore == null){
                giocatoreVisible(i, false);
            }
            else {
                setInfoGiocatore(i, giocatore.getNome(), giocatore.getArmyColour());
                setPronto(i, giocatore.isReady());
            }
        }

        this.pronti[this.indexGiocatore].setEnabled(true);
        this.pronti[this.indexGiocatore].addActionListener(new AscoltatorePronti(salaAttesa, this));
        this.nomeGiocatore.setText(this.listaGiocatori.getNomeByIndex(this.indexGiocatore));
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

    public void eliminaGiocatore (int index) {
        this.giocatori[index].setVisible(false);
        this.pronti[index].setVisible(false);
    }

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