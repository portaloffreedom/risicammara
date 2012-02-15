package risicammara2.client;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import risicammara2.server.Server_test;

/**
 *
 * @author stengun
 */
public class FinestraConnessione extends JFrame implements ActionListener{
    private JButton connetti,avvia_server;
    private JCheckBox server_locale;
    private JLabel desc_nome,desc_server,desc_porta;
    private JTextField campo_nome,campo_testo_porta,campo_testo_server;
    private JPanel pannello_bottoni,pannello_testo_server,pannello_connessione;
    private Socket my_socket;
    private Server_test server;
    public FinestraConnessione(String title) throws HeadlessException {
        super(title);
        this.my_socket = null;
        this.server = null;
        this.connetti = new JButton("Connetti");
        this.avvia_server = new JButton("Avvia Server");
        this.server_locale = new JCheckBox("Server locale");
        this.desc_nome = new JLabel("Nickname");
        this.desc_porta = new JLabel("Porta");
        this.desc_server = new JLabel("Server");
        this.campo_nome = new JTextField();
        this.campo_testo_porta = new JTextField();
        campo_testo_porta.setText(new Integer(Server_test.DEFAULT_PORT).toString());
        this.campo_testo_server = new JTextField();
        campo_nome.setMinimumSize(new Dimension(50, 25));
        campo_nome.setMaximumSize(new Dimension(100, 25));
        desc_nome.setMinimumSize(campo_nome.getSize());
        desc_nome.setMaximumSize(campo_nome.getMaximumSize());
        campo_testo_server.setMinimumSize(new Dimension(120,25));
        campo_testo_server.setMaximumSize(new Dimension(1000, 25));
        desc_server.setMinimumSize(campo_testo_server.getSize());
        desc_server.setMaximumSize(campo_testo_server.getMaximumSize());
        campo_testo_porta.setMinimumSize(new Dimension(60, 25));
        campo_testo_porta.setMaximumSize(campo_testo_porta.getMinimumSize());
        desc_porta.setMaximumSize(campo_testo_porta.getMaximumSize());
        desc_porta.setMinimumSize(campo_testo_porta.getSize());
        
        this.pannello_bottoni = new JPanel();
        this.pannello_testo_server = new JPanel();
        this.imposta_finestra_connessione();
        
    }
    
    private void imposta_finestra_connessione()
    {
        server_locale.setSelected(false);
        avvia_server.setEnabled(false);
        server_locale.addActionListener(this);
        avvia_server.addActionListener(this);
        
        JPanel pan_labels = new JPanel();
        GroupLayout server_labels = new GroupLayout(pan_labels);
        server_labels.setAutoCreateContainerGaps(true);
        server_labels.setAutoCreateGaps(true);
        server_labels.setHorizontalGroup(server_labels.createSequentialGroup()
                .addComponent(desc_nome)
                .addComponent(desc_server)
                .addComponent(desc_porta));
        server_labels.setVerticalGroup(server_labels.createSequentialGroup()
                .addGroup(server_labels.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(desc_nome)
                .addComponent(desc_server)
                .addComponent(desc_porta)));
        pan_labels.setLayout(server_labels);
        
        GroupLayout server_testo = new GroupLayout(pannello_testo_server);
        server_testo.setAutoCreateContainerGaps(true);
        server_testo.setAutoCreateGaps(true);
        server_testo.setHorizontalGroup(server_testo.createSequentialGroup()
                .addComponent(campo_nome)
                .addComponent(campo_testo_server)
                .addComponent(campo_testo_porta));
        server_testo.setVerticalGroup(server_testo.createSequentialGroup()
                .addGroup(server_testo.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(campo_nome)
                .addComponent(campo_testo_server)
                .addComponent(campo_testo_porta)));
        this.pannello_testo_server.setLayout(server_testo);
        
        GroupLayout server_bottoni = new GroupLayout(pannello_bottoni);
        server_bottoni.setAutoCreateContainerGaps(true);
        server_bottoni.setAutoCreateGaps(true);
        server_bottoni.setHorizontalGroup(server_bottoni.createSequentialGroup()
                .addComponent(server_locale)
                .addComponent(avvia_server)
                .addComponent(connetti));
        server_bottoni.setVerticalGroup(server_bottoni.createSequentialGroup()
                .addGroup(server_bottoni.createParallelGroup()
                .addComponent(server_locale)
                .addComponent(avvia_server)
                .addComponent(connetti)));
        this.pannello_bottoni.setLayout(server_bottoni);
        JPanel tot = new JPanel();
        GroupLayout finestra = new GroupLayout(tot);
        finestra.setAutoCreateContainerGaps(false);
        finestra.setAutoCreateGaps(false);
        finestra.setHorizontalGroup(finestra.createSequentialGroup()
                .addGroup(finestra.createParallelGroup()
                .addComponent(pan_labels)
                .addComponent(pannello_testo_server)
                .addComponent(pannello_bottoni)));
        finestra.setVerticalGroup(finestra.createSequentialGroup()
                .addComponent(pan_labels)
                .addComponent(pannello_testo_server)
                .addComponent(pannello_bottoni));
        tot.setLayout(finestra);
        this.add(tot);
        connetti.addActionListener(this);
                //Finestra connessione
        this.setMinimumSize(new Dimension(380, 200));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //finestra_connessione.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource());
        if(e.getSource() == server_locale)
        {
            //server_locale.setSelected(server_locale.isSelected());
            avvia_server.setEnabled(server_locale.isSelected());
            return;
        }
        if(e.getSource() == avvia_server)
        {
            if(server == null)
            {
                //server.setStop(true);
                avvia_server.setText("Avvia server");
            }
            avvia_server.setText("Stop server");
            server = new Server_test(new Integer(campo_testo_porta.getText()).intValue());
            server.start();
            return;
        }
        if(e.getSource() == connetti)
        {
            this.setVisible(false);            
            
            try {
                InetAddress ip = InetAddress.getByName(campo_testo_server.getText());
                if(server_locale.isSelected())
                    ip = InetAddress.getLocalHost();
                my_socket = new Socket(ip,
                        new Integer(campo_testo_porta.getText()).intValue());
            } catch (UnknownHostException ex) {
                System.err.println("Host Sconosciuto!");
                return;
            } catch (IOException ex) {
                System.err.println("Errore IO server");
            }
            PreGioco lobbyd = new PreGioco("Sala D'attesa "+my_socket.getInetAddress().toString(),
                    my_socket,
                    campo_nome.getText());
            Thread th  = new Thread(lobbyd);
            th.setName("Sala attesa");
            th.start();
            this.dispose();
        }
    }
}
