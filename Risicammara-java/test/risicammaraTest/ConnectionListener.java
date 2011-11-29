/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package risicammaraTest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Classe che implementa l'ascoltatore delle connessioni in entrata al server.
 * Controlla la validità di un giocatore in fase di connessione, se tutto va bene
 * l'oggetto connesso viene passato al gestore connessioni che decide cosa fare.
 * @author stengun
 */
class ConnectionListener extends Thread{
    private ArrayBlockingQueue<Socket> codaconnessi;
    private boolean stop;
    private int porta,giocatoriconnessi;
    private ServerSocket tcpsock;
    
    public ConnectionListener(ArrayBlockingQueue<Socket> coda,int porta)
    {
        this.codaconnessi = coda;
        this.stop = false;
        this.porta = porta;
        this.tcpsock = null;
        this.giocatoriconnessi = 0;
    }
    
    @Override
    public void run()
    {
        System.out.println("Inizializzazione Ascoltatore connessioni in ingresso");
            try {
                tcpsock = new ServerSocket(this.porta);
            } catch (IOException ex) {
                System.err.println("Errore su tread "+this.getName()+" in apertura socket."
                        +"\n inizializzazione fallita");
                stop = true;
            }
        while(!stop)
        {
            Socket s = null;
            try {
                s = tcpsock.accept();
            } catch (IOException ex) {
                System.err.println("Errore IO nell'accettare una connessione.");
                continue;
            }
            if(giocatoriconnessi > Server_test.MAX_PLAYERS)
            {
                try {
                    s.close();
                } catch (IOException ex) {
                    System.err.println("Errore chiusura socket");
                }
                continue;
            }
            try {
                codaconnessi.put(s);
            } catch (InterruptedException ex) {
                System.err.println("Errore inserimento nuovo giocatore");
                continue;
            }
            this.giocatoriconnessi++;
        }
    }
    /**
     * Imposta il numero di giocatori attualmente connessi al server.
     * Si usa principalmente per sincronizzarlo con la lista dei giocatori
     * attualmente registrati.(quando avviene una disconnessione forzata);
     * @param giocatoriconnessi 
     */
    public void setGiocatoriconnessi(int giocatoriconnessi)
    {
        this.giocatoriconnessi = giocatoriconnessi;
    }


    
    
    
}
