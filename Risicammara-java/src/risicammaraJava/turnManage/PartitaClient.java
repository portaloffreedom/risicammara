package risicammaraJava.turnManage;

import risicammaraJava.playerManage.ListaGiocatoriClient;
import PacchettoGrafico.PannelloGiocoPackage.MenuCarte;
import risicammaraJava.boardManage.PlanciaClient;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.playerManage.Giocatore;

/**
 * Gestore della partita implementata per il client.
 * @author matteo
 */
public final class PartitaClient extends GestionePartita {
    private MenuCarte menuCarte;

    private int sequenzaGiocatori[];
    private int posizioneSequenza;
    
    public PartitaClient(ListaGiocatoriClient listagiocatori, PlanciaClient planciadigioco){
        super(listagiocatori);
        this.planciadigioco = planciadigioco;

        sequenzaGiocatori = new int[listagiocatori.getSize()];
        posizioneSequenza = 0;

        //parte da rimpiazzare con la sequenza che arriva dal server
        for (int i=0; i<listagiocatori.getSize(); i++){
            sequenzaGiocatori[i]=i;
        }
    }

    public void setMenuCarte(MenuCarte menuCarte) {
        this.menuCarte = menuCarte;
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
    
    public boolean eMioTurno(){
        return (giocTurno == getMeStessoIndex());
    }
    
    public void avanzaGiocatoreDiTurno(int giocatore){
        this.giocTurno = giocatore;
        posizioneSequenza++;
        posizioneSequenza = posizioneSequenza%(sequenzaGiocatori.length);
        if (sequenzaGiocatori[posizioneSequenza] != giocTurno)
            System.err.println("Attenzione! sequenza dei giocatori non rispettata!"
                    + " ("+giocTurno+"!="+sequenzaGiocatori[posizioneSequenza]+")");
    }

    public void aggiungiCartaMeStesso(Carta carta) {
        getListaGiocatori().meStesso().addCard(carta);
        menuCarte.aggiungiCarta(carta);
    }
}
