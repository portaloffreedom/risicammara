/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import risicammaraClient.territori_t;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.playerManage.Giocatore;

/**
 * SottoMenù che disegna veramente una carta con sue caratteristiche e la sua icona.
 * @author matteo
 */
public class CartaDisegnabile extends SottoMenuCarta {
    
    private Carta carta;
    private BufferedImage tipo;

    /**
     * Costruttore.
     * @param carta carta da visualizzare.
     * @param altezzaCarta altezza che deve avere il sottomenù.
     * @param larghezzaCarta larghezza che deve avere il sottomenù.
     * @param meStesso riferimento al giocatore proprietario del client.
     */
    public CartaDisegnabile(Carta carta, int altezzaCarta, int larghezzaCarta, Giocatore meStesso) {
        super(carta.tipoCarta(), altezzaCarta, larghezzaCarta);
        this.carta = carta;
        tipo = ((territori_t)carta).getBonus().getImmagine();
    }
    
    @Override
    public territori_t getCarta() {
        return (territori_t) carta;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        super.disegna(g2, ga);
        
        Rectangle contorni = super.getContorni();
        int x = contorni.x+contorni.width-MARGINE-tipo.getWidth();
        int y = contorni.y+(contorni.height-tipo.getHeight())/2;
        
        g2.drawImage(tipo, null, x, y);   
    }   
}
