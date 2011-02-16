package risicammaraJava.turnManage;

import risicammaraJava.boardManage.Plancia;
import risicammaraJava.playerManage.ListaPlayers;

/**
 *
 * @author stengun
 */
public class PartitaClient extends GestionePartita {
    
    public PartitaClient(ListaPlayers listagiocatori, Plancia planciadigioco){
        super(listagiocatori);
        this.planciadigioco = planciadigioco;
    }
}
