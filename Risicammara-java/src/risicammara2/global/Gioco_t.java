package risicammara2.global;

/**
 * Enumerato che gestisce tutti i comandi appositamente studiati per il gioco.
 * @author stengun
 */
public enum Gioco_t {
    /**
     * Lancia i dadi dell'attacco.
     * getSender = Il giocatore che invia il messaggio
     * getOptParameter = Il numero di dadi con cui attaccare (Massimo 3 )
     */
    LANCIADADO,     //Lancia i dadi (usato anche per continuare ad attaccare)
    /**
     * Chi invia questo messaggio ha ritirato un proprio attacco prima
     * di lanciare i dadi.
     * getSender = Il giocatore che invia il messaggio
     */
    RITIRATI,       //Ritirati da un attacco
    /**
     * Indica che il giocatore è stato eliminato da un altro giocatore.
     * getSender = Giocatore che ha eliminato.
     * getOptParameter = Giocatore che è stato appena eliminato.
     */
    ELIMINATO,
    /**
     * Indica che è presente un vincitore.
     * getSender = Indice del vincitore.
     */
    VINCITORE,
    /**
     * Questo messaggio informa su quale giocatore sta giocando adesso il turno.
     * getSender = il giocatore che deve giocare.
     */
    TURNOFPLAYER,   // Indica che il giocatore ricevente è quello di turno.
    /**
     * Indica che una sessione di attacco è terminata.
     * getSender = Il giocatore che effettuava l'attacco
     * getOptParameter = Il giocatore che si difendeva.
     */
    ATTACCOTERMINATO, // Indica che l'attacco in corso è terminato
}
