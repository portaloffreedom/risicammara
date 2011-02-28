
package risicammaraJava.boardManage;
import java.io.Serializable;
import risicammaraClient.Continente_t;
import risicammaraClient.territori_t;

/**
 * Classe che rappresenta un territorio della plancia di gioco e tutte
 * le operazioni che è possibile fare su di esso.
 * @author stengun
 */
public class TerritorioPlancia implements Serializable{

    private territori_t territorio;
    private int armate_presenti;
    private int proprietario;
    private territori_t[] adiacenze;

    /**
     * Inizializza tutti i dati del territorio.
     * @param territorio l'enumerato che rappresenta il territorio.
     */
    public TerritorioPlancia(territori_t territorio){
        this.territorio = territorio;
        this.armate_presenti = 1;
        this.proprietario = -1;
        this.CompletaAdiacenze();
    }

    /**
     * Costruisce un territorioPlancia partendo da un altro territorioPlancia.
     * Viene usato per le classi figlie.
     * @param territorioPlancia il territorio da cui prelevare i dati.
     */
    protected TerritorioPlancia(TerritorioPlancia territorioPlancia){
        this.territorio = territorioPlancia.territorio;
        this.armate_presenti = territorioPlancia.armate_presenti;
        this.proprietario = territorioPlancia.proprietario;
        this.adiacenze = territorioPlancia.adiacenze;
    }

    /**
     * Imposta il nuovo numero di armate del territorio.
     * @param num numero di armate.
     */
    public void setArmate(int num){
        this.armate_presenti = num;
    }
    
    /**
     * Modifica il numero di armate nel territorio (sommando il numero passato 
     * in ingresso - num deve essere negativo se si vogliono togliere armate)
     * @param num numero di armate aggiunte (o tolte se negativo)
     */
    public void aggiornaArmate(int num){
        this.armate_presenti += num;
    }

    /**
     * Imposta il proprietario di questo territorio a "player"
     * @param player Il nuovo proprietario del territorio.
     */
    public void setProprietario(int player){
        this.proprietario = player;
    }

    /**
     * Richiedi il nome (come dato enum) del territorio.
     * @return l'oggetto enumerato che rappresenta il territorio.
     */
    public territori_t getTerritorio(){
        return this.territorio;
    }

    /**
     * Richiedi il continente di cui fa parte questo territorio
     * @return continente del territorio.
     */
    public Continente_t getContinente(){
        return this.territorio.getContinente();
    }

    /**
     * Richieri il numero di armate presenti nel territorio
     * @return numero di armate
     */
    public int getArmate(){
        return this.armate_presenti;
    }

    /**
     * Richiedi proprietario del territorio
     * @return riferimento a Giocatore proprietario del territorio
     */
    public int getProprietario(){
        return this.proprietario;
    }
    /**
     * Controlla se un dato territorio è adiacente a questo.
     * @param terri il territorio da controllare
     * @return True se è adiacente, false altrimenti.
     */
    public boolean isAdiacent(territori_t terri){
        for(territori_t t : adiacenze){
            if(terri == t) return true;
        }
        return false;
    }

    /**
     * Completa la lista di adiacenza di tutti i territori.
     */
    //TODO caricamento adiacenze da file.
    private void CompletaAdiacenze(){
        this.adiacenze = new territori_t[territorio.getNumadiacenze()];
        switch(this.territorio){
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
}
