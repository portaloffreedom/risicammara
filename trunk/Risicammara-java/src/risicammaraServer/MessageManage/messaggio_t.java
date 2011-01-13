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
    //parte necessaria alla lobby
    AGGIUNGIGIOCATORE, //quando viene ricevuto un nuovo Socket di un nuovo giocatore connesso
    CONFERMAAGGIUNGIGIOCATORE, //per il messaggio di risposta che deve mandare il server

    //altro
    CHAT,       //Il pacchetto ricevuto è di tipo MESSAGGIO DI CHAT
    COMMAND,    //Il pacchetto ricevuto è un comando client/server
    ERROR,      //Il pacchetto ricevuto è una notifica di errore
    GRAPHICS, // ?? (grafica e pulsanti)
    GRAPHICSUPDATE, // ?? (Ridisegno forzato)
    CAMBIAARMATETERRITORIO, //Il pacchetto ricevuto notifica un cambiamento di armate in un territorio
    CAMBIAARMATEGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento di armate del giocatore
    AGGIORNADATIGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento nei dati giocatore
    AGGIORNALISTAGIOCATORI; //Il pacchetto ricevuto notifica un aggiornamento della lista dei giocatori

}
