/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import risicammaraServer.MessageManage.errori_t;

/**
 * La classe che rappresenta un oggetto "lobby" (sala d'attesa)
 * La lobby accetta le connessioni di massimo 6 giocatori contemporanei,
 * superato questo limite il thread che ascolta le connessioni entra in wait e sarà svegliato soltanto
 * quando i giocatori saranno meno di 6.
 * @author Sten_Gun
 */
public class Lobby {

    private ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;
    AscoltatoreLobby attendiConnessioni;

/**
 * Inizializza tutte le variabili necessarie.
 * @param porta la porta su cui aprire in ascolto il server
 * @param coda la coda dove verranno immessi i messaggi da processare
 */
    public Lobby (int porta, CodaMsg coda) {
        this.porta = porta;
        this.coda = coda;
        this.listaGiocatori = new ListaPlayers();
    }

    /**
     * Avvia la gestione dei messaggi da parte del server.
     * Il server attende che vi sia un messaggio sulla coda dei messaggi (che in questo caso
     * è come se fosse un buffer di messaggi).
     * Questa funzione esce soltanto quando un giocatore preme "nuova partita". A quel punto
     * i giocatori che fanno parte della lobby diventeranno i giocatori della partita vera e propria
     * e questa lista deve essere restituita dalla funzione.
     * @return la lista dei giocatori finale.
     */
    public ListaPlayers start(){
        attendiConnessioni = new AscoltatoreLobby(this.porta, this.coda);
        attendiConnessioni.start();
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
                    if(listaGiocatori.getSize() > 5){
                        try {
                            //TODO controllare questa sezione apre troppi stream di output
                            broadcastMessage(new MessaggioErrore(errori_t.CONNECTIONREFUSED, -1),gioctemp.getClientOut() );
                            mgio.getConnessioneGiocatore().close();
                        } catch (IOException ex) {
                            System.err.println("Errore di invio errore connessione: "+ex.getMessage());
                        }
                        finally{
                            ctt = null;
                        }
                        break;
                    }
                    gioctemp.setArmyColour(Colore_t.NULLO); //TODO probabilmente si può togliere perché non fa niente (è già nullo il colore)
                    // Indice nel quale viene inserito il giocatore
                    int plynumb = listaGiocatori.addPlayer(gioctemp);
                    PlayerThread gioth = new PlayerThread(coda,gioctemp.getClientIn(),plynumb);
                    
                    gioctemp.setNome("Giocatore"+plynumb);
                    gioctemp.AssignThread(gioth);
                    if(!gioth.isAlive()) gioth.start();
                    try {
                        ObjectOutputStream os = (gioctemp.getClientOut());
                        os.writeObject(new MessaggioConfermaNuovoGiocatore(listaGiocatori,plynumb));
                        os.flush();
                    } catch (IOException ex) {
                        System.out.println("Errore nel creare lo Stream di output verso il nuovo utente o di invio del messaggio: "+ex);
                        System.exit(-1);
                    }
                    escludi = plynumb;
                    attendiConnessioni.setNumeroGiocatori(listaGiocatori.getSize());
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

                    /* Questo codice è completamente inutile!
                    //codice temporaneo
                    MessaggioChat messaggioChat = (MessaggioChat) msg;
                    Giocatore_Net giocatore_Net = (Giocatore_Net) listaGiocatori.get(0);
                    try {
                        ObjectOutputStream stream = giocatore_Net.getClientOut();
                        stream.writeObject(messaggioChat);
                        stream.flush();
                    } catch (IOException ex) {
                        System.err.println("errore invio messaggio: "+ex);
                    }
                    //codice temporaneo
                    */

                    break;
                default:
                    ctt = new MessaggioChat(-1, "Messaggio non ancora gestito");
                    break;
                }
                //Stampa il messaggio di chat corrispondente e lo invia a tutti.
                    System.out.println(ctt.toString());
          if(ctt!= null) for(int i = 0;i<listaGiocatori.getSize();i++)
          {
             if(i == escludi) continue;
              //System.out.println("cl: "+all);
             Giocatore_Net giotmp = (Giocatore_Net)listaGiocatori.get(i);
             if(giotmp == null) continue;
             

             //Questo codice faceva andare in crash il Client: hai equivocato il
             // significato della funzione .isClosed();
             /*Socket cl = giotmp.getSocket();
             if(cl.isClosed()) continue;*/
             
                    try {
                        broadcastMessage(ctt, giotmp.getClientOut());
                    } catch (IOException ex) {
                        System.err.println("Errore broadcast: "+ex.getMessage());
                    }
                }
        }
        return listaGiocatori;
    }
   /**
    * Funzione che gestisce i messaggi di tipo MessaggioErrore per
    * la funzione receiveMessage
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
            listaGiocatori.remPlayer(cmdMsg.getSender());
            attendiConnessioni.setNumeroGiocatori(listaGiocatori.getSize());
            try {
                tempgioc.closeSocket();
            } catch (Exception ex) {
                System.err.println("Errore socket Disconnessione: "+ex.getMessage());
            }
            return new MessaggioComandi(comandi_t.DISCONNECT, cmdMsg.getSender());
        default:
            return new MessaggioChat(-1,"comando non riconosciuto.");
    }
}

    /**
     * Spedisce un pacchetto ad un client.
     * @param recMsg Il messaggio di chat
     * @param cl il client da notificare
     * @throws IOException Eccezione di I/O dovuta ai socket
     */
   private void broadcastMessage(Messaggio recMsg, ObjectOutputStream cl) throws IOException
   {
        cl.writeObject(recMsg);
        cl.flush();
        System.out.println("messaggio "+recMsg.toString()+" inviato!");
   }

}
