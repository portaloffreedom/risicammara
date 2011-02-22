package risicammaraJava.turnManage;

import risicammaraClient.territori_t;
import risicammaraJava.boardManage.Plancia;
import risicammaraJava.boardManage.TerritorioPlancia;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.Giocatore_Net;

/**
 *
 * @author Sten_Gun
 */
public abstract class GestionePartita {
    protected Plancia planciadigioco;
    protected ListaPlayers listagiocatori;
    protected int giocturno;
    protected Fasi_t fase_attuale;
    protected boolean giocato_tris;
    protected boolean attacking;
    protected boolean nuovogiro;
    protected int giocattaccato;
    protected territori_t territorioAttaccato,territorioAttaccante;

    protected GestionePartita(ListaPlayers listagiocatori){
        this.listagiocatori = listagiocatori;
        this.giocato_tris = false;
        this.attacking = false;
        this.territorioAttaccato = null;
        this.territorioAttaccante = null;
        this.giocattaccato = -1;
        this.nuovogiro = false;
        giocturno = 0;
        this.fase_attuale = Fasi_t.PREPARTITA;
    }

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
     * @deprecated Si imposta automaticamente da "territorio attaccato"
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
        if(terr==null){
            giocattaccato = -1;
            return;
        }
        this.giocattaccato = getProprietarioTerritorio(terr);
    }

    public void attaccoVinto(){
        if(territorioAttaccato == null) return;
        TerritorioPlancia tpla = planciadigioco.getTerritorio(territorioAttaccato);
        tpla.setProprietario(giocturno);
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
        TerritorioPlancia tpla = planciadigioco.getTerritorio(ter);
        return tpla.getProprietario();
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
        TerritorioPlancia temp = planciadigioco.getTerritorio(territorioAttaccante);
        int tmp = temp.getArmate();
        temp.setArmate(tmp-armate_da_rimuovere);
    }
    /**
     * Rimuovi armate dal territorio del difensore
     * @param armate_da_rimuovere le armate da rimuovere
     */
    public void removeArmateTerrDifensore(int armate_da_rimuovere){
        TerritorioPlancia temp = planciadigioco.getTerritorio(territorioAttaccato);
        int tmp = temp.getArmate();
        temp.setArmate(tmp-armate_da_rimuovere);
    }
    /**
     * Aggiungi delle armate in un territorio.
     * Usare numeri negativi per rimuovere le armate dal territorio.
     * @param territorio Il territorio dove aggiungere le armate
     * @param armate il numero di armate da aggiungere
     */
    public void addArmateTerritorio(territori_t territorio,int armate){
        TerritorioPlancia terpla = planciadigioco.getTerritorio(territorio);
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
        TerritorioPlancia tin = planciadigioco.getTerritorio(inizio);
        if(!tin.isAdiacent(arrivo)){
            System.err.println("Errore: Territorio non adiacente");
            return;
        }
        int armateinizio = tin.getArmate();
        if(armate >= armateinizio){
            System.err.println("Errore: armate spostate maggiori del dovuto");
            return;
        }
        TerritorioPlancia terrinizio = planciadigioco.getTerritorio(inizio);
        TerritorioPlancia terrarrivo = planciadigioco.getTerritorio(arrivo);
        terrarrivo.setArmate(terrarrivo.getArmate() + armate);
        terrinizio.setArmate(terrinizio.getArmate()-armate);
    }

        /**
     * Imposta la fase di gioco.
     * @param fase la nuova fase di gioco
     */
    public void setFase(Fasi_t fase){
        this.fase_attuale = fase;
    }
    /**
     * Informa sulla fase attuale del gioco
     * @return la fase attuale del gioco.
     */
    public Fasi_t getFase(){
        return fase_attuale;
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
            Giocatore_Net tmp = (Giocatore_Net)listagiocatori.getFirst(giocturno);
            if(tmp == null){
                giocturno = 0;
                return;
            }
            giocturno = tmp.getPlayerIndex();
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
}
