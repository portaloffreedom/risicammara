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

/**
 * Questa classe ha il compito di inizializzare tutti gli oggetti che servono per
 * un nuovo gioco.
 * Inizializza una Plancia di gioco
 * Assegna ad ogni giocatore dei territori "a caso"
 * prepara il mazzo per giocare.
 * @author Sten_Gun
 */
public class Partita {
    private Plancia planciadigioco;
    private ListaPlayers listagiocatori;
    private MazzoTerritori mazzo;
    private int giocturno;
    private Fasi_t[] fasi;
    private int fase_attuale;

    /**
     * Costruttore ::Partita che inizializza tutti gli oggetti
     * @param listagiocatori L'oggetto che rappresenta la lista dei giocatori
     */
    public Partita(ListaPlayers listagiocatori){
        this.listagiocatori = listagiocatori;
        this.planciadigioco = new Plancia();
        this.mazzo = new MazzoTerritori();
        //Distribuzione territori per i giocatori
        territori_t car;
        int numgioc = listagiocatori.getSize();
        int gio = numgioc-1;
        int inc = 1;
                while(mazzo.getCard(inc)!= null){
                    Giocatore giocorrente = listagiocatori.get(gio);
                    car = mazzo.getCard(inc);
                    inc++;
                    while((car == territori_t.Jolly1)||(car == territori_t.Jolly2)){
                        car = mazzo.getCard(inc);
                        inc++;
                    }
                    if(car == null) break;
                    planciadigioco.getTerritorio(car).setProprietario(giocorrente);
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

    public Plancia getPlancia(){
        return planciadigioco;
    };
    public Giocatore getGiocatoreDiTurno(){
        return listagiocatori.get(giocturno);
    };
    public void ProssimoGiocatore(){
        if(giocturno == listagiocatori.getSize()) giocturno = 0;
        else giocturno++;
        return;
    };
    public void ProssimaFase(){
        if(fase_attuale < fasi.length) fase_attuale++;
        else fase_attuale = 0;
        return;
    };
    private Giocatore Vincitore(){
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

    @Deprecated
    public ListaPlayers getListaGiocatori() {
        return this.listagiocatori;
    }
}
