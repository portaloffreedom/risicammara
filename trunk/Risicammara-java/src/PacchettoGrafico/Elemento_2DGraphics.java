package PacchettoGrafico;

import java.awt.Graphics2D;

/**
 * Elemento che deve essere disegnato con Graphics2D all'interno di un pannello
 * (nella funzione PaintComponent() )
 * 
 * @author matteo
 */
public interface Elemento_2DGraphics {
    /**
     * Funzioni che deve essere chiamata per disegnare
     * l'elemento sul contesto grafico del pannello
     *
     * @param graphics2D contesto grafico su cui disegnare
     * @param graphicsAdvanced colori su cui lavorare
     */
    public void disegna (Graphics2D graphics2D, GraphicsAdvanced graphicsAdvanced);

}
