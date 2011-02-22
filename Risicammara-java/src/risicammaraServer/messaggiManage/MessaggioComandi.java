package risicammaraServer.messaggiManage;

/**
 * Rappresenta l'invio di un comando client/server.
 * @author Sten_Gun
 */
public class MessaggioComandi implements Messaggio{

    private comandi_t comando;
    private int sender;
    private int opt_parameter;
// Qua ci vanno tutti i costruttori static per ogni tipo di messaggio, in modo da
    //facilitare la vita a chi crea i messaggi.
    /**
     * Crea un messaggio di inizio attacco
     * @param giocatorecheattacca l'indice di chi sta attaccando
     * @param giocatorecheDifende l'indice di chi viene attaccato
     * @return l'oggetto Messaggio IniziaAttacco.
     * @deprecated si usa MessaggioDichiaraAttacco
     */
    public static MessaggioComandi creaMsgIniziaAttacco(int giocatorecheattacca,int giocatorecheDifende){
        return new MessaggioComandi(comandi_t.INIZIAATTACCO, giocatorecheattacca, giocatorecheDifende);
    }
    /**
     * Messaggio che informa la fine di un attacco.
     * @param giocatorecheAttaccava il giocatore che effettuava l'attacco.
     * @return l'oggetto MessaggioAttaccoTerminato.
     */
    public static MessaggioComandi creaMsgAttaccoterminato(int giocatorecheAttaccava,int giocatoreCheDifendeva){
        return new MessaggioComandi(comandi_t.ATTACCOTERMINATO, giocatorecheAttaccava,giocatoreCheDifendeva);
    }
    /**
     * Lancia un certo numero di dadi
     * @param giocatorecheLanciaDado Il giocatore che lancia il dado
     * @param numerodadi il numero di dadi.
     * @return l'oggetto MessaggioLanciaDado
     */
    public static MessaggioComandi creaMsgLanciadado(int giocatorecheLanciaDado,int numerodadi){
        return new MessaggioComandi(comandi_t.LANCIADADO, giocatorecheLanciaDado,numerodadi);
    }
/**
 * Crea un messaggio dii ritiro attacco
 * @param giocatoreCheSiRitira il giocatore che si ritira (chi attaccava)
 * @return l'oggetto MessaggioRitirati
 */
    public static MessaggioComandi creaMsgRitirati(int giocatoreCheSiRitira){
        return new MessaggioComandi(comandi_t.RITIRATI, giocatoreCheSiRitira);
    }
    /**
     * Crea un messaggio di passaggio alla prossima fase
     * @param giocatoreChePassa il giocatore che passa alla fase successiva
     * @return l'oggetto MessaggioProssimaFase
     * @deprecated Usare MessaggioFase al posto di questo
     */
    public static MessaggioComandi creaMsgProssimaFase(int giocatoreChePassa){
        return new MessaggioComandi(comandi_t.PASSAFASE, giocatoreChePassa);
    }
    /**
     * Comando che informa il giocatore che può iniziare il suo turno.
     * @param giocatoreChePuoGiocare l'indice del giocatore che sta giocando e che può iniziare il turno.
     * @return l'oggetto MessaggioComandi con comando STARTYOURTURN
     * @deprecated
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
     * @param chi_invia Chi fa partire il server prima che siano tutti pronti (se non sei il lobby master il server rifiuterà il messaggio)
     * @return l'oggetto MessaggioComandi con comando AVVIAPARTITA
     */
    public static MessaggioComandi creaMsgAvviaPartita(int chi_invia){
        return new MessaggioComandi(comandi_t.AVVIAPARTITA, chi_invia);
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
     * @param opt_parameter l'indice di chi riceve questa azione(non può essere -1)
     */
    private MessaggioComandi(comandi_t cmd, int inviante, int opt_parameter){
        this.comando = cmd;
        this.sender = inviante;
        if((opt_parameter < -1) && (opt_parameter < 0)) opt_parameter=-2;
        this.opt_parameter = opt_parameter;
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
    public int getOptParameter(){
        return opt_parameter;
    }

    @Override
    public String toString() {
        return getType()+"|"+getComando();
    }


}