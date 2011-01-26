package risicammaraServer.turnManage;

import java.io.IOException;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.Partita;
import risicammaraServer.CodaMsg;
import risicammaraServer.Giocatore_Net;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioListaPlayers;
import risicammaraServer.messaggiManage.MessaggioPlancia;
import risicammaraServer.messaggiManage.messaggio_t;
import risicammaraServer.Server;
import risicammaraServer.messaggiManage.MessaggioSpostaArmate;

/**
 * Questa classe serve per rappresentare la successione dei turni di gioco.
 * Nella pratica rappresenta la partita e lo svolgersi degli eventi.
 * @author Sten_Gun
 */
public class SuccessioneTurni {
    private CodaMsg coda;
    private ListaPlayers listaGiocatori;
    /**
     * L'oggetto che conterrà la situazione attuale della partita.
     */
    protected Partita partita;
    /**
     * Serve per stabilire se è presente un vincitore
     */
    protected boolean vincitore;
    /**
     * Serve per stabilre se sta iniziando un nuovo giro dei giocatori.
     */
    protected boolean nuovogiro; // Per vedere se devo controllare le condizioni di vittoria.
    /**
     * Stabilisce se il giocatore ha conquistato uno o più territori nel suo turno.
     */
    protected boolean conquistato; //Se il giocatore ha conquistato almeno un territorio

    /**
     * Costruttore per inizializzare correttamente le variabili per la successione
     * dei turni di Risicammara.
     * @param listaGiocatori La lista dei giocatori
     * @param coda coda per i messaggi ricevuti via rete.
     */
    public SuccessioneTurni(ListaPlayers listaGiocatori,CodaMsg coda){
        this.coda = coda;
        this.listaGiocatori = listaGiocatori;
        this.nuovogiro = false;
        this.vincitore = false;
        this.conquistato = false;
    };
    /**
     * Fa iniziare una successione turni (esattamente inizia una nuova partita)
     * @return L'oggetto Giocatore che ha vinto la partita.
     */
    public Giocatore_Net start(){
        //PRE partita
        partita = new Partita(listaGiocatori);
        try {
            Server.SpedisciMsgTutti(new MessaggioPlancia(partita.getPlancia()), listaGiocatori, -1);
        } catch (IOException ex) {
            System.err.println("Errore nell'invio della Plancia a tutti i giocatori "+ex.getMessage());
        }

        try {
            Server.SpedisciMsgTutti(new MessaggioListaPlayers(listaGiocatori), listaGiocatori, -1);
        } catch (IOException ex) {
            System.err.println("Errore nell'invio della ListaGiocatori a tutti i client: "+ex.getMessage());
        }
        
        //Fase pre partita in cui tutti i giocatori mettono le loro armate preliminari nei territori.
        //Ogni giocatore a turno mette 3 armate dove vuole, il turno poi passa al prossimo giocatore.
        //Questo ciclo finisce quando tutti i giocatori non hanno più armate da mettere. (Se il prossimo giocatore non ha armate si termina)


        //Ciclo dei turni
        while(!vincitore){
            Messaggio msgReceived = coda.get();
            if(!validitaMessaggio(msgReceived)) continue;
            //Processa i messaggi finché non c'è un vincitore.
            //Il server legge il messaggio e lo smista a seconda del messaggio ricevuto.
            //es. se è un messaggio di chat viene processato subito, se è un messaggio da un giocatore non di turno
            //viene ignorato.Se è un messaggio dal giocatore giusto viene processato a seconda della fase
            //attuale di gioco (se è un messaggio premesso dalla fase)
            cicloFasi(msgReceived);
        }
        return null;
    }

    /**
     * Controlla la validità del messaggio in base al tipo (se è un messaggio di chat
     * viene mandato a tutti) e in base al giocatore di turno (se è un messaggio di un
     * giocatore diverso da quello che sta giocando devo ignorarlo completamente).
     * @param msgReceived il messaggio ricevuto;
     * @return true se il messaggio è valido,false altrimenti.
     */
    private boolean validitaMessaggio(Messaggio msgReceived){

        //Se è un messaggio di chat invialo a tutti e ritorna falso.(non è un messaggio di partita)
        if(msgReceived.getType() == messaggio_t.CHAT){
            try {
                Server.SpedisciMsgTutti(msgReceived, listaGiocatori, -1);
            } catch (IOException ex) {
                System.err.println("Errore nell'invio del messaggio di chat ai giocatori: "+ex.getMessage());
            }
            return false;
        }
        //se è un messaggio che non proviene dal giocatore di turno return false; (non è un messaggio di partita del giocatore)
        if(msgReceived.getSender() != partita.getGiocatoreTurnoIndice()) return false;
        //se è un messaggio che proviene dal giocatore di turno return true;
        return true;
    }

