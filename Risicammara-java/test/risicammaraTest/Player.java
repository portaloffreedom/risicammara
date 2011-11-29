package risicammaraTest;

import java.awt.image.BufferedImage;
import risicammaraClient.Colore_t;

/**
 * Questa classe gestisce tutti i dati di un generico giocatore.
 * Non deve essere confuso con il thread dedicato al giocatore, il quale si occupa
 * direttamente della ricezione e dell'invio dei messaggi.
 * @author stengun
 */
class Player
{

    // Dati generici del giocatore
    protected String nome;
    protected BufferedImage avatar;
    protected Colore_t coloreArmate;
    
    /**
     * Inizializza un uovo giocatore con un nome.
     * @param nome Il nome del giocatore
     */
    public Player(String nome) {
        this.nome = nome;
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
 
    
}
