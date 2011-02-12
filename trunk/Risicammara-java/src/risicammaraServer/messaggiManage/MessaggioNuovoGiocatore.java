/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

import java.net.Socket;

/**
 * È il messaggio "si è connesso un nuovo giocatore" che manda il Thread in
 * ascolto delle connessioni al Thread Server.
 * <p>
 * Contiene un riferimento al Socket di connessione con il giocatore in
 * questione.
 * @author matteo
 */
public class MessaggioNuovoGiocatore implements Messaggio {
    /** Riferimento al Socket di connessione del nuovo giocatore in questione */
    private Socket connessioneGiocatore;

    /**
     * Crea il messaggio "si è connesso un nuovo giocatore".
     * @param connessioneGiocatore il Socket della connessione con questo nuovo
     * giocatore
     */
    public MessaggioNuovoGiocatore(Socket connessioneGiocatore) {
        this.connessioneGiocatore = connessioneGiocatore;
    }

    
    public messaggio_t getType() {
        return messaggio_t.AGGIUNGIGIOCATORE;
    }

    /**
     *
     * @return il Socket di connessione tra il Server e il client del giocatore
     * in questione
     */
    public Socket getConnessioneGiocatore() {
        return connessioneGiocatore;
    }

    public int getSender() {
        return -1;
    }

    

}