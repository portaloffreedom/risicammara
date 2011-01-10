/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 *
 * @author matteo
 */
public class BarraSuperiore implements Elemento_2DGraphics {

    private Dimension dimensioni;
    protected int altezza;
    protected JPanel pannello;
    protected JButton giocatoreButton;
    protected JButton carteButton;


    public BarraSuperiore(Dimension dimensioni,JPanel pannello, int altezza){
        this.dimensioni=dimensioni;
        this.pannello=pannello;
        this.altezza=altezza;

        //this.giocatoreButton = new JButton("Giocatore");
        //this.giocatoreButton = new JButton(new ImageIcon("./risorse/sfondo_pulsante.png", carteButtonDescription));
        this.giocatoreButton = new BottoneRisicammara("Giocatore", dimensioni);
        pannello.add(this.giocatoreButton);

        //this.carteButton.setText("Carte");
        //this.carteButton = new JButton(new ImageIcon("./risorse/sfondo_pulsante.png", carteButtonDescription));
        this.carteButton = new BottoneRisicammara("Carte", dimensioni);
        pannello.add(this.carteButton);
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
    
    public void addCarteActionListener (ActionListener ascoltatore){
        this.carteButton.addActionListener(ascoltatore);
    }

}
