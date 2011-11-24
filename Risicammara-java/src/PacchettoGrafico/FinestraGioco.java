package PacchettoGrafico;

import PacchettoGrafico.PannelloGiocoPackage.RichiestaNumeroArmate;
import PacchettoGrafico.PannelloGiocoPackage.AscoltatorePlanciaAttacco;
import PacchettoGrafico.PannelloGiocoPackage.GestoreFasi;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import risicammaraJava.playerManage.ListaGiocatoriClient;
import PacchettoGrafico.PannelloGiocoPackage.PannelloGioco;
import PacchettoGrafico.PannelloGiocoPackage.PlanciaImmagine;
import PacchettoGrafico.salaAttesa.CronologiaChat;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import risicammaraClient.Client;
import risicammaraClient.Connessione;
import risicammaraClient.territori_t;
import risicammaraJava.boardManage.TerritorioPlanciaClient;
import risicammaraJava.deckManage.Carta;
import risicammaraJava.turnManage.Fasi_t;
import risicammaraJava.turnManage.PartitaClient;
import risicammaraServer.messaggiManage.*;

/**
 * Finestra della interfaccia di gioco.
 * @author matteo
 */
public class FinestraGioco extends JFrame implements Runnable, ActionListener {
    private Connessione server;
    private ListaGiocatoriClient listaGiocatori;
    private PartitaClient partita;
    private GestoreFasi gestoreFasi;
    private PlanciaImmagine plancia;
    private RichiestaNumeroArmate richiestaNumeroArmateAttacco;

    private PannelloGioco pannello;
    private JTextField jTextField1;
    private JButton bottoneinsulta;
    private JPanel pannello_chat;
    private CronologiaChat chat;
    /**
     * Costruisce la finestra di gioco di Risicammara.
     * @param server la Connessione al server
     * @param partita La partita lato Client.
     */
    public FinestraGioco(Connessione server, PartitaClient partita) {
        super("Risicammara - "+partita.getMeStesso().getNome());
        this.server = server;
        this.listaGiocatori = partita.getListaGiocatori();
        this.partita = partita;

        ImageIcon icona = new ImageIcon(this.getClass().getResource(Client.RISICAMLOGO));
        this.setIconImage(icona.getImage());

        Container contestoFinestra = this.getContentPane();
        
        
        this.pannello = new PannelloGioco(240,partita);
        contestoFinestra.add(pannello);
        Dimension dimensioniMinime = new Dimension(950, 600);
        super.setLocationByPlatform(true);
        this.setMinimumSize(dimensioniMinime);
        
        this.plancia = pannello.getPlanciaImmagine();
        this.gestoreFasi = new GestoreFasi(partita, plancia, pannello);
        this.richiestaNumeroArmateAttacco = new RichiestaNumeroArmate(pannello.getBottoneFaseAttacco(), server, partita);
        this.richiestaNumeroArmateAttacco.setOkActionListener(richiestaNumeroArmateAttacco);

        this.addWindowListener(new WindowListenerImpl(server));
        initlayout();
        this.setVisible(true);
    }

    @Override
    public void run() {
        /* APPUNTI
         * A tutti i giocatori un MessaggioFase che dice la fase in cui siamo, a
         * tutti i giocatori tranne quello ceh gioca arriva un messaggio
         * TurnOfPlayer(comandO) che dice chi sta giocando, e al giocatore che
         * deve giocare arriva un StartYourTurn (comando)
         */
        try {
            LinkedList<Integer> sequenzaGioco = partita.getSequenzaGioco();
            String seq = "";
            
//            for(Integer i : sequenzaGioco){
//                seq = seq + listaGiocatori.get(i.intValue()).getArmyColour().toString() + " ";
//            }
            chat.stampaMessaggioComando("La sequenza è: "+seq);
            
            MessaggioFase msgFase = (MessaggioFase) server.ricevi();
            if (msgFase.getFase() != Fasi_t.PREPARTITA) {
                riavviaPartitaErrore("Errore, server non sincronizzato");
                return;
            }

            this.cicloPreFase();

            this.cicloPartita();


        } catch (IOException ex) {
            riavviaPartitaErrore("Connessione al server persa o corrotta.");
            return;
        } catch (ClassNotFoundException ex) {
            riavviaPartitaErrore("Pacchetto inviato al server irriconoscibile. Disconnessione...");
            return;
        }
    }

