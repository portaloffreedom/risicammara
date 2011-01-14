/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 *
 * @author Sten_Gun
 */
public enum messaggio_t {
    MODIFICANICKCOLORE, // Pacchetto che segnala una modifica di nick e colore.
    OKADD,              // Pacchetto di "ok" ad una aggiunta giocatore.
    DISCONNECTED,       // Informa il server dell'avvenuta disconnessione di un giocatore
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
    COMMAND,    //Il pacchetto ricevuto è un comando client/server
    ERROR,      //Il pacchetto ricevuto è una notifica di errore
    CAMBIAARMATETERRITORIO, //Il pacchetto ricevuto notifica un cambiamento di armate in un territorio
    CAMBIAARMATEGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento di armate del giocatore
    AGGIORNADATIGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento nei dati giocatore
    AGGIORNALISTAGIOCATORI; //Il pacchetto ricevuto notifica un aggiornamento della lista dei giocatori

}
