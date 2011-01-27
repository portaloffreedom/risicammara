/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    public int getGiocattaccato() {
        return giocattaccato;
    }
    public void setGiocattaccato(int giocattaccato) {
        this.giocattaccato = giocattaccato;
    }
    public boolean isAttacking() {
        return attacking;
    }
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    public void setTerritorioAttaccato(territori_t terr){
        this.territorioAttaccato = terr;
        this.giocattaccato = getProprietarioTerritorio(terr);
    }
    public territori_t getTerritorioAttaccato(){
        return this.territorioAttaccato;
    }
    public territori_t getTerritorioAttaccante() {
        return territorioAttaccante;
    }
    public void setTerritorioAttaccante(territori_t territorioAttaccante) {
        this.territorioAttaccante = territorioAttaccante;
    }

    public int getProprietarioTerritorio(territori_t ter){
        Territorio_plancia tpla = planciadigioco.getTerritorio(ter);
        return tpla.getProprietario();
    }
    public int getNumeroGiocatori(){
        return listagiocatori.getSize();
    }
    public Plancia getPlancia(){
        return planciadigioco;
    }
    public int getNumTerritoriGiocatoreTurno(){
        return listagiocatori.get(giocturno).getNumTerritori();
    }
    public boolean playedTris(){
        return giocato_tris;
    }
    public void setPlayedTris(boolean value){
        giocato_tris = value;
    }
    public void removeArmateTerrAttaccante(int armate_da_rimuovere){
        Territorio_plancia temp = planciadigioco.getTerritorio(territorioAttaccante);
        int tmp = temp.getArmate();
        temp.setArmate(tmp-armate_da_rimuovere);
    }
    public void removeArmateTerrDifensore(int armate_da_rimuovere){
        Territorio_plancia temp = planciadigioco.getTerritorio(territorioAttaccato);
        int tmp = temp.getArmate();
        temp.setArmate(tmp-armate_da_rimuovere);
    }
    public void addArmateTerritorio(territori_t territorio,int armate){
        Territorio_plancia terpla = planciadigioco.getTerritorio(territorio);
        int armate_attuali = terpla.getArmate();
        terpla.setArmate(armate+armate_attuali);
    }
    public int getArmateTerrAttaccante(){
        return getArmateTerritorio(territorioAttaccante);
    }
    public int getArmateTerrDifensore(){
        return getArmateTerritorio(territorioAttaccato);
    }
    public int getArmateTerritorio(territori_t territorio){
        return planciadigioco.getTerritorio(territorio).getArmate();
    }

    //metodi per azioni di gioco
    public Carta getCarta(){
        return mazzo.Pesca();
    }
    public void discardCarta(Carta scarti[]){
        for(Carta c : scarti) mazzo.AddDiscardedCard(c);
    }
    public void spostamento(territori_t inizio,territori_t arrivo,int armate){
        int armateinizio = planciadigioco.getTerritorio(inizio).getArmate();
        if(armate >= armateinizio){
            System.err.println("Errore: armate spostate maggiori del dovuto");
            return;
        }
        Territorio_plancia terrinizio = planciadigioco.getTerritorio(inizio);
        Territorio_plancia terrarrivo = planciadigioco.getTerritorio(arrivo);
        terrarrivo.setArmate(terrarrivo.getArmate() + armate);
        terrinizio.setArmate(terrinizio.getArmate()-armate);
    }
    public int lanciaDado(){
        return dado.RollDice();
    }
    //Metodi per le fasi
    public Fasi_t getFase(){
        return fasi[fase_attuale];
    }
    public Giocatore getGiocatoreDiTurno(){
        return listagiocatori.get(giocturno);
    };
    public int getGiocatoreTurnoIndice(){
        return giocturno;
    }
    public void ProssimoGiocatore(){
        for(int i=giocturno;i<ListaPlayers.MAXPLAYERS;++i){
            Giocatore tmp = listagiocatori.get(i);
            if(tmp == null)continue;
            giocturno = i;
            return;
        }
        giocturno = 0;
        return;
    };
    public void ProssimaFase(){
        if(fase_attuale < fasi.length) fase_attuale++;
        else fase_attuale = 0;
        return;
    };


    public Giocatore Vincitore(){
        int numpl = listagiocatori.getSize();
        for(int i = 0;i<numpl;i++){
            Giocatore giocat = listagiocatori.get(i);
            Obbiettivi_t obj = giocat.getObbiettivo();
            if(getVictoryType(obj) == tipovittoria_t.TERRITORIALE){
                if(Verifica_territoriale(giocat)) return giocat;
            }
            if(getVictoryType(obj) == tipovittoria_t.CONTINENTALE){
                if(Verifica_continentale(giocat)) return giocat;
            }
        }
        return null;
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
