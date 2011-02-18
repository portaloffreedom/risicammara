package risicammaraJava.turnManage;

import risicammaraClient.Continente_t;
import risicammaraClient.Obbiettivi_t;
import risicammaraJava.boardManage.Plancia;
import risicammaraJava.deckManage.MazzoObbiettivi;
import risicammaraJava.deckManage.MazzoTerritori;
import risicammaraJava.playerManage.Giocatore;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraClient.territori_t;
import risicammaraClient.tipovittoria_t;
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
public class PartitaServer extends GestionePartita {
    private MazzoTerritori mazzo;
    private Dado dado;
    /**
     * Costruttore PartitaServer che inizializza tutti gli oggetti
     * @param listagiocatori L'oggetto che rappresenta la lista dei giocatori
     */
    public PartitaServer(ListaPlayers listagiocatori){
        super(listagiocatori);
        this.planciadigioco = new Plancia();
        this.mazzo = new MazzoTerritori();
        this.dado = new Dado(6);

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
    }

    //Metodi di partita (informazioni)

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
     * Effettua il lanco di un dado.
     * @return il valore ottenuto con il lancio.
     */
    public int lanciaDado(){
        return dado.RollDice();
    }
    //Metodi per le fasi



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
/**
 * Verifica se un giocatore può vincere in base ai continenti che possiede.
 * @param gioc il giocatore del quale verificare i continenti
 * @return true se ha vinto, false altrimenti.
 */
    private boolean Verifica_continentale(Giocatore gioc){
        switch(gioc.getObbiettivo()){
            case ASIAAFRICA:
                if(gioc.hasContinente(Continente_t.AFRICA)
                        && gioc.hasContinente(Continente_t.ASIA))
                    return true;
                return false;
            case ASIASUDAMERICA:
                if(gioc.hasContinente(Continente_t.SUDAMERICA)
                        && gioc.hasContinente(Continente_t.ASIA))
                    return true;
                return false;
            case NORDAMERICAAFRICA:
                if(gioc.hasContinente(Continente_t.NORDAMERICA)
                        && gioc.hasContinente(Continente_t.AFRICA))
                    return true;
                return false;
            case NORDAMERICAOCEANIA:
                if(gioc.hasContinente(Continente_t.AFRICA)
                        && gioc.hasContinente(Continente_t.NORDAMERICA))
                    return true;
                return false;
            case EUROPAOCEANIATERZO:
                if(gioc.numContinenti()>2
                        && gioc.hasContinente(Continente_t.EUROPA)
                        && gioc.hasContinente(Continente_t.OCEANIA))
                    return true;
                return false;
            case EUROPASUDAMERICATERZO:
                if(gioc.numContinenti()>2
                        && gioc.hasContinente(Continente_t.EUROPA)
                        && gioc.hasContinente(Continente_t.SUDAMERICA))
                    return true;
            default:
                return false;
        }
    };
}
