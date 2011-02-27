/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioComandi;

/**
 * Questa classe rappresenta il thread che gestisce tutti i messaggi inviati dai
 * client al server. Fa uso di una coda che implementa un monitor per gestire un
 * tipico scenario di "produttore-consumatore" dove il server consuma i messaggi
 * che questo thread gli fornisce.
 * @author Sten_Gun
 */
public class PlayerThread extends Thread{
    private boolean stop;
    private ObjectInputStream playerInput;
    private int playerIndex;
    private CodaMsg coda;
    private boolean leader;
    private boolean mustpass;
    private int numar;
    private boolean first;

/**
 * Inizializza tutti i dati necessari al thread che riceve i messaggi da un
 * preciso giocatore.
 * @param coda l'oggetto da dove il server leggerà i messaggi recapitati
 * @param playerInput L'input Stream del giocatore interessato
 * @param playerIndex L'indice del giocatore a cui è assegnato il thread
 */
    public PlayerThread(CodaMsg coda,ObjectInputStream playerInput,int playerIndex){
        this.coda = coda;
        this.playerIndex = playerIndex;
        this.playerInput = playerInput;
        this.setName("Thread giocatore"+playerIndex);
        this.leader = false;
        this.mustpass = false;
        this.first=true;
    }
/**
 * Metodo run per l'interfaccia Runnable. È il codice che viene eseguito dal thread
 */
    @Override
    public void run() {
        this.stop = false;
        
        while(!stop){
            try {
                coda.Send((Messaggio) this.playerInput.readObject());
            } catch (IOException ex) {
                System.err.println("Giocatore "+playerIndex+" non raggiungibile: "+ex.getMessage());
                stop = true;
                coda.Send(MessaggioComandi.creaMsgDisconnect(playerIndex));
            } catch (ClassNotFoundException ex) {
                System.err.println("Messaggio non riconosciuto: "+ex);
            } catch (Exception ex){
                System.err.println("Eccezione: "+ex.getMessage());
                stop = true;
            }
        }
        System.out.println(this.getName()+" stoppato");
    }
    /**
     * Ferma il thread in modo sicuro.
     * @param stop true per settare lo stato di stop.
     */
    public void setStop(boolean stop){
        this.stop = stop;
        while(holdsLock(coda)){
            System.out.println("Wait locks");
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }
/**
 * Fornisce lo stato di leader del giocatore.
 * @return true se il giocatore è leader della sala d'attesa, false altrimenti.
 */
    public boolean isLeader() {
        return leader;
    }
/**
 * Imposta lo stato di leader della sala d'attesa
 * @param leader true per impostare leader, false altrimenti.
 */
    public void setLeader(boolean leader) {
        this.leader = leader;
    }
/**
 * Fornisce l'indice da cui prelevare il giocatore nella lista giocatori.
 * @return indice del player
 */
    public int getPlayerIndex() {
        return playerIndex;
    }
/**
 * Chiede se il giocatore deve obbligatoriamente passare la fase.
 * @return true se deve passare, false altrimenti.
 */
    public boolean isMustpass() {
        return mustpass;
    }
/**
 * Imposta lo stato di "pass" obbligatorio del giocatore.
 * @param mustpass true se il giocatore deve passare il turno, false altrimenti.
 */
    public void setMustpass(boolean mustpass) {
        this.mustpass = mustpass;
    }
/**
 * Metodo che serve solo per la fase preliminare del gioco (per conteggiare
 * quante armate sono state messe dal giocatore nella fase pre partita).
 */
    public void incnumar(){
        if(numar<2){
            numar++;
            return;
        }
        mustpass = true;
        numar = 0;
    }



}
