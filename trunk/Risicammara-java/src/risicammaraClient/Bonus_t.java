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

    private static BufferedImage cannone = null;
    private static BufferedImage fante = null;
    private static BufferedImage cavallo = null;
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
        if(diversi) return 12;
        return numarm;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }

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
