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
public class MenuCarte extends MenuRisicammara {
    static public int LARGHEZZA_CARTA = 200;
    static public int ALTEZZA_CARTA = 50;

    private SottoMenuCarta richiestaUsoTris;
    private ArrayList<CartaDisegnabile> listaCarteDisegnabili;
    private AscoltatoreGiocaTris ascoltatoreTris;

    private boolean faseRinforzo;
    private Dimension dimensioniPannello;
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
        super(ag);
        this.dimensioniPannello = dimePanel;
        this.distanzaLatoSinistro = distanzaLatoSinistro;
        this.distanzaLatoSuperiore = altezza;
        this.partita = partita;

        this.richiestaUsoTris = new SottoMenuCarta("Gioca un TRIS", ALTEZZA_CARTA, LARGHEZZA_CARTA); //TODO fallo disegnare al centro il testo
        this.ascoltatoreTris = new AscoltatoreGiocaTris(this, ag, partita);
        this.richiestaUsoTris.setActionListener(ascoltatoreTris);
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

    public void setFaseRinforzo(boolean faseRinforzo) {
        if (this.faseRinforzo == faseRinforzo)
            return;

        this.faseRinforzo = faseRinforzo;
        ascoltatoreTris.setActive(faseRinforzo);
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
        cartaDis.setActionListener(ascoltatoreTris);
        listaCarteDisegnabili.add(cartaDis);
        
        getRectangle().height+=ALTEZZA_CARTA; //aggiunge l'altezza della carta aggiuta
        
        if (aperto)
            ridisegna();
    }

    public void rimuoviCarta(CartaDisegnabile carta){
        listaCarteDisegnabili.remove(carta);
        //listaCarte.remove(carta.getCarta()); da fare fare alla partitaClient

        //TODO ridisegna con la carta rimossa
        if (aperto)
            ridisegna();

        getRectangle().height-=ALTEZZA_CARTA; //toglie l'altezza della carta tolta
    }

    private void riposiziona(){
        getRectangle().x = dimensioniPannello.width-distanzaLatoSinistro-LARGHEZZA_CARTA;
    }

    public SottoMenuCarta getRichiestaUsoTris() {
        return richiestaUsoTris;
    }

    @Override
    public void disegna(Graphics2D g2, GraphicsAdvanced ga) {
        riposiziona(); //mette a posto le dimensioni

        Point posizioneCarta = getPosition();
        if (aperto){
            if (faseRinforzo){
                richiestaUsoTris.setPosizione(posizioneCarta);
                richiestaUsoTris.disegna(g2, ga);
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
                richiestaUsoTris.doClicked(e);
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
