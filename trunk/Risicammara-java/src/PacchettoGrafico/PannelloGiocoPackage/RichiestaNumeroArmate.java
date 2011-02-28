package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.io.IOException;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioSpostaArmate;

/**
 *
 * @author matteo
 */
public class RichiestaNumeroArmate implements RisicammaraEventListener {
    private BottoneFaseAvanzato bottoneFase;
    private Connessione server;
    private PartitaClient partita;
    private territori_t sorgente;
    private territori_t destinazione;

    public RichiestaNumeroArmate(BottoneFaseAvanzato bottoneFase, Connessione server, PartitaClient partita) {
        this.bottoneFase = bottoneFase;
        this.server = server;
        this.partita = partita;
    }

    public void setOkActionListener(RisicammaraEventListener listener) {
        this.bottoneFase.setOkActionListener(listener);
    }

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

    public int getNumeroArmateSpostamento(){
        return bottoneFase.getNumeroArmateSpostamento();
    }

    public void disattiva() {
        bottoneFase.setRichiestaVisible(false);
    }

    public void imposta(territori_t sorgente, territori_t destinazione, int massimo) {
        bottoneFase.setValori(massimo, 0);
        this.sorgente = sorgente;
        this.destinazione = destinazione;
    }

    public void attiva() {
        bottoneFase.setRichiestaVisible(true);
    }

    public void spostaArmate(int armate) throws IOException {
        server.spedisci(new MessaggioSpostaArmate(partita.getMeStessoIndex(), sorgente, destinazione, armate));
    }
}
