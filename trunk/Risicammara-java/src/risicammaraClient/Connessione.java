/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import risicammaraServer.messaggiManage.Messaggio;

/**
 *  Contenitore per le connessioni al server
 * @author matteo
 */
public class Connessione {

    private Socket server;

    private ObjectOutputStream scrivi;
    private ObjectInputStream  leggi;

    public Connessione(Socket server) throws IOException {
        this.server = server;

        if (server.isInputShutdown()) {
            System.err.println("Il server non permette di leggere da lui");
        }
        if (server.isOutputShutdown()) {
            System.err.println("Il server non permette di scrivere su di lui");
        }

        this.scrivi = new ObjectOutputStream(new BufferedOutputStream(server.getOutputStream()));
        this.scrivi.flush();
        this.leggi  = new ObjectInputStream(new BufferedInputStream(server.getInputStream()));
    }

    public Messaggio ricevi () throws IOException, ClassNotFoundException {
        return (Messaggio) leggi.readObject();
    }

    public void spedisci (Messaggio msg) throws IOException {
        scrivi.writeObject(msg);
        scrivi.flush();
    }

    public void chiudiConnessione () throws IOException {
        scrivi.flush();
        server.close();
    }

}
