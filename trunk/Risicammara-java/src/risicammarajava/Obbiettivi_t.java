/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

/**
 *
 * @author stengun
 */
public enum Obbiettivi_t {
    NORDAMERICAOCEANIA      ("Conquistare la totalità del Nord America e Dell'Oceania"),
    ASIAAFRICA              ("Conquistare la totalità dell'Asia e dell'Africa"),
    ASIASUDAMERICA          ("Conquistare la totalità dell'Asia e del Sud America"),
    EUROPASUDAMERICATERZO   ("Conquista la totalità dell'Europa, Sud America più un terzo continente a tua scelta"),
    EUROPAOCEANIATERZO      ("Conquista la totalità dell'Europa, Oceania più un terzo continente a tua scelta"),
    NORDAMERICAAFRICA       ("Conquista la totalità del Nord America e dell'Africa"),
    VENTIQUATTRO            ("Conquista 24 territori"),
    DICIOTTODUE             ("Conquista 18 territori e presidia ciascuno di esso con 2 armate"),
    BLU     ("Distruggi le armate Blu. Se tu stesso sei le armate blu o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\""),
    ROSSO   ("Distruggi le armate Rosse. Se tu stesso sei le armate Rosse o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\""),
    VERDE   ("Distruggi le armate Verdi. Se tu stesso sei le armate Verdi o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\""),
    GIALLO  ("Distruggi le armate Gialle. Se tu stesso sei le armate Gialle o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\""),
    VIOLA   ("Distruggi le armate Viola. Se tu stesso sei le armate Viola o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\""),
    NERO    ("Distruggi le armate Nere. Se tu stesso sei le armate Nere o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"");

    private String testo_obbiettivo;
    Obbiettivi_t(String testo){
        this.testo_obbiettivo = testo;
    }
    public String getTesto(){
        return testo_obbiettivo;
    }
}
