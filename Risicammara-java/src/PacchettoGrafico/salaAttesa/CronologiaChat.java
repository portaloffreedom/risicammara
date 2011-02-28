
package PacchettoGrafico.salaAttesa;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Classe che implementa i metodi per creare un pannello di cronologia dei messaggi di chat.
 * @author matteo
 */
public class CronologiaChat extends JLabel {

    private int maxRighe;
    private List<String> testoInRighe;
    private int contatore;

    /**
     * Lo scrollpane per la cronologia dei messaggi.
     * @param dimensioniScrollPane Dimensioni dello scrollpane
     * @return L'oggetto della cronologia chat inscatolato dentro lo scorrimento.
     */
    public JScrollPane inscatolaInScrollPane(Rectangle dimensioniScrollPane){
        JScrollPane konsoleScorrimento = new JScrollPane(this);
        konsoleScorrimento.setPreferredSize(new Dimension(dimensioniScrollPane.width, dimensioniScrollPane.height));
        konsoleScorrimento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return konsoleScorrimento;
    }

    /**
     * Il riquadro dove vengono stampati i messaggi passati. Costruttore.
     * @param maxRighe le righe di cronologia.
     */
    public CronologiaChat(int maxRighe){
        this.contatore = 0;
        this.maxRighe = maxRighe;
        testoInRighe = new ArrayList<String>(maxRighe+1);
        setVerticalAlignment(TOP);
        
        //this.setFont(Font.decode("Droid Serif"));

    }


    /**
     * Stampa un messaggio nella chat.Usa tag html
     * @param messaggio il messaggio da stampare
     */
    public synchronized void stampaMessaggio(String messaggio) {

        testoInRighe.add(messaggio);

        String testoCompleto = "<html><body>";
        int to  = testoInRighe.size();
        int from=to-maxRighe;
        if (from<0) from = 0;

        testoInRighe = testoInRighe.subList(from, to);

        for (String riga : testoInRighe) {
            testoCompleto = testoCompleto+riga+"<br>";
        }
        testoCompleto = testoCompleto.substring(0, testoCompleto.length()-4);

        testoCompleto = testoCompleto+"</body></html>";

        this.setText(testoCompleto);
        this.contatore = 0;
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        Rectangle bordi = this.getBounds();
        contatore++;
        
        if (contatore <= 3){
            //System.out.println("y:"+bordi.y+"\theight:"+bordi.height);
            Rectangle bordiNuovi = new Rectangle(0, bordi.height,0,0);
            this.scrollRectToVisible(bordiNuovi);
        }


        //konsoleScorrimento.getViewport().toViewCoordinates(new Point(0, bordi.height));
        //konsoleScorrimento.getViewport().setViewPosition(new Point(0, -bordi.y));
        //this.setBounds(0, -9999999, bordi.height, bordi.height);
    }



    /**
     * Stampa l'errore sulla cronologia di char e su stdERR. Nella chat imposta
     * il corsivo prima di stampare.
     * <p>
     * Stampa l'eccezzione solo su stdERR.
     * @param messaggio Stringa del messaggio di errore da stampare
     * @param ex Eccezzione da stampare
     */
    public void stampaMessaggioErrore(String messaggio, Exception ex){

        stampaMessaggioImportante(messaggio);

        if (ex != null) messaggio = messaggio+"|"+ex.getLocalizedMessage();
        System.err.println(messaggio);
    }

    /**
     * Gestisce in automatico anche la stampa su stdERR
     * @param messaggio Messaggio di errore da stampare
     */
    public void stampaMessaggioComando(String messaggio){
        stampaMessaggioImportante(messaggio);
        System.out.println("Comando|"+messaggio);
    }

    private void stampaMessaggioImportante(String messaggio) {
        //imposta italico, colore rosso e stampa messaggio su CronologiaChat
        stampaMessaggio("<font style=\"font-style:italic;color:red\">"+messaggio+"</font>");

    }

}
