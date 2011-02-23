package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import risicammaraClient.Colore_t;
import risicammaraJava.boardManage.TerritorioNonValido;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.boardManage.TerritorioPlancia;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.turnManage.PartitaClient;
 
/**
 * Classe che gestisce tutta l'immagine della plancia. Questo significa sia la
 * sua colorazione, sia la gestione degli eventi (mouse) sull'immagine.
 * @author matteo
 */
public class PlanciaImmagine extends Elemento_2DGraphicsCliccable {
    private static final int CERCHIO = 30;
    private static final int OFFSET_CORONA = 3;
    //private AttivatoreGrafica attivatoreGrafica;
    private BufferedImage planciaBMP;
    private BufferedImage planciaPNG;
    private BufferedImage planciaPNGfinal;
    private Dimension dimensioniPannello;
    private AttivatoreGrafica ag;

    private PartitaClient partita;
    PlanciaClient plancia;

    public PlanciaImmagine(Point posizione, PartitaClient partita, Dimension dimensioniPannello, AttivatoreGrafica ag) {
        super();
        this.dimensioniPannello = dimensioniPannello;
        this.partita = partita;
        this.plancia = partita.getPlancia();
        this.ag = ag;
        planciaBMP = loadImage("./risorse/risicammara_plancianew.bmp");
        planciaPNG = loadImage("./risorse/risicammara_plancianew.png");
        planciaPNGfinal = loadImage("./risorse/risicammara_plancianew.png");
        Rectangle rettangolo = new Rectangle(posizione);
        rettangolo.setSize(planciaPNG.getWidth(null), planciaPNG.getHeight(null));
        super.setShape(rettangolo);

        
        for (territori_t territorio : territori_t.values()) {
            if (territorio == territori_t.Jolly1 || territorio == territori_t.Jolly2)
                continue;
            int idTerritorio = territorio.getIdTerritorio();
            Rectangle rect = getRectangle(idTerritorio);
            Point p = new Point(rect.x+(rect.width/2), rect.y+(rect.height/2));
            plancia.setBounds(territorio, rect, p);

            //colora ogni territorio (in trasparenza) del colore del proprietario
            //int indexProprietario = plancia.getTerritorio(territorio).getProprietario();
            //coloraSfumato(idTerritorio, partita.getListaGiocatori().get(indexProprietario).getArmyColour().getColor(), 0.7);
        }
    }

    private Rectangle getRectangle(int idTerritorio) {
        int alto = planciaBMP.getHeight();
        int basso = 0;
        int sinistra = planciaBMP.getWidth();
        int destra = 0;
        for (int r=1; r<planciaBMP.getHeight(); r++){
            for (int c=1; c<planciaBMP.getWidth(); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio) {
                    if (alto > r)
                        alto = r;
                    if (basso < r)
                        basso = r;
                    if (sinistra > c)
                        sinistra = c;
                    if (destra < c)
                        destra = c;
                }
            }
        }
        return new Rectangle(sinistra, alto, destra-sinistra+1, basso-alto+1);
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        Rectangle rect = (Rectangle) posizione;
        rect.width = dimensioniPannello.width-rect.x;
        rect.height = dimensioniPannello.height-rect.y;
        g2.setColor(Color.cyan.brighter());
        g2.fill(posizione);
        //g2.drawImage(planciaPNG,p.x,p.y,null);
        g2.drawImage(planciaPNG, rect.x, rect.y, rect.width, rect.height, null);

