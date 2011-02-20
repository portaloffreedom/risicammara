/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.boardManage;

import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.territori_t;

/**
 *
 * @author matteo
 */
public class PlanciaClient extends Plancia {

    public PlanciaClient(Plancia plancia){
        super(plancia);
        for (int i=0; i<this.tabellone.length; i++){
            tabellone[i] = new TerritorioPlanciaClient(tabellone[i]);
        }
    }

    public void setBounds(territori_t territorio, Rectangle rettangolo, Point posizioneCerchio){
        TerritorioPlanciaClient territorioPlancia = (TerritorioPlanciaClient) super.getTerritorio(territorio);
        territorioPlancia.setPosizione(rettangolo);
        territorioPlancia.setPosizioneCerchietto(posizioneCerchio);
    }

    public void setBounds(int idTerritorio, Rectangle rettangolo, Point posizioneCerchio){
        setBounds(territori_t.GetTerritorio(idTerritorio), rettangolo, posizioneCerchio);
    }

    @Override
    public TerritorioPlanciaClient getTerritorio(territori_t territorio) {
        return (TerritorioPlanciaClient) super.getTerritorio(territorio);
    }

    @Override
    public TerritorioPlanciaClient getTerritorio(int idTerritorio) {
        return (TerritorioPlanciaClient) super.getTerritorio(idTerritorio);
    }

    public TerritorioPlancia[] getTabellone() {
        return tabellone;
    }

}
