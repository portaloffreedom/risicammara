package PacchettoGrafico.salaAttesa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import risicammaraClient.Colore_t;
import risicammaraServer.messaggiManage.MessaggioComandi;

/**
 * Ascoltatore per gli stati di pronto.
 * @author matteo
 */
public class AscoltatorePronti implements ActionListener {
    private SalaAttesa salaAttesa;
    private PannelloSalaAttesa pannello;

    /**
     * Inizializza tutte le vvariabili dell'ascoltatore.
     * @param salaAttesa La sala d'attesa
     * @param pannello Il pannello associato.
     */
    public AscoltatorePronti(SalaAttesa salaAttesa, PannelloSalaAttesa pannello) {
        this.salaAttesa = salaAttesa;
        this.pannello = pannello;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (salaAttesa.listaGiocatori.get(salaAttesa.indexGiocatore).getArmyColour() == Colore_t.NULLO){
            pannello.invertiPronto(salaAttesa.indexGiocatore);
            pannello.stampaMessaggioComando("Non puoi segnarti come pronto finchè non scegli un colore valido");
            return;
        }

        try {
            salaAttesa.server.spedisci(MessaggioComandi.creaMsgSetPronto(salaAttesa.indexGiocatore));
        } catch (IOException ex) {
            pannello.stampaMessaggioErrore("Il tuo stato non è stato impostato come pronto", ex);
        }
    }

}
