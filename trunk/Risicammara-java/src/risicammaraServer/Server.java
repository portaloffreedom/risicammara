/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import risicammaraClient.Client;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.turnManage.SuccessioneTurni;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server implements Runnable {
    private ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {

    Server meStesso = new Server(Client.PORT);
    meStesso.run(); //Attenzione non viene creato un Thread, viene solo eseguito
                    //il metodo run

}

    public Server(int porta) {
        this.porta = porta;
        this.coda = new CodaMsg();


    }
/**
 * Metodo che serve per far partire (all'occorrenza) il server come thread
 * (per esempio quando si fa partire un server in locale direttamente dalla gui).
 */
    public void run() {
        while(true){
            Lobby server = new Lobby(porta,coda);
            listaGiocatori = server.start();
            boolean vincitore = false;
            SuccessioneTurni svolgimento = new SuccessioneTurni(listaGiocatori,coda);
            svolgimento.start();
        }
    }

//---------------------------------------------------- Metodi statici
    /**
     * Spedisce un pacchetto ad un client.
     * @param recMsg Il messaggio di chat
     * @param cl il client da notificare
     * @throws IOException Eccezione di I/O dovuta ai socket
     */
   public static void BroadcastMessage(Messaggio recMsg,
           ObjectOutputStream cl)
           throws IOException
   {
        cl.writeObject(recMsg);
        cl.flush();
        System.out.println("messaggio "+recMsg.toString()+" inviato!");
   }
/**
 * Spedisce un messaggio ad un preciso giocatore nella lista.
 * @param messaggioDaInviare Il messaggio da spedire
 * @param listaGiocatori la lista da dove prelevare il giocatore
 * @param indicegiocatore l'indice della lista dove prelevare il giocatore
 * @throws IOException eccezione dovuta a broadcastMessage (socket)
 * @deprecated usa il metodo di Giocatore_Net per inviare un messaggio ad uno
 * specifico giocatore.
 */
    public static void SpedisciMsgUno(Messaggio messaggioDaInviare,
            ListaPlayers listaGiocatori,int indicegiocatore)
            throws IOException
    {
       Giocatore_Net gtmp = (Giocatore_Net)listaGiocatori.get(indicegiocatore);
       BroadcastMessage(messaggioDaInviare,gtmp.getClientOut());
    }
   /**
    * Spedisce un messaggio a tutti i client con possibilità di escluderne
    * uno in particolare.
    * @param recMsg Il messaggio da spedire
    * @param listaGiocatori La lista dei client a cui inviare
    * @param escludi L'indice del client da escludere
    */
    public static void SpedisciMsgTutti(Messaggio recMsg,
           ListaPlayers listaGiocatori,int escludi)
           throws IOException
     {
        for(int i=0;i<ListaPlayers.MAXPLAYERS;i++){
            if(i==escludi) continue;
            Giocatore_Net gtmp = (Giocatore_Net)listaGiocatori.get(i);
            if(gtmp == null)continue;
            BroadcastMessage(recMsg,gtmp.getClientOut());
        }
    }
}

