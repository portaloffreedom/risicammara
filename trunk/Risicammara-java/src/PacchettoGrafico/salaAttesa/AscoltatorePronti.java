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

/**
 *
 * @author matteo
 */
public class AscoltatorePronti implements ActionListener {
    private SalaAttesa salaAttesa;
    private PannelloSalaAttesa pannello;

    public AscoltatorePronti(SalaAttesa salaAttesa, PannelloSalaAttesa pannello) {
        this.salaAttesa = salaAttesa;
        this.pannello = pannello;
    }

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