        FontMetrics font = g2.getFontMetrics();
        for (TerritorioPlancia territorio : plancia.getTabellone()) {
            disegnaSegnaposto(g2, colori, font, (TerritorioPlanciaClient) territorio);
        }
    }

    public void disegnaSegnaposto(Graphics2D g2, GraphicsAdvanced colori, FontMetrics font, TerritorioPlanciaClient territorio){
        Point p = getTransformedPointFromImage(territorio.getPosizioneCerchietto());
        Colore_t coloreProprietario = partita.getListaGiocatori().get(territorio.getProprietario()).getArmyColour();

        //determina la posizione del numero (delle armate) da stampare
        String numeroArmate = ""+territorio.getArmate();
        Rectangle posizioneTesto = new Rectangle();
        posizioneTesto.width = font.stringWidth(numeroArmate);
        posizioneTesto.height = font.getHeight();
        posizioneTesto.x = p.x-(posizioneTesto.width/2);
        posizioneTesto.y = p.y+(posizioneTesto.height/2);
        
        //determina le dimensioni dell'ovale da disegnare
        Rectangle dimensioniOvale = new Rectangle(p.x-(CERCHIO/2), p.y-(CERCHIO/2), CERCHIO, CERCHIO);
        if (posizioneTesto.width > dimensioniOvale.width+OFFSET_CORONA*2) {
            dimensioniOvale.width = posizioneTesto.width+OFFSET_CORONA*2;
            dimensioniOvale.x = p.x-(dimensioniOvale.width/2);
        }
        Rectangle dimensioneOvaleRidotto = new Rectangle(dimensioniOvale.width-OFFSET_CORONA*2, dimensioniOvale.height-OFFSET_CORONA*2);
        dimensioneOvaleRidotto.x = p.x-(dimensioneOvaleRidotto.width/2);
        dimensioneOvaleRidotto.y = p.y-(dimensioneOvaleRidotto.height/2);

        //disegna l'ovale
        g2.setColor(Color.BLACK);
        g2.fillOval(dimensioniOvale.x,dimensioniOvale.y, dimensioniOvale.width, dimensioniOvale.height);
        g2.setColor(coloreProprietario.getColor());
        g2.fillOval(dimensioneOvaleRidotto.x, dimensioneOvaleRidotto.y, dimensioneOvaleRidotto.width, dimensioneOvaleRidotto.height);

        //disegna il testo sopra all'ovale (numero di armate)
        g2.setColor(GraphicsAdvanced.getTextColor(coloreProprietario));
        g2.drawString(numeroArmate, posizioneTesto.x, posizioneTesto.y);
    }

    public Rectangle getRettangolo(){
        return (Rectangle) posizione;
    }

    public Dimension getDimension (){
        return getRettangolo().getSize();
    }

    public Point getPosizione(){
        return getRettangolo().getLocation();
    }

    public int getidTerritorio(Point p){
        return planciaBMP.getRGB(p.x, p.y);
    }

    private BufferedImage loadImage (String pad){
        //return Toolkit.getDefaultToolkit().getImage(getClass().getResource(pad));
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pad));
        } catch (IOException e) {
            System.err.println("Errore nel caricare \""+pad+"\":"+e);
        }
        return img;
    }

    //<editor-fold defaultstate="collapsed" desc="Colora">
    public void colora(TerritorioPlanciaClient territorio, Color colore){
        coloraSfumato(territorio, colore, 1);
    }
    
    public void colora(territori_t territorio, Color colore) {
        coloraSfumato(territorio, colore, 1);
    }
    
    public Rectangle colora(int idTerritorio, Color colore) throws TerritorioNonValido {
        /*
         * Rectangle rettangolo = plancia.getTerritorio(idTerritorio).getPosizione();
         * for (int r=rettangolo.y; r<(rettangolo.height+rettangolo.y); r++){
         * for (int c=rettangolo.x; c<(rettangolo.width+rettangolo.x); c++){
         * int tempRGB = this.planciaBMP.getRGB(c, r);
         * if (tempRGB == idTerritorio)
         * this.planciaPNG.setRGB(c, r, colore.getRGB());
         * }
         * }
         * return rettangolo;
         */
        return coloraSfumato(idTerritorio, colore, 1);
    }
    
    private void colora(int idTerritorio, Rectangle rettangolo, Color colore) {
        coloraSfumato(idTerritorio, rettangolo, colore, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ColoraSfumato"> 

    public void coloraSfumato(TerritorioPlanciaClient territorio, Color colore, double trasparenza) {
        coloraSfumato(territorio.getTerritorio().getIdTerritorio(), territorio.getPosizione(), colore, trasparenza);
        this.aggiornaTerritorio(territorio);
    }

    public void coloraSfumato(territori_t territorio, Color colore, double trasparenza) {
        coloraSfumato(plancia.getTerritorio(territorio), colore, trasparenza);
    }
    
    public Rectangle coloraSfumato(int idTerritorio, Color colore, double trasparenza) throws TerritorioNonValido {
       Rectangle rettangolo = plancia.getTerritorio(idTerritorio).getPosizione();
       coloraSfumato(idTerritorio, rettangolo, colore, trasparenza);
       return rettangolo;
    }
    
    private void coloraSfumato (int idTerritorio, Rectangle rettangolo, Color colore, double trasparenza){  
       for (int r=rettangolo.y; r<(rettangolo.height+rettangolo.y); r++){
           for (int c=rettangolo.x; c<(rettangolo.width+rettangolo.x); c++){
               int tempRGB = this.planciaBMP.getRGB(c, r);
               if (tempRGB == idTerritorio)
                   this.planciaPNG.setRGB(c, r, Sfuma(colore.getRGB(), this.planciaPNGfinal.getRGB(c, r), trasparenza));
           }
       }
    }

    private static int Sfuma(int RGB1, int RGB2, double trasparenza){
       int RGBsfumato = 0;
       int  mask = 0x000000ff;
       //int Rmask = 0x0000ff00;
       //int Gmask = 0x00ff0000;
       //int Bmask = 0xff000000;

       for (int i=0; i<4; i++){
           int tempColor1 = (RGB1 & mask)>>i*8;
           int tempColor2 = (RGB2 & mask)>>i*8;
           tempColor1 = (int) (tempColor1*trasparenza + tempColor2*(1-trasparenza));
           RGBsfumato = RGBsfumato | (tempColor1<<i*8);
           mask = mask<<8;
       }

       return RGBsfumato;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="RipristinaTerritorio">
    /**
     * Ripristina il territorio del colore originale. Inoltre chiama un repaint
     * sul territorio
     * @param territorio territorio da ripristinare.
     */
    public void ripristinaTerritorio(TerritorioPlanciaClient territorio) {
        ripristinaTerritorio(territorio.getTerritorio().getIdTerritorio(), territorio.getPosizione());
        this.aggiornaTerritorio(territorio);
    }
    
    /**
     * Ripristina il territorio del colore originale. Inoltre chiama un repaint
     * sul territorio
     * @param territorio territorio da ripristinare.
     */
    public void ripristinaTerritorio(territori_t territorio) {
        ripristinaTerritorio(plancia.getTerritorio(territorio));
    }
    
    public Rectangle ripristinaTerritorio(int idTerritorio) throws TerritorioNonValido {
        Rectangle rettangolo = plancia.getTerritorio(idTerritorio).getPosizione();
        ripristinaTerritorio(idTerritorio, rettangolo);
        return rettangolo;
    }
    
    private void ripristinaTerritorio(int idTerritorio, Rectangle rettangolo){
        for (int r=rettangolo.y; r<(rettangolo.height+rettangolo.y); r++){
            for (int c=rettangolo.x; c<(rettangolo.width+rettangolo.x); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, this.planciaPNGfinal.getRGB(c, r));
            }
        }
    }
    
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Gestione idTerritorio">
    public static int GetIdTerritorio(int continente, int territorio) {
        int idTerritorio = 0xff000000;

        idTerritorio = (idTerritorio | (continente << (2 * 4)));
        idTerritorio = (idTerritorio | (territorio << (4 * 4)));

        return idTerritorio;
    }

    public static int GetContinente(int idTerritorio) {
        int continenteMask = 0x00000f00;
        return ((idTerritorio & continenteMask) >> 2 * 4);
    }

    public static int GetTerritorio(int idTerritorio) {
        int territorioMask = 0x000f0000;
        return ((idTerritorio & territorioMask) >> 4 * 4);
    }

    public static boolean eTerritorio(int idTerritorio) {
        boolean territorio = false;
        int mask = 0x00f0f0ff;
        idTerritorio = (idTerritorio & mask);
        if ((idTerritorio & mask) == 0) {
            territorio = true;


        }
        return territorio;
    }

    public static void StampaA4Bit(int RGB) {
        for (int i = 0; i < (8); i++) {
            System.out.print(((RGB >> (i * 4)) & 0x0000000f) + " ");
        }
        System.out.println();
    }

    public static void StampaA8Bit(int RGB) {
        for (int i = 0; i < (4); i++) {
            System.out.print(((RGB >> (i * 8)) & 0x000000ff) + " ");
        }
        System.out.println();
    }// </editor-fold>

    @Override
    protected void actionPressed(MouseEvent e) {
        //transforma il punto translandadolo dallo spazio del pannello allo spazio
            //dell'immagine. (traslazione necessaria causa ridimensionamento
            //possibile dell'immagine)
        Point p = getTransformedPointToImage(e.getPoint());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), p.x, p.y, e.getClickCount(), false);
        
        super.actionPressed(e);
    }

    private int getXtranslate(boolean translate){
        if (translate)
            return ((Rectangle) posizione).x;
        else
            return 0;
    }

    private int getYtranslate(boolean translate){
        if (translate)
            return ((Rectangle) posizione).y;
        else
            return 0;
    }

    // pannelloP : immagineP = pannello : immagine
    private int transformXPointFromImage(int x, boolean translate){
        int translation = getXtranslate(translate);
        return (int) (x * ((double) (dimensioniPannello.width-translation) / (double) planciaPNG.getWidth()))+translation;
    }
    private int transformYPointFromImage(int y, boolean translate){
        int traslation = getYtranslate(translate);
        return (int) (y * ((double) (dimensioniPannello.height-traslation) / (double) planciaPNG.getHeight()))+traslation;
    }

    private int transformXPointToImage(int x, boolean translate){
        int translation = getXtranslate(translate);
        return (int) ((x-translation) * ((double) planciaPNG.getWidth() / (double) (dimensioniPannello.width-translation)));
    }
    private int transformYPointToImage(int y, boolean translate){
        int traslation = getYtranslate(translate);
        return (int) ((y-traslation) * ((double) planciaPNG.getHeight() / (double) (dimensioniPannello.height-traslation)));
    }

    private void transformPointFromImage(Point p){
        p.x = transformXPointFromImage(p.x, true);
        p.y = transformYPointFromImage(p.y, true);
    }

    private Point getTransformedPointFromImage(Point p){
        Point b = new Point(p.x, p.y);
        transformPointFromImage(b);
        return b;
    }

    private void transformPointToImage(Point p){
        // immagineP : pannelloP = immagine : pannello
        p.x = transformXPointToImage(p.x, true);
        p.y = transformYPointToImage(p.y, true);
    }

    private Point getTransformedPointToImage(Point p){
        Point b = new Point(p);
        transformPointToImage(b);
        return b;
    }

    private void transformRectangleToImage(Rectangle rect) {
        rect.x      = transformXPointFromImage(rect.x,      true);
        rect.width  = transformXPointFromImage(rect.width,  false);
        rect.y      = transformYPointFromImage(rect.y,      true);
        rect.height = transformYPointFromImage(rect.height, false);
    }

    private void aggiornaTerritorio(territori_t territorio){
        aggiornaTerritorio(plancia.getTerritorio(territorio));
    }

    private void aggiornaTerritorio(int idTerritorio) throws TerritorioNonValido{
        aggiornaTerritorio(plancia.getTerritorio(idTerritorio));
    }

    private void aggiornaTerritorio(TerritorioPlanciaClient territorio){
        repaintPlancia(territorio.getPosizione());
    }

    public void repaintPlancia(Rectangle rettangoloImmagine){
        rettangoloImmagine = new Rectangle(rettangoloImmagine);
        transformRectangleToImage(rettangoloImmagine);

        rettangoloImmagine.width++; //approssimazione per eccesso
        rettangoloImmagine.height++;
        ag.panelRepaint(rettangoloImmagine);
    }
    
    public void aggiornaArmateTerritorio(int armate, TerritorioPlanciaClient territorio){
        territorio.aggiornaArmate(armate);
        aggiornaTerritorio(territorio);
    }

    public void aggiornaArmateTerritorio(int armate, territori_t territorioT){
        this.aggiornaArmateTerritorio(armate, plancia.getTerritorio(territorioT));
    }
    
    public void setArmateTerritorio(int armate, TerritorioPlanciaClient territorio) {
        territorio.setArmate(armate);
        aggiornaTerritorio(territorio);
    }
    
    public void setArmateTerritorio(int armate, territori_t territorioT){
        this.setArmateTerritorio(armate, plancia.getTerritorio(territorioT));
    }
}
