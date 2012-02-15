package risicammara2.client;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import risicammara2.global.Player;
import PacchettoGrafico.salaAttesa.CronologiaChat;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import risicammaraClient.Colore_t;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import risicammara2.global.MessaggioLista;
import risicammara2.global.MessaggioPlayer;
import risicammaraServer.messaggiManage.Messaggio;
import risicammaraServer.messaggiManage.MessaggioCambiaNickColore;
import risicammaraServer.messaggiManage.MessaggioChat;
import risicammaraServer.messaggiManage.MessaggioComandi;
import risicammaraServer.messaggiManage.MessaggioHelo;

/**
 * Finestra principale del pre-partita. Qui i giocatori possono scegliere il loro
 * nickname, il colore, inviare messaggi di chat e decidere le regole della partita.
 * @author stengun
 */
public class PreGioco extends JFrame implements ActionListener,Runnable,WindowListener
{

    private void parseMsg(Messaggio msg) {
        switch(msg.getType()){
            case MODIFICANICKCOLORE:
                MessaggioCambiaNickColore msg2 = (MessaggioCambiaNickColore)msg;
                for(int i = 0; i<6;i++){
                    JButton_player btp = (JButton_player)btn_avat_arr[i][0];
                    if(btp.getPlayer_id() != msg2.getSender()) continue;
                    Player pl = lista_players.get(msg2.getSender());
                    btp.setText(msg2.getNick());
                    pl.setNome(msg2.getNick());
                    if(msg2.getColore() == pl.getColoreArmate()) break;
                    if(pl.getId() != my_id){
                        campo_colore.addItem(pl.getColoreArmate());
                        campo_colore.removeItem(msg2.getColore());
                    }
                    pl.setColoreArmate(msg2.getColore());
                    btp.setForeground(msg2.getColore().getColor());
                    break;
                }
                break;
            case COMMAND:
                parseComando((MessaggioComandi)msg);
                break;
            case PLAYER:
                MessaggioPlayer tmp = ((MessaggioPlayer)msg);
                if(tmp.getId() == my_id) return;
                for(int i = 0; i<6; i++){
                    JButton_player bottone = (JButton_player)btn_avat_arr[i][0];
                    if(!bottone.isShowing()){
                        Player g = tmp.getPlayer();
                        bottone.setText(g.getNome());
                        bottone.setPlayer_id(tmp.getId());
                        lista_players.put(tmp.getId(), g);
                        bottone.setVisible(true);
                        ((JToggleButton)btn_avat_arr[i][2]).setVisible(true);
                        return;
                    }
                }
                break;
            case CHAT:
                if(msg.getSender() <= 0) cronologia_chat.stampaMessaggioComando(
                        ((MessaggioChat)msg).getMessaggio());
                else cronologia_chat.stampaMessaggio(
                        this.lista_players.get(msg.getSender()).getNome() + ": " + 
                        ((MessaggioChat)msg).getMessaggio());
                break;
            default:
                break;
        }
    }

    private void parseComando(MessaggioComandi messaggioComandi) {
        System.out.println("Messaggio: "+messaggioComandi.toString());
        switch(messaggioComandi.getComando()){
            case LEADER:
                diventa_leader();
                cronologia_chat.stampaMessaggioComando("Sei il leader della sala");
                break;            
            case KICKPLAYER:
                cronologia_chat.stampaMessaggioComando(lista_players.get(messaggioComandi.getOptParameter()).getNome() + 
                        " è stato espulso");
                if(messaggioComandi.getOptParameter() != my_id) break;
            case EXIT:
                setStop(true);
                break;
            case DISCONNECT:
                if(messaggioComandi.getOptParameter() == my_id){
                    setStop(true);
                    break;
                }
                for(int i = 0;i<6;i++){
                    JButton_player bp = (JButton_player)btn_avat_arr[i][0];
                    if(bp.getPlayer_id() == messaggioComandi.getSender()){
                        bp.setVisible(false);
                        ((JToggleButton)btn_avat_arr[i][2]).setVisible(false);
                        i=6;
                    }
                }
                cronologia_chat.stampaMessaggioComando(
                        lista_players.get(messaggioComandi.getSender()).getNome()
                        + "si è disconnesso");
                lista_players.remove(messaggioComandi.getSender());
                break;
            case SETPRONTO:
                for(int i=0;i<6;i++){
                    JToggleButton ready = ((JToggleButton)btn_avat_arr[i][2]);
                    JButton_player bt_player = (JButton_player)btn_avat_arr[i][0];
                    if(bt_player.getPlayer_id() 
                            == messaggioComandi.getSender()){
                        ready.setSelected(!ready.isSelected());
                    }
                    
                }
            default:
                break;
        }
    }

