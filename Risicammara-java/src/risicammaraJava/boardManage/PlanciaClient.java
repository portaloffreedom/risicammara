package risicammaraJava.boardManage;

import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.territori_t;

/**
 * Rappresenta una plancia per il client.
 * Gestisce il disegno dei bollini sui territori e imposta un rettangolo che contiene
 * il territorio in modo da dover disegnare solo quello e non tutta la plancia.
 * @author matteo
 */
public class PlanciaClient extends Plancia {

    /**
     * Costruisce una nuova Plancia lato client partendo da una plancia normale.
     * @param plancia riferimento alla plancia di gioco da cui ereditare i valori.
     */
    public PlanciaClient(Plancia plancia){
        super(plancia);
        for (int i=0; i<this.tabellone.length; i++){
            tabellone[i] = new TerritorioPlanciaClient(tabellone[i]);
        }
    }

    /**
     * Imposta il rettangolo associato e il centro del bollino nel territorio
     * @param territorio il territorio a cui associare
     * @param rettangolo rettangolo da associare
     * @param posizioneCerchio centro del bollino da associare.
     */
    public void setBounds(territori_t territorio, Rectangle rettangolo, Point posizioneCerchio){
        TerritorioPlanciaClient territorioPlancia = (TerritorioPlanciaClient) super.getTerritorio(territorio);
        territorioPlancia.setPosizione(rettangolo);
        territorioPlancia.setPosizioneCerchietto(posizioneCerchio);
    }

    /**
     * Imposta il rettangolo associato e il centro del bollino nel territorio.
     * @param idTerritorio l'identificativo del territorio
     * @param rettangolo il rettangolo associato
     * @param posizioneCerchio il centro del bollino
     * @throws TerritorioNonValido eccezione sollevata se l'id non è valido.
     */
    public void setBounds(int idTerritorio, Rectangle rettangolo, Point posizioneCerchio) throws TerritorioNonValido{
        setBounds(territori_t.GetTerritorio(idTerritorio), rettangolo, posizioneCerchio);
    }

    @Override
    public TerritorioPlanciaClient getTerritorio(territori_t territorio) {
        return (TerritorioPlanciaClient) super.getTerritorio(territorio);
    }

    @Override
    public TerritorioPlanciaClient getTerritorio(int idTerritorio) throws TerritorioNonValido {
        return (TerritorioPlanciaClient) super.getTerritorio(idTerritorio);
    }

    /**
     * Restituisce il tabellone di gioco
     * @return il tabellone di gioco
     */
    public TerritorioPlancia[] getTabellone() {
        return tabellone;
    }

}
