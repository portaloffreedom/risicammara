/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammarajava.playerManage.Giocatore;

/**
 *
 * @author Sten_Gun
 */
public class Server {
    static ServerSocket socksrv;
    static Giocatore[] listaGiocatori;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO Codice Server
        Lobby lobserv = new Lobby();
        try {
            lobserv.start();
        } catch (IOException ex) {
            System.err.print(ex.getStackTrace());
        }
    }
}
