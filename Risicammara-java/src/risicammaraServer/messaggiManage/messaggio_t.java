/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

/**
 *
 * @author Sten_Gun
 */
public enum messaggio_t {

    GIOCATRIS,  //Messaggio che indica la giocata di un tris
    PLANCIA,    //Messaggio plancia per inizio partita
    PLAYERLIST, //Messaggio lista iniziale per tutti i giocatori
    /**
     * Paccketto che al server la modifica dei dati di un altro giocatore.
     * Questo pacchetto invia la conferma AGGIORNADATIGIOCATORE.
     * @see risicammaraServer.MessageManage.MessaggioAggiornaDatiGiocatore
     * @see risicammaraServer.MessageManage.MessaggioCambiaNickColore
     */
    MODIFICANICKCOLORE, // Pacchetto che segnala una modifica di nick e colore.
    OKADD,              // Pacchetto di "ok" ad una aggiunta giocatore.
    /**
     * Se ricevuto dal server aggiunge un giocatore con il socket
     * @see risicammaraServer.MessageManage.MessaggioNuovoGiocatore
     * Se ricevuto da un client aggiunge un giocatore alla lista dei client
     * @see risicammaraServer.MessageManage.MessaggioAddPlayer
     */
    AGGIUNGIGIOCATORE, //quando viene ricevuto un nuovo Socket di un nuovo giocatore connesso
    /** Messaggio di conferma avvenuta connessione da parte del Server contenente
     * anche tutte le informazione che servono per inizializzare la partita e
     * la lista giocatori del client
     * @see risicammaraServer.MessageManage.MessaggioConfermaNuovoGiocatore
     */
    CONFERMAAGGIUNGIGIOCATORE, //per il messaggio di risposta che deve mandare il server

    //altro

    /** Messaggio di chat (contente testo e mittente)
     * @see risicammaraServer.MessageManage.Messaggio_chat
     */
    CHAT,       //Il pacchetto ricevuto è di tipo MESSAGGIO DI CHAT
    /**
     * Messaggio che contiene un comando speciale client/server.
     * <b> WARNING </b> L'implementazione è ancora incompleta.
     * @see risicammaraServer.MessageManage.MessaggioComandi
     */
    COMMAND,    //Il pacchetto ricevuto è un comando client/server
    /**
     * Messaggio che notifica un errore da parte del server.
     * <b> WARNING </b> L'implementazione è ancora incompleta.
     * @see risicammaraServer.MessageManage.MessaggioErrore
     */
    ERROR,      //Il pacchetto ricevuto è una notifica di errore
    /**
     * Pacchetto che indica il cambio di armate in un dato territorio.
     * <b> WARNING </b> L'implementazione è ancora incompleta.
     */
    CAMBIAARMATETERRITORIO, //Il pacchetto ricevuto notifica un cambiamento di armate in un territorio
    /**
     * Pacchetto che notifica un giocatore l'aggiunta di armate a sua disposizione.
     * Può essere inviato esclusivamente dal server!
     * <b> WARNING </b> L'implementazione è ancora incompleta.
     */
    CAMBIAARMATEGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento di armate del giocatore

    /**
     * Notifica tutti i giocatori che un altro giocatore ha modificato nick e colore.
     * La notifica non viene inviata a chi ha cambiato i propri attributi.
     * @see risicammaraServer.MessageManage.MessaggioAggiornaDatiGiocatore
     * @see risicammaraServer.MessageManage.MessaggioCambiaNickColore
     */
    AGGIORNADATIGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento nei dati giocatore
    AGGIORNALISTAGIOCATORI; //Il pacchetto ricevuto notifica un aggiornamento della lista dei giocatori

}
