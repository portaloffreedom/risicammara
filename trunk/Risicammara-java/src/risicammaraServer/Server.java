/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.*;
import java.rmi.server.SocketSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammarajava.Main;
import risicammarajava.playerManage.Giocatore;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server {
    private ServerSocket socksrv;
    private Giocatore[] listaGiocatori;
    private int porta;
    private

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Server server = new Server(Main.PORT);
    }

    public Server (int porta) {
        this.porta=porta;


        AscoltoreConnessioni prova = new AscoltoreConnessioni(Thread.currentThread(), this.porta);
        prova.run();
        try {
            this.wait();
        } catch (InterruptedException ex) {
            System.err.println(null);
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

    private class AscoltoreConnessioni implements Runnable {
        private Thread server;
        private int porta;
        private ServerSocket ascoltatore;
        private int numeroConnessioni;
        private final int numeroMassimoConnessi

        public AscoltoreConnessioni(Thread server, int porta) {
            this.server = server;
            this.porta = porta;
        }

        public void run() {
            try {
                this.ascoltatore = new ServerSocket(this.porta);
            } catch (IOException ex) {
                System.err.println("Impossibile aprire una connessione sulla porta: "+this.porta);
                System.err.println("Errore: "+ex.getStackTrace());
                System.exit(0);
            }


        }

        private void ascolta(){
            Socket giocatore = null;

            while (numeroConnessioni<=6)
            ascoltatore.accept()
        }

    }
}


