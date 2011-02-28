package risicammaraServer.messaggiManage;

import risicammaraJava.playerManage.ListaPlayers;

/**
 * Messaggio che viene inviato come conferma alla connessione di nuo nuovo giocatore.
 * Il messaggio fornisce la lista dei giocatori già connessi con i rispettivi stati di pronto.
 * @author Sten_Gun
 */
public class MessaggioConfermaNuovoGiocatore implements Messaggio{

    private ListaPlayers listgioc;
    private int indexply;

    /**
     * Costruisce un messaggio di Conferma nuovo giocatore.
     * @param listgioc la lista dei giocatori già connessi
     * @param indexply l'index del giocatore appena accettato.
     */
    public MessaggioConfermaNuovoGiocatore(ListaPlayers listgioc, int indexply) {
        this.listgioc = listgioc;
        this.indexply = indexply;
    }


    @Override
    public messaggio_t getType() {
        return messaggio_t.OKADD;
    }

    /**
     *
     * @return
     */
    public ListaPlayers getPlyList(){
        return listgioc;
    }

    /**
     * Restituisce l'indice del giocatore.
     * @return l'indice del giocatore
     */
    public int getPlyIndex(){
        return indexply;
    }


    @Override
    public int getSender() {
        return -1;
    }

}
