package risicammaraServer.messaggiManage;

/**
 * Questo enumerato rappresenta tutti i tipi di messaggio inviabili/ricevibili
 * da server e client.
 * @author Sten_Gun
 */
public enum messaggio_t {
    // ------------------------ Messaggi LOBBY
    /**
     * Pacchetto che indica la modifica dei dati di un altro giocatore.
     * Questo pacchetto invia la conferma AGGIORNADATIGIOCATORE.
     * @see risicammaraServer.MessageManage.MessaggioAggiornaDatiGiocatore
     * @see risicammaraServer.MessageManage.MessaggioCambiaNickColore
     */
    MODIFICANICKCOLORE, // Pacchetto che segnala una modifica di nick e colore.
    /**
     * Messaggio che conferma l'avvenuta aggiunta del giocatore al server.
     * Fornisce al giocatore appena connesso la lsita dei giocatori attualmente
     * connessi e i loro dati.
     * @see risicammaraServer.messaggiManage.MessaggioConfermaNuovoGiocatore
     */
    OKADD,              // Pacchetto di "ok" ad una aggiunta giocatore.
    /**
     * Se ricevuto dal server aggiunge un giocatore con il socket e il riferimento
     * al thread corrispondente alla propria lista giocatori.
     * @see risicammaraServer.MessageManage.MessaggioNuovoGiocatore
     * Se ricevuto da un client aggiunge un giocatore alla propria lista giocatori.
     * @see risicammaraServer.MessageManage.MessaggioAddPlayer
     */
    AGGIUNGIGIOCATORE, //quando viene ricevuto un nuovo Socket di un nuovo giocatore connesso
    /**
     * Messaggio di conferma avvenuta connessione da parte del Server contenente
     * anche tutte le informazione che servono per inizializzare la partita e
     * la lista giocatori del client
     * @see risicammaraServer.MessageManage.MessaggioConfermaNuovoGiocatore
     */
    CONFERMAAGGIUNGIGIOCATORE, //per il messaggio di risposta che deve mandare il server

    //----------------- MESSAGGI COMUNI

    /** Messaggio di chat (contente testo e mittente)
     * @see risicammaraServer.MessageManage.Messaggio_chat
     */
    CHAT,       //Il pacchetto ricevuto è di tipo MESSAGGIO DI CHAT
    /**
     * Messaggio che contiene un comando speciale client/server.
     * <b> WARNING </b> L'implementazione è ancora incompleta.
     * @see risicammaraServer.MessageManage.MessaggioComandi
     * @see risicammaraServer.messaggiManage.comandi_t
     */
    COMMAND,    //Il pacchetto ricevuto è un comando client/server
    /**
     * Messaggio che notifica un errore da parte del server.
     * <b> WARNING </b> L'implementazione è ancora incompleta.
     * @see risicammaraServer.MessageManage.MessaggioErrore
     * @see risicammaraServer.messaggiManage.errori_t
     */
    ERROR,      //Il pacchetto ricevuto è una notifica di errore

    //------------ Messaggi di partita e svolgimento partita
    /**
     * La carta appena pescata dal mazzo.
     */
    CARTA,
    /**
     * L'attacco ha portato alla conquista del territorio.
     */
    ATTACCOVINTO,
    /**
     * Armate a disposizione a inizio turno.
     */
    ARMATEDISPONIBILI, // Le armate a disposizione
    /**
     * Il risultato di un lancio di dado.
     * @see risicammaraServer.messaggiManage.MessaggioRisultatoDado
     */
    RISULTATOLANCI,      //Il server invia il risultato del lancio di UN dado
    /**
     * Pacchetto che indica uno spostamento di armate da parte dei giocatori.
     * @see risicammaraServer.messaggiManage.MessaggioSpostaArmate
     */
    SPOSTAARMATE,       //Sposta le armate
    /**
     * Messaggio che indica una dichiarazione di attacco da parte del giocatore
     * di turno ad un altro giocatore.
     * @see risicammaraServer.messaggiManage.MessaggioDichiaraAttacco
     */
    DICHIARAATTACCO, // Dichiara un attacco
    /**
     * Messaggio che indica l'utilizzo di un tris per ottenere un bonus armate.
     * @see risicammaraServer.messaggiManage.MessaggioGiocaTris
     */
    GIOCATRIS,  //Messaggio che indica la giocata di un tris
    /**
     * Invia a tutti i client la sequenza di come si svolgerà il gioco.
     */
    SEQUENZAGIOCO,
    /**
     * Messaggio inviato dal server per dare la plancia di gioco a tutti i giocatori
     * che iniziano la partita.
     * @see risicammaraServer.messaggiManage.MessaggioPlancia
     */
    PLANCIA,    //Messaggio plancia per inizio partita
    /**
     * Messaggio inviato dal server che fornisce la lista dei giocatori a tutti
     * i giocanti.
     * <b> WARNING </b> probabilmente è inutile.
     * @deprecated
     */
    PLAYERLIST, //Messaggio lista iniziale per tutti i giocatori
    /**
     * Pacchetto che indica il cambio di armate in un dato territorio.
     * @see risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio
     */    
    CAMBIAARMATETERRITORIO, //Il pacchetto ricevuto notifica un cambiamento di armate in un territorio
    /**
     * Pacchetto che notifica un giocatore l'aggiunta di armate a sua disposizione.
     * Può essere inviato esclusivamente dal server!
     * <p><center><b> WARNING </b> L'implementazione è ancora incompleta.</center></p>
     */
    //TODO creare un pacchetto CAMBIAARMATEGIOCATORE in messaggiManage
    CAMBIAARMATEGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento di armate del giocatore
    /**
     * Notifica tutti i giocatori che un altro giocatore ha modificato nick e colore.
     * La notifica non viene inviata a chi ha cambiato i propri attributi.
     * @see risicammaraServer.MessageManage.MessaggioAggiornaDatiGiocatore
     * @see risicammaraServer.MessageManage.MessaggioCambiaNickColore
     */
    AGGIORNADATIGIOCATORE,  //Il pacchetto ricevuto notifica un cambiamento nei dati giocatore
    /**
     * Il giocatore che riceve questo messaggio ha coem obbiettivo quello
     * assegnato dal server. Solo il server può inviare questo messaggio.
     * @see risicammaraServer.messaggiManage.MessaggioObbiettivo
     */
    AGGIORNAOBJGIOCATORE, //Aggiorna l'obbiettivo.
    /**
     * Aggiorna la fase del gioco.
     * @see risicammaraServer.messaggiManage.MessaggioFase
     */
    FASE,
    /**
     * Pacchetto che avvisa i giocatori di un avvenuto cambio nella lista giocatori.
     * @deprecated Inutilizabile (non serve);
     */
    AGGIORNALISTAGIOCATORI; //Il pacchetto ricevuto notifica un aggiornamento della lista dei giocatori

}
