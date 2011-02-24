/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.PannelloGiocoPackage.SottoMenuCarta;
import risicammaraJava.deckManage.Carta;

/**
 *
 * @author matteo
 */
public class CartaDisegnabile extends SottoMenuCarta {
    private Carta carta;

    public CartaDisegnabile(Carta carta) {
        super(carta.tipoCarta());
        this.carta = carta;
    }

    public Carta getCarta() {
        return carta;
    }
    
}
