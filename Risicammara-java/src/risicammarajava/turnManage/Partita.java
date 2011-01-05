/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava.turnManage;

import risicammarajava.Obbiettivi_t;
import risicammarajava.boardManage.Plancia;
import risicammarajava.deckManage.MazzoObbiettivi;
import risicammarajava.deckManage.MazzoTerritori;
import risicammarajava.playerManage.Giocatore;
import risicammarajava.playerManage.ListaPlayers;
import risicammarajava.territori_t;

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
    Partita(){
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
                     giocatorediturno.addTerr(car);
                     planciadigioco.getTerritorio(car).setArmate(1);
                     planciadigioco.getTerritorio(car).setProprietario(giocatorediturno);
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
    //private boolean theresVictory();
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
}
