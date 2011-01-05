/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author matteo
 */
public class PoteriDellaSuperMucca extends JFrame {

    public PoteriDellaSuperMucca() {
        super("Poteri della SuperMucca");

        //**********************************************************************
        //impostazioni della finestra
        this.setVisible(true);
        this.setSize(200, 200);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        //**********************************************************************
        //impostazione del pannello
        Container contestoFinestra = this.getContentPane();
        JPanel pannello = new JPanel();
        contestoFinestra.add(pannello);

        //**********************************************************************
        //impostazioni di pulsanti e robe varie
        
        // ComboBox per la selezione del Giocatore
        JComboBox selezioneGiocatore = new JComboBox();
        pannello.add(selezioneGiocatore);

        // Label con scritto "Numero Armate"
        pannello.add(new JLabel("Numero Armate"));

        // Spinner per cambiare il numero di armate
        JSpinner numeroArmate = new JSpinner();
        pannello.add(numeroArmate);

        // Testo dell'obbiettivo
        JTextField obbiettivo = new JTextField("PROOOOOOOVAAAAA!!!!\n a capo");
        obbiettivo.setEditable(false);
        pannello.add(obbiettivo);

        // Selezione Territorio
        JComboBox selezioneTerritorio = new JComboBox();
        pannello.add(selezioneTerritorio);

        // Spinner per cambiare il numero di armate nel territorio
        JSpinner numeroArmateTerritorio = new JSpinner();
        pannello.add(numeroArmateTerritorio);

        // lista dei territori del Giocatore selezionato
        JList listaTerrori = new JList();
        pannello.add(listaTerrori);

        // Colore armate del Giocatore selezionato
        JTextField coloreArmate = new JTextField("Colore");
        coloreArmate.setEditable(false);
        pannello.add(coloreArmate);

        // Selezione carte in mano
        JComboBox carteInMano = new JComboBox();
        pannello.add(carteInMano);

        // Dati per carta selezionata
        JTextField datiCarta = new JTextField("Dati della carta");
        datiCarta.setEditable(false);
        pannello.add(datiCarta);

        // Pulsante dai Carta
        JButton bottoneDaiCarta = new JButton("Dai Carta");
        pannello.add(bottoneDaiCarta);

        // Pulsante togli Carta
        JButton bottoneTogliCarta = new JButton("Tolgi Carta");
        pannello.add(bottoneTogliCarta);

        //**********************************************************************
    }

}