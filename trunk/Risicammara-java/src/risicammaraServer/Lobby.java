package risicammaraServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioAddPlayer;
import risicammaraServer.messaggiManage.MessaggioAggiornaDatiGiocatore;
import risicammaraServer.messaggiManage.MessaggioCambiaNickColore;
import risicammaraServer.messaggiManage.MessaggioChat;
import risicammaraServer.messaggiManage.MessaggioConfermaNuovoGiocatore;
import risicammaraServer.messaggiManage.MessaggioNuovoGiocatore;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioErrore;
import risicammaraServer.messaggiManage.errori_t;

/**
 * La classe che rappresenta un oggetto "lobby" (sala d'attesa)
 * La lobby accetta le connessioni di massimo 6 giocatori contemporanei,
 * superato questo limite il thread che ascolta le connessioni entra in wait e sarà svegliato soltanto
 * quando i giocatori saranno meno di 6.
 * @author Sten_Gun
 */
public class Lobby {

    private ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;
    private boolean inizia;
    AscoltatoreLobby attendiConnessioni;

    /**
     * Inizializza tutte le variabili necessarie.
     * @param porta la porta su cui aprire in ascolto il server
     * @param coda la coda dove verranno immessi i messaggi da processare
     */
    public Lobby (int porta, CodaMsg coda) {
        this.porta = porta;
        this.coda = coda;
        this.listaGiocatori = new ListaPlayers();
        this.inizia = false;
    }

