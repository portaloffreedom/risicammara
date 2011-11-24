/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package risicammaraTest;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraServer.messaggiManage.MessaggioErrore;
import risicammaraServer.messaggiManage.errori_t;

/**
 * Classe che implementa l'ascoltatore delle connessioni in entrata al server.
 * Controlla la validità di un giocatore in fase di connessione, se tutto va bene
 * l'oggetto connesso viene passato al gestore connessioni che decide cosa fare.
 * @author stengun
 */
class ConnectionListener extends Thread{
    private ArrayBlockingQueue<Player> codaconnessi;
    private boolean stop;
    private int porta,giocatoriconnessi;
    private ServerSocket tcpsock;
    
    public ConnectionListener(ArrayBlockingQueue<Player> coda,int porta) {
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
            Player gioc = null;
            //Attesa connessione
            try {
                s = tcpsock.accept();
                gioc = new Player(s, new DatagramSocket(s.getRemoteSocketAddress()), porta);
            } catch (SocketException ex) {
                System.err.println("Errore apertura Datagram Socket Giocatore"+ex.getMessage());
            } catch (IOException ex) {
                System.err.println("Errore Socket tcp in ascolto: "+ex.getMessage());
            }
            //Accettazione giocatore            
            if(giocatoriconnessi < Server_test.MAX_PLAYERS)
            {
                giocatoriconnessi++;
                //Inserimento in codaconnessi
                try {
                    codaconnessi.put(gioc);
                } catch (InterruptedException ex) {
                    System.err.println("Errore inserimento giocatore in lista");
                    gioc.invia(MessaggioErrore.creaMsgConnectionRefused(-1));
                    gioc.close();
                    giocatoriconnessi--;
                }
                continue;
            }
            gioc.invia(MessaggioErrore.creaMsgServerFull(-1));
            gioc.close();
        }
    }
    /**
     * Imposta il numero di giocatori attualmente connessi al server.
     * Si usa principalmente per sincronizzarlo con la lista dei giocatori
     * attualmente registrati.(quando avviene una disconnessione forzata);
     * @param giocatoriconnessi 
     */
    public void setGiocatoriconnessi(int giocatoriconnessi) {
        this.giocatoriconnessi = giocatoriconnessi;
    }
    
    
    
}
