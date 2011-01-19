package risicammaraServer.messaggiManage;

/**
 * Rappresenta l'invio di un comando client/server.
 * @author Sten_Gun
 */
public class MessaggioComandi implements Messaggio{

    private comandi_t comando;
    private int sender;
    private int receiver;
// Qua ci vanno tutti i costruttori static per ogni tipo di messaggio, in modo da
    //facilitare la vita a chi crea i messaggi.
    public static MessaggioComandi creaMsgProssimaFase(int giocatoreChePassa){
        return new MessaggioComandi(comandi_t.PASSAFASE, giocatoreChePassa);
    }
    /**
     * Comando che informa il giocatore che può iniziare il suo turno.
     * @param giocatoreChePuoGiocare l'indice del giocatore che sta giocando e che può iniziare il turno.
     * @return l'oggetto MessaggioComandi con comando STARTYOURTURN
     */
    public static MessaggioComandi creaMsgStartYourTurn(int giocatoreChePuoGiocare){
        return new MessaggioComandi(comandi_t.STARTYOURTURN, -1, giocatoreChePuoGiocare);
    }

    /**
     * Informa tutti i giocatori tranne chi sta giocando chi è il giocatore di turno adesso.
     * @param giocatoreCheStaGiocando l'indice del giocatore che è di turno
     * @return  l'oggetto messaggioComandi con comando TURNOFPLAYER
     */
    public static MessaggioComandi creaMsgTurnOfPlayer(int giocatoreCheStaGiocando){
        return new MessaggioComandi(comandi_t.TURNOFPLAYER, giocatoreCheStaGiocando);
    }

    /**
     * Da ad un giocatore il potere di leader
     * @param giocatoreLeader il giocatore da far diventare leader
     * @return l'oggetto messaggioComandi con comando LEADER
     */
    public static MessaggioComandi creaMsgLeader(int giocatoreLeader){
        return new MessaggioComandi(comandi_t.LEADER, -1,giocatoreLeader);
    }
    /**
     * Crea un messaggioComandi per i giocatori che si connettono.
     * @param giocatoreConnesso l'indice del giocatore connesso
     * @return l'oggetto MessaggioComandi con comando CONNECTED
     */
    public static MessaggioComandi creaMSGconnected(int giocatoreConnesso){
        return new MessaggioComandi(comandi_t.CONNECTED, giocatoreConnesso);
    }

    /**
     * crea un MessaggioComandi per i giocaotri che si disconnettono dal server
     * @param giocatoreDisconnesso il giocatore che è uscito dal server
     * @return l'oggetto MessaggioComandi con comando DISCONNECT
     */
    public static MessaggioComandi creaMsgDisconnect(int giocatoreDisconnesso){
        return new MessaggioComandi(comandi_t.DISCONNECT, giocatoreDisconnesso);
    }
    /**
     * Costruisce un messaggioComandi per espellere un giocatore
     * @param giocatoreCheCalcia Chi toglie il giocatore (solo lobbymaster)
     * @param giocatoreEspulso Chi viene espulso
     * @return oggetto MessaggioComandi con comando KICKPLAYER
     */
    public static MessaggioComandi creaMsgKickplayer(int giocatoreCheCalcia, int giocatoreEspulso){
        return new MessaggioComandi(comandi_t.KICKPLAYER, giocatoreCheCalcia, giocatoreEspulso);
    }
    /**
     * Costruisce un messaggioComandi per una NuovaPartita
     * @param giocatoreCheCrea Chi fa partire il server prima che siano tutti pronti (se non sei il lobby master il server rifiuterà il messaggio)
     * @return l'oggetto MessaggioComandi con comando NUOVAPARTITA
     */
    public static MessaggioComandi creaMsgNuovaPartita(int giocatoreCheCrea){
        return new MessaggioComandi(comandi_t.NUOVAPARTITA, giocatoreCheCrea);
    }
    /**
     * Ritorna un nuovo messaggio comando EXIT
     * @param giocatoreCheToglieLobby il giocatore che lancia il comando (non funzionerà se non è il lobby master)
     * @return  l'oggetto MessaggioComandi con comando EXIT
     */
    public static MessaggioComandi creaMsgExit(int giocatoreCheToglieLobby){
        return new MessaggioComandi(comandi_t.EXIT, giocatoreCheToglieLobby);
    }

    /**
     * Ritorna un nuovo messaggio comando "SETPRONTO"
     * @param giocatorePronto il giocatore che è pronto.
     * @return oggetto MessaggioComandi con comando SETPRONTO
     */
    public static MessaggioComandi creaMsgSetPronto(int giocatorePronto){
        return new MessaggioComandi(comandi_t.SETPRONTO, giocatorePronto);
    }
    /**
     * Costruttore del pacchetto
     * @param cmd Il comando desiderato
     * @param inviante  il nome del giocatore che lo ha inviato (-1 se è di sistema)
     */
    private MessaggioComandi(comandi_t cmd, int inviante){
        this(cmd, inviante, -2);
    }

    /**
     * Costruttore alternativo del pacchetto
     * @param cmd il comando desiderato.
     * @param inviante l'indice del giocatore che invia il comando (-1 se è il sistema,-2 se null)
     * @param who l'indice di chi riceve questa azione(non può essere -1)
     */
    private MessaggioComandi(comandi_t cmd, int inviante, int who){
        this.comando = cmd;
        this.sender = inviante;
        if((who < -1) && (who < 0)) who=-2;
        this.receiver = who;
    }
/**
 * Indica che il pacchetto è di tipo COMMAND (da ::messaggi_t )
 * @return ::messaggio_t.COMMAND
 */
    public messaggio_t getType() {
        return messaggio_t.COMMAND;
    }

    /**
     * Fornisce il comando contenuto nel pacchetto.
     * @return Il comando del pacchetto.
     */
    public comandi_t getComando(){
        return comando;
    }
/**
 * Fornisce il nome di chi ha inviato il pacchetto
 * @return l'indice del giocatore che invia il comando.
 */
    public int getSender(){
        return sender;
    }
/**
 * Fornisce chi riceve l'azione di questo comando.
 * @return l'indice del giocatore che riceve l'azione.
 */
    public int getReceiver(){
        return receiver;
    }

    @Override
    public String toString() {
        return getType()+"|"+getComando();
    }


}
