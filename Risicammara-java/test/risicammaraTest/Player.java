package risicammaraTest;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import risicammaraClient.Colore_t;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioChat;

/**
 * Questa classe gestisce tutti i dati di un generico giocatore.
 * Diviene così possibile comunicare con un giocatore direttamente dall'oggetto.
 * Non deve essere confuso con il thread dedicato al giocatore, il quale si occupa
 * direttamente della ricezione e dell'invio dei messaggi.
 * La classe contiene tutto il necessario per la comunicazione e la ricezione,
 * i dati del giocatore generico e così via.
 * @author stengun
 */
class Player
{
    
    //Identificazione del client
    private boolean valid;
    private Socket tcpsocket;
    private DatagramSocket udpsocket;
    private double clientVersion;
    //private Os_type sisoperativo; // Questo è per la versione android.
    
    // Dati generici del giocatore
    protected String nome;
    protected BufferedImage avatar;
    protected Colore_t coloreArmate;
    
    /**
     * Inizializza il giocatore con i dati essenziali: Socket per TCP, UDP e
     * la versione rilevata.
     * @param tcpsocket socket per tcp (socket)
     * @param udpsocket socket per udp (datagramsocket)
     * @param clientVersion  versione del client.
     */
    public Player(Socket tcpsocket, DatagramSocket udpsocket, double clientVersion) {
        this.tcpsocket = tcpsocket;
        this.udpsocket = udpsocket;
        this.clientVersion = clientVersion;
        this.valid = true;
    }
    
    
    // Metodi per ottenere e impostare i campi generici del giocatore
    
    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    public Colore_t getColoreArmate() {
        return coloreArmate;
    }

    public void setColoreArmate(Colore_t coloreArmate) {
        this.coloreArmate = coloreArmate;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIndex() {
        return this.hashCode();
    }

    
    // Metodi di servizio

    /**
     * Imposta la validità di questo oggetto.
     * @param valid stato della validità.
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    /**
     * Richiede la validità di questo oggetto.
     * @return ture valido, false invalido.
     */
    public boolean isValid(){
        return this.valid;
    }
    
    
    public void invia(String chat){
        this.invia(new MessaggioChat(this.hashCode(), chat));
    }
    
    public void invia(Messaggio msg){
        
    }
    
    public void close() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public final double getVersion() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public final BufferedInputStream getTcpInputStream() throws IOException{
        return new BufferedInputStream(tcpsocket.getInputStream());
    }
    
    public final BufferedOutputStream getTcpOutputStream() throws IOException {
        return new BufferedOutputStream(tcpsocket.getOutputStream());
    }
    
    
}
