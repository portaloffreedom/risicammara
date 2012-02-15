package risicammara2.global;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import risicammara2.server.Threadplayer;
import risicammaraClient.Colore_t;

/**
 * Questa classe gestisce tutti i dati di un generico giocatore.
 * Non deve essere confuso con il thread dedicato al giocatore, il quale si occupa
 * direttamente della ricezione e dell'invio dei messaggi.
 * @author stengun
 */
public class Player implements Serializable
{

    // Dati generici del giocatore
    protected String nome;
    protected BufferedImage avatar;
    protected Colore_t coloreArmate;
    // privato
    private long id;
    private transient Threadplayer thread;
    
    /**
     * Inizializza un uovo giocatore con un nome.
     * @param nome Il nome del giocatore
     */
    public Player(String nome) {
        this.nome = nome;
        this.thread = null;
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

    public void setThread(Threadplayer threadplayer) {
        this.thread = threadplayer;
        id = thread.getId();
    }

    public Threadplayer getThread() {
        return thread;
    }

    public long getId() {
        return id;
    }
}
