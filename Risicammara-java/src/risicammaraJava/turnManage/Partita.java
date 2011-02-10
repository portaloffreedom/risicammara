package risicammaraJava.turnManage;

import risicammaraClient.Obbiettivi_t;
import risicammaraJava.boardManage.Plancia;
import risicammaraJava.deckManage.MazzoObbiettivi;
import risicammaraJava.deckManage.MazzoTerritori;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraClient.territori_t;
import risicammaraClient.tipovittoria_t;
import risicammaraJava.boardManage.Territorio_plancia;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.fightManage.Dado;

/**
 * Questa classe ha il compito di inizializzare tutti gli oggetti che servono per
 * un nuovo gioco.
 * Inizializza una Plancia di gioco
 * Assegna ad ogni giocatore dei territori "a caso"
 * prepara il mazzo per giocare.
 * fornsice tutte le azioni necessarie a modificare l'ambiente di gioco.
 * tiene traccia della fase corrente.
 * @author Sten_Gun
 */
public class Partita {
    private Plancia planciadigioco;
    private ListaPlayers listagiocatori;
    private MazzoTerritori mazzo;
    private int giocturno;
    private Fasi_t fasi[];
    private int fase_attuale;
    private boolean giocato_tris;
    private boolean attacking;
    private boolean nuovogiro;
    private Dado dado;
    private int giocattaccato;
    private territori_t territorioAttaccato,territorioAttaccante;
    /**
     * Costruttore Partita che inizializza tutti gli oggetti
     * @param listagiocatori L'oggetto che rappresenta la lista dei giocatori
     */
    public Partita(ListaPlayers listagiocatori){
        this.listagiocatori = listagiocatori;
        this.planciadigioco = new Plancia();
        this.mazzo = new MazzoTerritori();
        this.giocato_tris = false;
        this.attacking = false;
        this.dado = new Dado(6);
        this.territorioAttaccato = null;
        this.territorioAttaccante = null;
        this.giocattaccato = -1;
        this.nuovogiro = false;
        //Distribuzione territori per i giocatori
        territori_t car;
        int numgioc = listagiocatori.getSize();
        int gio = numgioc-1;
        int inc = 1;
                while(mazzo.getCard(inc)!= null){
                    Giocatore giocorrente = listagiocatori.get(gio);
                    while(giocorrente == null){
                        gio--;
                        giocorrente = listagiocatori.get(gio);
                    }
                    car = mazzo.getCard(inc);
                    inc++;
                    while((car == territori_t.Jolly1)||(car == territori_t.Jolly2)){
                        car = mazzo.getCard(inc);
                        inc++;
                    }
                    if(car == null) break;
                    planciadigioco.getTerritorio(car).setProprietario(gio);
                    giocorrente.addTerr(car);
                    giocorrente.setArmatedisponibili(NumeroArmate(numgioc)-giocorrente.getNumTerritori());
                    if(gio==0) gio = numgioc-1;
                    else gio--;
                }
        //Distribuzione obbiettivi
        MazzoObbiettivi mazzoobj = new MazzoObbiettivi();
        for(int i = 0; i<numgioc;i++) listagiocatori.get(i).setObj((Obbiettivi_t)mazzoobj.Pesca());
        giocturno = 0;

        this.fasi = Fasi_t.values();
        this.fase_attuale = 0;
    }

    //Metodi di partita (informazioni)

    /**
     * Ottiene il giocatore che viene attaccato
     * @return il giocatore difensore
     */
    public int getGiocattaccato() {
        return giocattaccato;
    }
    /**
     * imposta il giocatore che viene attaccato
     * @param giocattaccato l'indice del giocoatore che viene attaccato
     */
    public void setGiocattaccato(int giocattaccato) {
        this.giocattaccato = giocattaccato;
    }
    /**
     * Informa se c'è un attacco in corso
     * @return true se c'è un attacco, false altrimenti
     */
    public boolean isAttacking() {
        return attacking;
    }
    /**
     * Imposta lo stato di attacco.
     * @param attacking True se c'è un attacco, false altrimenti
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    /**
     * Imposta il territorio del difensore
     * @param terr il territorio del difensore
     */
    public void setTerritorioAttaccato(territori_t terr){
        this.territorioAttaccato = terr;
        this.giocattaccato = getProprietarioTerritorio(terr);
    }
    /**
     * restituisci il territorio del difensore
     * @return il territori odel difensore
     */
    public territori_t getTerritorioAttaccato(){
        return this.territorioAttaccato;
    }
    /**
     * restituisce il territorio dell'attaccante
     * @return territorio attaccato
     */
    public territori_t getTerritorioAttaccante() {
        return territorioAttaccante;
    }
    /**
     * Imposta il territorio da cui parte l'attacco
     * @param territorioAttaccante territorio da cui parte l'attacco
     */
    public void setTerritorioAttaccante(territori_t territorioAttaccante) {
        this.territorioAttaccante = territorioAttaccante;
    }

