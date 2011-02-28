package risicammaraClient;

import java.awt.image.BufferedImage;

/**
 * Enumerato che rappresenta tutti i bonus
 * @author stengun
 */
 public enum Bonus_t {
    /** Bonus cannone (tre cannoni uguali = 4 armate in più) */
    CANNONE (4, "/Images/cannone.png"),
    /** Bonus fanti (tre fanti = 6 armate in più)*/
    FANTE   (6, "/Images/fante.png"),
    /** Bonus cavallo (tre cavalli = 8 armate in più)*/
    CAVALLO (8, "/Images/cavallo.png"),
    /** Bonus Jolly. Un jolly e due carte uguale 12 armate in più*/
    JOLLY   (12, "/Images/jolly.png"); //TODO immagine per il jolly

    private int numarm;
    private String percorsoImmagine;
/** Riferimento all'immagine assegnata al bonus Cannone */
    private static BufferedImage cannone = null;
    /** Riferimento all'immagine assegnata al bonus fante */
    private static BufferedImage fante = null;
    /** Riferimento all'immagine assegnata al bonus Cavallo */
    private static BufferedImage cavallo = null;
/** Riferimento all'immagine assegnata al bonus Jolly */
    private static BufferedImage jolly = null;

    /** Inizializza il valore dell'enumerato in base al valore assegnato*/
    Bonus_t(int numarm, String percorsoImmagine){
        this.numarm=numarm;
        this.percorsoImmagine = percorsoImmagine;
    };
    /** Restituisce la quantità di armate del tris (di tipo uguale)
     * @param diversi Se le tre carte sono diverse o se sono uguali
     * @return Il valore del bonus corrispondente (territori esclusi)
     */
    public int TrisValue(boolean diversi){
        if(diversi) return 10;
        return numarm;
    }
/**
 * Restituisce il percorso dell'immagine assegnata al bonus.
 * @return la stringa che rappresenta il percorso dell'immagine.
 */
    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }
/**
 * Richiede l'immagine del bonus.
 * @return il riferimento all'immagine del bonus che questo oggetto rappresenta.
 */
    public BufferedImage getImmagine() {
        if (this == CANNONE)
            return cannone;
        if (this == FANTE)
            return fante;
        if (this == CAVALLO)
            return cavallo;
        if (this == JOLLY)
            return jolly;

        return null;
    }
/**
 * Carica l'immagine del bonus.
 * @param invertiColori Impostare a true se l'immagine deve avere i colori invertiti.
 */
    static public void caricaImmagini(boolean invertiColori) {
        cannone = Client.loadImage(CANNONE, CANNONE.percorsoImmagine);
        fante = Client.loadImage(FANTE, FANTE.percorsoImmagine);
        cavallo = Client.loadImage(CAVALLO, CAVALLO.percorsoImmagine);
        jolly = Client.loadImage(JOLLY, JOLLY.percorsoImmagine);

        if (invertiColori) {
            InvertiColori(cannone);
            InvertiColori(fante);
            InvertiColori(cavallo);
            InvertiColori(jolly);
        }
    }
/**
 * Inverte i colori di una immagine.
 * @param image L'immagine della quale invertire i colori.
 */
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
/**
 * Inverte i colori RGB di una immagine.
 * @param RGB l'intero che rappresenta gli RGB
 * @return L'intero RGB invertito
 */
    static private int InvertiColori(int RGB) {
//         int Amask = 0x000000ff;    //(Alpha)
//         int Rmask = 0x0000ff00;    //(Red)
       //int Gmask = 0x00ff0000;    //(Green)
         int Bmask = 0xff000000;    //(Blue)

       int RGBsfumato = ~RGB;
       RGBsfumato = (RGBsfumato & ~Bmask) | (RGB & Bmask);


       return RGBsfumato;
    }
 }
