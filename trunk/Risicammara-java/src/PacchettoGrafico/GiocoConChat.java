package PacchettoGrafico;

import java.awt.Dimension;
import javax.swing.*;
import risicammaraClient.Connessione;
import risicammaraJava.turnManage.PartitaClient;

/**
 *
 * @author stengun
 */
public class GiocoConChat extends JFrame implements Runnable{

    private JInternalFrame finestragioco;
    private JInternalFrame finestrachat;
    private JDesktopPane desktopvirtuale;
    private JFrame  finestraprincipale;
    private JPanel  container;
    private FinestraGioco partita;
    
    public GiocoConChat(Connessione conn,PartitaClient cli) {
        impostaComponenti(conn,cli);
    }
    
    private void impostaComponenti(Connessione conn,PartitaClient cli){
        finestraprincipale = new JFrame("Risicammara - Partita");
        finestragioco = new JInternalFrame("Partita", true, false, false, true);
        partita = new FinestraGioco(conn, cli);
        finestrachat = new JInternalFrame("Chat", true, false, false, true);
        desktopvirtuale = new JDesktopPane();
        container = new JPanel();
        finestrachat.setVisible(true);
        finestragioco.setVisible(true);
        desktopvirtuale.setVisible(true);
        container.setVisible(true);
        
        finestragioco.add(partita);
        desktopvirtuale.add(finestragioco);
        desktopvirtuale.add(finestrachat);
        container.add(desktopvirtuale);
        finestraprincipale.add(container);
        
        finestraprincipale.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        finestraprincipale.setMinimumSize(new Dimension(500, 500));
        
        
    }


    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
