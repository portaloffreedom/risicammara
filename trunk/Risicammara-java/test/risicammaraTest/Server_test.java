package risicammaraTest;

/**
 * @author Sten_Gun
 * Server version 2
 * Questa versione del server implementa un altro modello di scambio messaggi
 * e gestione della partita.
 * 
 * Thread:
 * - Messaggi in Entrata
 * - gestore connessioni in ingresso
 * - Gestione della partita
 * - Messaggi in uscita
 * - Server Message Processor
 * strutture dati:
 * - coda per i messaggi al server message processor
 * - coda per i messaggi in uscita
 * - coda per i messaggi di gioco
 * - Lista utenti connessi (global) (gli utenti connessi sono trattati ognuno come un oggetto "giocatore" vero e proprio.)
 * - coda notifiche per il server
 */
public class Server_test extends Thread{
    /* Threads */
    private GesConn gestoreconn;
    private ServMsgProc smp;
    private MsgIn msgin;
    private Msgout msgout;
    private GesMatch gestorematch;
    /* strutture dati*/
    private Connessioni Connessi;

    /**
     * Inizializza tutti i thread e le strutture dati
     */
    public Server_test(){

    };
    /**
     * Avvia il server
     */
    private void avvia(){

}
    /**
     * Avvia il thread del server
     */
    @Override
    public void run(){

    }
}
