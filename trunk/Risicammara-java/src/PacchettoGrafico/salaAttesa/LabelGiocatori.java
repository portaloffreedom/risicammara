package PacchettoGrafico.salaAttesa;

import javax.swing.JLabel;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
import risicammaraClient.Colore_t;

/**
 * L'etichetta che rappresenta il nome del giocatore nelle interfacce non di Lobby Master
 * @author matteo
 */
public class LabelGiocatori extends JLabel implements QuadratoGiocatori {

    /**
     * Costruttore del label giocatori senza un testo
     */
    public LabelGiocatori() {
        this.setHorizontalAlignment(CENTER);
        this.setBorder(new TextFieldBorder());
    }

    /**
     * Costruttore dell'etichetta Giocatori
     * @param text Il testo dell'etichetta
     */
    public LabelGiocatori(String text) {
        super(text);
        this.setHorizontalAlignment(CENTER);
    }

    @Override
    public void setNome(String testo) {
        this.setText(testo);
    }

    @Override
    public void setColore(Colore_t colore) {
        this.setForeground(colore.getColor());
    }
}
