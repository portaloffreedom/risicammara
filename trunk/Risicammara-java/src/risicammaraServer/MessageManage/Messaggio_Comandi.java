/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.MessageManage;

/**
 * Rappresenta l'invio di un comando client/server.
 * @author Sten_Gun
 */
public class Messaggio_Comandi implements Messaggio{

    private comandi_t comando;
    private int sender;
    private int receiver;

    /**
     * Costruttore del pacchetto
     * @param cmd Il comando desiderato
     * @param inviante  il nome del giocatore che lo ha inviato (SERVER se è di sistema)
     */
    public Messaggio_Comandi(comandi_t cmd, int inviante){
        this(cmd, inviante, -2);
    }

    /**
     * Costruttore alternativo del pacchetto
     * @param cmd il comando desiderato.
     * @param inviante l'indice del giocatore che invia il comando (-1 se è il sistema,-2 se null)
     * @param who l'indice di chi riceve questa azione(non può essere -1)
     */
    public Messaggio_Comandi(comandi_t cmd, int inviante, int who){
        this.comando = cmd;
        this.sender = inviante;
        if((who < -1) && (who < 0)) who=-2;
        this.receiver = who;
    }
/**
 * Indica che il pacchetto è di tipo COMMAND (da ::messaggi_t )
 * @return ::messaggio_t.COMMAND
 */
    public messaggio_t getType() {
        return messaggio_t.COMMAND;
    }

    /**
     * Fornisce il comando contenuto nel pacchetto.
     * @return Il comando del pacchetto.
     */
    public comandi_t getComando(){
        return comando;
    }
/**
 * Fornisce il nome di chi ha inviato il pacchetto
 * @return l'indice del giocatore che invia il comando.
 */
    public int getSender(){
        return sender;
    }
/**
 * Fornisce chi riceve l'azione di questo comando.
 * @return l'indice del giocatore che riceve l'azione.
 */
    public int getReceiver(){
        return receiver;
    }
}
