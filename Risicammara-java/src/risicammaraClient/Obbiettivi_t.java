package risicammaraClient;

import risicammaraJava.deckManage.Carta;

/**
 * Enumerato che contiene tutti i possibili obbiettivi.
 * @author stengun
 */
public enum Obbiettivi_t implements Carta {
    /** Conquista Norda america e Oceania. */
    NORDAMERICAOCEANIA      (
            "Conquistare la totalità del Nord America e Dell'Oceania",
            tipovittoria_t.CONTINENTALE),
    /** Conquista asia e africa */
    ASIAAFRICA              (
            "Conquistare la totalità dell'Asia e dell'Africa",
            tipovittoria_t.CONTINENTALE),
    /** Conquista asia e sud america */
    ASIASUDAMERICA          (
            "Conquistare la totalità dell'Asia e del Sud America",
            tipovittoria_t.CONTINENTALE),
    /** Conquista europa, sudamerica e un terzo continente a scelta */
    EUROPASUDAMERICATERZO   (
            "Conquista la totalità dell'Europa, Sud America più"+
            " un terzo continente a tua scelta",tipovittoria_t.CONTINENTALE),
    /** Conquista Europa, Oceania e un terzo continente a scelta */
    EUROPAOCEANIATERZO      (
            "Conquista la totalità dell'Europa, Oceania più un terzo "+
            "continente a tua scelta",tipovittoria_t.CONTINENTALE),
    /** Conquista Nord america e Africa */
    NORDAMERICAAFRICA       (
            "Conquista la totalità del Nord America e dell'Africa",
            tipovittoria_t.CONTINENTALE),
    /** Conquista 24 territori */
    VENTIQUATTRO            ("Conquista 24 territori",tipovittoria_t.TERRITORIALE),
    /** Conquista diciotto territori e presidia ciascuno di esso con 2 armate */
    DICIOTTODUE             (
            "Conquista 18 territori e presidia ciascuno di esso con 2 armate",
            tipovittoria_t.TERRITORIALE),
    /** Distrutggi le armate di colore BLU */
    BLU     ("Distruggi le armate Blu. Se tu stesso sei le armate blu o"+
            " se qualcun altro ha eliminato le armate dal gioco il tuo "+
            "obbiettivo diventa automaticamente \"Conquistare 24 territori\"",
            tipovittoria_t.DISTRUZIONE),
    /** Distruggi le armate di colore Rosso */
    ROSSO   ("Distruggi le armate Rosse. Se tu stesso sei le armate Rosse o"+
            " se qualcun altro ha eliminato le armate dal gioco il tuo "+
            "obbiettivo diventa automaticamente \"Conquistare 24 territori\"",
            tipovittoria_t.DISTRUZIONE),
    /** Distruggi le armate di colore Verde */
    VERDE   ("Distruggi le armate Verdi. Se tu stesso sei le armate Verdi o"+
            " se qualcun altro ha eliminato le armate dal gioco il tuo "+
            "obbiettivo diventa automaticamente \"Conquistare 24 territori\"",
            tipovittoria_t.DISTRUZIONE),
    /** Distruggi le armate di colore Giallo */
    GIALLO  ("Distruggi le armate Gialle. Se tu stesso sei le armate Gialle o "+
            "se qualcun altro ha eliminato le armate dal gioco il tuo "+
            "obbiettivo diventa automaticamente \"Conquistare 24 territori\"",
            tipovittoria_t.DISTRUZIONE),
    /** Distruggi le armate di colore Viola */
    VIOLA   ("Distruggi le armate Viola. Se tu stesso sei le armate Viola o "+
            "se qualcun altro ha eliminato le armate dal gioco il tuo "+
            "obbiettivo diventa automaticamente \"Conquistare 24 territori\"",
            tipovittoria_t.DISTRUZIONE),
    /* Distruggi le armate di colore Nero */
            /**
             *
             */
            NERO    ("Distruggi le armate Nere. Se tu stesso sei le armate Nere o "+
            "se qualcun altro ha eliminato le armate dal gioco il tuo "+
            "obbiettivo diventa automaticamente \"Conquistare 24 territori\"",
            tipovittoria_t.DISTRUZIONE);
/** Contenitore per il Testo dell'obbiettivo */
    private String testo_obbiettivo;
/** Contenitore per il tipo di vittoria dell'obbiettivo */
    private tipovittoria_t tipovittoria;
/** Costruisce l'oggetto in baste al tipo di valore dell'enumerato */
    Obbiettivi_t(String testo,tipovittoria_t tipovict){
        this.testo_obbiettivo = testo;
        this.tipovittoria = tipovict;
    }
/** Sovrascrive il metodo toString per restituire il testo dell'obbiettivo */
    @Override
    public String toString(){
        return testo_obbiettivo;
    }
/** Restituisce il tipo di carta */
    @Override
    public String tipoCarta() {
        return this.toString();
    }
/** Restituisce il tipo di vittoria di questo obbiettivo.
 *  @return il tipo di vittoria dell'obbiettivo
 * @see risicammaraClient.tipovittoria_t
 */
    public tipovittoria_t VictoryType(){
        return tipovittoria;
    }
}