    /**
     * Stabilisce la validità del messaggio in base alla fase in cui ci troviamo
     * attualmente. Per ogni fase il messaggio viene processato e viene effettuata
     * l'azione corrispondente. (es: i messaggi di chat sono sempre accettati)
     * @param msgReceived il messaggio ricevuto
     */
    private void cicloFasi(Messaggio msgReceived){
        Giocatore gio = partita.getGiocatoreDiTurno();
        switch(partita.getFase()){
            case RINFORZO:
                if((msgReceived.getType() != messaggio_t.CAMBIAARMATETERRITORIO) &&
                        (msgReceived.getType() != messaggio_t.GIOCATRIS)) return;
                //Assegno le armate in base ai territori posseduti dal giocatore.
                int armattu = gio.getArmateperturno();
                if(armattu == 0){
                    gio.setArmatedisponibili(partita.getNumTerritoriGiocatoreTurno()/3);
                }
                if((msgReceived.getType() == messaggio_t.GIOCATRIS) 
                        && !partita.playedTris())
                {
                    int armate_bonus = getBonusFromTris(msgReceived);
                    int armatedispo = gio.getArmateperturno();
                    gio.setArmatedisponibili(armate_bonus+armatedispo);
                    partita.setPlayedTris(true);
                }
                MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio)msgReceived;
                partita.addArmateTerritorio(msgArmate.getTerritorio(), msgArmate.getArmate());
                gio.setArmatedisponibili(armattu-msgArmate.getArmate());
                //Assegno eventuali armate bonus per tris giocati (Permetto di giocare tris)
                //Permetti al giocatore di posizionare le sue armate
                //Il giocatore mette tutte le armate e quando ha finito si passa
                //Alla prossima fase.
                if(gio.getArmateperturno()==0){
                    partita.setPlayedTris(false);
                    break;
                }
                return;
            case ATTACCO:
                // Se il messaggio non è permesso RETURN.
                if(!parseMsgAttacco(msgReceived)) return;
                // Quando il messaggio è comando può essere solo il passa fase, quindi break.
                if(msgReceived.getType() != messaggio_t.DICHIARAATTACCO) break;


                //Dichiara di attaccare un territorio e gestisce tutte la
                //parte di attacco. Finché il giocatore non dichiara di aver finito l'attacco
                //si continua in questa fase, altrimenti si passa alla prossima.
                return;
            case SPOSTAMENTO:
                if(!validoSpostamento(msgReceived)) return;
                if(msgReceived.getType() != messaggio_t.SPOSTAARMATE) break;
                MessaggioSpostaArmate msgSpostamento = (MessaggioSpostaArmate)msgReceived;

                partita.spostamento(msgSpostamento.getSorgente(), 
                                    msgSpostamento.getArrivo(),
                                    msgSpostamento.getNumarmate());
                //Il giocatore sceglie di spostare n armate da un territorio all'altro.
                //Questa fase finisce se il giocatore passa all'altro turno o se effettua
                //Il suo spostamento.
                break;
            case FINETURNO:
                //Se il giocatore ha conquistato un territorio allora pesca una carta.
                //Viene settato il prossimo giocatore.
                if(conquistato){
                    Carta ctmp = partita.getCarta();
                    if(ctmp != null) listaGiocatori.get(partita.getGiocatoreTurnoIndice()).addCard(ctmp);
                }
                partita.ProssimoGiocatore();
                int gioturno = partita.getGiocatoreTurnoIndice();
                try {
                    Server.SpedisciMsgTutti(MessaggioComandi.creaMsgTurnOfPlayer(gioturno), listaGiocatori, gioturno);
                    Server.SpedisciMsgUno(MessaggioComandi.creaMsgStartYourTurn(gioturno), listaGiocatori, gioturno);
                } catch (IOException ex) {
                    System.err.println("Errore nell'invio messaggi fine turno e cambio turno: "+ex.getMessage());
                }
            default:
                break;
        }
        partita.ProssimaFase();
        try {
            Server.SpedisciMsgTutti(MessaggioComandi.creaMsgProssimaFase(
                    partita.getGiocatoreTurnoIndice()),
                    listaGiocatori,
                    -1);
            //messaggi per il cambio di fase
        } catch (IOException ex) {
            System.err.println("Errore nell'invio del messaggio di CambioFase");
        }
    }

    /**
     * Stabilisce se il messaggio è valido per la fase SPOSTAMENTO.
     * @param msg il messaggio da processare
     * @return true se il messaggio è valido, false se va scartato.
     */
    private boolean validoSpostamento(Messaggio msg){
        switch(msg.getType()){
            case COMMAND:
            MessaggioComandi cmd = ((MessaggioComandi)msg);
            switch(cmd.getComando()){
                case PASSAFASE:
                    break;
                default:
                    return false;
            }
            case SPOSTAARMATE:
                return true;
            default:
                return false;
        }
    }
    /**
     * Stabilisce se il messaggio è valido per la fase ATTACCO.
     * @param msg il messaggio da processare
     * @return true se il messaggio è valido, false se va scartato.
     */
    private boolean parseMsgAttacco(Messaggio msg){
        switch(msg.getType()){
            case COMMAND:
                return parseCommandAttacco((MessaggioComandi)msg);
            case DICHIARAATTACCO:
                return true;
            default:
                return false;
        }
    }
    /**
     * Stabilisce se il comando (MessaggioComandi) è valido per la fase ATTACCO.
     * @param msg il messaggio da processare
     * @return true se il messaggio è valido, false se va scartato.
     */
    private boolean parseCommandAttacco(MessaggioComandi msg){
        switch(msg.getComando()){
            case PASSAFASE:
                return true;
            default:
                return false;
        }
    }
    /**
     * Calcola il numero di armate bonus che ottieni dal tris giocato.
     * @param msg messaggio contenente i dati del tris.
     * @return numero delle armate ottenute dal tris.
     */
    //TODO completare la funzione che calcola il numero di armate bonus ottenute dai tris
    private int getBonusFromTris(Messaggio msg){
        return 1;
    }
}
