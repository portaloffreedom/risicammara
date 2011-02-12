package risicammaraClient;

import risicammaraJava.deckManage.Carta;

/**
 * Enumerato che rappresenta tutti i territori, i relativi bonus e il continente
 * a cui appartengono.
 * @author stengun
 */
public enum territori_t implements Carta {
    //Nord America
    Alaska                      (Bonus_t.FANTE,  Continente_t.NORDAMERICA, 3),
    Territori_del_Nord_Ovest    (Bonus_t.CANNONE,Continente_t.NORDAMERICA, 4),
    Groenlandia                 (Bonus_t.CAVALLO,Continente_t.NORDAMERICA, 4),
    Alberta                     (Bonus_t.FANTE,  Continente_t.NORDAMERICA, 4),
    Ontario                     (Bonus_t.CAVALLO,Continente_t.NORDAMERICA, 6),
    Quebec                      (Bonus_t.CANNONE,Continente_t.NORDAMERICA, 3),
    Stati_Uniti_Occidentali     (Bonus_t.FANTE,  Continente_t.NORDAMERICA, 4),
    Stati_Uniti_Orientali       (Bonus_t.CANNONE,Continente_t.NORDAMERICA, 4),
    America_Centrale            (Bonus_t.CAVALLO,Continente_t.NORDAMERICA, 3),
    //America del sud
    Venezuela                   (Bonus_t.CANNONE, Continente_t.SUDAMERICA, 3),
    Peru                        (Bonus_t.CAVALLO, Continente_t.SUDAMERICA, 3),
    Brasile                     (Bonus_t.CANNONE, Continente_t.SUDAMERICA, 4),
    Argentina                   (Bonus_t.FANTE,   Continente_t.SUDAMERICA, 2),
    //Europa
    Islanda                     (Bonus_t.FANTE,       Continente_t.EUROPA, 3),
    Scandinavia                 (Bonus_t.CANNONE,     Continente_t.EUROPA, 4),
    Gran_Bretagna               (Bonus_t.CAVALLO,     Continente_t.EUROPA, 4),
    Europa_Settentrionale       (Bonus_t.CAVALLO,     Continente_t.EUROPA, 5),
    Europa_Occidentale          (Bonus_t.FANTE,       Continente_t.EUROPA, 4),
    Europa_Meridionale          (Bonus_t.CAVALLO,     Continente_t.EUROPA, 6),
    Ucraina                     (Bonus_t.CANNONE,     Continente_t.EUROPA, 6),
    //Africa
    Africa_del_Nord             (Bonus_t.FANTE,       Continente_t.AFRICA, 6),
    Egitto                      (Bonus_t.FANTE,       Continente_t.AFRICA, 4),
    Congo                       (Bonus_t.CAVALLO,     Continente_t.AFRICA, 3),
    Africa_Orientale            (Bonus_t.CANNONE,     Continente_t.AFRICA, 5),
    Africa_del_Sud              (Bonus_t.CANNONE,     Continente_t.AFRICA, 3),
    Madagascar                  (Bonus_t.FANTE,       Continente_t.AFRICA, 2),
    //Asia
    Urali                       (Bonus_t.CAVALLO,       Continente_t.ASIA, 4),
    Siberia                     (Bonus_t.CANNONE,       Continente_t.ASIA, 5),
    Jacuzia                     (Bonus_t.CAVALLO,       Continente_t.ASIA, 3),
    Cita                        (Bonus_t.FANTE,         Continente_t.ASIA, 4),
    Kamchatka                   (Bonus_t.CAVALLO,       Continente_t.ASIA, 5),
    Giappone                    (Bonus_t.FANTE,         Continente_t.ASIA, 2),
    Mongolia                    (Bonus_t.CANNONE,       Continente_t.ASIA, 5),
    Afghanistan                 (Bonus_t.FANTE,         Continente_t.ASIA, 4),
    Medio_Oriente               (Bonus_t.CANNONE,       Continente_t.ASIA, 6),
    India                       (Bonus_t.FANTE,         Continente_t.ASIA, 3),
    Cina                        (Bonus_t.CAVALLO,       Continente_t.ASIA, 7),
    Siam                        (Bonus_t.CANNONE,       Continente_t.ASIA, 3),
    //Oceania
    Indonesia                   (Bonus_t.CAVALLO,    Continente_t.OCEANIA, 3),
    Nuova_Guinea                (Bonus_t.CAVALLO,    Continente_t.OCEANIA, 3),
    Australia_Orientale         (Bonus_t.FANTE,      Continente_t.OCEANIA, 2),
    Australia_Occidentale       (Bonus_t.CANNONE,    Continente_t.OCEANIA, 3),
    //carte jolly
    Jolly1                      (Bonus_t.JOLLY,      Continente_t.NESSUNO, 0),
    Jolly2                      (Bonus_t.JOLLY,      Continente_t.NESSUNO, 0);

    private Bonus_t bonus;
    private Continente_t continente;
    private int territori_adiacenti;
/** Costruttore per i territori con la loro lista di adiacenza */
    territori_t(Bonus_t bonus,Continente_t continente,int territori_adiacenti){
        this.bonus = bonus;
        this.continente = continente;
        this.territori_adiacenti = territori_adiacenti;
    }
    /**
     * Interfaccia per leggere il bonus legato al territorio
     * @return Bonus relativo al territorio
     */
    public Bonus_t getBonus(){
        return this.bonus;
    }
    /**
     * Interfaccia per leggere il continente di cui il territorio
     * fa parte.
     * @return continente di cui fa parte il territorio.
     */
    public Continente_t getContinente(){
        return this.continente;
    }
    /**
     * Leggere quanti territori sono vicini a questo
     * @return Il numero dei territori adiacenti
     */
    public int getNumadiacenze(){
        return this.territori_adiacenti;
    }
/** Implementa il metodo per TipoCarta */
    public String tipoCarta() {
        return this.toString();
    }


};
