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
    private String sender;
    private String receiver;

    /**
     * Costruttore del pacchetto
     * @param cmd Il comando desiderato
     * @param inviante  il nome del giocatore che lo ha inviato (SERVER se è di sistema)
     */
    public Messaggio_Comandi(comandi_t cmd, String inviante){
        this(cmd, inviante, null);
    }

    /**
     * Costruttore alternativo del pacchetto
     * @param cmd il comando desiderato.
     * @param inviante il nome del giocatore che invia il comando (SERVER se è il sistema)
     * @param who il nome di chi riceve questa azione(non può essere SERVER)
     */
    public Messaggio_Comandi(comandi_t cmd, String inviante, String who){
        this.comando = cmd;
        this.sender = inviante;
        if((who != null) && who.equals("SERVER")) who=null;
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
 * @return il nome del mandante come Stringa
 */
    public String getSender(){
        return sender;
    }
/**
 * Fornisce chi riceve l'azione di questo comando.
 * @return il nome di chi riceve l'azione come Stringa
 */
    public String getReceiver(){
        return receiver;
    }
}
