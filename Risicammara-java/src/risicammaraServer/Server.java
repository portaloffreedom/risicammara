/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sten_Gun
 */
public class Server {
    private ServerSocket socksrv;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server nomeserver = new Server(12312);
        // TODO code application logic here
    }

    public Server(int porta){
        try {
            this.socksrv = new ServerSocket(porta, 6);
        } catch (IOException ex) {
            //TODO cattura eccezione server
            System.err.println("Errore: "+ex);
            System.exit(-1);
        }
        try {
            Socket accettazione = this.socksrv.accept();
        } catch (IOException ex) {
            //TODO cattura eccezione "aspetta client"
            System.err.println("Errore: "+ex);
            System.exit(-2);
        }
    }
}
