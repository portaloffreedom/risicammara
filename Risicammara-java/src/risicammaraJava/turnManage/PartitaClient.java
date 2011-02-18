package risicammaraJava.turnManage;

import PacchettoGrafico.ListaGiocatoriClient;
import risicammaraJava.boardManage.PlanciaClient;

/**
 *
 * @author stengun
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
}
