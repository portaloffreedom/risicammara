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
    private Thread listaGiocatori[];
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



        for (int i=0; i<listaGiocatori.length; i++) {
            Socket accettazione = null;

            try {
                accettazione = this.socksrv.accept();
            } catch (IOException ex) {
                //TODO cattura eccezione "aspetta client"
                System.err.println("Errore: "+ex);
                System.exit(-2);
            }
            listaGiocatori[i]= new Thread(new GiocatoreThread(accettazione));
        }
    }
}
