package risicammaraServer;

import java.io.IOException;
import risicammaraJava.playerManage.ListaPlayers;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.turnManage.SuccessioneTurni;

/**
 * Lato lobby di Risicammara.
 * @author Sten_Gun
 */
public class Server extends Thread {
    private ListaPlayers listaGiocatori;
    private CodaMsg coda;
    private int porta;
    private boolean stop;
    private Lobby lobby;
/**
 * Ininzializza tutti i parametri del lobby.
 * @param porta la porta da cui ascoltare le connessioni.
 */
    public Server(int porta) {
        this.porta = porta;
        this.coda = new CodaMsg();
        this.stop = false;

    }
/**
 * Metodo che serve per far partire (all'occorrenza) il lobby come thread
 * (per esempio quando si fa partire un lobby in locale direttamente dalla gui).
 *
 */
    @Override
    public void run() {
        while(!stop){
            lobby = new Lobby(porta,coda);
            listaGiocatori = lobby.start();
            if(listaGiocatori == null) stop = true;
            else {
                SuccessioneTurni svolgimento = new SuccessioneTurni(listaGiocatori, coda);
                svolgimento.start();
            }
        }
    }

    public void setStop(boolean stop){
        this.stop=stop;
        lobby.setStop(stop);
    }
//---------------------------------------------------- Metodi statici
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
            gtmp.sendMessage(recMsg);
        }
    }
}

