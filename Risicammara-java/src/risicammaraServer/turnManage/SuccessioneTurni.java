package risicammaraServer.turnManage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import risicammaraClient.Bonus_t;
import risicammaraClient.Colore_t;
import risicammaraClient.Obbiettivi_t;
import risicammaraClient.territori_t;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraJava.turnManage.*;
import risicammaraServer.CodaMsg;
import risicammaraServer.Giocatore_Net;
import risicammaraServer.PlayerThread;
import risicammaraServer.messaggiManage.*;
import risicammaraServer.Server;

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

    private boolean saltare;
    private boolean spostaattacco;
    private boolean saltafine;
    private int indexvincente;

    /**
     * Costruttore per inizializzare correttamente le variabili per la successione
     * dei turni di Risicammara.
     * @param listaGiocatori La lista dei giocatori
     * @param coda coda per i messaggi ricevuti via rete.
     */
    public SuccessioneTurni(ListaPlayers listaGiocatori,CodaMsg coda){
        this.indexvincente = -1;
        saltafine = false;
        saltare = false;
        this.coda = coda;
        this.listaGiocatori = listaGiocatori;
        this.vincitore = false;
        this.conquistato = false;
        this.spostaattacco = false;
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
            Server.SpedisciMsgTutti(new MessaggioSequenzaGioco(
                                   partita.getSequenzaGioco().toArray(new Integer[partita.getNumeroGiocatori()]),
                                   partita.getGiocatoreTurnoIndice()), listaGiocatori, -1);
        } catch (IOException ex) {
            System.err.println(
                    "Errore nell'invio della Plancia a tutti i giocatori "
                    +ex.getMessage());
        }
        partita.preparaSequenza();
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
        int giotin = partita.getGiocatoreTurnoIndice();
        try{
            
            Server.SpedisciMsgTutti(new MessaggioFase(Fasi_t.PREPARTITA, giotin),
                    listaGiocatori, -1);            
            Server.SpedisciMsgTutti(MessaggioComandi.creaMsgTurnOfPlayer(giotin),
                                    listaGiocatori, -1);
        }
        catch (IOException ex){
            System.err.println("Errore nell'invio messaggio di inizio: "
                    +ex.getMessage());
        }
        try {
            inviaArmateDisponibili();
        } catch (IOException ex) {
            System.err.println("Errore invio armate disponibili inizio turno: "
                    +ex.getMessage());
        }

        // Ciclo principale
        while(!vincitore){
            Messaggio msgReceived = null;
            
            if(saltare){
                saltare = false;
            }
            else{
                msgReceived = coda.get();
                System.out.println("Tipo messaggio: "+msgReceived.getType().toString());
            }
//            if(msgReceived.getType() == messaggio_t.COMMAND
//                    && ((MessaggioComandi)msgReceived).getComando() == comandi_t.DISCONNECT){
//                return null;
//            }
            if(partita.getFase() != Fasi_t.FINETURNO && !validitaMessaggio(msgReceived)) continue;
            //Processa i messaggi finché non c'è un vincitore.
            //Il server legge il messaggio e lo smista a seconda del messaggio ricevuto.
            //es. se è un messaggio di chat viene processato subito, se è un messaggio da un giocatore non di turno
            //viene ignorato.Se è un messaggio dal giocatore giusto viene processato a seconda della fase
            //attuale di gioco (se è un messaggio premesso dalla fase)
            cicloFasi(msgReceived);
        }
        inviaMessaggioVincitore();

        return (Giocatore_Net) listaGiocatori.get(indexvincente);
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
        if(msgReceived.getType() == messaggio_t.COMMAND){
            if(((MessaggioComandi)msgReceived).getComando() == comandi_t.DISCONNECT){
                try {
                    if(msgReceived.getSender() == partita.getGiocatoreTurnoIndice()){
                        partita.ProssimoGiocatore();
                        spedisciMsgCambioTurno(partita.getGiocatoreTurnoIndice());
                    }
                    partita.eliminaPrimaOccorrenza((int)msgReceived.getSender());
                    Server.SpedisciMsgTutti(msgReceived, listaGiocatori,(int)msgReceived.getSender());
                } catch (IOException ex) {
                    System.err.println("Errore invio disconnessione in gioco.");
                }
                return false;
            }
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
        Fasi_t prossima_fase = null;
        int indice_prossimo_giocatore = partita.getGiocatoreTurnoIndice();
        int indice_giocatore_turno = partita.getGiocatoreTurnoIndice();
        Giocatore_Net oggetto_giocatore = (Giocatore_Net)partita.getGiocatoreDiTurno();
        PlayerThread thread_giocatore = (PlayerThread)oggetto_giocatore.getThread();


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
                int armatt = oggetto_giocatore.getArmateperturno()-1;
                oggetto_giocatore.setArmatedisponibili(armatt);

                if(armatt == 0) thread_giocatore.setMustpass(true);
                else thread_giocatore.incnumar();
                if(thread_giocatore.isMustpass()){
                    thread_giocatore.setMustpass(false);
                    partita.ProssimoGiocatore();
                    //Oggetto giocatore contiene un riferimento all'oggetto del giocatore che
                    // effettivamente giocherà il prossimo turno.
                    oggetto_giocatore = (Giocatore_Net) partita.getGiocatoreDiTurno();
                    indice_prossimo_giocatore = oggetto_giocatore.getPlayerIndex();
                    if(oggetto_giocatore.getArmateperturno() == 0){
                        prossima_fase = Fasi_t.FINETURNO;
                        saltafine = true;
                        saltare = true;
                        break;
                    }
                    spedisciMsgCambioTurno(indice_prossimo_giocatore);
                }
                return;
                // Ogni 3 territori una armata, per difetto. Possibilità di
                //giocare tris e prendere bonus se si ha continenti.
            case RINFORZO:
                if((msgReceived.getType() != messaggio_t.CAMBIAARMATETERRITORIO) 
                        && (msgReceived.getType() != messaggio_t.GIOCATRIS)
                        && (msgReceived.getType() != messaggio_t.FASE))
                    return;
                //Assegno le armate in base ai territori posseduti dal giocatore.
                if((msgReceived.getType() == messaggio_t.GIOCATRIS) 
                        && !partita.playedTris())
                {
                    int armate_bonus = getBonusFromTris(msgReceived);
                    int armatedispo = oggetto_giocatore.getArmateperturno();
                    int disp = armatedispo+armate_bonus;
                    oggetto_giocatore.setArmatedisponibili(disp);
                    try {
                        oggetto_giocatore.sendMessage(new MessaggioArmateDisponibili(disp, -1));
                        Server.SpedisciMsgTutti(msgReceived, listaGiocatori, indice_giocatore_turno);
                    } catch (IOException ex) {
                        System.err.println(
                                "Errore nell'invio Armate disponibili Tris: "
                                +ex.getMessage());
                    }
                    partita.setPlayedTris(true);
                    //scarto carte
                    MessaggioGiocaTris mes = (MessaggioGiocaTris)msgReceived;
                    Carta tempcard[] = new Carta[3];
                    tempcard[0] = mes.getCarta1();
                    tempcard[1] = mes.getCarta2();
                    tempcard[2] = mes.getCarta3();
                    for(Carta i : tempcard) {
                        oggetto_giocatore.remCarta(i);
                    }
                    partita.discardCarta(tempcard);
                    return;
                }
                int armattu = oggetto_giocatore.getArmateperturno();
                if(armattu == 0){
                    if(msgReceived.getType() ==  messaggio_t.FASE) {
                    MessaggioFase msgFase = (MessaggioFase) msgReceived;
                    prossima_fase = msgFase.getFase();
                    if(prossima_fase == Fasi_t.FINETURNO) saltare = true;
                    break;
                    }
                    break;
                }
                MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio)msgReceived;
                partita.addArmateTerritorio(msgArmate.getTerritorio(), msgArmate.getArmate());
                oggetto_giocatore.setArmatedisponibili(armattu-msgArmate.getArmate());
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
                if(oggetto_giocatore.getArmateperturno()==0){
                    partita.setPlayedTris(false);
                    prossima_fase = Fasi_t.ATTACCO;
                    break;
                }
                return;
                // Evetuale attacco. Uno o più attacchi vinti danno diritto a una
                //sola carta.
            case ATTACCO:
                if(!parseMsgAttacco(msgReceived)) return;
                if(msgReceived.getType() ==  messaggio_t.FASE) {
                    MessaggioFase msgFase = (MessaggioFase) msgReceived;
                    prossima_fase = msgFase.getFase();
                    if(prossima_fase == Fasi_t.FINETURNO) saltare = true;
                    break;
                }

                if(msgReceived.getType() == messaggio_t.SPOSTAARMATE){
                    MessaggioSpostaArmate msposta = (MessaggioSpostaArmate) msgReceived;
                    partita.spostamento(msposta.getSorgente(), msposta.getArrivo(), msposta.getNumarmate());
                    spostaattacco = false;
                    try {
                        Server.SpedisciMsgTutti(msgReceived, listaGiocatori, -1);
                    } catch (IOException ex) {
                        System.err.println("Errore invio spostamento dopo attacco: "
                                +ex.getMessage());
                    }
                    msgReceived = MessaggioComandi.creaMsgRitirati(indice_giocatore_turno);
                }
                // I messaggi  comando sono della fase "attaccando" e vengono accettati
                //in automatico dalla funzione precedente se si sta attaccando.
                //Sono praticamente sicuro che se entro qui e non sto attaccando allora
                //Passerò la fase e non farò altro.
                if(msgReceived.getType() != messaggio_t.DICHIARAATTACCO){
                    MessaggioComandi cmd = (MessaggioComandi)msgReceived;
                        switch(cmd.getComando()){
                            case LANCIADADO:
                                risolviAttacco((int)cmd.getOptParameter());
                                Giocatore_Net difensore = (Giocatore_Net) listaGiocatori.get(partita.getGiocattaccato());
                                if(partita.getArmateTerrDifensore() == 0){
                                    if(verificaEliminazione(difensore, oggetto_giocatore)) return;
                                    int armterrat = partita.getArmateTerrAttaccante() -1;
                                    if(armterrat > (int)cmd.getOptParameter()) armterrat = (int)cmd.getOptParameter();
                                    conquistato = true;
                                    spostaattacco = true;
                                    partita.attaccoVinto();
                                    partita.spostamento(
                                            partita.getTerritorioAttaccante(),
                                            partita.getTerritorioAttaccato(),
                                            armterrat);
                                    try {
                                        Server.SpedisciMsgTutti(
                                                new MessaggioAttaccoVinto(
                                                                armterrat,
                                                                partita.getTerritorioAttaccato()),
                                                listaGiocatori,
                                                -1);
                                    } catch (IOException ex) {
                                        System.err.println("Errore invio AttaccoVinto"+ex);
                                    }
                                    return;
                                }
                            case RITIRATI:
                                try {
                                  Server.SpedisciMsgTutti(
                                       MessaggioComandi.creaMsgAttaccoterminato(
                                                    indice_giocatore_turno,
                                                    partita.getGiocattaccato()),
                                       listaGiocatori,
                                       -1);
                                } catch (IOException ex) {
                                    System.err.println(
                               "Errore nell'invio messaggio attacco Terminato: "
                                            +ex.getMessage());
                                }
                                partita.setAttacking(false);
                                partita.setTerritorioAttaccante(null);
                                partita.setTerritorioAttaccato(null);
                                //partita.setGiocattaccato(-1);
                            default:
                                return;
                        }
                }
                MessaggioDichiaraAttacco msgatt = (MessaggioDichiaraAttacco)msgReceived;
                partita.setTerritorioAttaccante(msgatt.getTerritorio_attaccante());
                partita.setTerritorioAttaccato(msgatt.getTerritorio_difensore());
                partita.setAttacking(true);
                try {
                  Server.SpedisciMsgTutti(msgReceived,
                  listaGiocatori,
                  indice_giocatore_turno);
                } catch (IOException ex) {
                    System.err.println(
                           "Errore nell'invio del messaggio di inizio attacco: "
                           +ex.getMessage());
                }
                return;

                //Spostare armate da un territorio all'altro.
            case SPOSTAMENTO:
                //Da qui passano solo spostaarmate e passafase e Fase
                if(!validoSpostamento(msgReceived)) return;
                if(msgReceived.getType() != messaggio_t.SPOSTAARMATE){
                    prossima_fase = Fasi_t.FINETURNO;
                    saltare = true;
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
                prossima_fase = Fasi_t.FINETURNO;
                saltare = true;
                //Il giocatore sceglie di spostare n armate da un territorio all'altro.
                //Questa fase finisce se il giocatore passa all'altro turno o se effettua
                //Il suo spostamento.
                break;
                //Fine del turno
            case FINETURNO:
                //Se il giocatore ha conquistato un territorio allora pesca una carta.
                //Viene settato il indice_prossimo_giocatore giocatore.
                if(!saltafine){
                    if(conquistato){
                        conquistato = false;
                        Carta ctmp = partita.getCarta();
                        if(ctmp != null){
                            oggetto_giocatore.addCard(ctmp);
                            try {
                                oggetto_giocatore.sendMessage(new MessaggioCarta(ctmp, oggetto_giocatore.getPlayerIndex()));
                                Server.SpedisciMsgTutti(new MessaggioCarta(null, oggetto_giocatore.getPlayerIndex()), listaGiocatori, oggetto_giocatore.getPlayerIndex());
                            } catch (IOException ex) {
                                System.err.println("Errore invio carta pescata: "
                                        +ex.getMessage());
                            }
                            //TODO invio a tutti i giocatori "giocatore ha pescato la carta"
                        }
                    }
                    partita.ProssimoGiocatore();
                    
                }
                else saltafine = false;
                indice_prossimo_giocatore = partita.getGiocatoreTurnoIndice();
                prossima_fase = Fasi_t.RINFORZO;
                oggetto_giocatore = (Giocatore_Net) partita.getGiocatoreDiTurno();
            default:                
                break;
        }
        partita.setFase(prossima_fase);
        if(prossima_fase == Fasi_t.RINFORZO) {
            if(partita.isVincitore(oggetto_giocatore)) setVincitore(indice_prossimo_giocatore);
            spedisciMsgCambioTurno(indice_prossimo_giocatore);
            int abon = oggetto_giocatore.calcolaArmate();
            oggetto_giocatore.setArmatedisponibili(abon);
            try {
                oggetto_giocatore.sendMessage(new MessaggioArmateDisponibili(abon, -1));
            } catch (IOException ex) {
                System.err.println(
                        "Errore nell'invio Armate disponibili: "
                        +ex.getMessage());
            }
        }
                try {
            Server.SpedisciMsgTutti(new MessaggioFase( prossima_fase,
                    indice_prossimo_giocatore),
                    listaGiocatori,
                    -1);
            //messaggi per il cambio di fase
        } catch (IOException ex) {
            System.err.println("Errore nell'invio del messaggio di CambioFase: "
                    +ex.getMessage());
        }
    }
    /**
     * Verifica se c'è stata una eliminazione dopo l'attacco.
     * @param difensore Il giocatore che difendeva.
     * @param attaccante il giocatore che attaccava
     * @return True se il difensore è stato eliminato, false altrimenti.
     */
    private boolean verificaEliminazione(Giocatore_Net difensore,Giocatore_Net attaccante){
        if(difensore.getNumTerritori()-1 == 0){
            Colore_t eliminato = difensore.getArmyColour();
            if(partita.hasDistruggiArmate(attaccante)){
                if(vittoriaDistruzione(attaccante,eliminato)){
                    setVincitore(attaccante.getPlayerIndex());
                    return true;
                }
            }
            partita.eliminaGiocatoreAttaccato();
            partita.modificaDistruzione(eliminato);
            LinkedList<Carta> cartedif = difensore.getCarte();
            while(!cartedif.isEmpty()){
                try {
                    Server.SpedisciMsgTutti(new MessaggioCarta(null, -1), listaGiocatori, attaccante.getPlayerIndex());
                    attaccante.sendMessage(new MessaggioCarta(cartedif.poll(), -1));
               } catch (IOException ex) {
                    System.err.println("Errore nell'invio carte del difensore: "
                            +ex.getMessage());
               }
            }
            try {
                Server.SpedisciMsgTutti(
                        MessaggioComandi.creaMsgEliminato(
                            partita.getGiocatoreTurnoIndice(),
                            difensore.getPlayerIndex()),
                        listaGiocatori,
                        -1);
            } catch (IOException ex) {
                System.err.println(
                        "Errore nell'invio Messaggio Eliminato: "
                        +ex.getMessage());
            }

        }
        return false;
    }
    /**
     * Imposta il giocatore che ha vinto la partita.
     * @param vincito L'indice del giocatore che ha vinto la partita.
     */
    private void setVincitore(int vincito){
        this.indexvincente = vincito;
        this.vincitore = true;
    }
    /**
     * Invia il messaggio "vincitore" a tutti i giocatori.
     */
    private void inviaMessaggioVincitore(){
        try {
            Server.SpedisciMsgTutti(MessaggioComandi.creaMsgVincitore(indexvincente), listaGiocatori, -1);
        } catch (IOException ex) {
            System.err.println(
                    "Errore nell'invio del messaggio di vittoria a tutti i giocatori: "
                    +ex.getMessage());
        }
    }
    /**
     * Controlla se il giocatore che ha eliminato un altro aveva l'obbiettivo
     * di distruggere le armate.
     * @param oggetto_giocatore Il giocatore da controllare
     * @param eliminato Il colore delle armate eliminate
     * @return True se erano le armate da distruggere, false altrimenti.
     */
    private boolean vittoriaDistruzione(Giocatore gio,Colore_t eliminato){
        Obbiettivi_t ob = gio.getObbiettivo();
        switch(ob){
            case BLU:
                if(eliminato == Colore_t.BLU) return true;
                break;
            case GIALLO:
                if(eliminato == Colore_t.GIALLO) return true;
                break;
            case NERO:
                if(eliminato == Colore_t.NERO) return true;
                break;
            case ROSSO:
                if(eliminato == Colore_t.ROSSO) return true;
                break;
            case VERDE:
                if(eliminato == Colore_t.VERDE) return true;
                break;
            case VIOLA:
                if(eliminato == Colore_t.VIOLA) return true;
            default:
                break;
        }
        return false;
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
     * armate.
     * Invia un messaggio contenente tutti i risultati dell'attaccante e del difensore,
     * Già ordinati secondo le regole di risoluzione attacco.
     * @param numdadi Il numero dei dadi che lancia l'attaccante.
     */
    private void risolviAttacco(int numdadi){
        PriorityQueue<Integer> lancidifensore = new PriorityQueue<Integer>();
        PriorityQueue<Integer> lanciattaccante = new PriorityQueue<Integer>();
        //--------------------------------------- difesa
        int armdif = partita.getArmateTerrDifensore();
        if(armdif > 2) armdif = 3;
        for(int i=0;i<armdif;i++){
            int lancio = partita.lanciaDado();
            lancidifensore.offer(new Integer(-lancio));
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
            lanciattaccante.offer(new Integer(-lancio));
        }
        try {
            Server.SpedisciMsgTutti(
                    new MessaggioRisultatoLanci(lanciattaccante,
                                                lancidifensore,
                                                partita.getGiocatoreTurnoIndice(),
                                                partita.getGiocattaccato()),
                    listaGiocatori,
                    -1);
        }
        catch (IOException ex) {
            System.err.println("Errore nell'invio del risultato dei dadi Difensore: "+ex.getMessage());
        }
        int rimuovi_att = 0;
        int rimuovi_dif = 0;
        while(true){
            Integer lancioAtt = lanciattaccante.poll();
            Integer lancioDif = lancidifensore.poll();
            if(lancioAtt < lancioDif) rimuovi_dif++;
            else rimuovi_att++;
            if(lanciattaccante.isEmpty() || lancidifensore.isEmpty()) break;
        }
        //Rimozione delle armate
        if(rimuovi_att != 0){
            partita.removeArmateTerrAttaccante(rimuovi_att);
            try {
                Server.SpedisciMsgTutti(new MessaggioCambiaArmateTerritorio(partita.getGiocatoreTurnoIndice(),
                        -rimuovi_att,
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
                    -rimuovi_dif,
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
                default:
                    return false;
            }
            case FASE:
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
                if(partita.isAttacking() && spostaattacco) return false;
                if(partita.isAttacking()) return parseCmdAttaccando((MessaggioComandi)msg);
                return parseCommandAttacco((MessaggioComandi)msg);
            case SPOSTAARMATE:
                if(conquistato) return true;
                return false;
            case FASE:
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
            case RITIRATI:
            case LANCIADADO:
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
        List<territori_t> tergio = listaGiocatori.get((int)msg.getSender()).getListaterr();
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
 * Invia il messaggio di armate disponibili al giocatore.
 * @throws IOException lancia l'eccezione se ci sono problemi con il socket
 */
    private void inviaArmateDisponibili() throws IOException {
        for(int i=0;i<ListaPlayers.MAXPLAYERS;i++){
            Giocatore_Net g = (Giocatore_Net)listaGiocatori.get(i);
            if(g==null)continue;
            g.sendMessage(new MessaggioArmateDisponibili(g.getArmateperturno(), -1));
        }
    }
}
