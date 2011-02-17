package PacchettoGrafico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * Barra costituita dai necessari bottoni delle fasi
 * @see BottoneFase
 * @author matteo
 */
public class BarraFasi extends Elemento_2DGraphicsCliccable {
    private Dimension dimPannello;
    private int inizio;
    private int fine;

    private BottoneFase spostamento;
    private RisicammaraEventListener ascoltatoreSpostamento;

    private BottoneFase attacco;
    private RisicammaraEventListener ascoltatoreAttacco;

    private BottoneFase rinforzi;
    private RisicammaraEventListener ascoltatoreRinforzi;

    private RisicammaraEventListener ascoltatoreFineTurno;

    private ContatoreFasi contatoreFasi;

    public final static int LARGHEZZABORDO = 50;
    public static Color SFONDO = new Color(255, 192, 0);

    /**
     * Costruttore
     * @param dimPannello Dimensioni del pannello variabili al ridimensionamento.
     * @param ag AttivatoreGrafica che serve ai pulsanti per fare le animazioni.
     * @param inizio Distanza dal bordo sinistro.
     * @param fine Distanza dal bordo destro.
     * @param altezza Altezza della barra.
     * @param bordoSup Distanza da bordo superiore.
     */
    public BarraFasi(Dimension dimPannello, AttivatoreGrafica ag, int inizio, int fine, int altezza, int bordoSup) {
        this.dimPannello = dimPannello;
        this.inizio = inizio;
        this.fine = fine;
        this.contatoreFasi = new ContatoreFasi();
        Rectangle ret = this.ridimensiona(inizio, fine, altezza, bordoSup);

        //TODO posizionare per bene i bottone fasi(uno sopra all'altro, poi vengono rimpiccioliti
        int larghezzaB = LarghezzaBottoniMassima(ret.width);
        this.spostamento = new BottoneFase(dimPannello, ag, new Point(inizio,     bordoSup),larghezzaB, altezza);
        this.attacco     = new BottoneFase(dimPannello, ag, new Point(inizio+50,  bordoSup),larghezzaB, altezza);
        this.rinforzi    = new BottoneFase(dimPannello, ag, new Point(inizio+100, bordoSup),larghezzaB, altezza);

        this.spostamento.setActionListener(spostamento);
        this.attacco.setActionListener(attacco);
        this.rinforzi.setActionListener(rinforzi);
    }

    public void disegna(Graphics2D graphics2D, GraphicsAdvanced colori) {
        this.ridimensiona(); //questa riga rende dinamiche le dimensioni

        //disegna lo SFONDO magenta (e quindi il pulsante finale)
        graphics2D.setColor(SFONDO);
        graphics2D.fill(posizione);

        //disegna i tre bottonifase nella giusta sequenza
        this.rinforzi.disegna(graphics2D, colori);
        graphics2D.setColor(colori.getColoreGiocatore());
        graphics2D.fill(attacco.getMinBounds());

        this.attacco.disegna(graphics2D, colori);
        graphics2D.setColor(colori.getColoreGiocatore());
        graphics2D.fill(spostamento.getMinBounds());
        
        this.spostamento.disegna(graphics2D, colori);

        //disegna il contorno
        graphics2D.setColor(colori.getColoreScuro());
        graphics2D.draw(posizione);
    }

    @Override
    protected void actionPressed(MouseEvent e) {
        int who = whoIsPressed(e.getPoint());
        switch (who){
            default:
                System.err.println("Errore nel pulsante schiacciato\n"
                        + "di default viene premuto il pulsante FineTurno");
            case ContatoreFasi.ATTESA:
                azionaAscoltatore(ascoltatoreFineTurno, e);
                break;
            case ContatoreFasi.RINFORZO:
                azionaAscoltatore(ascoltatoreRinforzi, e);
                break;
            case ContatoreFasi.ATTACCO:
                azionaAscoltatore(ascoltatoreAttacco, e);
                break;
            case ContatoreFasi.SPOSTAMENTI:
                azionaAscoltatore(ascoltatoreSpostamento, e);
                break;
        }
    }

    private boolean azionaAscoltatore(RisicammaraEventListener eventListener, MouseEvent e){
        if (eventListener == null)
            return false;
        else {
            eventListener.actionPerformed(new EventoAzioneRisicammara(this, 0, "FaseButton", e.getPoint()));
            return true;
        }
    }

    private int whoIsPressed(Point p){
        if (spostamento.isClicked(p)){
            return ContatoreFasi.SPOSTAMENTI;
        }

        if (attacco.isClicked(p)){
            return ContatoreFasi.ATTACCO;
        }

        if (rinforzi.isClicked(p)){
            return ContatoreFasi.RINFORZO;
        }

        else {
            return ContatoreFasi.ATTESA;
        }
    }

    private void fineturno(){
        //TODO codice per fare "fine turno"
    }

