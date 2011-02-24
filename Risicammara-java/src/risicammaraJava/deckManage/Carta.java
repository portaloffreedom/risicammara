package risicammaraJava.deckManage;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Questa interfaccia implementa tutti i metodi necessari alla gestione delle
 * carte. È  l'oggetto principale del mazzo ed è principalmente usato come
 * contenitore di tipo comune per la classe mazzo e per l'invio dei messaggi
 * sui tris e sulla pesca di una carta.
 * @author stengun
 */
public interface Carta extends Serializable{
    /**
     * Indica di che tipo è la carta.
     * @return Stringa che descrive il tipo della carta
     */
    public String tipoCarta();
    
    public BufferedImage getIcona();

}