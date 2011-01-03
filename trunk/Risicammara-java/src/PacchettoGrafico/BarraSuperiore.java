/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 *
 * @author matteo
 */
public class BarraSuperiore extends Elemento_2DGraphics {
    
    protected int altezza;
    protected JPanel pannello;
    protected JButton giocatoreButton;
    protected JButton carteButton;
    protected JButton addplayerButton;
    protected JButton prossimoButton;


    public BarraSuperiore(Dimension dimensioni,JPanel pannello, int altezza){
        super(dimensioni);
        this.pannello=pannello;
        this.altezza=altezza;

        this.giocatoreButton = new JButton("Giocatore");
        pannello.add(this.giocatoreButton);

        this.addplayerButton = new JButton("Aggiungi Giocatore");
        pannello.add(this.addplayerButton);
        
        this.carteButton = new JButton("Carte");
        pannello.add(this.carteButton);

        this.prossimoButton = new JButton ("prossimo");
        pannello.add(this.prossimoButton);
    }

    @Override
    public void disegna(Graphics2D graphics2D) {
        graphics2D.fillRect(0, 0, dimensioni.width, altezza);
        this.giocatoreButton.setBounds(5, 5, 100, 50);
        this.carteButton.setBounds((dimensioni.width-5-100), 5, 100, 50);
    }

    public void addGiocatoreActionListener (ActionListener ascoltatore){
        this.giocatoreButton.addActionListener(ascoltatore);
    }

    public void addProssimoMouseListener (ActionListener ascoltatore){
        this.prossimoButton.addActionListener(ascoltatore);
    }

    public void addNewPlayerActionListener (ActionListener ascoltatore){
        this.addplayerButton.addActionListener(ascoltatore);
    }
    
    public void addCarteActionListener (ActionListener ascoltatore){
        this.carteButton.addActionListener(ascoltatore);
    }

}