    /**
     * Avvia la gestione dei messaggi da parte del server.
     * Il server attende che vi sia un messaggio sulla coda dei messaggi (che in questo caso
     * è come se fosse un buffer di messaggi).
     * Questa funzione esce soltanto quando un giocatore preme "nuova partita". A quel punto
     * i giocatori che fanno parte della lobby diventeranno i giocatori della partita vera e propria
     * e questa lista deve essere restituita dalla funzione.
     * @return la lista dei giocatori finale.
     */
    public ListaPlayers start(){
        attendiConnessioni = new AscoltatoreLobby(this.porta, this.coda);
        attendiConnessioni.start();
        Messaggio msg = null;
        while (!inizia) {
         msg = coda.get();
         System.out.println("Tipo messaggio: "+msg.getType().toString());
          //System.out.println("Sono fuori");
         int escludi = -1;
         Messaggio ctt = null;
            switch (msg.getType()) {
                // Processa i vari tipi di pacchetto possibili
                case MODIFICANICKCOLORE:
                    MessaggioCambiaNickColore mnick = (MessaggioCambiaNickColore)msg;
                    Giocatore_Net giotmp = (Giocatore_Net)listaGiocatori.get(msg.getSender());
                    giotmp.setNome(mnick.getNick());
                    giotmp.setArmyColour(mnick.getColore());
                    ctt = new MessaggioAggiornaDatiGiocatore(mnick.getNick(), mnick.getColore(), mnick.getSender());
                    escludi = mnick.getSender();
                    break;
                case AGGIUNGIGIOCATORE:
                    MessaggioNuovoGiocatore mgio = (MessaggioNuovoGiocatore)msg;
                    Giocatore_Net gioctemp = new Giocatore_Net(mgio.getConnessioneGiocatore());
                    if(listaGiocatori.getSize() > 5){
                        try {
                            //TODO fare il costruttore di MessaggioErrore connection refused
                            Server.BroadcastMessage(new MessaggioErrore(errori_t.CONNECTIONREFUSED, -1),gioctemp.getClientOut() );
                            mgio.getConnessioneGiocatore().close();
                        } catch (IOException ex) {
                            System.err.println("Errore di invio errore connessione: "+ex.getMessage());
                        }
                        finally{
                            ctt = null;
                        }
                        break;
                    }
                    gioctemp.setArmyColour(Colore_t.NULLO); //TODO probabilmente si può togliere perché non fa niente (è già nullo il colore)
                    // Indice nel quale viene inserito il giocatore
                    int plynumb = listaGiocatori.addPlayer(gioctemp);
                    PlayerThread gioth = new PlayerThread(coda,gioctemp.getClientIn(),plynumb);
                    gioctemp.setNome("Giocatore"+plynumb);
                    gioctemp.AssignThread(gioth);
                    if(!gioth.isAlive()) gioth.start();
                    try {
                        ObjectOutputStream os = (gioctemp.getClientOut());
                        os.writeObject(new MessaggioConfermaNuovoGiocatore(listaGiocatori,plynumb));
                        os.flush();
                    } catch (IOException ex) {
                        System.out.println("Errore nel creare lo Stream di output verso il nuovo utente o di invio del messaggio: "+ex);
                        System.exit(-1);
                    }
                    escludi = plynumb;
                    attendiConnessioni.setNumeroGiocatori(listaGiocatori.getSize());
                    ctt = new MessaggioAddPlayer(gioctemp);
                    break;
                case COMMAND:
                    ctt = CommandHandling((MessaggioComandi)msg);
                    escludi = msg.getSender();
                    break;
                case ERROR:
                    ctt = ErrorHandling((MessaggioErrore)msg);
                    break;
                case CHAT:
                    ctt = msg;
                    break;
                default:
                    ctt = new MessaggioChat(-1, "Messaggio non ancora gestito");
                    break;
                }
                //Stampa il messaggio di chat corrispondente e lo invia a tutti.
                    System.out.println(ctt.toString());
          if(ctt!= null){
                try {
                    Server.SpedisciMsgTutti(ctt, listaGiocatori, escludi);
                } catch (IOException ex) {
                    System.err.println("Errore nell'invio messaggio a tutti i giocatori (Lobby) "+ex.getMessage());
                }
          }

                    // Quando c'è un solo giocatore quello è il leader della lobby
            if(listaGiocatori.getSize() == 1){
                        Giocatore_Net gtmp = (Giocatore_Net)listaGiocatori.getFirst();
                        if(!gtmp.isLeader()){
                            gtmp.setLeader(true);
                            try {
                                Server.BroadcastMessage(MessaggioComandi.creaMsgLeader(gtmp.getPlayerIndex()), gtmp.getClientOut());
                            } catch (IOException ex) {
                                System.err.println("Errore nell'invio di \"LEADER\" al primo giocatore "+ex.getMessage());
                            }
                        }
            }
        }
        try {
            Server.SpedisciMsgTutti(MessaggioComandi.creaMsgAvviaPartita(-1), listaGiocatori, -1);
        } catch (IOException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaGiocatori;
    }

    /**
    * Funzione che gestisce i messaggi di tipo MessaggioErrore per
    * la funzione receiveMessage
    * @param errorMsg Il pacchetto MessaggioErrore
    */
    private Messaggio ErrorHandling(MessaggioErrore errorMsg){
       switch(errorMsg.getError()){
           //TODO completare la gestione messaggioErrore del server
           default:
               return new MessaggioChat(-1,"Errore non gestito.");
       }
    }

    /**
    * Funzione che gestisce i messaggi di tipo MessaggioComando per la costruzione della risposta.
    * @see risicammaraServer.Lobby
    * @param cmdMsg il pacchetto Messaggio_Comando
    */
    private Messaggio CommandHandling(MessaggioComandi cmdMsg){
        //TODO Completare il codice di Exit.
        //TODO Completare il codice di AVVIAPARTITA
        switch(cmdMsg.getComando()){
            case SETPRONTO:
                serverSetPronto(cmdMsg.getSender());
                break;
            case KICKPLAYER:
                serverPlayerRemove(cmdMsg.getReceiver());
                break;
            case DISCONNECT:
                serverPlayerRemove(cmdMsg.getSender());
                listaGiocatori.remPlayer(cmdMsg.getSender());
                break;
            default:
                return new MessaggioChat(-1,"comando non riconosciuto.");
        }
        return cmdMsg;
    }
    /**
     * Processa un messaggio di tipo "SETPRONTO" e "SETNOPRONTO"
     * impostando il thread al corretto valore
     * @param sender Chi ha inviato il messaggio
     * @param ready Se sei pronto o meno.
     */
    private void serverSetPronto(int sender){
            Giocatore_Net giotmp = (Giocatore_Net)listaGiocatori.get(sender);
            if(giotmp.isReady()) giotmp.setReady(false);
            else
            {
                giotmp.setReady(true);
                if(allReady()) this.inizia = true;
            }
    }

    /**
     * Processa un messaggio che implica la rimozione di un giocatore.
     * Questa funzione è valida anche per un messaggio di tipo "Kickplayer" e "Disconnect"
     * @param index Il giocatore da eliminare.
     */
    private void serverPlayerRemove(int index){
            Giocatore_Net tempgioc = (Giocatore_Net)listaGiocatori.get(index);
            //se il giocatore che va via è leader bisogna sceglierne un altro
            if(tempgioc.isLeader())
            {
                if(listaGiocatori.getSize()-1 > 0){
                    Giocatore_Net gtm = ((Giocatore_Net)listaGiocatori.getFirst(index));
                        gtm.setLeader(true);
                        try {
                            Server.BroadcastMessage(MessaggioComandi.creaMsgLeader(gtm.getPlayerIndex()), gtm.getClientOut());
                        } catch (IOException ex) {
                            System.err.println("Errore nell'invio leader "+ex.getMessage());
                        }
                }
            }
            attendiConnessioni.setNumeroGiocatori(listaGiocatori.getSize());
            try {
                tempgioc.closeSocket();
            } catch (Exception ex) {
                System.err.println("Errore socket Disconnessione: "+ex.getMessage());
            }
    }

    /**
     * Controlla se tutti i giocatori hanno premuto ready
     * @return True se tutti sono pronti, false altrimenti
     */
    private boolean allReady(){
        if(listaGiocatori.getSize() < 3) return false;
        int i = -1;
        while(true){
            Giocatore_Net temp = (Giocatore_Net)listaGiocatori.getFirst(i++);
            if(temp == null) break;
            if(temp.isReady()) continue;
            return false;
        }
        return true;
    }

}
