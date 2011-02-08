/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioChat;

/**
 *
 * @author matteo
 */
public class AscoltatoreMandaChat implements ActionListener {
        private SalaAttesa salaAttesa;
        private PannelloSalaAttesa pannello;

    public AscoltatoreMandaChat(SalaAttesa salaAttesa, PannelloSalaAttesa pannello) {
        this.salaAttesa = salaAttesa;
        this.pannello = pannello;
    }

    public void actionPerformed(ActionEvent e) {
        mandaMessaggioChat();
    }

    /**
     * Gestisce l'intero invio dei messaggi. Prende il messaggio dalla casella di
     * testo e se manda il messaggio con successo sulla rete resetta il campo di
     * testo di immissioneChat.
     */
    void mandaMessaggioChat () {
        String messaggio = pannello.immissioneChat_getText();

            //feedback più realistico se aspetta il messaggio dal server
        //this.cronologiaChat.stampaMessaggio("Me: "+messaggio);

        pannello.immissioneChat_requestFocus();
        if (messaggio.equals("")) return;

        try {
            salaAttesa.server.spedisci((Messaggio) new MessaggioChat(salaAttesa.indexGiocatore, messaggio));
            pannello.immissioneChat_resetText();
        } catch (IOException ex) {
            pannello.stampaMessaggioErrore("Attenzione messaggio \""+messaggio+"\" non inviato", ex);
        }
        return;
    }
}