    private void cicloPreFase() throws ClassNotFoundException, IOException {
        Messaggio msg = null;
        //gestoreFasi.faseToAttesa();
        while (true) {
            msg = server.ricevi();
            if (Client.DEBUG)
                System.out.println("Arrivato messaggio: "+msg);
            switch (msg.getType()) {
                case FASE:
                    gestoreFasi.finePreFase();
                    if (Client.DEBUG)
                        System.out.println("Finita la prefase");
                    return;  //è finita la prefase
                case COMMAND:
                    MessaggioComandi msgComandi = (MessaggioComandi) msg;
                    if (Client.DEBUG && (msgComandi.getComando() != comandi_t.TURNOFPLAYER)){
                        riavviaPartitaErrore("Errore di sincronizzazione col server");
                    }
                    if(msgComandi.getComando() == comandi_t.DISCONNECT){
                        chat.stampaMessaggioComando(listaGiocatori.getNomeByIndex((int)msgComandi.getSender())+" è uscito");
                        break;
                    }
                    if (msgComandi.getSender() == listaGiocatori.meStessoIndex()){
                        gestoreFasi.avanzaFase();
                    }
                    else
                        gestoreFasi.faseToAttesa();
                    break;
                case CAMBIAARMATETERRITORIO:
                    MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio) msg;
                    plancia.aggiornaArmateTerritorio(msgArmate.getArmate(), msgArmate.getTerritorio());
                    if (gestoreFasi.getFaseCorrente() == Fasi_t.RINFORZO)
                        gestoreFasi.diminuisciArmateRinforzoDisponibili();
                    break;

                case ARMATEDISPONIBILI:
                    MessaggioArmateDisponibili msgMad = (MessaggioArmateDisponibili) msg;
                    gestoreFasi.setArmateRinforzoDisponibili(msgMad.getNumarm());
                    break;
                case CHAT:
                    MessaggioChat msgch = (MessaggioChat)msg;
                    String nome = listaGiocatori.getNomeByIndex((int)msgch.getSender());
                    chat.stampaMessaggio(nome+": "+msgch.getMessaggio());
                    chat.repaint();
                    break;
            }
        }
    }

    private void cicloPartita() throws IOException, ClassNotFoundException {
        Messaggio msg = null;
        while (true) {
            msg = server.ricevi();
            if (Client.DEBUG)
                System.out.println("Arrivato messaggio: "+msg);
            switch (msg.getType()) {
                case COMMAND: {
                    MessaggioComandi msgComandi = (MessaggioComandi) msg;
                    switch (msgComandi.getComando()) {
                        case TURNOFPLAYER: {
                            int giocatoreDiTurno = (int)msg.getSender();
                            partita.avanzaGiocatoreDiTurno(giocatoreDiTurno);
                            
                            if (giocatoreDiTurno == listaGiocatori.meStessoIndex()) {
                                //TODO rimpicciolisci tutte le frecce degli altri
                                System.out.println("tocca a te");
                            }
                            else {
                                //TODO tocca al giocatore avanza le frecce
                                System.out.println("tocca al giocatore "+giocatoreDiTurno);
                            }
                            break;
                        }

                        case ATTACCOTERMINATO: {
                            if (partita.eMioTurno())
                                gestoreFasi.terminaAttaccoInCorso();
                            else {
                                territori_t attaccante = partita.getTerritorioAttaccante();
                                territori_t difensore  = partita.getTerritorioAttaccato();
                                plancia.ripristinaTerritorio(attaccante);
                                plancia.ripristinaTerritorio(difensore);
                            }
                            break;
                        }

                        case VINCITORE: {
                            if (msgComandi.getSender() == partita.getMeStessoIndex())
                                riavviaPartitaErrore("HAI VINTO!");
                            else
                                riavviaPartitaErrore("HA VINTO "+listaGiocatori.getNomeByIndex((int)msg.getSender()));
                            return;
                        }
                        
                        case DISCONNECT:{
                            chat.stampaMessaggioComando(listaGiocatori.getNomeByIndex((int)msgComandi.getSender())+" è uscito");
                            break;
                        }
                        default:
                            System.err.println("MESSAGGIO COMANDO NON RICONOSCIUTO! "+msg);
                            break;
                    }
                    break;
                }
                
                case ARMATEDISPONIBILI: {
                    MessaggioArmateDisponibili msgMad = (MessaggioArmateDisponibili) msg;
                    gestoreFasi.setArmateRinforzoDisponibili(msgMad.getNumarm());
                    break;
                }
                
                case CAMBIAARMATETERRITORIO: {
                    MessaggioCambiaArmateTerritorio msgArmate = (MessaggioCambiaArmateTerritorio) msg;
                    plancia.aggiornaArmateTerritorio(msgArmate.getArmate(), msgArmate.getTerritorio());
                    if (gestoreFasi.getFaseCorrente() == Fasi_t.RINFORZO)
                        gestoreFasi.diminuisciArmateRinforzoDisponibili();
                    break;
                }
                case SPOSTAARMATE: {
                    MessaggioSpostaArmate msgSpostaArmate = (MessaggioSpostaArmate) msg;
                    int armateSpostate = msgSpostaArmate.getNumarmate();
                    plancia.aggiornaArmateTerritorio(-armateSpostate, msgSpostaArmate.getSorgente());
                    plancia.aggiornaArmateTerritorio(armateSpostate, msgSpostaArmate.getArrivo());
                    break;
                }

                case FASE: {
                    if (!partita.eMioTurno())
                        break;
                    MessaggioFase msgFase = (MessaggioFase) msg;
                    gestoreFasi.setFase(msgFase.getFase());
                    if (Client.DEBUG)
                        System.out.println("Fase impostata a:"+gestoreFasi.getFaseCorrente()+" = "+msgFase.getFase());
                    break;
                }

                case DICHIARAATTACCO: {
                    MessaggioDichiaraAttacco msgAttacco = (MessaggioDichiaraAttacco) msg;
                    if (msgAttacco.getSender() == listaGiocatori.meStessoIndex())
                        break;
                    territori_t attaccante = msgAttacco.getTerritorio_attaccante();
                    territori_t difensore  = msgAttacco.getTerritorio_difensore();
                    partita.setTerritorioAttaccante(attaccante);
                    partita.setTerritorioAttaccato(difensore);
                    plancia.coloraSfumato(attaccante, AscoltatorePlanciaAttacco.Attacco, AscoltatorePlanciaAttacco.pesantezzaSfumatura);
                    plancia.coloraSfumato(difensore, AscoltatorePlanciaAttacco.Difesa, AscoltatorePlanciaAttacco.pesantezzaSfumatura);
                    break;
                }
                    
                case ATTACCOVINTO: {
                    MessaggioAttaccoVinto msgVincita = (MessaggioAttaccoVinto) msg;
                    int armateSpostate = msgVincita.getArmSpost();
                    TerritorioPlanciaClient attaccante = plancia.getTerritorio(partita.getTerritorioAttaccante());
                    TerritorioPlanciaClient difensore = plancia.getTerritorio(partita.getTerritorioAttaccato());
                    
                    difensore.setProprietario(attaccante.getProprietario());
                    plancia.aggiornaArmateTerritorio(-armateSpostate, attaccante);
                    plancia.setArmateTerritorio(armateSpostate, difensore);
                    
                    if (attaccante.getProprietario() == listaGiocatori.meStessoIndex()){
                        //chiedere se vuoi spostare altre armate
                        int armateDaSpostare = attaccante.getArmate()-1;
                        this.richiestaNumeroArmateAttacco.imposta(attaccante.getTerritorio(), difensore.getTerritorio(),armateDaSpostare);
                        if (armateDaSpostare <= 0){
                            richiestaNumeroArmateAttacco.spostaArmate(0);
                            break;
                        }
                        richiestaNumeroArmateAttacco.attiva();
                    }
                    break;
                }
                case RISULTATOLANCI: {
                    MessaggioRisultatoLanci msgDado = (MessaggioRisultatoLanci) msg;
                    int valoriAttacco[] = msgDado.getValoriAttacco();
                    int valoriDifesa[]  = msgDado.getValoriDifesa();

                    pannello.setRisultatoDadi(valoriAttacco, valoriDifesa);
                    
                    break;
                }
                case CARTA: {
                    MessaggioCarta msgCarta = (MessaggioCarta) msg;
                    Carta carta = msgCarta.getCarta();
                    if (carta == null) {
                        //TODO l'altro giocatore ha pescato una carta
                    }
                    else {
                        partita.aggiungiCartaMeStesso(carta);
                    }
                    break;
                }

                case CHAT:
                    MessaggioChat msgch = (MessaggioChat)msg;
                    String nome = listaGiocatori.getNomeByIndex((int)msgch.getSender());
                    chat.stampaMessaggio(nome+": "+msgch.getMessaggio());
                    chat.repaint();
                    break;
                default:
                    System.err.println("MESSAGGIO NON RICONOSCIUTO! "+msg);
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MessaggioChat msg = null;
        Insulti insulto;
        try {
            insulto = new Insulti(this.getClass().getResourceAsStream(Client.INSULTI));
        } catch (IOException ex) {
            System.err.println("Errore IO su chiusura stream insulto");
            insulto = null;
        }
        
        if(e.getSource() == bottoneinsulta && insulto != null)
        {
            msg = new MessaggioChat(listaGiocatori.meStessoIndex(), insulto.getinsulto());
            try {
                server.spedisci(msg);
            } catch (IOException ex) {
                System.err.print("Errore nell'invio del messaggio di chat");
            }
        }
        else if(e.getSource() == jTextField1)
        {
            msg = new MessaggioChat(listaGiocatori.meStessoIndex(), jTextField1.getText());
            try {
                server.spedisci(msg);
            } catch (IOException ex) {
                System.err.print("Errore nell'invio del messaggio di chat");
            }
            jTextField1.setText("");
            jTextField1.requestFocus();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="ascoltatore della finestra">
    private static class WindowListenerImpl implements WindowListener {

        Connessione server;

        public WindowListenerImpl(Connessione server) {
            this.server = server;
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            //TODO casella di conferma uscita
            try {
                server.chiudiConnessione();
            } catch (IOException ex) {
                Client.RiavviaClientErrore("Connessione al server non chiusa nella maniera corretta");
            } finally {
                e.getWindow().setVisible(false);
                e.getWindow().dispose();
            }
            Client.RiavviaClient();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }// </editor-fold>

    

    /**
     * Riavvia la partita in caso di errore.
     * @param messaggio Il messaggio di errore
     */
    public void riavviaPartitaErrore(String messaggio){
        try { server.chiudiConnessione(); } catch (IOException ex) { }
        this.setVisible(false);
        this.dispose();
        Client.RiavviaClientErrore(messaggio);
    }

    
    private void initlayout(){
        jTextField1 = new javax.swing.JTextField();
        this.pannello_chat = new JPanel();
        this.chat = new CronologiaChat(20);
        this.bottoneinsulta = new JButton("Insulta");
        
        this.bottoneinsulta.addActionListener(this);
        jTextField1.addActionListener(this);
        jTextField1.setMaximumSize(new Dimension(pannello.getMaximumSize().width, 15));
        JScrollPane scrollchat = chat.inscatolaInScrollPane(
                new Rectangle(pannello.getDimensioniMinime().width, 60));
        scrollchat.setMaximumSize(new Dimension(pannello.getMaximumSize().width, 60));
        //Layout per il pannello di chat
        GroupLayout vert = new GroupLayout(pannello_chat);

        pannello_chat.setLayout(vert);
        pannello_chat.setMinimumSize(pannello.getDimensioniMinime());
        
        JPanel pinsult = new JPanel();
        GroupLayout hor = new GroupLayout(pinsult);
        pinsult.setLayout(hor);
        hor.setAutoCreateContainerGaps(true);
        hor.setAutoCreateGaps(true);
        hor.setHorizontalGroup(hor.createSequentialGroup()
                .addComponent(jTextField1)
                .addComponent(bottoneinsulta));
        hor.setVerticalGroup(hor.createSequentialGroup()
                .addGroup(hor.createParallelGroup()
                .addComponent(jTextField1)
                .addComponent(bottoneinsulta)));
        
        vert.setAutoCreateContainerGaps(true);
        vert.setAutoCreateGaps(true);
        vert.setHorizontalGroup(
            vert.createSequentialGroup()
            .addGroup(vert.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(pannello)
            .addComponent(scrollchat)
            .addComponent(pinsult)));
        
        vert.setVerticalGroup(
            vert.createSequentialGroup()
            .addComponent(pannello)
            .addComponent(scrollchat)
            .addComponent(pinsult));

        getContentPane().add(pannello_chat);
    }
    
}