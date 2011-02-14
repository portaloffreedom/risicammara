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
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import risicammaraClient.Client;
 
/**
 *
 * @author matteo
 */
public class PlanciaImmagine extends Elemento_2DGraphicsCliccable {
    private BufferedImage planciaBMP;
    private BufferedImage planciaPNG;
    private BufferedImage planciaPNGfinal;
    private int RGBselezionato;

    public PlanciaImmagine(Point posizione) {
        super();
        RGBselezionato = 0;
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

    public Dimension getDimension (){
        return ((Rectangle)posizione).getSize();
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

    @Override
    protected void actionPressed(MouseEvent e) {
        super.actionPressed(e);
        Point p = e.getPoint();
        Point offset = ((Rectangle)posizione).getLocation();
        p.translate(-offset.x, -offset.y);
        int RGB = this.planciaBMP.getRGB(p.x, p.y);

        int continenteMask = 0x00000f00;
        int territorioMask = 0x000f0000;
        int continente = ((RGB & continenteMask)>>2*4);
        int territorio = ((RGB & territorioMask)>>4*4);
        if (Client.DEBUG == true) {
            System.out.println("Continente: "+continente);
            System.out.println("Territorio: "+territorio);
        }

        //this.planciaPNG.setRGB(p.x, p.y, Color.WHITE.getRGB());
        for (int r=1; r<planciaBMP.getHeight(); r++){
            for (int c=1; c<planciaBMP.getWidth(); c++){
                int tempRGB = this.planciaBMP.getRGB(c, r);
                if (tempRGB == RGB)
                    this.planciaPNG.setRGB(c, r, Color.WHITE.getRGB());
            }
        }
    }

}
