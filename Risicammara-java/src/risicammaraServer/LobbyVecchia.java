
package risicammaraServer;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.MessageManage.Messaggio_Comandi;
import risicammaraServer.MessageManage.Messaggio_Errore;
import risicammaraServer.MessageManage.Messaggio_chat;
import risicammaraJava.playerManage.ListaPlayers;
/**
 * Classe che rappresenta la LobbyVecchia lato server (che fornisce la lista dei giocatori necessaria
 * per avviare una partita).
 * @author Sten_Gun
 */
public class LobbyVecchia
{
   private ServerSocket serverSocket;
   int clientPort;
   ArrayList clients;
   ArrayList giocatori;
   ListaPlayers gioc;
   Giocatore_Net gioctemp;

/**
 * Avvia il thread della lobby per l'ascolto.
 * @throws IOException Eccezione di Input Output (se il socket è nullo o non disponibile)
 */
   public void start() throws IOException
   {
      clients = new ArrayList();
      giocatori = new ArrayList();
      gioc = new ListaPlayers();
      Socket client;
      serverSocket = new ServerSocket(12345);
      getServerInfo();

      // Ciclo infinito in ascolto di eventuali Client
      while (true)
      {
         client = startListening();
         clients.add(client);
         getClientInfo(client);
         startListeningSingleClient(client);
      }
   }

/**
 * Funzione che avvia il thread che si occuperà di ascoltare esclusivamente un
 * giocatore preciso.
 * @param client Il giocatore che il thread dovrà seguire
 */
   private void startListeningSingleClient(Socket client)
   {
      Thread t = new Thread (new ParallelServer(client));
      gioctemp = new Giocatore_Net();
      gioctemp.AssignThread(t);
      gioctemp.setSocket(client);
      t.start();
   }

/**
 * La classe che rappresenta il Thread.
 */
   public class ParallelServer implements Runnable
   {
      Socket client;
      ObjectInputStream is;
      ObjectOutputStream os;
/**
 * Il costruttore della classe che rappresenta il Thread per ogni giocatore.
 * Inizializza le variabili necessarie e gli stream di input e output di oggetti serializzati.
 * @param client Il socket del giocatore da seguire.
 */
      public ParallelServer(Socket client)
      {
         this.client = client;
         try
         {
            // Stream di byte utilizzato per la comunicazione via socket
            is = new ObjectInputStream(client.getInputStream());
            os = new ObjectOutputStream(client.getOutputStream());
         }
         catch (Exception ex)
         {
         }
      }
/**
 * Implementa il metodo run di ::Runnable.
 */
      public void run()
      {
         try
         {
            while (this.client.isConnected())
            {
               sendMessage(receiveMessage(is), is, os, client);
            }
            System.out.println("Utente "+client+" disconnesso.");
         }
         catch (Exception ex)
         {
         }
      }
   }

   /**
    * Fornisce le informazioni del server per il logging
    */
   public void getServerInfo()
   {
      // Informazioni sul Server in ascolto
      InetAddress indirizzo = serverSocket.getInetAddress();
      String server = indirizzo.getHostAddress();
      int port = serverSocket.getLocalPort();
      System.out.println("In ascolto Server: " + server + " porta: " + port);
   }

/**
 * Fornisce le informazioni del client per il logging.
 * @param client il socket del client da inserire nei logs.
 */
   public void getClientInfo(Socket client)
   {
      // Informazioni sul Client che ha effettuato la chiamata
      InetAddress address = client.getInetAddress();
      String clientInfo = address.getHostName();
      clientPort = client.getPort();
      System.out.println("In chiamata Client: " + clientInfo + " porta: "
	   + clientPort);
   }

/**
 * Lascia il server in ascolto finché non si collega un client.
 * @return il socket della connessione.
 * @throws IOException Eccezione di InputOutput causata dal socket (read error).
 */
   public Socket startListening() throws IOException
   {
      System.out.println("In attesa di chiamate dai Client... ");
      return serverSocket.accept();
   }

/**
 * Preleva il messaggio da uno stream di input e ne legge il contenuto per gestire
 * tutti i casi possibili.
 * @param is Lo stream di input di oggetti serializzati.
 * @return L'oggetto "Messaggio" ricevuto
 * @throws IOException Eccezione di Input Output causata dagli input stream.
 * @throws ClassNotFoundException Eccezione legata alla Deserializzazione dell'oggetto.
 */
   public synchronized Messaggio receiveMessage(ObjectInputStream is) throws IOException, ClassNotFoundException
   {
      Messaggio msgReceived = (Messaggio)is.readObject();
      switch(msgReceived.getType()){
          case ERROR:
              ErrorHandling_recv(((Messaggio_Errore)msgReceived));
          case COMMAND:
              CommandHandling_recv((Messaggio_Comandi)msgReceived);
          default:
              break;
      }
      
      return msgReceived;
   }

   /**
    * Funzione che gestisce i messaggi di tipo ::Messaggio_Errore per
    * la funzione ::receiveMessage
    * @param errorMsg Il pacchetto Messaggio_Errore
    */
   private void ErrorHandling_recv (Messaggio_Errore errorMsg){
       switch(errorMsg.getError()){
           default:
               break;
       }
   }

