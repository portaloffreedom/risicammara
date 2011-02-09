/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import javax.swing.JPanel;

/**
 *
 * @author matteo
 */
public class AttivatoreGrafica {
    private JPanel pannello;
    private int contatore;

    public AttivatoreGrafica (JPanel pannello){
        this.pannello = pannello;
        this.contatore = 0;
    }

    public boolean ridisegna(){
        return (contatore > 0);
    }

    public synchronized void attiva(){
        contatore++;
        pannello.repaint();
    }

    public void completato (){
        if (contatore <=0) {
            System.err.println("Errore nell'attivatore grafica: impossibile "+
                               "diminuire ancora il contatore");
            return;
        }
        contatore--;
    }

}
