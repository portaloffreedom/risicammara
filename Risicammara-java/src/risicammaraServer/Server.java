/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.*;
import risicammaraClient.Main;
import risicammaraJava.playerManage.Giocatore;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server {
    private ServerSocket socksrv;
    private Giocatore[] listaGiocatori;
    private int porta;
    //TODO riferimento alla coda
    //TODO riferimento al numero giocatori

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Server server = new Server(Main.PORT);
    }

    public Server (int porta) {
        this.porta=porta;


        AscoltatoreLobby prova = new AscoltatoreLobby(Thread.currentThread(), this.porta);
        prova.run();


        while (true) {

            

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
}

