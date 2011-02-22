package risicammaraJava.turnManage;

import PacchettoGrafico.ListaGiocatoriClient;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.playerManage.Giocatore;

/**
 *
 * @author matteo
 */
public class PartitaClient extends GestionePartita {
    
    public PartitaClient(ListaGiocatoriClient listagiocatori, PlanciaClient planciadigioco){
        super(listagiocatori);
        this.planciadigioco = planciadigioco;
    }

    @Override
    public PlanciaClient getPlancia() {
        return (PlanciaClient) super.getPlancia();
    }

    public ListaGiocatoriClient getListaGiocatori(){
        return (ListaGiocatoriClient) this.listagiocatori;
    }

    public int getMeStessoIndex(){
        return getListaGiocatori().meStessoIndex();
    }

    public Giocatore getMeStesso(){
        return getListaGiocatori().meStesso();
    }
}
