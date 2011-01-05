/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammarajava;

import risicammarajava.deckManage.Carta;

/**
 *
 * @author stengun
 */
public enum Obbiettivi_t implements Carta {
    NORDAMERICAOCEANIA      ("Conquistare la totalità del Nord America e Dell'Oceania",tipovittoria_t.CONTINENTALE),
    ASIAAFRICA              ("Conquistare la totalità dell'Asia e dell'Africa",tipovittoria_t.CONTINENTALE),
    ASIASUDAMERICA          ("Conquistare la totalità dell'Asia e del Sud America",tipovittoria_t.CONTINENTALE),
    EUROPASUDAMERICATERZO   ("Conquista la totalità dell'Europa, Sud America più un terzo continente a tua scelta",tipovittoria_t.CONTINENTALE),
    EUROPAOCEANIATERZO      ("Conquista la totalità dell'Europa, Oceania più un terzo continente a tua scelta",tipovittoria_t.CONTINENTALE),
    NORDAMERICAAFRICA       ("Conquista la totalità del Nord America e dell'Africa",tipovittoria_t.CONTINENTALE),
    VENTIQUATTRO            ("Conquista 24 territori",tipovittoria_t.TERRITORIALE),
    DICIOTTODUE             ("Conquista 18 territori e presidia ciascuno di esso con 2 armate",tipovittoria_t.TERRITORIALE),
    BLU     ("Distruggi le armate Blu. Se tu stesso sei le armate blu o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"",tipovittoria_t.DISTRUZIONE),
    ROSSO   ("Distruggi le armate Rosse. Se tu stesso sei le armate Rosse o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"",tipovittoria_t.DISTRUZIONE),
    VERDE   ("Distruggi le armate Verdi. Se tu stesso sei le armate Verdi o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"",tipovittoria_t.DISTRUZIONE),
    GIALLO  ("Distruggi le armate Gialle. Se tu stesso sei le armate Gialle o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"",tipovittoria_t.DISTRUZIONE),
    VIOLA   ("Distruggi le armate Viola. Se tu stesso sei le armate Viola o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"",tipovittoria_t.DISTRUZIONE),
    NERO    ("Distruggi le armate Nere. Se tu stesso sei le armate Nere o se qualcun altro ha eliminato le armate dal gioco"
            + " il tuo obbiettivo diventa automaticamente \"Conquistare 24 territori\"",tipovittoria_t.DISTRUZIONE);

    private String testo_obbiettivo;
    private tipovittoria_t tipovittoria;

    Obbiettivi_t(String testo,tipovittoria_t tipovict){
        this.testo_obbiettivo = testo;
        this.tipovittoria = tipovict;
    }

    @Override
    public String toString(){
        return testo_obbiettivo;
    }

    public String tipoCarta() {
        return this.testo_obbiettivo;
    }

    public tipovittoria_t VictoryType(){
        return tipovittoria;
    }
}