   /**
    * Funzione che gestisce i messaggi di tipo ::Messaggio_Comando per la
    * funzione ::receiveMessage
    * @param cmdMsg il pacchetto Messaggio_Comando
    */
   private void CommandHandling_recv(Messaggio_Comandi cmdMsg){
        switch(cmdMsg.getComando()){
            case CONNECTED:
                Random rand = new Random();
                String nick = cmdMsg.getSender();
                int caz = 0;
                while(giocatori.contains(nick)){
                   caz = rand.nextInt(20);
                   nick = nick + caz;
                }
                giocatori.add(nick);
                gioctemp.setNome(nick);
                this.gioc.addPlayer(gioctemp);
                System.out.println(giocatori);
            default:
                break;
        }
   }

   /**
    * Funzione che gestisce i messaggi di tipo ::Messaggio_Comando per la
    * funzione ::sendMessage
    * @param cmdMsg il pacchetto Messaggio_Comando
    * @param cl il client su cui si sta iterando (dalla lista)
    * @param client il client che si sta servendo al momento.
    */
   private void CommandHandling(Messaggio_Comandi cmdMsg,Socket cl, Socket client,int idx){
       switch(cmdMsg.getComando()){
           case CONNECTED:
        try {
            addNewUser(cmdMsg.getSender(), cl, client);
        } catch (IOException ex) {
            System.err.println("Eccezione I/O su ADDUSER");
        }
            break;
           case DISCONNECT:
        try {
            idx = removeUser(cmdMsg.getSender(), idx, cl, client);
        } catch (IOException ex) {
            System.err.println("Eccezione I/O su REMOVE USER");
        }
           default:
               break;
       }
   }
    /**
     * Funzione che invia i messaggi al client in base al contenuto.
     * @param recMsg Il Messaggio da valutare
     * @param is Stream di input di oggetti serializzati
     * @param os Stream di output di oggetti serializzati
     * @param client client che stiamo servendo
     * @throws IOException Eccezione causata dagli stream di input/output e dal socket.
     */

   public synchronized void sendMessage(Messaggio recMsg, ObjectInputStream is, ObjectOutputStream os,
       Socket client ) throws IOException
   {
      int idxToRemove = -1;
      //System.out.println("Sono fuori");
       for(Iterator all = clients.iterator(); all.hasNext();)
      {
         //System.out.println("cl: "+all);
         Socket cl = (Socket)all.next();
         switch(recMsg.getType()){
             case COMMAND:
                 CommandHandling((Messaggio_Comandi)recMsg,cl,client,idxToRemove);
                 break;
             case CHAT:
                 Messaggio_chat ctt = (Messaggio_chat)recMsg;
                 System.out.println(ctt.getNick()+": "+ctt.getMessaggio());
                 broadcastMessage(ctt, cl);
             default:
                 break;
         }

      }

      if (idxToRemove > -1){
         clients.remove(idxToRemove);
         this.gioc.remPlayer(idxToRemove);
       }
   }

/**
 * Notifica tutti i giocatori che è presente un nuovo utente nella sala d'attesa.
 * @param recMsg Il nome del giocatore che è appena entrato nel server.
 * @param cl il client su cui stiamo iterando
 * @param client il client che stiamo servendo
 * @throws IOException Eccezione causata dai socket.
 */
   private void addNewUser(String recMsg, Socket cl, Socket client) throws IOException
   {
       recMsg = recMsg + " è appena entrato";
      if (cl.equals(client))
      {
         recMsg = "Joined.";
      }
         new ObjectOutputStream(cl.getOutputStream()).writeObject(new Messaggio_chat("SERVER", recMsg));
   }

/**
 * Notifica tutti i giocatori che un utente ha abbandonato la sala.
 * Fornisce l'indice del giocatore che verrà rimosso alla fine della funzione
 * "sendMessage"
 * @param recMsg Il nome dell'utente che è uscito
 * @param idxToRemove L'indice dell'utente che va rimosso.
 * @param cl Il client su cui stiamo iterando
 * @param client il client che stiamo servendo
 * @return L'indice da rimuovere
 * @throws IOException Eccezione di I/O dovuta ai socket.
 */
   private int removeUser(String recMsg, int idxToRemove, Socket cl, Socket client)
      throws IOException
   {
       //TODO codice per la rimozione di un utente dalla lista
      if (cl.equals(client))
      {
         close(cl.getInputStream(), cl.getOutputStream(), cl);
         idxToRemove = clients.indexOf(cl);
      }
      else
      {
          recMsg = recMsg + " è uscito dalla sala d'attesa.";
         new ObjectOutputStream(cl.getOutputStream()).writeObject(new Messaggio_chat("SERVER", recMsg));
      }
      return idxToRemove;
   }

/**
 * Notifica un client della ricezione di un messaggio di chat.
 * @param recMsg Il messaggio di chat
 * @param cl il client da notificare (iterato)
 * @throws IOException Eccezione di I/O dovuta ai socket
 */
   private void broadcastMessage(Messaggio_chat recMsg, Socket cl) throws IOException
   {
         new ObjectOutputStream(cl.getOutputStream()).writeObject(recMsg);
   }

/**
 * Chiude tutti gli input/output stream e chiude le comunicazioni con il client.
 * @param is Input stream da chiudere
 * @param os Output stream da chiudere
 * @param client Client da chiudere
 * @throws IOException Eccezione di I/O sollevata da Input/Output stream e Socket.
 */
   public void close(InputStream is, OutputStream os, Socket client)
      throws IOException
   {
      System.out.println("Chiudo tutti gli stream");
      // chiusura della comunicazione con il Client
      os.close();
      is.close();
      System.out.println("Chiusura chiamata Client: " +
         client.getInetAddress().getHostName() + " porta: " + clientPort);
      client.close();
   }
}