    private void setStop(boolean b) {
        this.stop = b;
        //messaggi_arrivo.notify();
        try {
            my_socket.close();
        } catch (IOException ex) {
            Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, 
                    "Socket chiuso per stop", ex);
        }
    }

    private void diventa_leader() {
        for(int i= 0;i<6;i++){
            ((JButton)btn_avat_arr[i][0]).setEnabled(true);
            //((JToggleButton)btn_avat_arr[i][2]).setEnabled(true);
        }
    }

    private static class AscoltaMessaggiArrivo extends Thread{
        ArrayBlockingQueue<Messaggio> coda;
        ObjectInputStream is;
        private boolean stop;
        
        public AscoltaMessaggiArrivo(ArrayBlockingQueue<Messaggio> coda,
                ObjectInputStream is) {
            this.coda = coda;
            this.is = is;
            this.stop = false;
        }
        
        @Override
        public void run()
        {
            while(!stop)
            {
                Messaggio msg = null;
                try {
                    msg = (Messaggio)is.readObject();
                } catch (IOException ex) {
                    Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, 
                            "Connessione persa!", ex);
                    setStop(true);
                    msg = MessaggioComandi.creaMsgExit(-1);                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
                    msg = null;
                } finally {
                    if(msg == null) continue;
                    try {
                        coda.put(msg);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
                        setStop(true);
                    }
                }
            }
        }
        public void setStop(boolean stop){
            this.stop = stop;
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Colore_t col_prec;
    private ArrayBlockingQueue<Messaggio> messaggi_arrivo;
    private long my_id;
    private String nome;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private Socket my_socket;
    private Object[][] btn_avat_arr;
    private JButton_player btn_giocatore1,
                    btn_giocatore2,
                    btn_giocatore3,
                    btn_giocatore4,
                    btn_giocatore5,
                    btn_giocatore6;
    private JButton conferma_dati,
                    invia_chat;
    private JToggleButton   ready1,ready2,
                            ready3,ready4,
                            ready5,ready6;
    private JLabel   avatar1,avatar2,
                        avatar3,avatar4,
                        avatar5,avatar6;
    private JLabel  desc_nick,desc_color;
    private CronologiaChat cronologia_chat;
    private JComboBox<Colore_t> campo_colore;
    private JTextField  campo_testo_chat,campo_testo_nick;
    private JPanel  pannello_chat,pannello_giocatori,pannello_campi;
    private ConcurrentHashMap<Long,Player> lista_players;
    private AscoltaMessaggiArrivo thread_arrivo;
    private boolean stop;
    private int my_local_id;
    
    public PreGioco(String title,Socket s,String nome) throws HeadlessException 
    {
        super(title);
        this.col_prec = Colore_t.NULLO;
        this.stop = false;
        this.my_socket = s;
        try{
            this.os = new ObjectOutputStream(s.getOutputStream());
            this.is = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex){
            System.err.println("Errore apertura stream su "+this.getName());
            return;
        }
        this.messaggi_arrivo = new ArrayBlockingQueue<Messaggio>(30);
        this.my_id = 0;
        this.lista_players = null;
        this.nome = nome;
        //Finestra Lobby
        //pannelli
        this.pannello_chat = new JPanel();
        this.pannello_giocatori = new JPanel();
        this.pannello_campi = new JPanel();
        //giocatori
        this.btn_giocatore1 = new JButton_player();
        this.btn_giocatore2 = new JButton_player();
        this.btn_giocatore3 = new JButton_player();
        this.btn_giocatore4 = new JButton_player();
        this.btn_giocatore5 = new JButton_player();
        this.btn_giocatore6 = new JButton_player();
        this.avatar1 = new JLabel();
        this.avatar2 = new JLabel();
        this.avatar3 = new JLabel();
        this.avatar4 = new JLabel();
        this.avatar5 = new JLabel();
        this.avatar6 = new JLabel();        
        this.ready1 = new JToggleButton("Ω");
        this.ready2 = new JToggleButton("Ω");
        this.ready3 = new JToggleButton("Ω");
        this.ready4 = new JToggleButton("Ω");
        this.ready5 = new JToggleButton("Ω");
        this.ready6 = new JToggleButton("Ω");
        //Matrice con corrispondenze su bottoni e avatar per facile cambio nomi.
        this.btn_avat_arr = new Object[6][3];
        this.btn_avat_arr[0][0] = btn_giocatore1;
        this.btn_avat_arr[1][0] = btn_giocatore2;
        this.btn_avat_arr[2][0] = btn_giocatore3;
        this.btn_avat_arr[3][0] = btn_giocatore4;
        this.btn_avat_arr[4][0] = btn_giocatore5;
        this.btn_avat_arr[5][0] = btn_giocatore6;
        this.btn_avat_arr[0][1]= null; //avatar1;
        this.btn_avat_arr[1][1]= null; //avatar2;
        this.btn_avat_arr[2][1]= null; //avatar3;
        this.btn_avat_arr[3][1]= null; //avatar4;
        this.btn_avat_arr[4][1]= null; //avatar5;
        this.btn_avat_arr[5][1]= null; //avatar6;
        this.btn_avat_arr[0][2] = ready1;
        this.btn_avat_arr[1][2] = ready2;
        this.btn_avat_arr[2][2] = ready3;
        this.btn_avat_arr[3][2] = ready4;
        this.btn_avat_arr[4][2] = ready5;
        this.btn_avat_arr[5][2] = ready6;
        imposta_tasti();
        
        this.imposta_layout_giocatori();
        //Campo per la scelta del colore e del nick.
        this.campo_colore = new JComboBox<Colore_t>(Colore_t.values());
        this.campo_testo_nick = new JTextField();
        this.conferma_dati = new JButton("Conferma dati");
        this.pannello_campi = new JPanel();
        this.desc_nick = new JLabel("Nickname:");
        this.desc_color = new JLabel("Colore:");
        this.imposta_layout_scelta();
        //Impostazione della chat
        this.pannello_chat = new JPanel();
        this.cronologia_chat = new CronologiaChat(512);
        this.campo_testo_chat = new JTextField();
        this.invia_chat = new JButton("Invia messaggio");
        this.imposta_layout_chat();
        
        this.imposta_layout();
        
        
        
        
    }
    private void imposta_tasti()
    {
        Dimension dimensione_bottoni = new Dimension(160, 45);
        Dimension dimensione_avatar = new Dimension(45, 45);
        for(int i = 0; i<6;i++){
            JButton bottone = ((JButton)btn_avat_arr[i][0]);
            JToggleButton ready = ((JToggleButton)btn_avat_arr[i][2]);
            bottone.setVisible(false);
            bottone.setMinimumSize(dimensione_bottoni);
            bottone.setMaximumSize(new Dimension(1024, 45));
            bottone.addActionListener(this);
            bottone.setEnabled(false);
            ready.setVisible(false);
            ready.setMinimumSize(dimensione_avatar);
            ready.setMaximumSize(dimensione_avatar);
            ready.addActionListener(this);
            ready.setEnabled(false);
            //((JLabel)btn_avat_arr[i][1]).setVisible(false);
        }
    }
    private void popola_tasti()
    {
        Iterator<Player> iteralista = lista_players.values().iterator();
        for(int i = 0;iteralista.hasNext();i++)
        {
            Player gio = iteralista.next();
            JButton_player bottone = ((JButton_player)btn_avat_arr[i][0]);
            bottone.setText(gio.getNome());
            bottone.setPlayer_id(gio.getId());
            
            JLabel avatar = ((JLabel)btn_avat_arr[1][1]);
            if(avatar != null){
                avatar.setIcon(new ImageIcon(gio.getAvatar()));
                ((JButton)btn_avat_arr[i][1]).setVisible(true);
            }
            ((JButton)btn_avat_arr[i][0]).setVisible(true);
            ((JToggleButton)btn_avat_arr[i][2]).setVisible(true);
            if(gio.getId() == my_id){
                bottone.setEnabled(true);
                ((JToggleButton)btn_avat_arr[i][2]).setEnabled(true);
            }
            
        }
    }
    
    private void imposta_layout_giocatori()
    {
        GroupLayout gioc1 = new GroupLayout(pannello_giocatori);
        gioc1.setAutoCreateGaps(true);
        gioc1.setAutoCreateContainerGaps(true);
        gioc1.setHorizontalGroup(gioc1.createSequentialGroup()
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore1)
                .addComponent(btn_giocatore2)
                .addComponent(btn_giocatore3)
                .addComponent(btn_giocatore4)
                .addComponent(btn_giocatore5)
                .addComponent(btn_giocatore6))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(avatar1)
                .addComponent(avatar2)
                .addComponent(avatar3)
                .addComponent(avatar4)
                .addComponent(avatar5)
                .addComponent(avatar6))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(ready1)
                .addComponent(ready2)
                .addComponent(ready3)
                .addComponent(ready4)
                .addComponent(ready5)
                .addComponent(ready6)));
        gioc1.setVerticalGroup(gioc1.createSequentialGroup()
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore1)
                .addComponent(avatar1)
                .addComponent(ready1))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore2)
                .addComponent(avatar2)
                .addComponent(ready2))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore3)
                .addComponent(avatar3)
                .addComponent(ready3))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore4)
                .addComponent(avatar4)
                .addComponent(ready4))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore5)
                .addComponent(avatar5)
                .addComponent(ready5))
                .addGroup(gioc1.createParallelGroup()
                .addComponent(btn_giocatore6)
                .addComponent(avatar6)
                .addComponent(ready6)));
        pannello_giocatori.setLayout(gioc1);
        pannello_giocatori.setMaximumSize(new Dimension(200, 1024));
    }

    private void imposta_layout_scelta() 
    {
        desc_nick.setMinimumSize(new Dimension(180, 25));
        desc_color.setMinimumSize(new Dimension(180, 25));
        campo_testo_nick.setMinimumSize(new Dimension(180, 25));
        campo_colore.setMinimumSize(new Dimension(180, 25));
        campo_testo_nick.setMaximumSize(new Dimension(1024, 25));
        campo_colore.setMaximumSize(new Dimension(1024, 25));
        conferma_dati.addActionListener(this);
        
        GroupLayout descr = new GroupLayout(pannello_campi);
        descr.setAutoCreateContainerGaps(true);
        descr.setAutoCreateGaps(true);
        descr.setHorizontalGroup(descr.createSequentialGroup()
                .addGroup(descr.createParallelGroup()
                .addComponent(desc_nick)
                .addComponent(campo_testo_nick))
                .addGroup(descr.createParallelGroup()
                .addComponent(desc_color)
                .addGroup(descr.createSequentialGroup()
                .addComponent(campo_colore)
                .addComponent(conferma_dati))));
        descr.setVerticalGroup(descr.createSequentialGroup()
                .addGroup(descr.createParallelGroup()
                .addComponent(desc_nick)
                .addComponent(desc_color))
                .addGroup(descr.createParallelGroup()
                .addComponent(campo_testo_nick)
                .addComponent(campo_colore)
                .addComponent(conferma_dati)));
        pannello_campi.setLayout(descr);
    }

    private void imposta_layout_chat() 
    {
        JPanel testo = new JPanel();
        JScrollPane porco = cronologia_chat.inscatolaInScrollPane(new Rectangle(350, 500));
        GroupLayout invio = new GroupLayout(testo);
        invio.setAutoCreateContainerGaps(true);
        invio.setAutoCreateGaps(true);
        invio.setHorizontalGroup(invio.createSequentialGroup()
                .addComponent(campo_testo_chat)
                .addComponent(invia_chat));
        invio.setVerticalGroup(invio.createSequentialGroup()
                .addGroup(invio.createParallelGroup()
                .addComponent(campo_testo_chat)
                .addComponent(invia_chat)));
        
        GroupLayout chat = new GroupLayout(pannello_chat);
        chat.setAutoCreateContainerGaps(true);
        chat.setAutoCreateGaps(true);
        chat.setHorizontalGroup(chat.createSequentialGroup()
                .addGroup(chat.createParallelGroup()
                .addComponent(porco)
                .addComponent(testo)));
        chat.setVerticalGroup(chat.createSequentialGroup()
                .addComponent(porco)
                .addComponent(testo));
        
        testo.setLayout(invio);
        
        invia_chat.addActionListener(this);
        pannello_chat.setLayout(chat);
        cronologia_chat.setMinimumSize(new Dimension(350, 500));
    }

    private void imposta_layout() 
    {
        JPanel destro = new JPanel();
        GroupLayout l_destro = new GroupLayout(destro);
        l_destro.setAutoCreateContainerGaps(false);
        l_destro.setAutoCreateGaps(false);
        l_destro.setHorizontalGroup(l_destro.createSequentialGroup()
                .addGroup(l_destro.createParallelGroup()
                .addComponent(pannello_campi)
                .addComponent(pannello_chat)));
        l_destro.setVerticalGroup(l_destro.createSequentialGroup()
                .addComponent(pannello_campi)
                .addComponent(pannello_chat));
        destro.setLayout(l_destro);
        JPanel totale = new JPanel();
        GroupLayout layout_principale = new GroupLayout(totale);
        layout_principale.setAutoCreateContainerGaps(false);
        layout_principale.setAutoCreateGaps(false);
        layout_principale.setHorizontalGroup(layout_principale.createSequentialGroup()
                .addComponent(pannello_giocatori)
                .addComponent(destro));
        layout_principale.setVerticalGroup(layout_principale.createParallelGroup()
                .addGroup(layout_principale.createParallelGroup()
                .addComponent(pannello_giocatori)
                .addComponent(destro)));
        totale.setLayout(layout_principale);
        getContentPane().add(totale);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 600));
    }
        
    @Override
    public void run()
    {
        this.addWindowListener(this);
        campo_testo_chat.addActionListener(this);
        this.thread_arrivo = new AscoltaMessaggiArrivo(messaggi_arrivo, is);
        try {
                MessaggioComandi msg = (MessaggioComandi)is.readObject();
                my_id = msg.getSender();
                Object[] data = new Object[3];
                data[0] = nome;
                data[1] = new Double(0.2).doubleValue();
                data[2] = new Integer(1).intValue();
                os.writeObject(new MessaggioHelo(my_id,data));
                MessaggioLista list = (MessaggioLista)is.readObject();
                this.lista_players = list.getList();
            } catch (ClassNotFoundException ex){
                System.err.println("Errore classe non serializzata");
            } catch (IOException ex) {
                System.err.println("Errore IO");
            }
        popola_tasti();
        this.thread_arrivo.start();
        this.setVisible(true);
        while(!stop){
            Messaggio msg = null;
            try {
                msg = this.messaggi_arrivo.take();
            }catch (InterruptedException ex) {
                Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
                setStop(true);
            } 
            if(msg == null) continue;
            parseMsg(msg);
        }
        thread_arrivo.setStop(true);
        this.setVisible(false);
        FinestraConnessione fin = new FinestraConnessione("Risicammara 2 - Connessione");
        fin.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args)
    {
        FinestraConnessione connessione = new FinestraConnessione("Risicammara 2 - Connetti");
        connessione.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == conferma_dati){
            String nick = campo_testo_nick.getText();
            Colore_t colore = (Colore_t)campo_colore.getSelectedItem();
            if( nick.equals("") || nick.equals(nome)){
                nick = nome;
                if(colore.equals(col_prec)) return;
            }
            try {
                os.writeObject(new MessaggioCambiaNickColore(nick, colore, my_id));
            } catch (IOException ex) {
                Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(colore.equals(Colore_t.NULLO)){
                ((JToggleButton)btn_avat_arr[my_local_id][2]).setEnabled(false);
                ((JToggleButton)btn_avat_arr[my_local_id][2]).setSelected(false);
            }
        }
        if(e.getSource() == invia_chat || e.getSource() == campo_testo_chat){
            try {
                String testo = campo_testo_chat.getText();
                if(testo.equals("")) return;
                os.writeObject(new MessaggioChat(my_id, testo));
            } catch (IOException ex) {
                Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
            }
            campo_testo_chat.setText("");
            campo_testo_chat.requestFocus();
            return;
        }
        for(int i = 0;i<6;i++){
            JButton_player bottone = (JButton_player) btn_avat_arr[i][0];
            JToggleButton ready = (JToggleButton) btn_avat_arr[i][2];
            if(e.getSource() == bottone){
                try {
                    os.writeObject(MessaggioComandi.creaMsgKickplayer(my_id, bottone.getPlayer_id()));
                } catch (IOException ex) {
                    Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            else if(e.getSource() == ready){
                ready.setSelected(!ready.isSelected());
                try {
                    os.writeObject(MessaggioComandi.creaMsgSetPronto(my_id));
                } catch (IOException ex) {
                    Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            thread_arrivo.setStop(true);
            //throw new UnsupportedOperationException("Not supported yet.");
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(PreGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
        FinestraConnessione conn = new FinestraConnessione("Risicammara 2 - Connessione");
        conn.setVisible(true);
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        this.dispose();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
