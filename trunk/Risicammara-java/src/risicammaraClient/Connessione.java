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

    /**
     * Crea tutti gli stream dal server.
     * @param server il socket del server
     * @throws IOException Sollevata se il socket non è valido
     */
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

    /**
     * Riceve un messaggio dal server
     * @return Il messaggio Ricevuto
     * @throws IOException Solleva l'eccezione se il socket non è valido
     * @throws ClassNotFoundException Solleva l'eccezione se il messaggio non è
     * Serializzabile nell'oggetto Corrispondente.
     */
    public Messaggio ricevi() throws IOException, ClassNotFoundException {
        return (Messaggio) leggi.readObject();
    }

    /**
     * Spedisce un messaggio al server
     * @param msg il messaggio da spedire
     * @throws IOException Solleva l'eccezione se il socket non è valido
     */
    public void spedisci(Messaggio msg) throws IOException {
        scrivi.writeObject(msg);
        scrivi.flush();
    }

    /**
     * Chiude la connessione con il server.
     * @throws IOException Solleva una eccezione di IO quando il socket è occupato.
     */
    public void chiudiConnessione() throws IOException {
        scrivi.flush();
        server.close();
    }

}
