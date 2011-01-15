/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import javax.swing.JTextArea;

/**
 *
 * @author matteo
 */
public class CronologiaChat extends JTextArea {

    public void stampaMessaggio(String messaggio) {
        append(messaggio);
        append("\n");
        repaint();
    }

}
