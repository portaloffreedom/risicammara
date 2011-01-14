/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.net.*;
import risicammaraClient.Client;
import risicammaraJava.playerManage.ListaPlayers;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server implements Runnable {
    private static ListaPlayers listaGiocatori;
    protected CodaMsg coda;
    protected int porta;
    //TODO riferimento alla coda
    //TODO riferimento al numero giocatori

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


    }

    public void run() {
        Lobby server = new Lobby(porta);
        listaGiocatori = server.start();
        this.startPartita();
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

