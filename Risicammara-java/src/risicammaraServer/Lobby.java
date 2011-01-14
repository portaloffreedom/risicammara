/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import risicammaraClient.Colore_t;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.MessageManage.MessaggioChat;
import risicammaraServer.MessageManage.MessaggioConfermaNuovoGiocatore;
import risicammaraServer.MessageManage.MessaggioNuovoGiocatore;
import risicammaraServer.MessageManage.MessaggioComandi;
import risicammaraServer.MessageManage.MessaggioErrore;
import risicammaraServer.MessageManage.comandi_t;

/**
 *
 * @author Sten_Gun
 */
public class Lobby {

    private ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;

    public Lobby (int porta,ListaPlayers listaGiocatori) {
        this.porta = porta;
        this.coda = new CodaMsg();
    }

    public ListaPlayers start(){
        AscoltatoreLobby prova = new AscoltatoreLobby(this.porta, this.coda);
        prova.start();
        boolean inizia = false;
        Messaggio msg = null;
        while (!inizia) {
         msg = coda.get();
         System.out.println("Tipo messaggio: "+msg.getType().toString());
          //System.out.println("Sono fuori");

         MessaggioChat ctt = null;
            switch (msg.getType()) {
                case AGGIUNGIGIOCATORE:
                    MessaggioNuovoGiocatore mgio = (MessaggioNuovoGiocatore)msg;
                    Giocatore_Net gioctemp = new Giocatore_Net(mgio.getConnessioneGiocatore());
                    gioctemp.setArmyColour(Colore_t.NULLO);
                    int plynumb = listaGiocatori.addPlayer(gioctemp);
                    gioctemp.AssignThread(new Thread(new PlayerThread(coda,mgio.getConnessioneGiocatore(),plynumb)));
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(mgio.getConnessioneGiocatore().getOutputStream());
                        os.writeObject(new MessaggioConfermaNuovoGiocatore(listaGiocatori,plynumb));
                    } catch (IOException ex) {
                        System.out.println("errore stream");
                        System.exit(-1);
                    }
                    ctt = new MessaggioChat(-1, "Nuovo giocatore aggiunto: "+gioctemp.getNome());
                    break;
                case COMMAND:
                    ctt = CommandHandling((MessaggioComandi)msg);
                    break;
                case ERROR:
                    ctt = ErrorHandling((MessaggioErrore)msg);
                    break;
                case CHAT:
                    ctt = (MessaggioChat)msg;
                default:
                    ctt = new MessaggioChat(-1, "Messaggio non ancora gestito");
                    break;
                }
                //Stampa il messaggio di chat corrispondente e lo invia a tutti.
                    System.out.println(listaGiocatori.getNomeByIndex(ctt.getSender())+": "+ctt.getMessaggio());
          for(int i = 0;i<listaGiocatori.getSize();i++)
          {
             //System.out.println("cl: "+all);
             Giocatore_Net giotmp = (Giocatore_Net)listaGiocatori.get(i);
             if(giotmp == null) continue;
             Socket cl = giotmp.getSocket();

                    try {
                        broadcastMessage(ctt, cl);
                    } catch (IOException ex) {
                        System.err.println("Errore broadcast: "+ex.getStackTrace());
                        System.exit(-1);
                    }
                }
        }
        return listaGiocatori;
    }
   /**
    * Funzione che gestisce i messaggi di tipo ::MessaggioErrore per
    * la funzione ::receiveMessage
    * @param errorMsg Il pacchetto MessaggioErrore
    */
   private MessaggioChat ErrorHandling(MessaggioErrore errorMsg){
       switch(errorMsg.getError()){
           default:
               return new MessaggioChat(-1,"Errore non gestito.");
       }
   }

   /**
    * Funzione che gestisce i messaggi di tipo ::Messaggio_Comando per la
    * funzione ::receiveMessage
    * @param cmdMsg il pacchetto Messaggio_Comando
    */
private MessaggioChat CommandHandling(MessaggioComandi cmdMsg){
    //TODO Completare il codice di Exit.
    //TODO Completare il codice di KICKPLAYER
    //TODO Completare il codice di NUOVAPARTITA
    switch(cmdMsg.getComando()){
        case DISCONNECT:
            Giocatore_Net tempgioc = (Giocatore_Net)listaGiocatori.get(cmdMsg.getSender());
            Socket giosock = tempgioc.getSocket();
            String nomegioc = tempgioc.getNome();
            listaGiocatori.remPlayer(cmdMsg.getSender());
            try {
                ObjectOutputStream os = new ObjectOutputStream(giosock.getOutputStream());
                os.writeObject(new MessaggioComandi(comandi_t.DISCONNECT, -1));
                os.writeObject(new MessaggioChat(-1, "sei stato disconnesso"));
                giosock.close();
            } catch (IOException ex) {
                System.err.println("Errore socket: "+ex.getStackTrace());
            }
            return new MessaggioChat(-1,"Giocatore "+nomegioc+" disconnesso.");
        default:
            return new MessaggioChat(-1,"comando non riconosciuto.");
    }
}

    /**
     * Il server notifica i client tramite un messaggio che appare nella chat
     * @param recMsg Il messaggio di chat
     * @param cl il client da notificare (iterato)
     * @throws IOException Eccezione di I/O dovuta ai socket
     */
   private void broadcastMessage(MessaggioChat recMsg, Socket cl) throws IOException
   {
         new ObjectOutputStream(cl.getOutputStream()).writeObject(recMsg);
   }

}
