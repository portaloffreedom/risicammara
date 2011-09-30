package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;
import java.io.IOException;
import risicammaraClient.Bonus_t;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.MessaggioGiocaTris;

/**
 * Ascoltatore per le giocate dei tris.
 * @author matteo
 */
public class AscoltatoreGiocaTris implements RisicammaraEventListener {
    private static final int NUMERO_CARTE_TRIS = 3;
    private static final String TESTO_DESELEZIONE = "Gioca Tris";

    private MenuCarte menuCarte;
    private PartitaClient partita;
    private AnimatoreGraficaPannelli attivatoreGrafica;
    private Connessione server;
    private SottoMenuCarta richiestaUsoTris;

    private int contatoreSelezionate;
    private CartaDisegnabile carte[];
    private boolean active;

    /**
     * Ascoltatore per le giocate del tris.
     * @param menuCarte il riferimento al menu delle carte
     * @param ag Attivatore Grafico
     * @param partita Partita.
     */
    public AscoltatoreGiocaTris(MenuCarte menuCarte, AnimatoreGraficaPannelli ag, PartitaClient partita) {
        this.menuCarte = menuCarte;
        this.attivatoreGrafica = ag;
        this.partita = partita;
        this.server = partita.getConnessione();
        this.richiestaUsoTris = menuCarte.getRichiestaUsoTris();

        this.active = false;
        this.contatoreSelezionate = 0;
        this.carte = new CartaDisegnabile[NUMERO_CARTE_TRIS];
    }

    /**
     * Imposta lo stato Attivo.
     * @param active true se è attivo, false altrimenti
     */
    public void setActive(boolean active) {
        this.active = active;
        if (!active)
            deseleziona_Pulisci();
    }

    private void deseleziona_Pulisci(){
        contatoreSelezionate = 0;

        for(int i=0; i<NUMERO_CARTE_TRIS; i++) {
            if (carte[i] != null) {
                selezionaCarta(carte[i], false);
                carte[i] = null;
            }
        }
    }

    /**
     * Quando viene effettuata una azione allora l'ascoltatore fa partire questo metodo.
     * @param e L'evento.
     */
    @Override
    public void actionPerformed(EventoAzioneRisicammara e) {
        if (!active)
            return;
        
        SottoMenuCarta pulsanteCarta = (SottoMenuCarta) e.getSource();
        if (Client.DEBUG)
            System.out.println("Tasto premuto: "+pulsanteCarta);

        territori_t carta = pulsanteCarta.getCarta();

        //controlla se è stato premuto il pulsante di GIOCA TRIS
        if (carta == null) {
            if (contatoreSelezionate == NUMERO_CARTE_TRIS)
                mandaTris();
            
            return;
        }

        //cerca se la carte è già selezionata allora la deseleziona
        for(int i=0; i<NUMERO_CARTE_TRIS; i++) {
            if (carte[i] == pulsanteCarta) {
                selezionaCarta(carte[i], false);
                carte[i] = null;
                contatoreSelezionate--;
                if (Client.DEBUG)
                    System.out.println("Deselezionato");
                return;
            }
        }

        //imposta alla prima posizione disponibile
        for(int i=0; i<NUMERO_CARTE_TRIS; i++) {
            if (carte[i] == null) {
                carte[i] = (CartaDisegnabile) pulsanteCarta;
                //controllo se c'è un tris o se sono state selezionate tre carte a caso
                if (contatoreSelezionate == 2 && !eTRIS(carte[0].getCarta().getBonus(), carte[1].getCarta().getBonus(), carte[2].getCarta().getBonus())) {
                    carte[i] = null;
                    return;
                }
                selezionaCarta(carte[i], true);
                contatoreSelezionate++;
                if (Client.DEBUG)
                    System.out.println("Selezionato");
                return;
            }
        }

        
    }

    private void selezionaCarta(SottoMenuCarta carta, boolean selezionata) {
        carta.setSelezionato(selezionata);
        attivatoreGrafica.panelRepaint(carta.getContorni());
    }

    private void mandaTris(){
        selezionaCarta(richiestaUsoTris, true);
        try {
            server.spedisci(new MessaggioGiocaTris(partita.getMeStessoIndex(), carte[0].getCarta(), carte[1].getCarta(), carte[2].getCarta()));
        } catch (IOException ex) {
            System.out.println("Impossibile mandare il messaggio di Tris: "+ex);
        }
        
        contatoreSelezionate = 0;
        richiestaUsoTris.setTesto(TESTO_DESELEZIONE);
        for(int i=0; i<NUMERO_CARTE_TRIS; i++) {
            partita.getMeStesso().remTerr(carte[i].getCarta());
            menuCarte.rimuoviCarta(carte[i]);
            carte[i] = null;
        }

        selezionaCarta(richiestaUsoTris, false);
        menuCarte.setFaseRinforzo(false);
        return;
    }

    static private boolean eTRIS (Bonus_t bonus1,Bonus_t bonus2,Bonus_t bonus3) {
        if (bonus1 == Bonus_t.JOLLY || bonus2 == Bonus_t.JOLLY || bonus3 == Bonus_t.JOLLY) {
            if (       ( bonus3 == Bonus_t.JOLLY && bonus1 == bonus2 )
                    || ( bonus1 == Bonus_t.JOLLY && bonus2 == bonus3 )
                    || ( bonus2 == Bonus_t.JOLLY && bonus1 == bonus3 ) )
                return true;
            else
                return false;
        }

        if (bonus1 == bonus2 && bonus2 == bonus3)
            return true;

        if (bonus1 != bonus2 && bonus2 != bonus3 && bonus1 != bonus3)
            return true;

        return false;
    }

}
