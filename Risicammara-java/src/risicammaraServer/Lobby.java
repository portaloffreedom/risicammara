
package risicammaraServer;

import risicammaraServer.MessageManage.errori_t;
import java.net.*;
import java.io.*;
import java.util.*;
import risicammaraServer.MessageManage.Messaggio;
import risicammaraServer.MessageManage.Messaggio_Comandi;
import risicammaraServer.MessageManage.Messaggio_Errore;
import risicammaraServer.MessageManage.Messaggio_chat;
import risicammaraServer.MessageManage.comandi_t;
import risicammaraServer.MessageManage.messaggio_t;

public class Lobby
{
   private ServerSocket serverSocket;
   int clientPort;
   ArrayList clients;
   ArrayList nicks;


   public void start() throws IOException
   {
      clients = new ArrayList();
      nicks = new ArrayList();
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
            while (true)
            {
               sendMessage(receiveMessage(is), is, os, client);
            }
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


   public Messaggio receiveMessage(ObjectInputStream is) throws IOException, ClassNotFoundException
   {
      Messaggio msgReceived = (Messaggio)is.readObject();
      switch(msgReceived.getType()){
          case ERROR:
              if(((Messaggio_Errore)msgReceived).getError().equals(errori_t.INVALIDNICK)){
                //TODO codice per nick non valido
              }
          case COMMAND:
              if(((Messaggio_Comandi)msgReceived).getComando().equals(comandi_t.DISCONNECT)){
                  //TODO codice per giocatore disconnesso
              }
          default:
              break;
      }
      
      return msgReceived;
   }


   public void sendMessage(Messaggio recMsg, ObjectInputStream is, ObjectOutputStream os,
       Socket client ) throws IOException
   {
      int idxToRemove = -1;
      for(Iterator all = clients.iterator(); all.hasNext();)
      {
         Socket cl = (Socket)all.next();
         switch(recMsg.getType()){
             case COMMAND:
             case CHAT:
                 broadcastMessage(((Messaggio_chat)recMsg), cl);
             default:
                 break;
         }
         /*         if(recMsg.startsWith("~"))
            addNewUser(recMsg, cl, client);
         else if(recMsg.startsWith("^"))
            idxToRemove = removeUser(recMsg, idxToRemove, cl, client);
         else
         {
            if(recMsg.equals("NOGOODUSER") && cl.equals(client))
            {
               new DataOutputStream(cl.getOutputStream()).writeBytes(recMsg + "\n");
               idxToRemove = removeUser(recMsg, idxToRemove, cl, client);
            }
            else
               broadcastMessage(recMsg, cl);
         }
 */

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
       /*
      if (cl.equals(client))
      {
         StringBuffer response = new StringBuffer();
         for (int i = 0; i < nicks.size(); i++)
         {
            response.append(nicks.get(i));
            response.append("~");
         }

         String strResponse = "OKNEWUSER" + response.toString();
         System.out.println(strResponse);
         new DataOutputStream(cl.getOutputStream()).writeBytes(strResponse + "\n");
      }
      else
      {
         String strName = recMsg.substring(1);
         new DataOutputStream(cl.getOutputStream()).writeBytes("NEWUSER" + strName +
            "\n");
      }
        * 
        */
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
