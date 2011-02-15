/*
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
 
/**
 *
 * @author matteo
 */
public class PlanciaImmagine extends Elemento_2DGraphicsCliccable {
    //private AttivatoreGrafica attivatoreGrafica;
    private BufferedImage planciaBMP;
    private BufferedImage planciaPNG;
    private BufferedImage planciaPNGfinal;

    public PlanciaImmagine(Point posizione) {
        super();
        planciaBMP = loadImage("./risorse/risicammara_plancia.bmp");
        planciaPNG = loadImage("./risorse/risicammara_plancia.png");
        planciaPNGfinal = loadImage("./risorse/risicammara_plancia.png");
        Rectangle rettangolo = new Rectangle(posizione);
        rettangolo.setSize(planciaPNG.getWidth(null), planciaPNG.getHeight(null));
        super.setShape(rettangolo);
    }

    public void disegna(Graphics2D graphics2D, GraphicsAdvanced graphicsAdvanced) {
        Point p = ((Rectangle)posizione).getLocation();
        graphics2D.setColor(Color.cyan.brighter());
        graphics2D.fill(posizione);
        graphics2D.drawImage(planciaPNG,p.x,p.y,null);
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

    public void colora(int idTerritorio, Color colore){
        for (int r=1; r<planciaBMP.getHeight(); r++){
            for (int c=1; c<planciaBMP.getWidth(); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, colore.getRGB());
            }
        }
    }

    public void coloraSfumato(int idTerritorio, Color colore, double trasparenza){
        for (int r=1; r<planciaBMP.getHeight(); r++){
            for (int c=1; c<planciaBMP.getWidth(); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, Sfuma(colore.getRGB(), this.planciaPNGfinal.getRGB(c, r), trasparenza));
            }
        }
    }

    private static int Sfuma(int RGB1, int RGB2, double trasparenza){
        int RGBsfumato = 0;
        int mask = 0x000000ff;
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

    public void ripristinaTerritorio(int idTerritorio){
        for (int r=1; r<planciaBMP.getHeight(); r++){
            for (int c=1; c<planciaBMP.getWidth(); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == idTerritorio)
                    this.planciaPNG.setRGB(c, r, this.planciaPNGfinal.getRGB(c, r));
            }
        }
    }

    public static int GetIdTerritorio(int continente, int territorio){
        int idTerritorio = 0;
        
        idTerritorio = (idTerritorio | (continente >>2*4));
        idTerritorio = (idTerritorio | (territorio >>4*4));

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

    private static void StampaA4Bit(int RGB){
        for (int i=0; i<(4*4); i++){
            System.out.print(((RGB>>(i*4)) & 0x0000000f)+" ");
        }
        System.out.println();
    }

}
