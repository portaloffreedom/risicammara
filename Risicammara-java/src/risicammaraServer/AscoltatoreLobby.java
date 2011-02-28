package risicammaraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import risicammaraServer.messaggiManage.MessaggioNuovoGiocatore;

/**
 * Classe (implementata come Thread) che rimane in ascolto di nuove connessioni,
 * e nel caso le notifica al Thread Server che penserà a processarle.
 * @author matteo
 */
public class AscoltatoreLobby extends Thread {
/** Variabile che memorizza la coda che server per mandare messaggi al "Server"
 */
    private CodaMsg coda;

/** Variabile intera che identifica la porta in cui l'Ascoltatore deve rimanere 
 * in ascolto
 */
    private int porta;
/** Variabile che memorizza il ServerSocket che deve rimanere in ascolto */
    private ServerSocket ascoltatore;

/** Variabile che serve per stoppare il thread a comando */
    private boolean stop;
    /**
     * Inizializza i parametri necessari a fare partire un Socket
     * in ascolto di nuove connessioni.
     * @param porta porta su cui il server deve rimanere in ascolto
     * @param coda coda con cui l'ascoltatore comunica con il server (Thread)
     * principale
     */
    public AscoltatoreLobby(int porta, CodaMsg coda) {
        this.porta = porta;
        this.coda = coda;
        this.stop = false;
        this.numerogiocatori = 0;
        try {
            this.ascoltatore = new ServerSocket(this.porta);
        }
        catch (IOException ex) {
            System.err.println(
                    "Impossibile aprire una connessione sulla porta: "
                    +this.porta);
            System.err.println("Errore: "+ex);
            return;
        }
    }

    /**
     * Fa partire il lavoro di ascolto di nuove connessioni.
     */
    @Override
    public void run() {
        try {
            this.ascolta();
        } catch (IOException ex) {
            System.err.println("Errore nell'ascoltare nuovi client: "+ex);
            return;
        }
    }

    /**
     * Comincia ad ascoltare sul ServerSocket e notifica al Thread server ogni
     * volta che si connette un nuovo giocatore.
     * <p>
     * Da Implelentare: Si blocca al compimento della 6 connessione fino a che
     * il server non si notifica una disconnessione o non vuole terminare il
     * Thread (quando inizia la partita)
     * @throws IOException lancia IOException nel caso in cui il ServerSocket
     * non riesca (più) ad ascoltare.
     */
    private void ascolta() throws IOException {
        Socket giocatore = null;

        while (!this.stop) {
            giocatore = ascoltatore.accept();
            if(numerogiocatori>5){
                giocatore.close();
                continue;
            }
            numerogiocatori++;
            coda.Send(new MessaggioNuovoGiocatore(giocatore));
        }
    }

    private int numerogiocatori;

    /**
     * Imposta il numero di giocatori attivi.
     * @param num il numero dei giocatori.
     */
    public void setNumeroGiocatori(int num){
        this.numerogiocatori = num;
    }

    /**
     * Imposta lo stato di stop del Thread per poter chiudere tutto in maniera sicura.
     * @param stop true se fermare, false altrimenti.
     */
    public void setStop(boolean stop) {
        this.stop = stop;
        try{
        ascoltatore.close();
        }
        catch(IOException ex){}
    }

}
