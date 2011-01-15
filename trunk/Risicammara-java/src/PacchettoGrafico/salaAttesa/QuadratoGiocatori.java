/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import risicammaraClient.Colore_t;

/**
 *
 * @author matteo
 */
public interface QuadratoGiocatori {

        public void setNome(String testo);

        public void setColore(Colore_t colore);

        public void setVisible(boolean visible);

        public void setBounds(int x, int y, int width, int height);
}
