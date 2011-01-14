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
import risicammaraServer.MessageManage.MessaggioAddPlayer;
import risicammaraServer.MessageManage.MessaggioAggiornaDatiGiocatore;
import risicammaraServer.MessageManage.MessaggioCambiaNickColore;
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

    public Lobby (int porta, CodaMsg coda) {
        this.porta = porta;
        this.coda = coda;
        this.listaGiocatori = new ListaPlayers();
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
         int escludi = -1;
         Messaggio ctt = null;
            switch (msg.getType()) {
                // Processa i vari tipi di pacchetto possibili
                case MODIFICANICKCOLORE:
                    MessaggioCambiaNickColore mnick = (MessaggioCambiaNickColore)msg;
                    Giocatore_Net giotmp = (Giocatore_Net)listaGiocatori.get(msg.getSender());
                    giotmp.setNome(mnick.getNick());
                    giotmp.setArmyColour(mnick.getColore());
                    ctt = new MessaggioAggiornaDatiGiocatore(mnick.getNick(), mnick.getColore(), mnick.getSender());
                    escludi = mnick.getSender();
                    break;
                case AGGIUNGIGIOCATORE:
                    MessaggioNuovoGiocatore mgio = (MessaggioNuovoGiocatore)msg;
                    Giocatore_Net gioctemp = new Giocatore_Net(mgio.getConnessioneGiocatore());
                    gioctemp.setArmyColour(Colore_t.NULLO); //TODO probabilmente si può togliere perché non fa niente (è già nullo il colore)
                    int plynumb = listaGiocatori.addPlayer(gioctemp);
                    PlayerThread gioth = new PlayerThread(coda,mgio.getConnessioneGiocatore(),plynumb);
                    if(!gioth.isAlive()) gioth.start();
                    gioctemp.setNome("Giocatore"+plynumb);
                    gioctemp.AssignThread(gioth);
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(mgio.getConnessioneGiocatore().getOutputStream());
                        os.writeObject(new MessaggioConfermaNuovoGiocatore(listaGiocatori,plynumb));
                    } catch (IOException ex) {
                        System.out.println("Errore nel creare lo Stream di output verso il nuovo utente o di invio del messaggio: "+ex);
                        System.exit(-1);
                    }
                    ctt = new MessaggioAddPlayer(gioctemp);
                    break;
                case COMMAND:
                    ctt = CommandHandling((MessaggioComandi)msg);
                    break;
                case ERROR:
                    ctt = ErrorHandling((MessaggioErrore)msg);
                    break;
                case CHAT:
                    ctt = msg;
                default:
                    ctt = new MessaggioChat(-1, "Messaggio non ancora gestito");
                    break;
                }
                //Stampa il messaggio di chat corrispondente e lo invia a tutti.
                    System.out.println(ctt.toString());
          for(int i = 0;i<listaGiocatori.getSize();i++)
          {
             if(i == escludi) continue;
              //System.out.println("cl: "+all);
             Giocatore_Net giotmp = (Giocatore_Net)listaGiocatori.get(i);
             if(giotmp == null) continue;
             Socket cl = giotmp.getSocket();
             if(cl.isClosed()) continue;
                    try {
                        broadcastMessage(ctt, cl);
                    } catch (IOException ex) {
                        System.err.println("Errore broadcast: "+ex.getMessage());
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
   private Messaggio ErrorHandling(MessaggioErrore errorMsg){
       switch(errorMsg.getError()){
           default:
               return new MessaggioChat(-1,"Errore non gestito.");
       }
   }

   /**
    * Funzione che gestisce i messaggi di tipo MessaggioComando per la costruzione della risposta.
    * @see risicammaraServer.Lobby
    * @param cmdMsg il pacchetto Messaggio_Comando
    */
private Messaggio CommandHandling(MessaggioComandi cmdMsg){
    //TODO Completare il codice di Exit.
    //TODO Completare il codice di KICKPLAYER
    //TODO Completare il codice di NUOVAPARTITA
    switch(cmdMsg.getComando()){
        case DISCONNECT:
            Giocatore_Net tempgioc = (Giocatore_Net)listaGiocatori.get(cmdMsg.getSender());
            PlayerThread th = (PlayerThread)tempgioc.getThread();
            if(th.isAlive()) th.setStop(true);
            Socket giosock = tempgioc.getSocket();
            listaGiocatori.remPlayer(cmdMsg.getSender());
            try {
                giosock.close();
            } catch (Exception ex) {
                System.err.println("Errore socket Disconnessione: "+ex.getMessage());
            }
            return new MessaggioComandi(comandi_t.DISCONNECT, cmdMsg.getSender());
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
   private void broadcastMessage(Messaggio recMsg, Socket cl) throws IOException
   {
         new ObjectOutputStream(cl.getOutputStream()).writeObject(recMsg);
   }

}
