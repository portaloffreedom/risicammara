/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.playerManage.Giocatore;
import risicammaraClient.Colore_t;

/**
 *
 * @author matteo
 */
public class CartaDisegnabile extends SottoMenuCarta {
    
    private Carta carta;
    private BufferedImage tipo;

    public CartaDisegnabile(Carta carta, int altezzaCarta, int larghezzaCarta, Giocatore meStesso) {
        super(carta.tipoCarta(), altezzaCarta, larghezzaCarta);
        this.carta = carta;
        tipo = carta.getIcona();
        
        //fai diventare l'immagine bianca per il giocatore nero
        if (meStesso.getArmyColour() == Colore_t.NERO)
            InvertiColori(tipo);
    }

    public Carta getCarta() {
        return carta;
    }


    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        super.disegna(g2, ga);
        
        Rectangle contorni = super.getContorni();
        int x = contorni.x+contorni.width-MARGINE-tipo.getWidth();
        int y = contorni.y+(contorni.height-tipo.getHeight())/2;
        
        g2.drawImage(tipo, null, x, y);
        
    }
    
    static private void InvertiColori(BufferedImage image){
        int RGB;
        for (int r=0; r<image.getHeight(); r++) {
            for (int c=0; c<image.getWidth(); c++) {
                RGB = image.getRGB(c, r);
                RGB = InvertiColori(RGB);
                image.setRGB(c, r, RGB);
            }
        }
    }
    
    static private int InvertiColori(int RGB) {
         int Amask = 0x000000ff;    //(Alpha)
         int Rmask = 0x0000ff00;    //(Red)
       //int Gmask = 0x00ff0000;    //(Green)
         int Bmask = 0xff000000;    //(Blue)
         
       int RGBsfumato = ~RGB;
       RGBsfumato = (RGBsfumato & ~Bmask) | (RGB & Bmask);


       return RGBsfumato;
    }
   
}