    /**
     * Cambia la posizione del rettangolo e si occupa di cambiare la posizione
     * anche in riferimento al pannello principale che ascolta i MouseEvent
     * @param inizio distanza dal bordo sinistro
     * @param fine distanza dal bordo destro
     * @param altezza altezza della barra
     * @param bordoSup distanza da bordo superiore
     * @return ritorna il rettangolo che rappresenta la dimensione della barra
     */
    final public Rectangle ridimensiona(int inizio, int fine, int altezza, int bordoSup){
        int larghezza = larghezza(inizio, fine);
        this.inizio = inizio;
        this.fine = fine;

        this.posizione = new Rectangle(inizio, bordoSup, larghezza, altezza);
        return (Rectangle) this.posizione;
    }

    /**
     * Cambia la posizione del rettangolo e si occupa di cambiare la posizione
     * anche in riferimento al pannello principale che ascolta i MouseEvent
     * @param inizio distanza dal bordo sinistro
     * @param fine distanza dal bordo destro
     */
    final public void ridimensiona(int inizio, int fine){
        Rectangle ret = (Rectangle) this.posizione;
        this.ridimensiona(inizio, fine, ret.height, ret.y);
    }

    /**
     * Cambia la posizione del rettangolo e si occupa di cambiare la posizione
     * anche in riferimento al pannello principale che ascolta i MouseEvent
     */
    private void ridimensiona(){
        this.ridimensiona(inizio, fine);
        //int larghezzaB = LarghezzaBottoniMassima(larghezza(inizio, fine));
        //this.rinforzi.cambiaLarghezza(larghezzaB); //tolti perché gestiti internamente dalle frecce
        //this.attacco.cambiaLarghezza(larghezzaB);
        //this.spostamento.cambiaLarghezza(larghezzaB);
    }

    private int larghezza(int inizio, int fine){
        return dimPannello.width-(inizio+fine);
    }

    /**
     * Funzione che ritorna la dimensione la dimensione massima che possono
     * avere i bottoni
     * @param larghezza Larghezza della BarraFasi (spesso espresso in funzione
     * delle dimensioni del pannello)
     * @return la larghezza massima che può raggiungere il bottone in pixel
     */
    static int LarghezzaBottoniMassima(int larghezza){
        return larghezza-LARGHEZZABORDO*3;
    }

    /**
     * Passa alla fase successiva. Le fasi si ripetono ciclicamente.<p>
     * Le fasi si susseguono in:<br>
     * - ATTESA<br>
     * - RINFORZO<br>
     * - ATTACCO<br>
     * - SPOSTAMENTO
     */
    public void avanzaFase(){
        contatoreFasi.avanzaFase();
        switch(contatoreFasi.getFase()){

            case ContatoreFasi.RINFORZO: //Passo a RINFORZO
                rinforzi.setSmosciato(false);
                break;

            case ContatoreFasi.ATTACCO: //Passo ad ATTACCO
                attacco.setSmosciato(false);
                break;

            case ContatoreFasi.SPOSTAMENTI: //Passo a SPOSTAMENTO
                spostamento.setSmosciato(false);
                break;

            default: //ERRORE
                System.err.println("Errore fase. Impostata come fase d'attesa");
            case ContatoreFasi.ATTESA: //Passo ad ATTESA
                rinforzi.setSmosciato(true);
                attacco.setSmosciato(true);
                spostamento.setSmosciato(true);
                break;
        }
    }

    /**
     * Imposta alla fase d'attesa.
     */
    public void resetFase(){
        contatoreFasi.resetta();
        rinforzi.setSmosciato(true);
        attacco.setSmosciato(true);
        spostamento.setSmosciato(true);
    }

    /**
     * Serve per sapere la fase in cui è attualmente la barra.<p>
     * ContatoreFase.ATTESA      = 0<br>
     * ContatoreFase.RINFORZO    = 1<br>
     * ContatoreFase.ATTACCO     = 2<br>
     * ContatoreFase.SPOSTAMENTO = 3<br>
     * @return un intero rappresentante la fase in cui si è.
     */
    public int getFase(){
        return contatoreFasi.getFase();
    }

    public void setAscoltatoreAttacco(RisicammaraEventListener ascoltatoreAttacco) {
        this.ascoltatoreAttacco = ascoltatoreAttacco;
    }

    public void setAscoltatoreFineTurno(RisicammaraEventListener ascoltatoreFineTurno) {
        this.ascoltatoreFineTurno = ascoltatoreFineTurno;
    }

    public void setAscoltatoreRinforzi(RisicammaraEventListener ascoltatoreRinforzi) {
        this.ascoltatoreRinforzi = ascoltatoreRinforzi;
    }

    public void setAscoltatoreSpostamento(RisicammaraEventListener ascoltatoreSpostamento) {
        this.ascoltatoreSpostamento = ascoltatoreSpostamento;
    }
}
