/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
    //private JSpinner numeroArmateTerritorio;
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
        Giocatore giocatoreSelezionato=partita.getListaGiocatori().get(0);
        this.giocatoreSelezionato = null; // importante impostarlo in null, o la
                                          // funzione this.giocatoreSelezionato
                                          // non funziona al primo giro.
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
        this.numeroArmateTerritorio = new JSpinner();
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
        //controlla di non chiamare la funzione per niente (cosa che avverrebbe una volta su due)
        if (giocatoreSelezionato == this.giocatoreSelezionato)
            { return; }
        else 
            {this.giocatoreSelezionato = giocatoreSelezionato;}

        //*********************************************************************
        String testoObbiettivo = this.giocatoreSelezionato.getObbiettivo().toString();
        if (testoObbiettivo.length() > 35) {
            testoObbiettivo = testoObbiettivo.substring(0, 35);
        }
        obbiettivo.setText(testoObbiettivo);

        numeroArmate.setValue(this.giocatoreSelezionato.getArmateperturno());

        selezioneTerritorio.removeAllItems();
        for (territori_t territorio : this.giocatoreSelezionato.getListaterr()){
            selezioneTerritorio.addItem(territorio);
        }

        coloreArmate.setText(this.giocatoreSelezionato.getArmyColour().toString());
        
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
