package risicammaraJava.playerManage;

import risicammaraClient.Obbiettivi_t;

/**
 * Classe che specializza la ListaPlayers usata lato server.
 * Detiene l'indice del giocatore locale e può costruire l'oggetto
 * partendo da una lista giocatori Server.
 * @author matteo
 */
public class ListaGiocatoriClient extends ListaPlayers {
    private int indexGiocatore;

    /**
     * Costruttore per la lista dei giocatori lato Client. Inizializza la lista
     * partendo da una lista giocatori da server.
     * @param listaGiocatori La lista dei giocatori ottenuta dalla sala d'attesa
     * @param indexGiocatore L'indice del giocatore locale
     * @param mioObbiettivo L'obbiettivo del giocatore locale.
     */
    public ListaGiocatoriClient(ListaPlayers listaGiocatori, int indexGiocatore, Obbiettivi_t mioObbiettivo) {
        super(listaGiocatori);
        this.indexGiocatore = indexGiocatore;
        this.aggiungiObbiettivoMio(mioObbiettivo);
    }

    /**
     * Chiede L'oggetto Giocatore associato al giocatore locale.
     * @return L'oggetto Giocatore.
     */
    public Giocatore meStesso(){
        return this.get(indexGiocatore);
    }

    /**
     * Chiede l'indice del giocatore locale.
     * @return L'indice del giocatore locale
     */
    public int meStessoIndex(){
        return indexGiocatore;
    }
/**
 * Aggiunge l'obbiettivo assegnato dal server.
 * @param obbiettivo obbiettivo assegnato.
 */
    private void aggiungiObbiettivoMio(Obbiettivi_t obbiettivo){
        Giocatore catz = listaPlayers.get(indexGiocatore);
        if(catz == null){
            System.out.println("Giocatore nullo: "+indexGiocatore);
            System.exit(133);
        }
        catz.setObj(obbiettivo);
    }
}
