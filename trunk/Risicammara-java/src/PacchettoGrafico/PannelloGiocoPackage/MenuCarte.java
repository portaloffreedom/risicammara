/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import PacchettoGrafico.GraphicsAdvanced;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import risicammaraClient.Bonus_t;
import risicammaraClient.Colore_t;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.turnManage.PartitaClient;

/**
 *
 * @author matteo
 */
public class MenuCarte extends Elemento_2DGraphicsCliccable implements RisicammaraEventListener {
    static public int LARGHEZZA_CARTA = 200;
    static public int ALTEZZA_CARTA = 50;

    private SottoMenuCarta richiestaSelezioneCarte;
    private ArrayList<CartaDisegnabile> listaCarteDisegnabili;

    private boolean aperto;
    private boolean faseRinforzo;
    private Dimension dimensioniPannello;
    private AttivatoreGrafica ag;
    private int distanzaLatoSinistro;
    private int distanzaLatoSuperiore;
    private PartitaClient partita;

    /**
     *
     * @param dimePanel riferimento alle dimensioni del pannelloGioco intero
     * @param ag riferimento all'attivatore grafica (per attivare ridisegnamenti,
     * e quindi anche possibili animazioni)
     * @param altezza Quanto spazio dall'alto prima di disegnare il pannello.
     * @param distanzaLatoSinistro Quanto spazio da sinistra prima di disegnare il pannello.
     */
    public MenuCarte(Dimension dimePanel, AttivatoreGrafica ag, int altezza, int distanzaLatoSinistro, PartitaClient partita) {
        this.dimensioniPannello = dimePanel;
        this.distanzaLatoSinistro = distanzaLatoSinistro;
        this.distanzaLatoSuperiore = altezza;
        this.partita = partita;
        this.ag = ag;

        this.richiestaSelezioneCarte = new SottoMenuCarta("Gioca un TRIS", ALTEZZA_CARTA, LARGHEZZA_CARTA); //TODO fallo disegnare al centro il testo
        this.listaCarteDisegnabili = new ArrayList<CartaDisegnabile>();

        this.aperto = false;
        this.faseRinforzo = false;

        super.posizione = new Rectangle(dimePanel.width-distanzaLatoSinistro-LARGHEZZA_CARTA, altezza, LARGHEZZA_CARTA, 0);


        //carica in memoria le immagini dei bonus
        boolean invertiColori = false;
        if (partita.getMeStesso().getArmyColour() == Colore_t.NERO)
            invertiColori = true;
        
        Bonus_t.caricaImmagini(invertiColori);
    }

    public void setAperto(boolean aperto){
        this.aperto = aperto;
        
        Rectangle rect = getRectangle(); //rettangolo originale - serve pure per catturare le azioni del mouse

        if (aperto) {
            //ricava le sottodimensioni
            riposiziona();
            int numeroSottoMenu = listaCarteDisegnabili.size();
            if (faseRinforzo)
                numeroSottoMenu++;
            rect.height = (numeroSottoMenu*ALTEZZA_CARTA);
            ridisegna();
        }
        else {
            ridisegna();
            //imposta le dimensioni a 0 (non è più cliccabile)
            rect.height = 0;
        }
    }

    /**
     * Chiamerà panel.repaint nelle dimensioni giuste
     */
    public void ridisegna(){
        final int BORDO_OFFSET = 2;
        
        //rettangolo copia, per fare un repaint più ampio
        Rectangle rettangolo = rettangolo = new Rectangle(getRectangle()); 

        //allarga i contorni di un pixel e poi ingloba anche parte del pulsante
        rettangolo.y /= 2;
        rettangolo.height+= rettangolo.y + BORDO_OFFSET;
        rettangolo.x -= BORDO_OFFSET;
        rettangolo.width += BORDO_OFFSET*2;
        
        //ridisegna
        ag.panelRepaint(rettangolo);
    }

    public void setFaseRinforzo(boolean faseRinforzo) {
        this.faseRinforzo = faseRinforzo;
        if (faseRinforzo) {
            getRectangle().height+=ALTEZZA_CARTA;
            ridisegna();
        }
        else {
            ridisegna();
            getRectangle().height-=ALTEZZA_CARTA;
        }
    }

    public void aggiungiCarta(Carta carta){
        //this.listaCarte.add(carta); devo farlo in partitaClient

        CartaDisegnabile cartaDis = new CartaDisegnabile(carta, ALTEZZA_CARTA, LARGHEZZA_CARTA, partita.getMeStesso());
        listaCarteDisegnabili.add(cartaDis);
        if (aperto){
            getRectangle().height+=ALTEZZA_CARTA; //aggiunge l'altezza della carta aggiuta
            ridisegna();
        }
    }

    public void rimuoviCarta(CartaDisegnabile carta){
        listaCarteDisegnabili.remove(carta);
        //listaCarte.remove(carta.getCarta()); da fare fare alla partitaClient

        //TODO ridisegna con la carta rimossa
        if (aperto){
            ridisegna();
            getRectangle().height-=ALTEZZA_CARTA; //toglie l'altezza della carta tolta
        }
    }

    private Point getPosition(){
        return getRectangle().getLocation();
    }

    private Rectangle getRectangle(){
        return (Rectangle) posizione;
    }

    private void riposiziona(){
        getRectangle().x = dimensioniPannello.width-distanzaLatoSinistro-LARGHEZZA_CARTA;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        riposiziona(); //mette a posto le dimensioni

        Point posizioneCarta = getPosition();
        if (aperto){
            if (faseRinforzo){
                richiestaSelezioneCarte.setPosizione(posizioneCarta);
                richiestaSelezioneCarte.disegna(g2, ga);
                posizioneCarta.y+=ALTEZZA_CARTA;
            }
            for (CartaDisegnabile cartaDisegnabile : listaCarteDisegnabili) {
                cartaDisegnabile.setPosizione(posizioneCarta);
                cartaDisegnabile.disegna(g2, ga);
                posizioneCarta.y+=ALTEZZA_CARTA;
            }
        }
        
    }

    @Override //cattura il mouse su se stesso
    protected void actionPressed(MouseEvent e) {
        if (!aperto)
            return;
        //non serve riposizionarle perché se sono cliccabili sono anche visibili
            //posto giusto

        //cerca la posizione schiacciata
        int y = e.getY()-distanzaLatoSuperiore;
        y = y/ALTEZZA_CARTA;
        
        if (faseRinforzo) {
            if (y == 0) {
                richiestaSelezioneCarte.doClicked(e);
                return;
            }
            else
                y--;
        }

        listaCarteDisegnabili.get(y).doClicked(e);
        return;
    }

    @Override  //l'azione che deve fare quando si preme il pulsante menu
    public void actionPerformed(EventoAzioneRisicammara e) {
        //attiva questo menu :D
        setAperto(!aperto);
    }

}
