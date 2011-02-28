package PacchettoGrafico.PannelloGiocoPackage;

import PacchettoGrafico.EventoAzioneRisicammara;

/**
 * Interfaccia che accoumuna tutti gli ascoltatori degli eventi in risicammara.
 * @author matteo
 */
public interface RisicammaraEventListener {

    /**
     * Implementare questa funzione per stabilire cosa succede in reazione
     * all'evento.
     * @param e
     */
    public void actionPerformed(EventoAzioneRisicammara e);
    
}
