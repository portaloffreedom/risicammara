/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import risicammaraClient.Client;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.turnManage.SuccessioneTurni;

/**
 * Lato server di Risicammara.
 * @author Sten_Gun
 */
public class Server implements Runnable {
    private ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;
    //TODO riferimento al numero giocatori (probabilmente inutile avendo il riferimento del thread.)

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

    public void run() {

        Lobby server = new Lobby(porta,coda);
        listaGiocatori = server.start();
        boolean vincitore = false;
        SuccessioneTurni svolgimento = new SuccessioneTurni(listaGiocatori,coda);
    }


    /**
     * Spedisce un pacchetto ad un client.
     * @param recMsg Il messaggio di chat
     * @param cl il client da notificare
     * @throws IOException Eccezione di I/O dovuta ai socket
     */
   public static void broadcastMessage(Messaggio recMsg, ObjectOutputStream cl) throws IOException
   {
        cl.writeObject(recMsg);
        cl.flush();
        System.out.println("messaggio "+recMsg.toString()+" inviato!");
   }

   /**
    * Spedisce un messaggio a tutti i client con possibilità di escluderne uno in particolare.
    * @param recMsg Il messaggio da spedire
    * @param listaGiocatori La lista dei client a cui inviare
    * @param escludi L'indice del client da escludere
    */
   public static void spedisciMsgTutti(Messaggio recMsg,ListaPlayers listaGiocatori,int escludi) throws IOException{
       for(int i=0;i<ListaPlayers.MAXPLAYERS;i++){
           if(i==escludi) continue;
           Giocatore_Net gtmp = (Giocatore_Net)listaGiocatori.get(i);
           if(gtmp == null)continue;
           broadcastMessage(recMsg,gtmp.getClientOut());
       }
   }
}

