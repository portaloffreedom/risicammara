package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.GraphicsAdvanced;
import risicammaraJava.turnManage.PartitaClient;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Oggetto che rappresenta la barra superiore della GUI contenente tutti
 * pulsanti e i menù per la gestione della partita.<p>
 * Il colore utilizzato per lo sfondo sarà quello impostato in GraphicsAdvanced
 * come "colore scuro".<p>
 * Ricordarsi di disegnare la barra per ultima in quanto contiene il disegno dei
 * menù. Dimenticarsi di questa permura significherà rendere praticamente
 * illeggibili i menù.
 * @author matteo
 * @see GraphicsAdvanced
 */
public class BarraSuperiore implements Elemento_2DGraphics {
    private Dimension dimensioniPannello;
    private Rectangle dimensioni;
    private BottoneRisicammara giocatoreButton;
    private BottoneRisicammara carteButton;
    private Point posizioneGiocatore;
    private Point posizioneCarte;
    private MenuGiocatore menuGiocatore;
    private MenuCarte menuCarte;
    private BarraFasi barraFasi;

    /**
     * Costruttore
     * @param dimensioniPannello Riferimento alle dimensioni del pannello.
     * Necessarie per quando questo viene ridimensionato.
     * @param altezza Altezza totale della barra superiore.
     * @param pannello Riferimento al Pannello superiore su cui viene aggiunta
     * la barra. (Necessario per aggiungere i pulsanti alla MatricePannello)
     * @param partita
     * @param attivatoreGrafica Riferimento all'attivatore Grafica necessario
     * per le animazioni.
     * @see MatricePannello
     */
    public BarraSuperiore(Dimension dimensioniPannello, int altezza,PannelloGioco pannello, PartitaClient partita, AttivatoreGrafica attivatoreGrafica){
        this.dimensioniPannello = dimensioniPannello;
        this.dimensioni= new Rectangle(dimensioniPannello);
        this.dimensioni.height = altezza;
        this.posizioneGiocatore = new Point(5, 5);
        this.posizioneCarte     = new Point(dimensioni.width-5-100, 5);
        this.giocatoreButton = new BottoneRisicammara(posizioneGiocatore, partita.getMeStesso().getNome());
        this.carteButton     = new BottoneRisicammara(posizioneCarte, "Carte");
        this.menuGiocatore = new MenuGiocatore( partita, attivatoreGrafica);
        this.menuCarte = new MenuCarte(dimensioniPannello, attivatoreGrafica, 55, 5, partita);
        this.barraFasi = new BarraFasi(dimensioniPannello, attivatoreGrafica, 125, 125, altezza-20, 10);

        
        this.giocatoreButton.setActionListener(menuGiocatore);
        this.carteButton.setActionListener(menuCarte);
    }

    @Override
    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        //Corregge le posizioni per il ridimensionamento
        if (true) { //TODO implementare questa fase solo se il pannello è stato ridimensionato
            this.dimensioni.width = this.dimensioniPannello.width;
            this.posizioneCarte.x = dimensioni.width-5-100;
            this.carteButton.setPosizione(posizioneCarte);
        }

        //Disegna lo sfondo nero della barra
        graphics2D.setColor(colori.getColoreScuro());
        graphics2D.fillRect(dimensioni.x, dimensioni.y, dimensioni.width, dimensioni.height);

        //disegna i menù 
        this.menuGiocatore.disegna(graphics2D, colori);
        this.menuCarte.disegna(graphics2D, colori);

        //Disegna i pulsanti
        this.barraFasi.disegna(graphics2D, colori);
        this.giocatoreButton.disegna(graphics2D, colori);
        this.carteButton.disegna(graphics2D, colori);
    }

    /**
     * Richiede il riferimento alla barra delle fasi
     * @return il riferimento alla barra delle fasi
     */
    public BarraFasi getBarraFasi(){
        return barraFasi;
    }

    /**
     * Richiede il riferimento al menu delle carte
     * @return il riferimento al menu delle carte
     */
    public MenuCarte getMenuCarte() {
        return menuCarte;
    }

    /**
     * Richiede il riferimento al menu giocatore
     * @return il riferimento al menu giocatore
     */
    public MenuGiocatore getMenuGiocatore() {
        return menuGiocatore;
    }

    /**
     * Richiede il riferimento al bottone delle carte
     * @return il riferimento al bottone dlle carte
     */
    public BottoneRisicammara getCarteButton() {
        return carteButton;
    }

    /**
     * Richiede un bottone Giocatore
     * @return il riferimento al Bottone Giocatore
     */
    public BottoneRisicammara getGiocatoreButton() {
        return giocatoreButton;
    }

    /**
     * Restituisce il BottoneFase Spostamento
     * @return il riferimento al bottone Fase Spostamento
     */
    public BottoneFaseAvanzato getBottoneFaseSpostamento() {
        return barraFasi.getBottoneFaseSpostamento();
    }

    /**
     * Restituisce il BottoneFaseAttacco
     * @return il riferimento al bottoneFase Attacco
     */
    public BottoneFaseAttaccoAvanzato getBottoneFaseAttacco() {
        return barraFasi.getBottoneFaseAttacco();
    }
    
}
