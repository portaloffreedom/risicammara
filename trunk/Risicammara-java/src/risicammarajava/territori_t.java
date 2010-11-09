package risicammarajava;

/**
 * Enumerato che rappresenta tutti i territori, i relativi bonus e il continente
 * a cui appartengono.
 * @author stengun
 */
public enum territori_t {
                    //Nord America
                    Alaska                      (Bonus_t.FANTE,  Continente_t.NORDAMERICA),
                    Territori_del_Nord_Ovest    (Bonus_t.CANNONE,Continente_t.NORDAMERICA),
                    Groenlandia                 (Bonus_t.CAVALLO,Continente_t.NORDAMERICA),
                    Alberta                     (Bonus_t.FANTE,  Continente_t.NORDAMERICA),
                    Ontario                     (Bonus_t.CAVALLO,Continente_t.NORDAMERICA),
                    Quebec                      (Bonus_t.CANNONE,Continente_t.NORDAMERICA),
                    Stati_Uniti_Occidentali     (Bonus_t.FANTE,  Continente_t.NORDAMERICA),
                    Stati_Uniti_Orientali       (Bonus_t.CANNONE,Continente_t.NORDAMERICA),
                    America_Centrale            (Bonus_t.CAVALLO,Continente_t.NORDAMERICA),
                    //America del sud
                    Venezuela                   (Bonus_t.CANNONE, Continente_t.SUDAMERICA),
                    Peru                        (Bonus_t.CAVALLO, Continente_t.SUDAMERICA),
                    Brasile                     (Bonus_t.CANNONE, Continente_t.SUDAMERICA),
                    Argentina                   (Bonus_t.FANTE,   Continente_t.SUDAMERICA),
                    //Europa
                    Islanda                     (Bonus_t.FANTE,       Continente_t.EUROPA),
                    Scandinavia                 (Bonus_t.CANNONE,     Continente_t.EUROPA),
                    Gran_Bretagna               (Bonus_t.CAVALLO,     Continente_t.EUROPA),
                    Europa_Settentrionale       (Bonus_t.CAVALLO,     Continente_t.EUROPA),
                    Europa_Occidentale          (Bonus_t.FANTE,       Continente_t.EUROPA),
                    Europa_Meridionale          (Bonus_t.CAVALLO,     Continente_t.EUROPA),
                    Ucraina                     (Bonus_t.CANNONE,     Continente_t.EUROPA),
                    //Africa
                    Africa_del_Nord             (Bonus_t.FANTE,       Continente_t.AFRICA),
                    Egitto                      (Bonus_t.FANTE,       Continente_t.AFRICA),
                    Congo                       (Bonus_t.CAVALLO,     Continente_t.AFRICA),
                    Africa_Orientale            (Bonus_t.CANNONE,     Continente_t.AFRICA),
                    Africa_del_Sud              (Bonus_t.CANNONE,     Continente_t.AFRICA),
                    Madagascar                  (Bonus_t.FANTE,       Continente_t.AFRICA),
                    //Asia
                    Urali                       (Bonus_t.CAVALLO,       Continente_t.ASIA),
                    Siberia                     (Bonus_t.CANNONE,       Continente_t.ASIA),
                    Jacuzia                     (Bonus_t.CAVALLO,       Continente_t.ASIA),
                    Cita                        (Bonus_t.FANTE,         Continente_t.ASIA),
                    Kamchatka                   (Bonus_t.CAVALLO,       Continente_t.ASIA),
                    Giappone                    (Bonus_t.FANTE,         Continente_t.ASIA),
                    Mongolia                    (Bonus_t.CANNONE,       Continente_t.ASIA),
                    Afghanistan                 (Bonus_t.FANTE,         Continente_t.ASIA),
                    Medio_Oriente               (Bonus_t.CANNONE,       Continente_t.ASIA),
                    India                       (Bonus_t.FANTE,         Continente_t.ASIA),
                    Cina                        (Bonus_t.CAVALLO,       Continente_t.ASIA),
                    Siam                        (Bonus_t.CANNONE,       Continente_t.ASIA),
                    //Oceania
                    Indonesia                   (Bonus_t.CAVALLO,    Continente_t.OCEANIA),
                    Nuova_Guinea                (Bonus_t.CAVALLO,    Continente_t.OCEANIA),
                    Australia_Orientale         (Bonus_t.FANTE,      Continente_t.OCEANIA),
                    Australia_Occidentale       (Bonus_t.CANNONE,    Continente_t.OCEANIA),
                    //carte jolly
                    Jolly1                      (Bonus_t.JOLLY,      Continente_t.NESSUNO),
                    Jolly2                      (Bonus_t.JOLLY,      Continente_t.NESSUNO);

                    private Bonus_t bonus;
                    private Continente_t continente;

                    territori_t(Bonus_t bonus,Continente_t continente){
                        this.bonus = bonus;
                        this.continente = continente;
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
};

