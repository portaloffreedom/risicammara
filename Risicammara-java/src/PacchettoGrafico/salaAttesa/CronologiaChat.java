/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico.salaAttesa;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author matteo
 */
public class CronologiaChat extends JTextArea {

    private int maxRighe;
    private List<String> testoInRighe;

    public CronologiaChat(Rectangle cronologiaR) {
        this.setBounds(cronologiaR);
        this.setEditable(false);


        //Calcola il numero massimo di righe dato dallo spazio vertivale diviso
        ///l'altezza di ogni riga
        maxRighe = ( cronologiaR.height ) / ( getFontMetrics(this.getFont()).getHeight() );
        testoInRighe = new ArrayList<String>(maxRighe+1);
    }

    public CronologiaChat(int maxRighe){
        this.setEditable(false);
        this.maxRighe = maxRighe;
        testoInRighe = new ArrayList<String>(maxRighe+1);
        
        //this.setFont(Font.decode("Droid Serif"));

    }


    public void stampaMessaggio(String messaggio) {

        testoInRighe.add(messaggio);

        String testoCompleto = "";
        int to  = testoInRighe.size();
        int from=to-maxRighe;
        if (from<0) from = 0;

        testoInRighe = testoInRighe.subList(from, to);

        for (String riga : testoInRighe) {
            testoCompleto = testoCompleto+riga+'\n';
        }
        testoCompleto = testoCompleto.substring(0, testoCompleto.length()-1);

        this.setText(testoCompleto);
        Rectangle bordi = this.getBounds();
        System.out.println(bordi);
        bordi.y = bordi.height;
        bordi.height = 0;
        this.scrollRectToVisible(bordi);

        //append(messaggio);
        //append("\n");
        //repaint();
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
        //Backup Font
        Font tmp = this.getFont();

        //imposta italico e stampa messaggio su CronologiaChat
        this.setFont(new Font(tmp.getName(), Font.ITALIC, tmp.getSize()));
        //this.setFont(Font.decode("Droid Serif Bold Italic"));
        stampaMessaggio(messaggio);

        //ripristino Font
        this.setFont(tmp);
    }

}
