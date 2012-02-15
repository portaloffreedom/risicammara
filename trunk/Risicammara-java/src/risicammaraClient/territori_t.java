package risicammaraClient;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import risicammaraJava.boardManage.TerritorioNonValido;
import PacchettoGrafico.PannelloGiocoPackage.PlanciaImmagine;
import risicammara2.global.Territorio_Plancia;
import risicammaraJava.deckManage.Carta;

/**
 * Enumerato che rappresenta tutti i territori, i relativi bonus e il continente
 * a cui appartengono.
 * @author stengun
 */
public enum territori_t implements Carta,Territorio_Plancia {
    //Asia
    Afghanistan                 (Bonus_t.FANTE,         Continente_t.ASIA, 4, 1),
    Cina                        (Bonus_t.CAVALLO,       Continente_t.ASIA, 7, 2),
    India                       (Bonus_t.FANTE,         Continente_t.ASIA, 3, 3),
    Cita                        (Bonus_t.FANTE,         Continente_t.ASIA, 4, 4),
    Giappone                    (Bonus_t.FANTE,         Continente_t.ASIA, 2, 5),
    Kamchatka                   (Bonus_t.CAVALLO,       Continente_t.ASIA, 5, 6),
    Medio_Oriente               (Bonus_t.CANNONE,       Continente_t.ASIA, 6, 7),
    Mongolia                    (Bonus_t.CANNONE,       Continente_t.ASIA, 5, 8),
    Siam                        (Bonus_t.CANNONE,       Continente_t.ASIA, 3, 9),
    Siberia                     (Bonus_t.CANNONE,       Continente_t.ASIA, 5, 10),
    Urali                       (Bonus_t.CAVALLO,       Continente_t.ASIA, 4, 11),
    Jacuzia                     (Bonus_t.CAVALLO,       Continente_t.ASIA, 3, 12),
    //Africa
    Congo                       (Bonus_t.CAVALLO,     Continente_t.AFRICA, 3, 1),
    Africa_Orientale            (Bonus_t.CANNONE,     Continente_t.AFRICA, 5, 2),
    Egitto                      (Bonus_t.FANTE,       Continente_t.AFRICA, 4, 3),
    Madagascar                  (Bonus_t.FANTE,       Continente_t.AFRICA, 2, 4),
    Africa_del_Nord             (Bonus_t.FANTE,       Continente_t.AFRICA, 6, 5),
    Africa_del_Sud              (Bonus_t.CANNONE,     Continente_t.AFRICA, 3, 6),
    //Europa
    Gran_Bretagna               (Bonus_t.CAVALLO,     Continente_t.EUROPA, 4, 1),
    Islanda                     (Bonus_t.FANTE,       Continente_t.EUROPA, 3, 2),
    Europa_Settentrionale       (Bonus_t.CAVALLO,     Continente_t.EUROPA, 5, 3),
    Scandinavia                 (Bonus_t.CANNONE,     Continente_t.EUROPA, 4, 4),
    Europa_Meridionale          (Bonus_t.CAVALLO,     Continente_t.EUROPA, 6, 5),
    Ucraina                     (Bonus_t.CANNONE,     Continente_t.EUROPA, 6, 6),
    Europa_Occidentale          (Bonus_t.FANTE,       Continente_t.EUROPA, 4, 7),
    //Nord America
    Alaska                      (Bonus_t.FANTE,  Continente_t.NORDAMERICA, 3, 1),
    Alberta                     (Bonus_t.FANTE,  Continente_t.NORDAMERICA, 4, 2),
    America_Centrale            (Bonus_t.CAVALLO,Continente_t.NORDAMERICA, 3, 3),
    Stati_Uniti_Orientali       (Bonus_t.CANNONE,Continente_t.NORDAMERICA, 4, 4),
    Groenlandia                 (Bonus_t.CAVALLO,Continente_t.NORDAMERICA, 4, 5),
    Territori_del_Nord_Ovest    (Bonus_t.CANNONE,Continente_t.NORDAMERICA, 4, 6),
    Ontario                     (Bonus_t.CAVALLO,Continente_t.NORDAMERICA, 6, 7),
    Quebec                      (Bonus_t.CANNONE,Continente_t.NORDAMERICA, 3, 8),
    Stati_Uniti_Occidentali     (Bonus_t.FANTE,  Continente_t.NORDAMERICA, 4, 9),
    //America del sud
    Argentina                   (Bonus_t.FANTE,   Continente_t.SUDAMERICA, 2, 1),
    Brasile                     (Bonus_t.CANNONE, Continente_t.SUDAMERICA, 4, 2),
    Peru                        (Bonus_t.CAVALLO, Continente_t.SUDAMERICA, 3, 3),
    Venezuela                   (Bonus_t.CANNONE, Continente_t.SUDAMERICA, 3, 4),
    //Oceania
    Australia_Occidentale       (Bonus_t.CANNONE,    Continente_t.OCEANIA, 3, 1),
    Indonesia                   (Bonus_t.CAVALLO,    Continente_t.OCEANIA, 3, 2),
    Nuova_Guinea                (Bonus_t.CAVALLO,    Continente_t.OCEANIA, 3, 3),
    Australia_Orientale         (Bonus_t.FANTE,      Continente_t.OCEANIA, 2, 4),
    //carte jolly
    Jolly1                      (Bonus_t.JOLLY,      Continente_t.NESSUNO, 0, 0),
    Jolly2                      (Bonus_t.JOLLY,      Continente_t.NESSUNO, 0, 0);
/** Contenitore per il valore "bonus" */
    private Bonus_t bonus;
    /** Contenitore per il valore "continente" */
    private Continente_t continente;
    /** Contenitore che tiene traccia del numero dei territori adiacenti */
    private int territori_adiacenti;
    /** Contenitore per l'identificativo del territorio */
    private int id;
    
/** Costruttore per i territori con la loro lista di adiacenza */
    territori_t(Bonus_t bonus,Continente_t continente,int territori_adiacenti, int id){
        this.bonus = bonus;
        this.continente = continente;
        this.territori_adiacenti = territori_adiacenti;
        this.id = id;
        //aggiunta 24 dic per risicammara 2
        this.rect = null;
        this.bollino = null;
        this.armate_presenti = 1;
        this.proprietario = -1;
        this.CompletaAdiacenze();
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
    @Override
    public String tipoCarta() {
        return this.toString();
    }
    /**
     * Interfaccia per prendere l'idTerritorio in codifica RGB.
     * @return l'idTerritorio generato dalla PlanciaImmagine
     * @see PlanciaImmagine.GetIdTerritorio(int, int);
     */
    public int getIdTerritorio(){
        return PlanciaImmagine.GetIdTerritorio(continente.getId(), id);
    }
/**
 * Restituisce un territorio a partire dal suo identificativo.
 * @param idTerritorio l'identificativo da cercare.
 * @return l'oggetto territori_t corrispondente.
 * @throws TerritorioNonValido Se l'id non è valido viene sollevata questa eccezione.
 */
    public static territori_t GetTerritorio(int idTerritorio) throws TerritorioNonValido {
        int territorio = PlanciaImmagine.GetTerritorio(idTerritorio);
        int continente = PlanciaImmagine.GetContinente(idTerritorio);
        return GetTerritorio(continente, territorio);
    }
/**
 * Metodo di supporto per GetTerritorio per cercare un territorio in base a id e continente.
 * @param continente l'intero che identifica il continente
 * @param territorio l'intero che identifica il territorio dentro un continente
 * @return il territorio corrispondente
 * @throws TerritorioNonValido Sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorio(int continente, int territorio) throws TerritorioNonValido {
        
        if (continente == Continente_t.ASIA.getId())//Asia
            return GetTerritorioAsia(territorio);
        if (continente == Continente_t.AFRICA.getId())//Africa
            return GetTerritorioAfrica(territorio);
        if (continente == Continente_t.EUROPA.getId())//Europa
            return GetTerritorioEuropa(territorio);
        if (continente == Continente_t.NORDAMERICA.getId())//America del Nord
            return GetTerritorioAmericaNord(territorio);
        if (continente == Continente_t.SUDAMERICA.getId())//America del Sud
            return GetTerritorioAmericaSud(territorio);
        if (continente == Continente_t.OCEANIA.getId())//Oceania
            return GetTerritorioOceania(territorio);

        //System.err.println("ERRORE, non esiste un settimo continente");
        throw new TerritorioNonValido("ERRORE, non esiste il continente="+continente+" (territorio="+territorio+")");
        //return null;
    }
/**
 * Metodo di supporto a GetTerritorio per restituire i territori che appartengono all'asia
 * @param territorio l'id del territorio
 * @return il territorio corrispondente
 * @throws TerritorioNonValido sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorioAsia (int territorio) throws TerritorioNonValido{
        if (territorio == Afghanistan.id)
            return Afghanistan;
        if (territorio == Cina.id)
            return Cina;
        if (territorio == India.id)
            return India;
        if (territorio == Cita.id)
            return Cita;
        if (territorio == Giappone.id)
            return Giappone;
        if (territorio == Kamchatka.id)
            return Kamchatka;
        if (territorio == Medio_Oriente.id)
            return Medio_Oriente;
        if (territorio == Mongolia.id)
            return Mongolia;
        if (territorio == Siam.id)
            return Siam;
        if (territorio == Siberia.id)
            return Siberia;
        if (territorio == Urali.id)
            return Urali;
        if (territorio == Jacuzia.id)
            return Jacuzia;

        throw new TerritorioNonValido("Non esiste il territorio="+territorio+" in Asia");
        //return null;
    }
/**
 * Metodo di supporto a GetTerritorio per restituire i territori che appartengono all'Africa
 * @param territorio l'id del territorio
 * @return il territorio corrispondente
 * @throws TerritorioNonValido sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorioAfrica (int territorio) throws TerritorioNonValido{
        if (territorio == Congo.id)
            return Congo;
        if (territorio == territori_t.Africa_Orientale.id)
            return Africa_Orientale;
        if (territorio == Egitto.id)
            return Egitto;
        if (territorio == Madagascar.id)
            return Madagascar;
        if (territorio == Africa_del_Nord.id)
            return Africa_del_Nord;
        if (territorio == Africa_del_Sud.id)
            return Africa_del_Sud;

        throw new TerritorioNonValido("Non esiste il territorio="+territorio+" in Africa");
        //return null;
    }
/**
 * Metodo di supporto a GetTerritorio per restituire i territori che appartengono all'Europa
 * @param territorio l'id del territorio
 * @return il territorio corrispondente
 * @throws TerritorioNonValido sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorioEuropa (int territorio) throws TerritorioNonValido{
        if (territorio == Gran_Bretagna.id)
            return Gran_Bretagna;
        if (territorio == Islanda.id)
            return Islanda;
        if (territorio == Europa_Settentrionale.id)
            return Europa_Settentrionale;
        if (territorio == Scandinavia.id)
            return Scandinavia;
        if (territorio == Europa_Meridionale.id)
            return Europa_Meridionale;
        if (territorio == Ucraina.id)
            return Ucraina;
        if (territorio == Europa_Occidentale.id)
            return Europa_Occidentale;

        throw new TerritorioNonValido("Non esiste il territorio="+territorio+" in Europa");
        //return null;
    }
/**
 * Metodo di supporto a GetTerritorio per restituire i territori che appartengono all'America del Nord
 * @param territorio l'id del territorio
 * @return il territorio corrispondente
 * @throws TerritorioNonValido sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorioAmericaNord (int territorio) throws TerritorioNonValido{

        if (territorio == Alaska.id)
            return Alaska;
        if (territorio == Alberta.id)
            return Alberta;
        if (territorio == America_Centrale.id)
            return America_Centrale;
        if (territorio == Stati_Uniti_Orientali.id)
            return Stati_Uniti_Orientali;
        if (territorio == Groenlandia.id)
            return Groenlandia;
        if (territorio == Territori_del_Nord_Ovest.id)
            return Territori_del_Nord_Ovest;
        if (territorio == Ontario.id)
            return Ontario;
        if (territorio == Quebec.id)
            return Quebec;
        if (territorio == Stati_Uniti_Occidentali.id)
            return Stati_Uniti_Occidentali;

        throw new TerritorioNonValido("Non esiste il territorio="+territorio+" in Nord America");
        //return null;
    }
/**
 * Metodo di supporto a GetTerritorio per restituire i territori che appartengono all'America del Sud
 * @param territorio l'id del territorio
 * @return il territorio corrispondente
 * @throws TerritorioNonValido sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorioAmericaSud (int territorio) throws TerritorioNonValido{
        if (territorio == Argentina.id)
            return Argentina;
        if (territorio == Brasile.id)
            return Brasile;
        if (territorio == Peru.id)
            return Peru;
        if (territorio == Venezuela.id)
            return Venezuela;

        throw new TerritorioNonValido("Non esiste il territorio="+territorio+" in Sud America");
        //return null;
    }
/**
 * Metodo di supporto a GetTerritorio per restituire i territori che appartengono all'Oceania
 * @param territorio l'id del territorio
 * @return il territorio corrispondente
 * @throws TerritorioNonValido sollevata se l'id non è valido.
 */
    private static territori_t GetTerritorioOceania (int territorio) throws TerritorioNonValido{
        if (territorio == Australia_Occidentale.id)
            return Australia_Occidentale;
        if (territorio == Indonesia.id)
            return Indonesia;
        if (territorio == Nuova_Guinea.id)
            return Nuova_Guinea;
        if (territorio == Australia_Orientale.id)
            return Australia_Orientale;

        throw new TerritorioNonValido("Non esiste il territorio="+territorio+" in Oceania");
        //return null;
    }

    /**
     * Restituisce L'icona assegnata al bonus del territorio.
     * @return Il riferimento all'immagine del bonus.
     */
    @Override
    public BufferedImage getIcona() {
        String icona = this.getBonus().getPercorsoImmagine();
        return Client.loadImage(this, icona);
    }

    // RIsicammara 2
    
    private int armate_presenti;
    private Rectangle rect;
    private Point bollino;
    private long proprietario;
    private territori_t[] adiacenze;
    
    @Override
    public void addArmate(int armate) {
        armate_presenti += armate;
    }

    @Override
    public void setArmate(int armate) {
        armate_presenti = armate;
    }

    @Override
    public int getArmate() {
        return armate_presenti;
    }

    @Override
    public void setProprietario(long player_id) {
        this.proprietario = player_id;
    }

    @Override
    public long getProprietario() {
        return this.proprietario;
    }

        
    private void CompletaAdiacenze(){
        this.adiacenze = new territori_t[this.getNumadiacenze()];
        switch(this){
            case Alaska:
                adiacenze[0] = territori_t.Alberta;
                adiacenze[1] = territori_t.Territori_del_Nord_Ovest;
                adiacenze[2] = territori_t.Kamchatka;
                break;
            case Territori_del_Nord_Ovest:
                adiacenze[0] = territori_t.Alaska;
                adiacenze[1] = territori_t.Groenlandia;
                adiacenze[2] = territori_t.Alberta;
                adiacenze[3] = territori_t.Ontario;
                break;
            case Groenlandia:
                adiacenze[0] = territori_t.Territori_del_Nord_Ovest;
                adiacenze[1] = territori_t.Quebec;
                adiacenze[2] = territori_t.Ontario;
                adiacenze[3] = territori_t.Islanda;
                break;
            case Alberta:
                adiacenze[0] = territori_t.Alaska;
                adiacenze[1] = territori_t.Territori_del_Nord_Ovest;
                adiacenze[2] = territori_t.Ontario;
                adiacenze[3] = territori_t.Stati_Uniti_Occidentali;
                break;
            case Ontario:
                adiacenze[0] = territori_t.Territori_del_Nord_Ovest;
                adiacenze[1] = territori_t.Groenlandia;
                adiacenze[2] = territori_t.Quebec;
                adiacenze[3] = territori_t.Stati_Uniti_Occidentali;
                adiacenze[4] = territori_t.Alberta;
                adiacenze[5] = territori_t.Stati_Uniti_Orientali;
                break;
            case Quebec:
                adiacenze[0] = territori_t.Groenlandia;
                adiacenze[1] = territori_t.Ontario;
                adiacenze[2] = territori_t.Stati_Uniti_Orientali;
                break;
            case Stati_Uniti_Occidentali:
                adiacenze[0] = territori_t.Alberta;
                adiacenze[1] = territori_t.Ontario;
                adiacenze[2] = territori_t.Stati_Uniti_Orientali;
                adiacenze[3] = territori_t.America_Centrale;
                break;
            case Stati_Uniti_Orientali:
                adiacenze[0] = territori_t.Stati_Uniti_Occidentali;
                adiacenze[1] = territori_t.Ontario;
                adiacenze[2] = territori_t.Quebec;
                adiacenze[3] = territori_t.America_Centrale;
                break;
            case America_Centrale:
                adiacenze[0] = territori_t.Stati_Uniti_Occidentali;
                adiacenze[1] = territori_t.Stati_Uniti_Orientali;
                adiacenze[2] = territori_t.Venezuela;
                break;
            case Venezuela:
                adiacenze[0] = territori_t.Peru;
                adiacenze[1] = territori_t.Brasile;
                adiacenze[2] = territori_t.America_Centrale;
                break;
            case Peru:
                adiacenze[0] = territori_t.Venezuela;
                adiacenze[1] = territori_t.Brasile;
                adiacenze[2] = territori_t.Argentina;
                break;
            case Brasile:
                adiacenze[0] = territori_t.Venezuela;
                adiacenze[1] = territori_t.Peru;
                adiacenze[2] = territori_t.Argentina;
                adiacenze[3] = territori_t.Africa_del_Nord;
                break;
            case Argentina:
                adiacenze[0] = territori_t.Peru;
                adiacenze[1] = territori_t.Brasile;
                break;
            case Islanda:
                adiacenze[0] = territori_t.Groenlandia;
                adiacenze[1] = territori_t.Scandinavia;
                adiacenze[2] = territori_t.Gran_Bretagna;
                break;
            case Scandinavia:
                adiacenze[0] = territori_t.Ucraina;
                adiacenze[1] = territori_t.Gran_Bretagna;
                adiacenze[2] = territori_t.Europa_Settentrionale;
                adiacenze[3] = territori_t.Islanda;
                break;
            case  Gran_Bretagna:
                adiacenze[0] = territori_t.Scandinavia;
                adiacenze[1] = territori_t.Europa_Settentrionale;
                adiacenze[2] = territori_t.Islanda;
                adiacenze[3] = territori_t.Europa_Occidentale;
                break;
            case Europa_Settentrionale:
                adiacenze[0] = territori_t.Scandinavia;
                adiacenze[1] = territori_t.Ucraina;
                adiacenze[2] = territori_t.Gran_Bretagna;
                adiacenze[3] = territori_t.Europa_Occidentale;
                adiacenze[4] = territori_t.Europa_Meridionale;
                break;
            case Europa_Occidentale:
                adiacenze[0] = territori_t.Gran_Bretagna;
                adiacenze[1] = territori_t.Europa_Settentrionale;
                adiacenze[2] = territori_t.Europa_Meridionale;
                adiacenze[3] = territori_t.Africa_del_Nord;
                break;
            case Europa_Meridionale:
                adiacenze[0] = territori_t.Europa_Settentrionale;
                adiacenze[1] = territori_t.Europa_Occidentale;
                adiacenze[2] = territori_t.Africa_del_Nord;
                adiacenze[3] = territori_t.Egitto;
                adiacenze[4] = territori_t.Ucraina;
                adiacenze[5] = territori_t.Medio_Oriente;
                break;
            case Ucraina:
                adiacenze[0] = territori_t.Scandinavia;
                adiacenze[1] = territori_t.Europa_Settentrionale;
                adiacenze[2] = territori_t.Europa_Meridionale;
                adiacenze[3] = territori_t.Medio_Oriente;
                adiacenze[4] = territori_t.Urali;
                adiacenze[5] = territori_t.Afghanistan;
                break;
                        //Africa
            case Africa_del_Nord:
                adiacenze[0] = territori_t.Brasile;
                adiacenze[1] = territori_t.Europa_Occidentale;
                adiacenze[2] = territori_t.Europa_Meridionale;
                adiacenze[3] = territori_t.Egitto;
                adiacenze[4] = territori_t.Congo;
                adiacenze[5] = territori_t.Africa_Orientale;
                break;
            case Egitto:
                adiacenze[0] = territori_t.Africa_del_Nord;
                adiacenze[1] = territori_t.Europa_Meridionale;
                adiacenze[2] = territori_t.Medio_Oriente;
                adiacenze[3] = territori_t.Africa_Orientale;
                break;
            case Congo:
                adiacenze[0] = territori_t.Africa_del_Nord;
                adiacenze[1] = territori_t.Africa_Orientale;
                adiacenze[2] = territori_t.Africa_del_Sud;
                break;
            case Africa_Orientale:
                adiacenze[0] = territori_t.Egitto;
                adiacenze[1] = territori_t.Africa_del_Nord;
                adiacenze[2] = territori_t.Congo;
                adiacenze[3] = territori_t.Madagascar;
                adiacenze[4] = territori_t.Africa_del_Sud;
                break;
            case Africa_del_Sud:
                adiacenze[0] = territori_t.Madagascar;
                adiacenze[1] = territori_t.Congo;
                adiacenze[2] = territori_t.Africa_Orientale;
                break;
            case Madagascar:
                adiacenze[0] = territori_t.Africa_Orientale;
                adiacenze[1] = territori_t.Africa_del_Sud;
                break;
                        //Asia
            case Urali:
                adiacenze[0] = territori_t.Ucraina;
                adiacenze[1] = territori_t.Afghanistan;
                adiacenze[2] = territori_t.Cina;
                adiacenze[3] = territori_t.Siberia;
                break;
            case Siberia:
                adiacenze[0] = territori_t.Urali;
                adiacenze[1] = territori_t.Cina;
                adiacenze[2] = territori_t.Mongolia;
                adiacenze[3] = territori_t.Cita;
                adiacenze[4] = territori_t.Jacuzia;
                break;
            case Jacuzia:
                adiacenze[0] = territori_t.Cita;
                adiacenze[1] = territori_t.Siberia;
                adiacenze[2] = territori_t.Kamchatka;
                break;
            case Cita:
                adiacenze[0] = territori_t.Siberia;
                adiacenze[1] = territori_t.Jacuzia;
                adiacenze[2] = territori_t.Mongolia;
                adiacenze[3] = territori_t.Kamchatka;
                break;
            case Kamchatka:
                adiacenze[0] = territori_t.Jacuzia;
                adiacenze[1] = territori_t.Mongolia;
                adiacenze[2] = territori_t.Cita;
                adiacenze[3] = territori_t.Giappone;
                adiacenze[4] = territori_t.Alaska;
                break;
            case Giappone:
                adiacenze[0] = territori_t.Kamchatka;
                adiacenze[1] = territori_t.Mongolia;
                break;
            case Mongolia:
                adiacenze[0] = territori_t.Giappone;
                adiacenze[1] = territori_t.Cina;
                adiacenze[2] = territori_t.Siberia;
                adiacenze[3] = territori_t.Cita;
                adiacenze[4] = territori_t.Kamchatka;
                break;
            case Afghanistan:
                adiacenze[0] = territori_t.Ucraina;
                adiacenze[1] = territori_t.Urali;
                adiacenze[2] = territori_t.Cina;
                adiacenze[3] = territori_t.Medio_Oriente;
                break;
            case Medio_Oriente:
                adiacenze[0] = territori_t.Ucraina;
                adiacenze[1] = territori_t.Europa_Meridionale;
                adiacenze[2] = territori_t.Egitto;
                adiacenze[3] = territori_t.Cina;
                adiacenze[4] = territori_t.India;
                adiacenze[5] = territori_t.Afghanistan;
                break;
            case India:
                adiacenze[0] = territori_t.Medio_Oriente;
                adiacenze[1] = territori_t.Cina;
                adiacenze[2] = territori_t.Siam;
                break;
            case Cina:
                adiacenze[0] = territori_t.Urali;
                adiacenze[1] = territori_t.Siberia;
                adiacenze[2] = territori_t.Mongolia;
                adiacenze[3] = territori_t.Afghanistan;
                adiacenze[4] = territori_t.Medio_Oriente;
                adiacenze[5] = territori_t.Siam;
                adiacenze[6] = territori_t.India;
                break;
            case Siam:
                adiacenze[0] = territori_t.Indonesia;
                adiacenze[1] = territori_t.India;
                adiacenze[2] = territori_t.Cina;
                break;
                        //Oceania
            case Indonesia:
                adiacenze[0] = territori_t.Siam;
                adiacenze[1] = territori_t.Nuova_Guinea;
                adiacenze[2] = territori_t.Australia_Occidentale;
                break;
            case Nuova_Guinea:
                adiacenze[0] = territori_t.Indonesia;
                adiacenze[1] = territori_t.Australia_Orientale;
                adiacenze[2] = territori_t.Australia_Occidentale;
                break;
            case Australia_Orientale:
                adiacenze[0] = territori_t.Australia_Occidentale;
                adiacenze[1] = territori_t.Nuova_Guinea;
                break;
            case Australia_Occidentale:
                adiacenze[0] = territori_t.Indonesia;
                adiacenze[1] = territori_t.Nuova_Guinea;
                adiacenze[2] = territori_t.Australia_Orientale;
             default:
                            break;
        }
    }
    
    /**
     * Controlla se un dato territorio è adiacente a questo.
     * @param terri il territorio da controllare
     * @return True se è adiacente, false altrimenti.
     */
    @Override
    public boolean isAdiacent(territori_t terri){
        for(territori_t t : adiacenze){
            if(terri == t) return true;
        }
        return false;
    }

    @Override
    public void setPosizione(Rectangle rect) {
        this.rect = rect;
    }

    @Override
    public Rectangle getPosizione() {
        return rect;
    }

    @Override
    public void setPosizioneBollino(Point punto_bollino) {
        this.bollino = punto_bollino;
    }

    @Override
    public Point getPosizioneBollino() {
        return this.bollino;
    }

};

