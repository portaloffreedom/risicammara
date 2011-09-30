package PacchettoGrafico.PannelloGiocoPackage;

import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 * Classe che serve interamente per permettere di fare le animazioni. Si può
 * scegliere se fare una animazione con i metodi interni ad AnimatoreGraficaPannelli o
 * semplicemente richiamare repaint sul pannello principale.<p>
 * Il metodo repaint(Rectangle rect) è da preferire in quanto renderizza una
 * parte più piccola del pannello e quindi alleggerisce il carico del sistema.
 * @author matteo
 */
public class AnimatoreGraficaPannelli {
    private JPanel pannello;
    private int contatore;

    /**
     * Costruttore
     * @param pannello JPanel su cui si vogliono fare le animazioni.
     */
    public AnimatoreGraficaPannelli (JPanel pannello){
        this.pannello = pannello;
        this.contatore = 0;
    }

    /**
     * Funzione che stabilisce se il paint del pannello principale deve continuare
     * a ridisegnare o fermarsi.
     * @return true se c'è ancora bisogno di ridisegnare.
     */
    public boolean continuaRidisegna(){
        return (contatore > 0);
    }

    /**
     * Attiva una nuova animazione.
     */
    public synchronized void attiva(){
        contatore++;
        pannello.repaint();
    }

    /**
     * Disattiva una animazione di quelle in esecuzione.
     */
    public void completato (){
        if (contatore <=0) {
            System.err.println("Errore nell'attivatore grafica: impossibile "+
                               "diminuire ancora il contatore");
            return;
        }
        contatore--;
    }

    /**
     * Delega il repaint dell'intero pannello.
     */
    public void panelRepaint (){
        this.pannello.repaint();
    }

    /**
     * Delega il repaint di un'area rettangolare del pannello.
     * @param r Rettangolo in cui limitare il ridisegno.
     */
    public void panelRepaint(Rectangle r) {
        pannello.repaint(r);
    }

    /**
     * Delega il repaint di una specifica area del pannello.
     * @param tm Questo parametro non è usato.
     * @param x Ascissa del vertice in alto a destra.
     * @param y Ordinata del vertice in alto a destra.
     * @param width Larghezza della zona da ridimensionare.
     * @param height Altezza della zona da ridimensionare.
     */
    public void panelRepaint(long tm, int x, int y, int width, int height) {
        pannello.repaint(tm, x, y, width, height);
    }

}
