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
    
    /**
     * Costruisce una nuova partita lato client.
     * @param server L'oggettoConnessioneServer
     * @param listagiocatori la listadei giocatori
     * @param planciadigioco la plancia di gioco
     * @param sequenza la sequenza di gioco
     */
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

    /**
     * Aggancia il menu delle carte della plancia alla partita.
     * @param menuCarte il riferimento al menu da agganciare
     */
    public void setMenuCarte(MenuCarte menuCarte) {
        this.menuCarte = menuCarte;
    }

    @Override
    public PlanciaClient getPlancia() {
        return (PlanciaClient) super.getPlancia();
    }

    /**
     * Restituisce la lista dei giocatori
     * @return la lista dei giocatori
     */
    public ListaGiocatoriClient getListaGiocatori(){
        return (ListaGiocatoriClient) this.listagiocatori;
    }

    /**
     * Restituisce l'indice del giocatore locale
     * @return L'indice del giocatore locale
     */
    public int getMeStessoIndex(){
        return getListaGiocatori().meStessoIndex();
    }

    /**
     * Restituisce L'oggetto Giocatore associato al giocatore locale.
     * @return L'oggetto Giocatore assegnato al giocatore locale.
     */
    public Giocatore getMeStesso(){
        return getListaGiocatori().meStesso();
    }

    /**
     * Restituisce la connessione con il server
     * @return L'oggetto Connessione che rappresenta la comunicazione con il server.
     */
    public Connessione getConnessione() {
        return server;
    }
    
    /**
     * Chiede se è il mio turno
     * @return true se è il mio turno, false altrimenti.
     */
    public boolean eMioTurno(){
        return (giocTurno == getMeStessoIndex());
    }
    
    /**
     * Imposta il giocatore di turno e controlla se la sequenza dei giocatori è rispettata.
     * @param giocatore L'intero del giocatore di turno.
     */
    public void avanzaGiocatoreDiTurno(int giocatore){
        this.giocTurno = giocatore;
        posizioneSequenza++;
        posizioneSequenza = posizioneSequenza%(sequenzaGiocatori.length);
        if (sequenzaGiocatori[posizioneSequenza] != giocTurno)
            System.err.println("Attenzione! sequenza dei giocatori non rispettata!"
                    + " ("+giocTurno+"!="+sequenzaGiocatori[posizioneSequenza]+")");
    }

    /**
     * Aggiunge una carta pescata anche graficamente.
     * @param carta La carta da aggiungere.
     */
    public void aggiungiCartaMeStesso(Carta carta) {
        getListaGiocatori().meStesso().addCard(carta);
        menuCarte.aggiungiCarta(carta);
    }
}
