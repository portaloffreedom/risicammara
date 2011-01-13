package risicammaraClient;

import java.awt.Color;

/**
 * Enumerato per elencare i colori di armate disponibili.
 * @author stengun
 */
public enum Colore_t {
    NULLO   (Color.WHITE),
    BLU     (Color.BLUE),
    ROSSO   (Color.RED),
    GIALLO  (Color.YELLOW),
    VIOLA   (Color.PINK),
    NERO    (Color.BLACK),
    VERDE   (Color.GREEN);
    private Color col;
    Colore_t(Color col){
        this.col = col;
    };
    
    public Color getColor(){
        return this.col;
    }

}