    /**
     * Chiedi il proprietario di un dato territorio
     * @param ter il territori odi cui cercare il proprietario
     * @return il proprietario del territorio
     */
    public int getProprietarioTerritorio(territori_t ter){
        Territorio_plancia tpla = planciadigioco.getTerritorio(ter);
        return tpla.getProprietario();
    }
    /**
     * Restituisce il numero di giocatori effettivi.
     * @return numero di giocatori.
     */
    public int getNumeroGiocatori(){
        return listagiocatori.getSize();
    }
    /**
     * Restituisce l'oggetto che rappresenta la plancia dei territori.
     * @return L'oggetto Plancia.
     */
    public Plancia getPlancia(){
        return planciadigioco;
    }
    /**
     * Restituisce il numero dei territori del giocatore di turno.
     * @return il numero di territori del gicoatore di turno
     */
    public int getNumTerritoriGiocatoreTurno(){
        return listagiocatori.get(giocturno).getNumTerritori();
    }
    /**
     * Controlla se è stato giocato un tris nel turno
     * @return true se è stato giocato, false altrimenti
     */
    public boolean playedTris(){
        return giocato_tris;
    }
    /**
     * Imposta se è stato giocato un tris nel turno.
     * @param value true se è stato giocato, false altrimenti
     */
    public void setPlayedTris(boolean value){
        giocato_tris = value;
    }
    /**
     * Rimuovi armate dal territorio attaccante
     * @param armate_da_rimuovere le armate da rimuovere
     */
    public void removeArmateTerrAttaccante(int armate_da_rimuovere){
        Territorio_plancia temp = planciadigioco.getTerritorio(territorioAttaccante);
        int tmp = temp.getArmate();
        temp.setArmate(tmp-armate_da_rimuovere);
    }
    /**
     * Rimuovi armate dal territorio del difensore
     * @param armate_da_rimuovere le armate da rimuovere
     */
    public void removeArmateTerrDifensore(int armate_da_rimuovere){
        Territorio_plancia temp = planciadigioco.getTerritorio(territorioAttaccato);
        int tmp = temp.getArmate();
        temp.setArmate(tmp-armate_da_rimuovere);
    }
    /**
     * Aggiungi delle armate in un territorio.
     * @param territorio Il territorio dove aggiungere le armate
     * @param armate il numero di armate da aggiungere
     */
    public void addArmateTerritorio(territori_t territorio,int armate){
        Territorio_plancia terpla = planciadigioco.getTerritorio(territorio);
        int armate_attuali = terpla.getArmate();
        terpla.setArmate(armate+armate_attuali);
    }
    /**
     * Numero di armate presenti nel territorio di chi sta attaccando
     * @return numero di armate dell'attaccante.
     */
    public int getArmateTerrAttaccante(){
        return getArmateTerritorio(territorioAttaccante);
    }
    /**
     * Numero di armate nel territorio di chi sta difendendo
     * @return il numero di armate.
     */
    public int getArmateTerrDifensore(){
        return getArmateTerritorio(territorioAttaccato);
    }
    /**
     * Fornisce le armate presenti in un territorio
     * @param territorio il territorio del quale controllare le armate
     * @return il numero di armate presenti nel territorio
     */
    public int getArmateTerritorio(territori_t territorio){
        return planciadigioco.getTerritorio(territorio).getArmate();
    }

