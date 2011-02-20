/*
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import risicammaraClient.Colore_t;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.boardManage.TerritorioPlancia;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.turnManage.PartitaClient;
 
/**
 *
 * @author matteo
 */
public class PlanciaImmagine extends Elemento_2DGraphicsCliccable {
    private static final int CERCHIO = 30;
    private static final int OFFSET_CORONA = 3;
    //private AttivatoreGrafica attivatoreGrafica;
    private BufferedImage planciaBMP;
    private BufferedImage planciaPNG;
    private BufferedImage planciaPNGfinal;
    private PartitaClient partita;
    private PlanciaClient plancia;

    public PlanciaImmagine(Point posizione, PartitaClient partita) {
        super();
        this.partita = partita;
        this.plancia = partita.getPlancia();
        planciaBMP = loadImage("./risorse/risicammara_plancia.bmp");
        planciaPNG = loadImage("./risorse/risicammara_plancia.png");
        planciaPNGfinal = loadImage("./risorse/risicammara_plancia.png");
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

    public void disegna(Graphics2D g2, GraphicsAdvanced colori) {
        Point p = ((Rectangle)posizione).getLocation();
        g2.setColor(Color.cyan.brighter());
        g2.fill(posizione);
        g2.drawImage(planciaPNG,p.x,p.y,null);

        FontMetrics font = g2.getFontMetrics();
        for (TerritorioPlancia territorio : plancia.getTabellone()) {
            disegnaSegnaposto(g2, colori, font, (TerritorioPlanciaClient) territorio);
        }
    }

    public void disegnaSegnaposto(Graphics2D g2, GraphicsAdvanced colori, FontMetrics font, TerritorioPlanciaClient territorio){
        Point p = territorio.getPosizioneCerchietto();
        Colore_t coloreProprietario = partita.getListaGiocatori().get(territorio.getProprietario()).getArmyColour();

        //determina la posizione del numero (delle armate) da stampare
        String numeroArmate = ""+territorio.getArmate();
        Rectangle posizioneTesto = new Rectangle();
        posizioneTesto.width = font.stringWidth(numeroArmate);
        posizioneTesto.height = font.getHeight();
        posizioneTesto.x = p.x-(posizioneTesto.width/2);
        posizioneTesto.y = p.y+(posizioneTesto.height/2)+PannelloGioco.ALTEZZAPANNELLO;
        
        //determina le dimensioni dell'ovale da disegnare
        Rectangle dimensioniOvale = new Rectangle(p.x-(CERCHIO/2), p.y-(CERCHIO/2)+PannelloGioco.ALTEZZAPANNELLO, CERCHIO, CERCHIO);
        if (posizioneTesto.width > dimensioniOvale.width+OFFSET_CORONA*2) {
            dimensioniOvale.width = posizioneTesto.width+OFFSET_CORONA*2;
            dimensioniOvale.x = p.x-(dimensioniOvale.width/2);
        }
        Rectangle dimensioneOvaleRidotto = new Rectangle(dimensioniOvale.width-OFFSET_CORONA*2, dimensioniOvale.height-OFFSET_CORONA*2);
        dimensioneOvaleRidotto.x = p.x-(dimensioneOvaleRidotto.width/2);
        dimensioneOvaleRidotto.y = p.y-(dimensioneOvaleRidotto.height/2)+PannelloGioco.ALTEZZAPANNELLO;

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

    final public Rectangle colora(int idTerritorio, Color colore){
        Rectangle rettangolo = plancia.getTerritorio(idTerritorio).getPosizione();
        for (int r=rettangolo.y; r<(rettangolo.height+rettangolo.y); r++){
            for (int c=rettangolo.x; c<(rettangolo.width+rettangolo.x); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, colore.getRGB());
            }
        }
        return rettangolo;
    }

    final public Rectangle coloraSfumato(int idTerritorio, Color colore, double trasparenza){
        Rectangle rettangolo = plancia.getTerritorio(idTerritorio).getPosizione();
        for (int r=rettangolo.y; r<(rettangolo.height+rettangolo.y); r++){
            for (int c=rettangolo.x; c<(rettangolo.width+rettangolo.x); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, Sfuma(colore.getRGB(), this.planciaPNGfinal.getRGB(c, r), trasparenza));
            }
        }
        return rettangolo;
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

    public Rectangle ripristinaTerritorio(int idTerritorio){
        Rectangle rettangolo = plancia.getTerritorio(idTerritorio).getPosizione();
        for (int r=rettangolo.y; r<(rettangolo.height+rettangolo.y); r++){
            for (int c=rettangolo.x; c<(rettangolo.width+rettangolo.x); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, this.planciaPNGfinal.getRGB(c, r));
            }
        }
        return rettangolo;
    }

    public static int GetIdTerritorio(int continente, int territorio){
        int idTerritorio = 0xff000000;
        
        idTerritorio = (idTerritorio | (continente <<(2*4)));
        idTerritorio = (idTerritorio | (territorio <<(4*4)));

        return idTerritorio;
    }

    public static int GetContinente (int idTerritorio){
        int continenteMask = 0x00000f00;
        return ((idTerritorio & continenteMask)>>2*4);
    }

    public static int GetTerritorio (int idTerritorio){
        int territorioMask = 0x000f0000;
        return ((idTerritorio & territorioMask)>>4*4);
    }

    public static boolean eTerritorio(int idTerritorio){
        boolean territorio = false;
        int mask = 0x00f0f0ff;
        idTerritorio = (idTerritorio & mask);
        if( (idTerritorio & mask) == 0)
            territorio = true;

        return territorio;
    }

    public static void StampaA4Bit(int RGB){
        for (int i=0; i<(8); i++){
            System.out.print(((RGB>>(i*4)) & 0x0000000f)+" ");
        }
        System.out.println();
    }

    public static void StampaA8Bit(int RGB){
        for (int i=0; i<(4); i++){
            System.out.print(((RGB>>(i*8)) & 0x000000ff)+" ");
        }
        System.out.println();
    }

}
