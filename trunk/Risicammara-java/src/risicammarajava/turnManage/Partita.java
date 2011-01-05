/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava.turnManage;

import java.util.List;
import risicammarajava.Obbiettivi_t;
import risicammarajava.boardManage.Plancia;
import risicammarajava.boardManage.Territorio_plancia;
import risicammarajava.deckManage.MazzoObbiettivi;
import risicammarajava.deckManage.MazzoTerritori;
import risicammarajava.playerManage.Giocatore;
import risicammarajava.playerManage.ListaPlayers;
import risicammarajava.territori_t;
import risicammarajava.tipovittoria_t;

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
     * Costruttore ::Partita che inizializza tutti gli oggetti in modo da prepararli
     * all'utilizzo da parte di altri oggetti dentro il programma principale.
     * I parametri che vengono passati possono anche non essere inizializzati.
     * @param planciadigioco L'oggetto che rappresenta la plancia di gioco
     * @param listagiocatori L'oggetto che rappresenta la lista dei giocatori
     * @param mazzo L'oggetto che rappresenta il mazzo
     */
    public Partita(ListaPlayers listagiocatori){
        this.listagiocatori = listagiocatori;
        this.planciadigioco = new Plancia();
        this.mazzo = new MazzoTerritori();
        territori_t car = mazzo.getCard(1);
        int mult = 1;
        int numgioc = listagiocatori.getSize();
        //Distribuzione territori e armate
        for(int i=numgioc-1;i>=0;i--){
            Giocatore giocatorediturno = listagiocatori.get(i);
            while(car!=null){
                 if(!((car == territori_t.Jolly1)|(car == territori_t.Jolly2))){
                     mult+=numgioc;
                     Territorio_plancia terpla = planciadigioco.getTerritorio(car);
                     giocatorediturno.addTerr(terpla);
                     terpla.setProprietario(giocatorediturno);
                     car = mazzo.getCard(i+mult);
                 }
                 else{
                     mult+=1;
                     car = mazzo.getCard(i+mult);
                 }
             }
             giocatorediturno.setArmatedisponibili(NumeroArmate(numgioc)-giocatorediturno.getNumTerritori());
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
                if(Verifica_territoriale(giocat,obj)) return giocat;
            }
            if(getVictoryType(obj) == tipovittoria_t.CONTINENTALE){
                if(Verifica_continentale(giocat,obj)) return giocat;
            }
        }
        return null;
    };
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

    private boolean Verifica_territoriale(Giocatore gioc,Obbiettivi_t obj){
        int numterritori = 24;
        switch(obj){
            case DICIOTTODUE:
                numterritori = 18;
                if(gioc.getNumTerritori() >= numterritori){
                    for(TerritorialArmy t : gioc.getListaterr()){
                        if(t.getArmate() < 2) return false;
                    }
                    return true;
                }
            default:
                break;
        }
        if(gioc.getNumTerritori() >= numterritori) return true;
        return false;
    };

    private boolean Verifica_continentale(Giocatore gioc,Obbiettivi_t obj){
        //TODO Completare la verifica vittoria per i continenti
        return false;
    };
}
