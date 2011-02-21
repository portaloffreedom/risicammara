package risicammaraServer.turnManage;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraClient.Bonus_t;
import risicammaraClient.Continente_t;
import risicammaraClient.territori_t;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaServer;
import risicammaraServer.CodaMsg;
import risicammaraServer.Giocatore_Net;
import risicammaraServer.PlayerThread;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioCambiaArmateTerritorio;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioPlancia;
import risicammaraServer.messaggiManage.messaggio_t;
import risicammaraServer.Server;
import risicammaraServer.messaggiManage.MessaggioArmateDisponibili;
import risicammaraServer.messaggiManage.MessaggioDichiaraAttacco;
import risicammaraServer.messaggiManage.MessaggioFase;
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
    protected PartitaServer partita;
    /** Serve per stabilire se è presente un vincitore */
    protected boolean vincitore;
    /** Serve per stabilre se sta iniziando un nuovo giro dei giocatori. */
    //protected boolean nuovogiro; // Per vedere se devo controllare le condizioni di vittoria.
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
        this.vincitore = false;
        this.conquistato = false;
    };
    /**
     * Fa iniziare una successione turni (esattamente inizia una nuova partita)
     * @return L'oggetto Giocatore che ha vinto la partita.
     */
    public Giocatore_Net start(){
        //PRE partita
        partita = new PartitaServer(listaGiocatori);
        try {
            Server.SpedisciMsgTutti(
                    new MessaggioPlancia(partita.getPlancia()),
                    listaGiocatori,
                    -1);
        } catch (IOException ex) {
            System.err.println(
                    "Errore nell'invio della Plancia a tutti i giocatori "
                    +ex.getMessage());
        }
        //Assegna gli obbiettivi ai giocatori
        try {
            for(int i=0;i<ListaPlayers.MAXPLAYERS;i++){
                Giocatore_Net gi = (Giocatore_Net)listaGiocatori.get(i);
                if(gi == null) continue;
                gi.sendMessage(new MessaggioObbiettivo(gi.getObbiettivo()));
            }
        } catch (IOException ex) {
            System.err.println(
                    "Errore nell'invio della ListaGiocatori a tutti i client: "
                    +ex.getMessage());
        }
        //Ciclo dei turni
        try{
            int giotin = partita.getGiocatoreTurnoIndice();
            Server.SpedisciMsgTutti(new MessaggioFase(Fasi_t.PREPARTITA, -1),
                    listaGiocatori, -1);            
            Server.SpedisciMsgTutti(MessaggioComandi.creaMsgTurnOfPlayer(giotin),
                                    listaGiocatori, -1);
        }
        catch (IOException ex){
            System.err.println("Errore nell'invio messaggio di inizio: "
                    +ex.getMessage());
        }
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
     * l'azione corrispondente.
     * @param msgReceived il messaggio ricevuto
     */
    private void cicloFasi(Messaggio msgReceived){
        Fasi_t proxfase = null;
        int prossimo = -1;
        int gioint = partita.getGiocatoreTurnoIndice();
        Giocatore_Net gio = (Giocatore_Net)partita.getGiocatoreDiTurno();
        PlayerThread pthread = (PlayerThread)gio.getThread();


        switch(partita.getFase()){
            //tre armate ognuno finché non finiscono a tutti
            case PREPARTITA:
                if(msgReceived.getType()!=messaggio_t.CAMBIAARMATETERRITORIO) return;
                MessaggioCambiaArmateTerritorio mss
                        = (MessaggioCambiaArmateTerritorio)msgReceived;
                partita.addArmateTerritorio(mss.getTerritorio(), 1);
                try {
                    Server.SpedisciMsgTutti(msgReceived, listaGiocatori, -1);
                } catch (IOException ex) {
                    System.err.println("Errore invio aggiornaArmate: "
                            +ex.getMessage());
                }
                if(gio.isFirst()){
                    int armatt = gio.getArmateperturno();
                    gio.setArmatedisponibili(armatt);
                    try {
                            gio.sendMessage(new MessaggioArmateDisponibili(armatt, -1));
                        } catch (IOException ex) {
                            System.err.println(
                                    "Errore nell'invio Armate disponibili Tris: "
                                    +ex.getMessage());
                    }
                    gio.setFirst(false);
                }
                pthread.incnumar();
                if(pthread.isMustpass()){
                    partita.ProssimoGiocatore();
                    prossimo = partita.getGiocatoreTurnoIndice();
                    Giocatore tmp = partita.getGiocatoreDiTurno();
                    spedisciMsgCambioTurno(prossimo);
                    if(tmp.getArmateperturno() == 0){
                        proxfase = Fasi_t.RINFORZO;
                        if(!partita.isNuovogiro()) gio.setFirst(true);
                        break;
                    }
                }
                return;
                // Ogni 3 territori una armata, per difetto. Possibilità di
                //giocare tris e prendere bonus se si ha continenti.
            case RINFORZO:
                if((msgReceived.getType() != messaggio_t.CAMBIAARMATETERRITORIO) 
                        && (msgReceived.getType() != messaggio_t.GIOCATRIS))
                    return;
                //Assegno le armate in base ai territori posseduti dal giocatore.
                int armattu = gio.getArmateperturno();
                if(armattu == 0){
                    int abon = (gio.getNumTerritori()/3)+getTotalContinentalBonus(gio);
                    gio.setArmatedisponibili(abon);
                    try {
                        gio.sendMessage(new MessaggioArmateDisponibili(abon, -1));
                    } catch (IOException ex) {
                        System.err.println(
                                "Errore nell'invio Armate disponibili: "
                                +ex.getMessage());
                    }
                }
                if((msgReceived.getType() == messaggio_t.GIOCATRIS) 
                        && !partita.playedTris())
                {
                    int armate_bonus = getBonusFromTris(msgReceived);
                    int armatedispo = gio.getArmateperturno();
                    int disp = armatedispo+armate_bonus;
                    gio.setArmatedisponibili(disp);
                    try {
                        gio.sendMessage(new MessaggioArmateDisponibili(disp, -1));
                    } catch (IOException ex) {
                        System.err.println(
                                "Errore nell'invio Armate disponibili Tris: "
                                +ex.getMessage());
                    }
                    partita.setPlayedTris(true);
                }
                MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio)msgReceived;
                partita.addArmateTerritorio(msgArmate.getTerritorio(), msgArmate.getArmate());
                gio.setArmatedisponibili(armattu-msgArmate.getArmate());
                // Informo tutti del cambio armate.
                try {
                    Server.SpedisciMsgTutti(msgReceived, listaGiocatori, -1);
                } catch (IOException ex) {
                    System.err.println("Errore invio aggiornaArmate (rinforzo): "
                            +ex.getMessage());
                }
                //Assegno eventuali armate bonus per tris giocati (Permetto di giocare tris)
                //Permetti al giocatore di posizionare le sue armate
                //Il giocatore mette tutte le armate e quando ha finito si passa
                //Alla prossima fase.
                if(gio.getArmateperturno()==0){
                    partita.setPlayedTris(false);
                    proxfase = Fasi_t.ATTACCO;
                    break;
                }
                return;
                // Evetuale attacco. Uno o più attacchi vinti danno diritto a una
                //sola carta.
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
                                proxfase = Fasi_t.SPOSTAMENTO;
                                break;
                            case LANCIADADO:
                                risolviAttacco(cmd.getOptParameter());
                            //non uso return perché se no duplico codice.
                            //Ritirati è deprecato perché non esiste l'attacco perpetuo
                            //Si fa un attacco e ci si ritira
                            case RITIRATI:
                                partita.setAttacking(false);
                                partita.setTerritorioAttaccante(null);
                                partita.setTerritorioAttaccato(null);
                                partita.setGiocattaccato(-1);
                                try {
                                  Server.SpedisciMsgTutti(
                                       MessaggioComandi.creaMsgAttaccoterminato(
                                                                        gioint),
                                       listaGiocatori,
                                       -1);
                                } catch (IOException ex) {
                                    System.err.println(
                               "Errore nell'invio messaggio attacco Terminato: "
                                            +ex.getMessage());
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
                  Server.SpedisciMsgTutti(MessaggioComandi.creaMsgIniziaAttacco(
                                                    gioint,
                                                    partita.getGiocattaccato()),
                  listaGiocatori,
                  -1);
                } catch (IOException ex) {
                    System.err.println(
                           "Errore nell'invio del messaggio di inizio attacco: "
                           +ex.getMessage());
                }
                return;

                //Spostare armate da un territorio all'altro.
            case SPOSTAMENTO:
                //Da qui passano solo spostaarmate e passafase
                if(!validoSpostamento(msgReceived)) return;
                if(msgReceived.getType() != messaggio_t.SPOSTAARMATE){
                    proxfase = Fasi_t.FINETURNO;
                    break;
                }
                MessaggioSpostaArmate msgSpostamento = (MessaggioSpostaArmate)msgReceived;

                partita.spostamento(msgSpostamento.getSorgente(), 
                                    msgSpostamento.getArrivo(),
                                    msgSpostamento.getNumarmate());
                try {
                    Server.SpedisciMsgTutti(msgReceived, listaGiocatori, -1);
                } catch (IOException ex) {
                    System.err.print("Errore invio spostamento armate: "
                            +ex.getMessage());
                }
                //Il giocatore sceglie di spostare n armate da un territorio all'altro.
                //Questa fase finisce se il giocatore passa all'altro turno o se effettua
                //Il suo spostamento.
                break;
                //Fine del turno
            case FINETURNO:
                //Se il giocatore ha conquistato un territorio allora pesca una carta.
                //Viene settato il prossimo giocatore.
                if(conquistato){
                    Carta ctmp = partita.getCarta();
                    if(ctmp != null) gio.addCard(ctmp);
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
                prossimo = partita.getGiocatoreTurnoIndice();
                spedisciMsgCambioTurno(prossimo);
                proxfase = Fasi_t.RINFORZO;
            default:                
                break;
        }
        partita.setFase(proxfase);
        try {
            Server.SpedisciMsgTutti(new MessaggioFase( proxfase,
                    prossimo),
                    listaGiocatori,
                    -1);
            //messaggi per il cambio di fase
        } catch (IOException ex) {
            System.err.println("Errore nell'invio del messaggio di CambioFase: "
                    +ex.getMessage());
        }
    }
    /**
     * Spedisce a tutti i giocatori il messaggio di avvenuto cambio turno di
     * un giocatore e spedisce al giocatore di turno il messaggio che indica
     * che può iniziare a giocare.
     * @param giocatore_che_deve_giocare L'indice del giocatore che deve giocare
     */
    private void spedisciMsgCambioTurno(int giocatore_che_deve_giocare){
        try {
            Server.SpedisciMsgTutti(
              MessaggioComandi.creaMsgTurnOfPlayer(giocatore_che_deve_giocare),
              listaGiocatori,
              -1);
        } catch (IOException ex) {
            System.err.println(
                    "Errore nell'invio messaggi fine turno/ cambio turno: "
                    +ex.getMessage());
        }
    }

    /** Risolve l'attacco effettuando il lancio dei dadi e la rimozione delle
     * armate. */
    private void risolviAttacco(int numdadi){
        Queue<Integer> lancidifensore = new PriorityQueue<Integer>();
        Queue<Integer> lanciattaccante = new PriorityQueue<Integer>();
        //--------------------------------------- difesa
        int armdif = partita.getArmateTerrDifensore();
        if(armdif > 2) armdif = 3;
        for(int i=0;i<armdif;i++){
            int lancio = partita.lanciaDado();
            try {
                Server.SpedisciMsgTutti(
                        new MessaggioRisultatoDado(
                                                    lancio,
                                                    partita.getGiocattaccato()),
                        listaGiocatori,
                        -1);
            } catch (IOException ex) {
                System.err.println("Errore nell'invio del risultato dei dadi Difensore: "+ex.getMessage());
            }
            lancidifensore.offer(new Integer(lancio));
        }
        //-------------------------------------- attacco
        int att = partita.getArmateTerrAttaccante() -1;
        switch(numdadi){
            case 1:
            case 2:
            case 3:
                if(att >= numdadi) att = numdadi;
            default:
                break;
        }
        for(int i=0;i<att;i++){
            int lancio = partita.lanciaDado();
            try {
                Server.SpedisciMsgTutti(
                        new MessaggioRisultatoDado(
                                            lancio,
                                            partita.getGiocatoreTurnoIndice()
                                            ),
                        listaGiocatori,
                        -1);
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
            return tuno.getBonus().TrisValue(false) + bonus;
        }
        //bonus per carte diverse
        return tuno.getBonus().TrisValue(true) + bonus;
    }
/**
 * Calcola a quanto ammonta il numero di armate bonus per i continenti posseduti
 * dal giocatore.
 * @param gioc Il giocatore di cui calcolare le armate.
 * @return il totale delle armate bonus per continenti.
 */
    private int getTotalContinentalBonus(Giocatore gioc){
        int total = 0;
        for(Continente_t c:Continente_t.values()){
            if(gioc.hasContinente(c)){
                total += c.getArmate();
            }
        }
        return total;
    }
}
