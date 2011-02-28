package PacchettoGrafico.salaAttesa;

import javax.swing.JButton;
import risicammaraClient.Colore_t;

/**
 * Classe che implementa i metodi per i bottoni cliccabili dei giocatori.
 * @author matteo
 */
public class BottoneGiocatori extends JButton implements QuadratoGiocatori {
    int index;

    /**
     * Costruisce un bottone cliccabile senza testo.
     * @param index L'indice dove inserire il bottone.
     */
    public BottoneGiocatori(int index) {
        this.index = index;
    }

    /**
     * Costruisce un Bottone cliccabile con un testo.
     * @param text il testo del bottone
     */
    public BottoneGiocatori(String text) {
        super(text);
    }

    @Override
    public void setNome(String testo) {
        this.setText(testo);
    }

    @Override
    public void setColore(Colore_t colore) {
        this.setForeground(colore.getColor());
    }

    /**
     * Restituisce l'indice del giocatore associato al bottone.
     * @return L'indice del giocatore associato al bottone
     */
    public int getIndex() {
        return index;
    }
}
