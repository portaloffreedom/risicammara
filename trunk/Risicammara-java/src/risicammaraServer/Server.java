/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import risicammaraClient.Client;
import risicammaraJava.playerManage.ListaPlayers;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server implements Runnable {
    private static ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;
    //TODO riferimento al numero giocatori (probabilmente inutile avendo il riferimento del thread.)

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {

    Server meStesso = new Server(Client.PORT);
    meStesso.run(); //Attenzione non viene creato un Thread, viene solo eseguito
                    //il metodo run

}

    public Server(int porta) {
        this.porta = porta;
        this.coda = new CodaMsg();


    }

    public void run() {

        Lobby server = new Lobby(porta,coda);
        listaGiocatori = server.start();
        startPartita();
    }


    public static void startPartita(){

    }


/*
    Lobby lobserv = new Lobby(Thread.currentThread());
    try {
    lobserv.start();
    } catch (IOException ex) {
    System.err.print(ex.getStackTrace());
    }
     */
}

