
package risicammaraServer;

import java.net.*;
import java.io.*;
import java.util.*;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.MessageManage.Messaggio_Comandi;
import risicammaraServer.MessageManage.Messaggio_Errore;
import risicammaraServer.MessageManage.Messaggio_chat;
import risicammarajava.playerManage.ListaPlayers;

public class Lobby
{
   private ServerSocket serverSocket;
   int clientPort;
   ArrayList clients;
   ArrayList giocatori;
   ListaPlayers gioc;
   Giocatore_Net gioctemp;


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


   private void startListeningSingleClient(Socket client)
   {
      Thread t = new Thread (new ParallelServer(client));
      gioctemp = new Giocatore_Net();
      gioctemp.AssignThread(t);
      gioctemp.setSocket(client);
      t.start();
   }


   public class ParallelServer implements Runnable
   {
      Socket client;
      ObjectInputStream is;
      ObjectOutputStream os;

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

   public void getServerInfo()
   {
      // Informazioni sul Server in ascolto
      InetAddress indirizzo = serverSocket.getInetAddress();
      String server = indirizzo.getHostAddress();
      int port = serverSocket.getLocalPort();
      System.out.println("In ascolto Server: " + server + " porta: " + port);
   }


   public void getClientInfo(Socket client)
   {
      // Informazioni sul Client che ha effettuato la chiamata
      InetAddress address = client.getInetAddress();
      String clientInfo = address.getHostName();
      clientPort = client.getPort();
      System.out.println("In chiamata Client: " + clientInfo + " porta: "
	   + clientPort);
   }


   public Socket startListening() throws IOException
   {
      System.out.println("In attesa di chiamate dai Client... ");
      return serverSocket.accept();
   }


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

   private void ErrorHandling_recv (Messaggio_Errore errorMsg){
       switch(errorMsg.getError()){
           default:
               break;
       }
   }
   
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

   private void CommandHandling(Messaggio_Comandi cmdMsg,Socket cl, Socket client){
       switch(cmdMsg.getComando()){
           case CONNECTED:
        try {
            addNewUser(cmdMsg.getSender(), cl, client);
        } catch (IOException ex) {
            System.out.println("Eccezione IO su ADDUSER");
        }
           default:
               break;
       }
   }
    /**
     *
     * @param recMsg
     * @param is
     * @param os
     * @param client
     * @throws IOException
     */

   public synchronized void sendMessage(Messaggio recMsg, ObjectInputStream is, ObjectOutputStream os,
       Socket client ) throws IOException
   {
      //int idxToRemove = -1;
      System.out.println("Sono fuori");
       for(Iterator all = clients.iterator(); all.hasNext();)
      {
         System.out.println("cl: "+all);
         Socket cl = (Socket)all.next();
         switch(recMsg.getType()){
             case COMMAND:
                 CommandHandling((Messaggio_Comandi)recMsg,cl,client);
                 break;
             case CHAT:
                 Messaggio_chat ctt = (Messaggio_chat)recMsg;
                 System.out.println(ctt.getNick()+": "+ctt.getMessaggio());
                 broadcastMessage(ctt, cl);
             default:
                 break;
         }

      }
      /*
      // Se ci sono client da rimuovere, tale rimozione viene effettuata
      // solo al termine del ciclo per evitare problemi sugli indici.
      if (idxToRemove > -1)
         clients.remove(idxToRemove);
       * 
       */
   }


   private void addNewUser(String recMsg, Socket cl, Socket client) throws IOException
   {
       //TODO codice per l'aggiunta di un utente nella lista
      if (cl.equals(client))
      {
         recMsg = "Aggiunto";
      }
         new ObjectOutputStream(cl.getOutputStream()).writeObject(new Messaggio_chat("SERVER", recMsg));
   }


   private int removeUser(String recMsg, int idxToRemove, Socket cl, Socket client)
      throws IOException
   {
       //TODO codice per la rimozione di un utente dalla lista
       /*
      if (cl.equals(client))
      {
         close(cl.getInputStream(), cl.getOutputStream(), cl);
         idxToRemove = clients.indexOf(cl);
      }
      else
      {
         String strName = recMsg.substring(1);
         new DataOutputStream(cl.getOutputStream()).writeBytes("REMOVEUSER" +
            strName + "\n");
      }
      return idxToRemove;
        *
        */
       return 0;
   }


   private void broadcastMessage(Messaggio_chat recMsg, Socket cl) throws IOException
   {
      // Il messaggio "NOGOODUSER" deve essere inviato
      // solo ad uno specifico  client (gestito nella sendMessage).
         new ObjectOutputStream(cl.getOutputStream()).writeObject(recMsg);
   }


   public void close(InputStream is, OutputStream os, Socket client)
      throws IOException
   {
      System.out.println("Chiamata close");
      // chiusura della comunicazione con il Client
      os.close();
      is.close();
      System.out.println("Chiusura chiamata Client: " +
         client.getInetAddress().getHostName() + "su porta: " + clientPort);
      client.close();
   }
}
