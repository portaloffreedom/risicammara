package risicammaraClient;

import java.awt.Color;

/**
 * Enumerato per elencare i colori di armate disponibili.
 * @author stengun
 */
public enum Colore_t {
    /** Colore di inizio, ancora non scelto dal giocatore.*/
    NULLO   (Color.WHITE),
    /** Armate BLU */
    BLU     (Color.BLUE),
    /** Armate Rosse*/
    ROSSO   (Color.RED),
    /** Armate Gialle */
    GIALLO  (Color.YELLOW),
    /** Armate Viola */
    VIOLA   (new Color(200, 6, 198)),
    /** Armate Nere */
    NERO    (Color.BLACK),
    /** Armate Verdi */
    VERDE   (Color.GREEN);

    /** Variabile che contiene il valore colore */
    private Color col;
    /** Costruttore dell'enumerato in base al valore assegnatogli */
    Colore_t(Color col){
        this.col = col;
    };
    /** Restituisce il COLORE dell'oggetto enumerato
     * @return l'oggetto Color corrispondente
     */
    public Color getColor(){
        return this.col;
    }

}
