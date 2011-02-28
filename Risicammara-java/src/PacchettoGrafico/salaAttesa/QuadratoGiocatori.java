package PacchettoGrafico.salaAttesa;

import risicammaraClient.Colore_t;

/**
 * Interfaccia per le azioni sui pulsanti dei giocatori.
 * @author matteo
 */
public interface QuadratoGiocatori {

    /**
     * Imposta il nome del pulsante
     * @param testo Il nome del pulsante
     */
    public void setNome(String testo);

        /**
         * Imposta il colore del testo
         * @param colore il colore del testo
         */
        public void setColore(Colore_t colore);

        /**
         * Imposta lo stato di visibilità
         * @param visible true se visibile, false altrimenti
         */
        public void setVisible(boolean visible);

        /**
         * Imposta le dimensioni del rettangolo
         * @param x spostamento x
         * @param y spostamento y
         * @param width larghezza
         * @param height altezza
         */
        public void setBounds(int x, int y, int width, int height);
}
