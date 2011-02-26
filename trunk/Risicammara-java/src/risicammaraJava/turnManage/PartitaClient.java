package risicammaraJava.turnManage;

import risicammaraJava.playerManage.ListaGiocatoriClient;
import PacchettoGrafico.PannelloGiocoPackage.MenuCarte;
import risicammaraClient.Connessione;
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
    private Connessione server;
    
    public PartitaClient(Connessione server, ListaGiocatoriClient listagiocatori, PlanciaClient planciadigioco, Integer[] sequenza){
        super(listagiocatori);
        this.server = server;
        this.planciadigioco = planciadigioco;

        if (listagiocatori.getSize() != sequenza.length)
            System.err.println("Incompatibilità della sequenza con la lista dei giocatori ("+listagiocatori.getSize()+" != "+sequenza.length+")");
        sequenzaGiocatori = new int[listagiocatori.getSize()];
        posizioneSequenza = 0;

        //parte da rimpiazzare con la sequenza che arriva dal server
        int a = 0;
        for (Integer i : sequenza){
            sequenzaGiocatori[a++]=i.intValue();
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

    public Connessione getConnessione() {
        return server;
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
