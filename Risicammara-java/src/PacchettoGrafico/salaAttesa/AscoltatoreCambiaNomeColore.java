/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import risicammaraClient.Colore_t;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioCambiaNickColore;

/**
 *
 * @author matteo
 */
public class AscoltatoreCambiaNomeColore implements ActionListener {
    private SalaAttesa salaAttesa;
    private PannelloSalaAttesa pannello;

    public AscoltatoreCambiaNomeColore(SalaAttesa salaAttesa, PannelloSalaAttesa pannello) {
        this.salaAttesa = salaAttesa;
        this.pannello = pannello;
    }

    

    public void actionPerformed(ActionEvent e) {
        aggiornaNomeColore();
    }

    /**
     * Manda il messaggio di cambio nome e colore, poi chiama la funzione di
     * cambio nome e colore
     * @see MessaggioCambiaNickColore
     */
    private void aggiornaNomeColore() {
        //TODO cercare la stringa nome se non è già posseduta da qualcun'altro
        String nuovoNome = pannello.nomeGiocatore_getText();
        if (nuovoNome.equals("")) return;

        Colore_t nuovoColore = pannello.colore_getSelectedItem();
        try {
            if (salaAttesa.pronto(salaAttesa.indexGiocatore)){
                salaAttesa.server.spedisci((Messaggio) MessaggioComandi.creaMsgSetPronto(salaAttesa.indexGiocatore));
                salaAttesa.invertiPronto(salaAttesa.indexGiocatore);
                pannello.stampaMessaggioComando("Adesso non sei più pronto");
            }
            salaAttesa.server.spedisci((Messaggio) new MessaggioCambiaNickColore(nuovoNome, nuovoColore, salaAttesa.indexGiocatore));
        } catch (IOException ex) {
            pannello.stampaMessaggioErrore("Cambio colore e/o nuck non riuscito", ex);
            return;
        }
        pannello.setInfoGiocatore(salaAttesa.indexGiocatore, nuovoNome, nuovoColore);
    }
}