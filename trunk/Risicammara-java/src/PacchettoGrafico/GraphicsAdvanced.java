/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;

/**
 * Classe che aiuta veicolando altri oggetti e funzioni utili in tutto il
 * contesto grafico.
 * @author matteo
 */
public class GraphicsAdvanced {
    private Color coloreScuro;
    private Color coloreChiaro;
    private Color testo;
    private Color coloreGiocatore;

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
}
