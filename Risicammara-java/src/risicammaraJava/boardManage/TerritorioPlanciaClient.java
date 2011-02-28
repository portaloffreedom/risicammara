package risicammaraJava.boardManage;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Rappresenta un TerritorioPlancia lato Client. Questa classe raccoglie tutte
 * le informazioni che servono per visualizare sul tabellone le informazioni
 * del territorioPlancia.
 * @author matteo
 */
public class TerritorioPlanciaClient extends TerritorioPlancia {
    private Rectangle posizione;
    private Point posizioneCerchietto;

    /**
     * Costruisce un territorio plancia Client partendo da un territorio Plancia
     * normale.
     * @param territorioPlancia il Territorio Plancia normale.
     */
    public TerritorioPlanciaClient(TerritorioPlancia territorioPlancia){
        super(territorioPlancia);
        this.posizione = null;
        this.posizioneCerchietto = null;
    }

    /**
     * Restituisce il rettangolo che contiene il territorio.
     * @return Il rettangolo che contiene il territorio
     */
    public Rectangle getPosizione() {
        return posizione;
    }

    /**
     * Imposta il quadrato più piccolo che contiene il territorio.
     * @param bordi il perimetro del rettangolo.
     */
    public void setPosizione(Rectangle bordi) {
        this.posizione = bordi;
    }

    /**
     * Restituisce il centro del cerchio del bollino che rappresenta le armate.
     * @return
     */
    public Point getPosizioneCerchietto() {
        return posizioneCerchietto;
    }

    /**
     * imposta la posizione dove disegnare il bollino per rappresentare le armate di un territorio.
     * @param posizioneCerchietto il punto che rappresenta il centro del cerchio.
     */
    public void setPosizioneCerchietto(Point posizioneCerchietto) {
        this.posizioneCerchietto = posizioneCerchietto;
    }
}
