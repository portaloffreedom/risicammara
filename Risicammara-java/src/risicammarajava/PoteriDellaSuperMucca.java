/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.html.HTMLDocument;
import risicammarajava.playerManage.Giocatore;
import risicammarajava.turnManage.Partita;

/**
 *
 * @author matteo
 */
public class PoteriDellaSuperMucca extends JFrame {

    private Partita partita;
    private Giocatore giocatoreSelezionato;

    private JPanel pannello;
    private JComboBox selezioneGiocatore;
    private JSpinner numeroArmate;
    private JTextArea obbiettivo;
    private JComboBox selezioneTerritorio;
    private JTextField coloreArmate;

    public PoteriDellaSuperMucca(Partita partita) {
        super("Poteri della SuperMucca");
        this.partita=partita;

        //**********************************************************************
        //impostazioni della finestra
        this.setVisible(true);
        this.setSize(200, 200);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setAlwaysOnTop(true);

        //**********************************************************************
        //impostazione del pannello
        Container contestoFinestra = this.getContentPane();
        this.pannello = new JPanel();
        contestoFinestra.add(pannello);

        //**********************************************************************
        //impostazioni di pulsanti e robe varie
        
        // ComboBox per la selezione del Giocatore------------------------------
        this.selezioneGiocatore = new JComboBox();
        for (int i=0; i<partita.getListaGiocatori().getSize(); i++){
            selezioneGiocatore.addItem(partita.getListaGiocatori().get(i));
        }
        this.giocatoreSelezionato=partita.getListaGiocatori().get(1);
        pannello.add(selezioneGiocatore);

        // Label con scritto "Numero Armate"------------------------------------
        //pannello.add(new JLabel("Numero Armate"));

        // Spinner per cambiare il numero di armate-----------------------------
        this.numeroArmate = new JSpinner();
        numeroArmate.createToolTip().setTipText("Numero Armate");
        pannello.add(numeroArmate);

        // Testo dell'obbiettivo------------------------------------------------
        this.obbiettivo = new JTextArea();
        obbiettivo.setEditable(false);
        pannello.add(obbiettivo);

        // Selezione Territorio-------------------------------------------------
        this.selezioneTerritorio = new JComboBox();
        pannello.add(selezioneTerritorio);

        /*
        // Spinner per cambiare il numero di armate nel territorio--------------
        JSpinner numeroArmateTerritorio = new JSpinner();
        pannello.add(numeroArmateTerritorio);

        // lista dei territori del Giocatore selezionato------------------------
        JList listaTerrori = new JList();
        pannello.add(listaTerrori);
         */

        // Colore armate del Giocatore selezionato------------------------------
        this.coloreArmate = new JTextField();
        coloreArmate.setEditable(false);
        pannello.add(coloreArmate);

        /*
        // Selezione carte in mano----------------------------------------------
        JComboBox carteInMano = new JComboBox();
        pannello.add(carteInMano);

        // Dati per carta selezionata-------------------------------------------
        JTextField datiCarta = new JTextField("Dati della carta");
        datiCarta.setEditable(false);
        pannello.add(datiCarta);

        // Pulsante dai Carta---------------------------------------------------
        JButton bottoneDaiCarta = new JButton("Dai Carta");
        pannello.add(bottoneDaiCarta);

        // Pulsante togli Carta-------------------------------------------------
        JButton bottoneTogliCarta = new JButton("Tolgi Carta");
        pannello.add(bottoneTogliCarta);
        */

        //**********************************************************************

        cambiaSelezioneGiocatore(giocatoreSelezionato);

        selezioneGiocatore.addItemListener(new ItemListenerImpl(this));
    }

    final void cambiaSelezioneGiocatore(Giocatore  giocatoreSelezionato){
        this.giocatoreSelezionato = giocatoreSelezionato;

        obbiettivo.setText(giocatoreSelezionato.getObbiettivo().toString().substring(0, 20));

        selezioneTerritorio.removeAllItems();
        for (territori_t territorio : giocatoreSelezionato.getListaterr()){
            selezioneTerritorio.addItem(territorio);
        }

        coloreArmate.setText(giocatoreSelezionato.getArmyColour().toString());
        
    }

    private static class ItemListenerImpl implements ItemListener {

        private PoteriDellaSuperMucca mucca;

        public ItemListenerImpl(PoteriDellaSuperMucca mucca) {
            this.mucca = mucca;
        }

        public void itemStateChanged(ItemEvent e) {
            mucca.cambiaSelezioneGiocatore((Giocatore) e.getItem());
        }
    }

}
