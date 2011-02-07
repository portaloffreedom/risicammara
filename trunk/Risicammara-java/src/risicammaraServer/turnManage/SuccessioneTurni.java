package risicammaraServer.turnManage;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import risicammaraClient.Bonus_t;
import risicammaraClient.Obbiettivi_t;
import risicammaraClient.territori_t;
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
import risicammaraServer.messaggiManage.MessaggioDichiaraAttacco;
import risicammaraServer.messaggiManage.MessaggioGiocaTris;
import risicammaraServer.messaggiManage.MessaggioObbiettivo;
import risicammaraServer.messaggiManage.MessaggioRisultatoDado;
import risicammaraServer.messaggiManage.MessaggioSpostaArmate;

/**
 * Questa classe serve per rappresentare la successione dei turni di gioco.
 * Nella pratica rappresenta il controllo dei messaggi per la partita in base
 * allo svolgersi degli eventi.
 * @author Sten_Gun
 */
public class SuccessioneTurni {
    /** Coda che raccoglie tutti i messaggi che vanno processati dal server.*/
    private CodaMsg coda;
    /** La lista che rappresenta i giocatori presenti.*/
    private ListaPlayers listaGiocatori;
    /** L'oggetto che conterrà la situazione attuale della partita. */
    protected Partita partita;
    /** Serve per stabilire se è presente un vincitore */
    protected boolean vincitore;
    /** Serve per stabilre se sta iniziando un nuovo giro dei giocatori. */
    protected boolean nuovogiro; // Per vedere se devo controllare le condizioni di vittoria.
    /** Stabilisce se il giocatore ha conquistato uno o più territori nel suo turno. */
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
        //Assegna gli obbiettivi ai giocatori
        try {
            for(int i=0;i<ListaPlayers.MAXPLAYERS;i++){
                Giocatore_Net gi = (Giocatore_Net)listaGiocatori.get(i);
                if(gi == null) continue;
                Server.BroadcastMessage(new MessaggioObbiettivo(gi.getObbiettivo())
                                        , gi.getClientOut());
            }
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
     * viene mandato a tutti ma ritorna falso, in quanto non è un messaggio di partita)
     * e in base al giocatore di turno (se è un messaggio di un giocatore diverso
     * da quello che sta giocando devo ignorarlo completamente).
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
        if(msgReceived.getSender() != partita.getGiocatoreTurnoIndice()) return false;
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
                if(!parseMsgAttacco(msgReceived)) return;
                // I messaggi  comando sono della fase "attaccando" e vengono accettati
                //in automatico dalla funzione precedente se si sta attaccando.
                //Sono praticamente sicuro che se entro qui e non sto attaccando allora
                //Passerò la fase e non farò altro.
                if(msgReceived.getType() != messaggio_t.DICHIARAATTACCO){
                    MessaggioComandi cmd = (MessaggioComandi)msgReceived;
                        switch(cmd.getComando()){
                            case PASSAFASE:
                                break;
                            case LANCIADADO:
                                risolviAttacco();
                                if(partita.getArmateTerrAttaccante()>1) return;
                            case RITIRATI:
                                partita.setAttacking(false);
                                partita.setTerritorioAttaccante(null);
                                partita.setTerritorioAttaccato(null);
                                partita.setGiocattaccato(-1);
                                try {
                                    Server.SpedisciMsgTutti(MessaggioComandi.creaMsgAttaccoterminato(partita.getGiocatoreTurnoIndice()),
                                            listaGiocatori,
                                            -1);
                                } catch (IOException ex) {
                                    System.err.println("Errore nell'invio messaggio attacco Terminato: "+ex.getMessage());
                                }
                            default:
                                return;
                        }
                }
                MessaggioDichiaraAttacco msgatt = (MessaggioDichiaraAttacco)msgReceived;
                partita.setTerritorioAttaccante(msgatt.getTerritorio_attaccante());
                partita.setTerritorioAttaccato(msgatt.getTerritorio_difensore());
                partita.setAttacking(true);
                try {
                    Server.SpedisciMsgTutti(MessaggioComandi.creaMsgIniziaAttacco(partita.getGiocatoreTurnoIndice(),
                            partita.getGiocattaccato()),
                            listaGiocatori,
                            -1);
                } catch (IOException ex) {
                    System.err.println("Errore nell'invio del messaggio di inizio attacco: "+ex.getMessage());
                }
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
                if(partita.isNuovogiro()){
                    int vitt = partita.Vincitore();
                    if(vitt >=0){
                        //TODO inviare il messaggio di vittoria a tutti
                        System.out.println("Il vincitore è il giocatore "+vitt);
                        return;
                    }
                }
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
    /** Risolve l'attacco effettuando il lancio dei dadi e la rimozione delle armate. */
    private void risolviAttacco(){
        Queue<Integer> lancidifensore = new PriorityQueue<Integer>();
        Queue<Integer> lanciattaccante = new PriorityQueue<Integer>();
        //--------------------------------------- difesa
        int armdif = partita.getArmateTerrDifensore();
        if(armdif > 2) armdif = 3;
        for(int i=0;i<armdif;i++){
            int lancio = partita.lanciaDado();
            try {
                Server.SpedisciMsgTutti(new MessaggioRisultatoDado(lancio,partita.getGiocattaccato()),
                        listaGiocatori,
                        -1);
            } catch (IOException ex) {
                System.err.println("Errore nell'invio del risultato dei dadi Difensore: "+ex.getMessage());
            }
            lancidifensore.offer(new Integer(lancio));
        }
        //-------------------------------------- attacco
        int att = partita.getArmateTerrAttaccante() -1;
        if(att >3) att = 3;
        for(int i=0;i<att;i++){
            int lancio = partita.lanciaDado();
            try {
                Server.SpedisciMsgTutti(new MessaggioRisultatoDado(lancio,partita.getGiocatoreTurnoIndice()),
                        listaGiocatori,
                        i);
            } catch (IOException ex) {
                System.err.println("Errore nell'invio del risultato dei dadi Attaccante: "+ex.getMessage());
            }
            lanciattaccante.offer(new Integer(lancio));
        }
        int rimuovi_att = 0;
        int rimuovi_dif = 0;
        while(true){
            Integer lancioAtt = lanciattaccante.poll();
            Integer lancioDif = lancidifensore.poll();
            if(lancioAtt > lancioDif) rimuovi_dif++;
            else rimuovi_att++;
            if(lanciattaccante.isEmpty() || lancidifensore.isEmpty()) break;
        }
        //Rimozione delle armate
        if(rimuovi_att != 0){
            partita.removeArmateTerrAttaccante(rimuovi_att);
            try {
                Server.SpedisciMsgTutti(new MessaggioCambiaArmateTerritorio(partita.getGiocatoreTurnoIndice(),
                        partita.getArmateTerrAttaccante(),
                        partita.getTerritorioAttaccante()),
                        listaGiocatori,
                        -1);
            } catch (IOException ex) {
                System.err.println("Errore nella notifica di cambio armate territorio attaccante: "+ex.getMessage());
            }
        }
        if(rimuovi_dif != 0){
            partita.removeArmateTerrDifensore(rimuovi_dif);
            try{
            Server.SpedisciMsgTutti(new MessaggioCambiaArmateTerritorio(partita.getGiocattaccato(),
                    partita.getArmateTerrDifensore(),
                    partita.getTerritorioAttaccato()),
                    listaGiocatori,
                    -1);
            }
            catch (IOException ex){
                System.err.println("Errore nella notifica di cambio armate territorio difensore: "+ex.getMessage());
            }
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
                if(partita.isAttacking()) return parseCmdAttaccando((MessaggioComandi)msg);
                return parseCommandAttacco((MessaggioComandi)msg);
            case DICHIARAATTACCO:
                if(partita.isAttacking()) return false;
                return true;
            default:
                return false;
        }
    }
    /**
     * Stabilisce se un Comando è accettabile per l'attacco vero e proprio oppure
     * no. Vengono accettati:
     * -Comando RITIRATI
     * -Comando LANCIADADI
     * @param msg il comando da analizzare
     * @return vero se è accettabile, falso altrimenti.
     */
    private boolean parseCmdAttaccando(MessaggioComandi msg){
        switch(msg.getComando()){
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
    private int getBonusFromTris(Messaggio msg){
        MessaggioGiocaTris msgTris = (MessaggioGiocaTris)msg;
        List<territori_t> tergio = listaGiocatori.get(msg.getSender()).getListaterr();
        int bonus = 0;
        territori_t tuno = (territori_t)msgTris.getCarta1();
        if(tergio.contains(tuno)) bonus += 2;
        territori_t tdue = (territori_t)msgTris.getCarta2();
        if(tergio.contains(tdue)) bonus +=2;
        territori_t ttre = (territori_t)msgTris.getCarta3();
        if(tergio.contains(ttre)) bonus +=2;
        //Bonus per jolly e 2 carte uguali
        if(        tuno.getBonus() == Bonus_t.JOLLY
                || tdue.getBonus() == Bonus_t.JOLLY
                || ttre.getBonus() == Bonus_t.JOLLY)
        {
            return 12 + bonus;
        }
        //Bonus per le carte uguali
        if(     tuno.getBonus() == tdue.getBonus()
                && tdue.getBonus() == ttre.getBonus())
        {
            switch(tuno.getBonus()){
                case CANNONE:
                    return 4 + bonus;
                case FANTE:
                    return 6 + bonus;
                case CAVALLO:
                    return 8 + bonus;
                default:
                    return 0;
            }
        }
        //bonus per carte diverse
        return 10 + bonus;
    }
}
