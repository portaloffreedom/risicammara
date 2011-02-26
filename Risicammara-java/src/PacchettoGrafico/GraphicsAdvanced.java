/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import risicammaraClient.Colore_t;

/**
 * Classe che aiuta veicolando altri oggetti e funzioni utili in tutto il
 * contesto grafico.
 * @author matteo
 */
public class GraphicsAdvanced {
    static private final double FATTORE_EVIDENZIAZIONE = 0.3;
    private Color coloreScuro;
    private Color coloreChiaro;
    private Color testo;
    private Color coloreGiocatore;
    private Color coloreGiocatoreEvidenziato;

    /**
     * Costruttore
     * @param coloreScuro Colore che diventa la scelta preferita per il colore
     * scuro (soprattutto per gli sfondi). Evitare il colore nero, potrebbe dare
     * dei problemi la confusione con gli alti oggetti.
     * @param coloreChiaro Colore che diventa la scelta preferita per il colore
     * chiaro (soprattutto per gli sfondi).
     * @param coloreTesto Colore del testo quando questo non si voglia stampare 
     * su normale sfondo bianco.
     * @param coloreGiocatore Colore del giocatore che possiede il client attuale.
     * Viene spesso utilizzato come sfondo per diversi pulsanti e menù.
     */
    public GraphicsAdvanced(Color coloreScuro, Color coloreChiaro, Color coloreTesto, Color coloreGiocatore) {
        this.coloreScuro = coloreScuro;
        this.coloreChiaro = coloreChiaro;
        this.testo = coloreTesto;
        this.coloreGiocatore = coloreGiocatore;
        if (coloreGiocatore == Color.black || coloreGiocatore == Color.blue)
            this.coloreGiocatoreEvidenziato = getBrighter(coloreGiocatore);
        else
            this.coloreGiocatoreEvidenziato = coloreGiocatore.darker();
    }

    /**
     * @return Il colore del giocatore.
     */
    public Color getColoreGiocatore() {
        return coloreGiocatore;
    }

    /**
     * @return Colore che diventa la scelta preferita per il colore
     * chiaro (soprattutto per gli sfondi).
     */
    public Color getColoreChiaro() {
        return coloreChiaro;
    }

    /**
     * @return Colore che diventa la scelta preferita per il colore
     * scuro (soprattutto per gli sfondi).
     */
    public Color getColoreScuro() {
        return coloreScuro;
    }

    /**
     * @return Il colore da usare per il testo se si vuole usare nello sfondo il
     * colore di getSfondoTesto().
     * @see getSfondoTesto()
     */
    public Color getTesto() {
        return testo;
    }

    /**
     * @return Il colore da usare se si vuole fare uno sfondo colorato. Il colore
     * è lo stesso del colore del giocatore.
     */
    public Color getSfondoTesto() {
        return coloreGiocatore;
    }

    /**
     * Serve per trovare il colore giusto con cui disegnare le scritte con sfondo
     * colorato.
     * @param colore Colore dello sfondo colorato (fa riferimento ai colori delle
     * armate)
     * @return il colore che dovrebbe essere usato per disegnare testo sullo sfondo
     * colorato di "colore"
     */
    public static Color getTextColor(Colore_t colore){
        if (colore != Colore_t.BLU && colore != Colore_t.NERO)
            return Color.black;
        else 
            return Color.white;
    }


    public Color getColoreGiocatoreEvidenziato() {
        return coloreGiocatoreEvidenziato;
    }

    private static Color getBrighter(Color partenza){
        int r = partenza.getRed();
        int g = partenza.getGreen();
        int b = partenza.getBlue();

        if (r==0)r++;
        if (g==0)g++;
        if (b==0)b++;

        r *= (255*FATTORE_EVIDENZIAZIONE);
        g *= (255*FATTORE_EVIDENZIAZIONE);
        b *= (255*FATTORE_EVIDENZIAZIONE);

        return new Color(Math.min(r, 255),
                         Math.min(g, 255),
                         Math.min(b, 255));
    }
}
