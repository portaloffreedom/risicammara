/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

//import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * Elemento che deve essere disegnato con Graphics2D all'interno di un pannello
 * (nella funzione PaintComonent() )
 * 
 * @author matteo
 */
interface Elemento_2DGraphics {

    /*
     * Dimensioni del pannello su cui si deve disegnare
     *
    protected Dimension dimensioni;

    /*
     * Costruttore base
     *
     * @param dimensioni passare il riferimento alle dimensioni del pannello
     * (con la funzioni pannello.getSize() )
     *
    protected Elemento_2DGraphics(Dimension dimensioni){
        super();
        this.dimensioni=dimensioni;
    }*/

    /**
     * Funzioni Astratta che deve richiamare essere chiamata per disegnare
     * l'elemento sul Graphics2D
     *
     * @param graphics2D contesto grafico su cui disegnare
     * @param graphicsAdvanced colori su cui lavorare
     */
    public void disegna (Graphics2D graphics2D, GraphicsAdvanced graphicsAdvanced);

}
