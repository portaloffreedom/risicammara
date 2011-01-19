/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioComandi;

/**
 *
 * @author matteo
 */
public class AscoltatoreKickGiocatore implements ActionListener {
        private SalaAttesa salaAttesa;
        private PannelloSalaAttesa pannello;

        public AscoltatoreKickGiocatore(SalaAttesa salaAttesa, PannelloSalaAttesa pannello) {
            this.salaAttesa = salaAttesa;
            this.pannello =  pannello;
        }

        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
            int index = ((BottoneGiocatori) (e.getSource())).getIndex();

            if (index == salaAttesa.indexGiocatore){
                //TODO dialogo sei sicuro di volere uscire
                System.exit(199);
            }
            else {
                try {
                    salaAttesa.mandaMessaggio((Messaggio) MessaggioComandi.creaMsgKickplayer(salaAttesa.indexGiocatore, index));
                    pannello.stampaMessaggioComando("Kickato giocatore "+index+" \""+salaAttesa.listaGiocatori.getNomeByIndex(index)+"\"");
                    salaAttesa.eliminaGiocatore(index);
                } catch (IOException ex) {
                    pannello.stampaMessaggioErrore("Messaggio di kick non eliminato", ex);

        }
            }
        }
    }