    //metodi per azioni di gioco
    /**
     * Pesca una carta
     * @return La carta pescata
     */
    public Carta getCarta(){
        return mazzo.Pesca();
    }
    /**
     * Aggiungi le carte al mazzo degli scarti
     * @param scarti l'array di carte da scartare.
     */
    public void discardCarta(Carta scarti[]){
        for(Carta c : scarti) mazzo.AddDiscardedCard(c);
    }
    /**
     * Serve per effettuare uno spostamento sulla plancia di gioco.
     * Se i due territori non sono adiacenti o le armate sono in un numero
     * maggiore rispetto a quello del territorio iniziale, questa funzione
     * stampa un messaggio di errore. //TODO throw clause per lo spostamento
     * @param inizio Il territorio da cui parte lo spostamento
     * @param arrivo Il territorio su cui si vuole spostare le armate
     * @param armate il numero di armate da spostare.
     */
    public void spostamento(territori_t inizio,territori_t arrivo,int armate){
        Territorio_plancia tin = planciadigioco.getTerritorio(inizio);
        if(!tin.isAdiacent(arrivo)){
            System.err.println("Errore: Territorio non adiacente");
            return;
        }
        int armateinizio = tin.getArmate();
        if(armate >= armateinizio){
            System.err.println("Errore: armate spostate maggiori del dovuto");
            return;
        }
        Territorio_plancia terrinizio = planciadigioco.getTerritorio(inizio);
        Territorio_plancia terrarrivo = planciadigioco.getTerritorio(arrivo);
        terrarrivo.setArmate(terrarrivo.getArmate() + armate);
        terrinizio.setArmate(terrinizio.getArmate()-armate);
    }
    /**
     * Effettua il lanco di un dado.
     * @return il valore ottenuto con il lancio.
     */
    public int lanciaDado(){
        return dado.RollDice();
    }
    //Metodi per le fasi
    /**
     * Informa sulla fase attuale del gioco
     * @return la fase attuale del gioco.
     */
    public Fasi_t getFase(){
        return fasi[fase_attuale];
    }
    /**
     * Restituisce l'oggetto Giocatore del giocatore di turno.
     * @return
     */
    public Giocatore getGiocatoreDiTurno(){
        return listagiocatori.get(giocturno);
    };
    /**
     * Restituisce l'indice del giocatore di turno
     * @return
     */
    public int getGiocatoreTurnoIndice(){
        return giocturno;
    }
    /**
     * Passa al prossimo giocatore di turno
     */
    public void ProssimoGiocatore(){
        for(int i=giocturno;i<ListaPlayers.MAXPLAYERS;++i){
            Giocatore tmp = listagiocatori.get(i);
            if(tmp == null)continue;
            giocturno = i;
            return;
        }
        giocturno = 0;
        this.nuovogiro = true;
        return;
    };
    /**
     * Passa alla prossima fase di gioco
     */
    public void ProssimaFase(){
        if(fase_attuale < fasi.length) fase_attuale++;
        else fase_attuale = 1;
        return;
    };
    /**
     * Chiede se questo è un nuovo giro.
     * @return true se lo è, false altrimenti
     */
    public boolean isNuovogiro() {
        return nuovogiro;
    }
    /**
     * Imposta lo stato di NuovoGiro (se tutti i giocatori hanno terminato il
     * turno )
     * @param nuovogiro true se è un nuovo giro, false altrimenti
     */
    public void setNuovogiro(boolean nuovogiro) {
        this.nuovogiro = nuovogiro;
    }


    /**
     * Verifica qual è il giocatore che ha vinto per territori.
     * @return l'indice del giocatore che soddisfa i requisiti. se è minore di 0
     * allora non c'è nessun vincitore.
     */
    public int Vincitore(){
        for(int i = 0;i<ListaPlayers.MAXPLAYERS;i++){
            Giocatore giocat = listagiocatori.get(i);
            if(giocat == null) continue;
            Obbiettivi_t obj = giocat.getObbiettivo();
            if(getVictoryType(obj) == tipovittoria_t.TERRITORIALE){
                if(Verifica_territoriale(giocat)) return i;
            }
            if(getVictoryType(obj) == tipovittoria_t.CONTINENTALE){
                if(Verifica_continentale(giocat)) return i;
            }
        }
        return -1;
    };

    //METODI PRIVATI
    /**
     * Restituisce il numero di armate iniziali in base al numero giocatori.
     * @param numerogiocatori Il numero dei giocanti alla partita
     * @return Il numero di armate disponibili
     */
    private int NumeroArmate(int numerogiocatori){
        switch(numerogiocatori){
            case 6:
                return 20;
            case 5:
                return 25;
            case 4:
                return 30;
            case 3:
                return 35;
            default:
                return 0;
        }
    };

    //Funzioni per verificare le condizioni di vittoria in base all'obbiettivo
    private tipovittoria_t getVictoryType(Obbiettivi_t obj){
        return obj.VictoryType();
    }

    /**
     * Funzione che verifica se c'è stata una vittoria territoriale da parte del
     * giocatore passato come parametro.
     * @param gioc Il giocatore con la vittoria da verificare
     * @return Il riferimento al giocatore se ha vinto, null altrimenti.
     */
    private boolean Verifica_territoriale(Giocatore gioc){
        int numterritori = 24;
        if(gioc.getObbiettivo() == Obbiettivi_t.DICIOTTODUE){
                numterritori = 18;
                if(gioc.getNumTerritori() >= numterritori){
                    for(territori_t t : gioc.getListaterr()){
                        if(planciadigioco.getTerritorio(t).getArmate() < 2) return false;
                    }
                    return true;
                }
        }
        if(gioc.getNumTerritori() >= numterritori) return true;
        return false;
    };

    private boolean Verifica_continentale(Giocatore gioc){
        //TODO Completare la verifica vittoria per i continenti
        return false;
    };

    /**
     * Restituisce la lista giocatori della partita.
     * @deprecated La lista dei giocatori viene già gestita dal server.
     * @return Lista giocatori attuale
     */
    public ListaPlayers getListaGiocatori() {
        return this.listagiocatori;
    }
}
