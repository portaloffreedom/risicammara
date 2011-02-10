

package risicammaraServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.Giocatore;
import risicammaraServer.messaggiManage.Messaggio;

/**
* Classe che rappresenta il giocatore nel server.
* Tutti gli elementi utili al server che fanno parte del giocatore
* vengono richiamati da questa classe. Contiene il thread corrispondente
* che comunica con il giocatore, il socket e gli incapsulamenti per input/output.
* @author stengun
*/
public class Giocatore_Net extends Giocatore {

    /** Socket del giocatore*/
    transient private Socket comunicatore;
    /** Output stream incapsulato*/
    transient private ObjectOutputStream clientOut;
    /** Input stream incapsulato*/
    transient private ObjectInputStream  clientIn;
    /** Il thread del giocatore*/
    transient private Thread thread_player;

    /**
     * Costruisce un oggetto Giocatore_Net assegnandogli un socket corrispondente
     * per ottenere gli oggetti di input/output già incapsulati e pronti.
     * @param comunicatore Il socket da assegnare permanentemente al gicoatore.
     */
    public Giocatore_Net(Socket comunicatore){
        super(null, Colore_t.NULLO);
        this.comunicatore = comunicatore;
        thread_player = null;

        try {    
            this.inizializzaStream();
        } catch (IOException ex) {
            System.err.println(
                    "Errore nell'apertura delle comunicazioni con "
                    +"il nuovo giocatore_net creato"+ex.getLocalizedMessage());
        }
    }

    private synchronized void inizializzaStream() throws IOException {
            this.clientOut = new ObjectOutputStream(
                    new BufferedOutputStream(comunicatore.getOutputStream()));
            this.clientOut.flush();
            this.clientIn = new ObjectInputStream(
                    new BufferedInputStream(comunicatore.getInputStream()));
    }
    /**
     * Costruttore del giocatore senza socket.
     * @deprecated Inutilizzabile in quanto è deprecato l'uso di setSocket.
     * Usa il costruttore di default della classe Giocatore_Net
     */
    @Deprecated
    public Giocatore_Net(){
        this(null);
    }


    //-------------- Funzioni che accedono al thread
    /**
     * Fa corrispondere un thread a questo giocatore.
     * Nota: Il thread deve essere esplicitamente assegnato al giocatore da
     * parte del server! altrimenti tutte le funzioni del giocatore che usano
     * il thread falliranno.
     * @param t Il thread che corrisponde al giocatore
     */
    public void AssignThread(Thread t){
        this.thread_player = t;
    }
    /**
     * Restituisce il riferimento al thread di questo giocatore
     * @return Thread del giocatore
     */
    public Thread getThread(){
        return thread_player;
    }
    /**
     * Imposta il giocatore come Leader della sala d'attesa.
     * Questa funzione usa una chiamata al thread corrispondente
     * (in quanto il valore che stabilisce se il giocatore è leader risiede
     * dentro il thread e viene modificato tramite suoi metodi)
     * @param leader true se è leader, false altrimenti
     */
    public void setLeader(boolean leader){
        ((PlayerThread)thread_player).setLeader(leader);
    }
    /**
     * Indica se il giocatore selezionato è leader o meno della sala d'attesa.
     * Questa funzione usa una chiamata al thread corrispondente
     * (in quanto il valore che stabilisce se il giocatore è leader risiede
     * dentro il thread e viene modificato tramite suoi metodi)
     * @return True se il giocatore è leader, False altrimenti
     */
    public boolean isLeader(){
        return ((PlayerThread)thread_player).isLeader();
    }
    /**
     * Restituisce l'indice della lista in cui si trova effettivamente
     * il giocatore.
     * Questa funzione fa una chiamata al thread corrispondente in quanto
     * il valore ricercato risiede nel thread.
     * @return L'indice della lista dove prelevare il giocatore.
     */
    public int getPlayerIndex(){
        return ((PlayerThread)thread_player).getPlayerIndex();
    }

    //------------------ Funzioni che utilizzano il socket
    /**
     * Imposta il socket del giocatore.
     * @param sock Il socket da assegnare al giocatore.
     * @deprecated Inutile in quanto è obbligatorio referenziare il giocatore
     * con il socket quando viene invocato il costruttore.
     */
    public void setSocket(Socket sock){
        this.comunicatore = sock;
    }
    /**
     * Restituisce il socket corrispondente al giocatore.
     * @return Il riferimento al socket del giocatore.
     * @deprecated Questa funzione è inutile, si può agire sul socket attraverso
     * altri metodi.
     */
    public Socket getSocket(){
        return comunicatore;
    }
    /**
     * Chiude il socket corrispondente al giocatore.
     * @throws IOException Eccezione sollevata in caso di problemi di IO con il
     * Socket
     */
    public void closeSocket() throws IOException{
        this.clientIn.close();
        this.clientOut.close();
        this.comunicatore.close();
    }
    /**
     * Chiama l'ObjectInputStream corrispondete al giocatore per poter inviare i
     * dati senza toccare il socket non incapsulato.
     * @return L'oggetto ObjectInputStream
     */
    public ObjectInputStream getClientIn() {
        return clientIn;
    }
    /**
     * Chiama L'ObjectOutputStream corrispondente al giocatore per poter inviare
     * i dati senza toccare il socket non incapsulato.
     * @return L'oggetto ObjectOutputStream
     */
    public ObjectOutputStream getClientOut() {
        return clientOut;
    }
    /**
     * Invia un messaggio a questo giocatore.
     * @param mess Il messaggiod a inviare
     * @throws IOException Errore di IO con il socket del giocatore.
     */
    public void sendMessage(Messaggio mess) throws IOException{
        clientOut.writeObject(mess);
        clientOut.flush();
        System.out.println("messaggio "+mess.toString()+" inviato!");
    }

}
