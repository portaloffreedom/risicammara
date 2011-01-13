/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.net.*;
import risicammaraClient.Main;
import risicammaraJava.playerManage.ListaPlayers;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server {
    private ServerSocket socksrv;
    private static ListaPlayers listaGiocatori;
    protected CodaMsg coda;
    protected int porta;
    //TODO riferimento alla coda
    //TODO riferimento al numero giocatori

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {

    Lobby server = new Lobby(Main.PORT,listaGiocatori);
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

