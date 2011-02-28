package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioSpostaArmate;

/**
 * Wrapper per il dialogo di spostamento armate nei bottoni fase.<br>
 * Può anche mandare il messaggio di Sposta Armate
 * @author matteo
 */
public class RichiestaNumeroArmate implements RisicammaraEventListener {
    private BottoneFaseAvanzato bottoneFase;
    private Connessione server;
    private PartitaClient partita;
    private territori_t sorgente;
    private territori_t destinazione;

    /**
     * Inizializza tutti i dati necessari.
     * @param bottoneFase Il riferimento al bottone fase a cui è allacciato
     * @param server il riferimento alla connessione con il server
     * @param partita il riferimento all'oggetto locale di Partita.
     */
    public RichiestaNumeroArmate(BottoneFaseAvanzato bottoneFase, Connessione server, PartitaClient partita) {
        this.bottoneFase = bottoneFase;
        this.server = server;
        this.partita = partita;
    }

    /**
     * Imposta l'ascoltatore di quando viene premuto il pulsante "ok" del dialogo spostamento.
     * @param listener il riferimento all'ascoltatore
     */
    public void setOkActionListener(RisicammaraEventListener listener) {
        this.bottoneFase.setOkActionListener(listener);
    }

    /**
     * Viene chiamato quando viene effettuata l'azione.<br>
     * Manda il messaggio di spostamento e disattiva il dialogo. Si attiva se viene impostato l'ok action listener su questo oggetto.
     * @param e
     */
    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        int armateDaSpostare = bottoneFase.getNumeroArmateSpostamento();
        try {
            spostaArmate(armateDaSpostare);
        } catch (IOException ex) {
            System.err.println("Messaggio sposta armate non inviato con successo: "+ex);
        }
        this.disattiva();
    }

    /**
     * Restituisce il numero di armate per lo spostamento
     * @return il numero di armate nello spinner
     */
    public int getNumeroArmateSpostamento(){
        return bottoneFase.getNumeroArmateSpostamento();
    }

    /**
     * Nasconte il dialogo della richeista per lo spostamento (spinner e bottone e testo)
     */
    public void disattiva() {
        bottoneFase.setRichiestaVisible(false);
    }

    /**
     * Imposta la sorgente e l'arrivo dello spostamento, compreso il massimo di armate spostabili.
     * @param sorgente il territorio da cui parte lo spostamento
     * @param destinazione il territorio nel quale sono spostate le armate
     * @param massimo il massimo di armate spostabili.
     */
    public void imposta(territori_t sorgente, territori_t destinazione, int massimo) {
        bottoneFase.setValori(massimo, 0);
        this.sorgente = sorgente;
        this.destinazione = destinazione;
    }

    /**
     * Rende visibile il dialogo per lo spostamento (spinner, bottone e testo).
     * Ricordarsi di impostare prima il valore massimo dello spinner (vedi @see imposta() )
     */
    public void attiva() {
        bottoneFase.setRichiestaVisible(true);
    }

    /**
     * Spedisce un messaggio di Spsota Armate
     * @param armate le armate da spostare
     * @throws IOException Eccezione sollevata se il socket non è valido
     */
    public void spostaArmate(int armate) throws IOException {
        server.spedisci(new MessaggioSpostaArmate(partita.getMeStessoIndex(), sorgente, destinazione, armate));
    }
}
