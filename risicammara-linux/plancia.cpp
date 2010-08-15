/*  Rappresentazione della plancia di gioco del risiko
    tramite una lista di adiacenza.*/

#include <list>
#include <iostream>
#include "headers/regolestruct.h"
#include "headers/plancia.h"
using namespace std;

extern giocatore ALLPLAYERS[];
extern tabellone plancia;

/** Completa le adiacenze dei territori per la costruzione del planisfero
 * @param d l'indice territorio. */
void tabellone::completa_adiacenze(int d){
    switch(d){
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
            plancia[d].continente = Nord_America;
            break;
        case 9:
        case 10:
        case 11:
        case 12:
            plancia[d].continente = Sud_America;
            break;
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
            plancia[d].continente = Europa;
            break;
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
            plancia[d].continente = Africa;
            break;
        default:
        case 37:
            plancia[d].continente = Asia;
            break;
        case 38:
        case 39:
        case 40:
        case 41:
            plancia[d].continente = Oceania;
            break;
    }

    switch(plancia[d].territorio)
    {
        case Alaska:
            plancia[d].adiacenze.push_back(Alberta);
            plancia[d].adiacenze.push_back(Territori_del_Nord_Ovest);
            plancia[d].adiacenze.push_back(Kamchatka);
            plancia[d].size_adj_list = 3;
            break;
        case Territori_del_Nord_Ovest:
            plancia[d].adiacenze.push_back(Alaska);
            plancia[d].adiacenze.push_back(Groenlandia);
            plancia[d].adiacenze.push_back(Alberta);
            plancia[d].adiacenze.push_back(Ontario);
            plancia[d].size_adj_list = 4;
            break;
        case Groenlandia:
            plancia[d].adiacenze.push_back(Territori_del_Nord_Ovest);
            plancia[d].adiacenze.push_back(Quebec);
            plancia[d].adiacenze.push_back(Ontario);
            plancia[d].adiacenze.push_back(Islanda);
            plancia[d].size_adj_list = 4;
            break;
        case Alberta:
            plancia[d].adiacenze.push_back(Alaska);
            plancia[d].adiacenze.push_back(Territori_del_Nord_Ovest);
            plancia[d].adiacenze.push_back(Ontario);
            plancia[d].adiacenze.push_back(Stati_Uniti_Occidentali);
            plancia[d].size_adj_list = 4;
            break;
        case Ontario:
            plancia[d].adiacenze.push_back(Territori_del_Nord_Ovest);
            plancia[d].adiacenze.push_back(Groenlandia);
            plancia[d].adiacenze.push_back(Quebec);
            plancia[d].adiacenze.push_back(Stati_Uniti_Occidentali);
            plancia[d].adiacenze.push_back(Alberta);
            plancia[d].adiacenze.push_back(Stati_Uniti_Orientali);
            plancia[d].size_adj_list = 6;
            break;
        case Quebec:
            plancia[d].adiacenze.push_back(Groenlandia);
            plancia[d].adiacenze.push_back(Ontario);
            plancia[d].adiacenze.push_back(Stati_Uniti_Orientali);
            plancia[d].size_adj_list = 3;
            break;
        case Stati_Uniti_Occidentali:
            plancia[d].adiacenze.push_back(Alberta);
            plancia[d].adiacenze.push_back(Ontario);
            plancia[d].adiacenze.push_back(Stati_Uniti_Orientali);
            plancia[d].adiacenze.push_back(America_Centrale);
            plancia[d].size_adj_list = 4;
            break;
        case Stati_Uniti_Orientali:
            plancia[d].adiacenze.push_back(Stati_Uniti_Occidentali);
            plancia[d].adiacenze.push_back(Ontario);
            plancia[d].adiacenze.push_back(Quebec);
            plancia[d].adiacenze.push_back(America_Centrale);
            plancia[d].size_adj_list = 4;
            break;
        case America_Centrale:
            plancia[d].adiacenze.push_back(Stati_Uniti_Occidentali);
            plancia[d].adiacenze.push_back(Stati_Uniti_Orientali);
            plancia[d].adiacenze.push_back(Venezuela);
            plancia[d].size_adj_list = 3;
            break;
        case Venezuela:
            plancia[d].adiacenze.push_back(Peru);
            plancia[d].adiacenze.push_back(Brasile);
            plancia[d].adiacenze.push_back(America_Centrale);
            plancia[d].size_adj_list = 3;
            break;
        case Peru:
            plancia[d].adiacenze.push_back(Venezuela);
            plancia[d].adiacenze.push_back(Brasile);
            plancia[d].adiacenze.push_back(Argentina);
            plancia[d].size_adj_list = 3;
            break;
        case Brasile:
            plancia[d].adiacenze.push_back(Venezuela);
            plancia[d].adiacenze.push_back(Peru);
            plancia[d].adiacenze.push_back(Argentina);
            plancia[d].adiacenze.push_back(Africa_del_Nord);
            plancia[d].size_adj_list = 4;
            break;
        case Argentina:
            plancia[d].adiacenze.push_back(Peru);
            plancia[d].adiacenze.push_back(Brasile);
            plancia[d].size_adj_list = 2;
            break;
        case Islanda:
            plancia[d].adiacenze.push_back(Groenlandia);
            plancia[d].adiacenze.push_back(Scandinavia);
            plancia[d].adiacenze.push_back(Gran_Bretagna);
            plancia[d].size_adj_list = 3;
            break;
        case Scandinavia:
            plancia[d].adiacenze.push_back(Ucraina);
            plancia[d].adiacenze.push_back(Gran_Bretagna);
            plancia[d].adiacenze.push_back(Europa_Settentrionale);
            plancia[d].adiacenze.push_back(Islanda);
            plancia[d].size_adj_list = 4;
            break;
        case  Gran_Bretagna:
            plancia[d].adiacenze.push_back(Scandinavia);
            plancia[d].adiacenze.push_back(Europa_Settentrionale);
            plancia[d].adiacenze.push_back(Islanda);
            plancia[d].adiacenze.push_back(Europa_Occidentale);
            plancia[d].size_adj_list = 4;
            break;
        case Europa_Settentrionale:
            plancia[d].adiacenze.push_back(Scandinavia);
            plancia[d].adiacenze.push_back(Ucraina);
            plancia[d].adiacenze.push_back(Gran_Bretagna);
            plancia[d].adiacenze.push_back(Europa_Occidentale);
            plancia[d].adiacenze.push_back(Europa_Meridionale);
            plancia[d].size_adj_list = 5;
            break;
        case Europa_Occidentale:
            plancia[d].adiacenze.push_back(Gran_Bretagna);
            plancia[d].adiacenze.push_back(Europa_Settentrionale);
            plancia[d].adiacenze.push_back(Europa_Meridionale);
            plancia[d].adiacenze.push_back(Africa_del_Nord);
            plancia[d].size_adj_list = 4;
            break;
        case Europa_Meridionale:
            plancia[d].adiacenze.push_back(Europa_Settentrionale);
            plancia[d].adiacenze.push_back(Europa_Occidentale);
            plancia[d].adiacenze.push_back(Africa_del_Nord);
            plancia[d].adiacenze.push_back(Egitto);
            plancia[d].adiacenze.push_back(Ucraina);
            plancia[d].adiacenze.push_back(Medio_Oriente);
            plancia[d].size_adj_list = 6;
            break;
        case Ucraina:
            plancia[d].adiacenze.push_back(Scandinavia);
            plancia[d].adiacenze.push_back(Europa_Settentrionale);
            plancia[d].adiacenze.push_back(Europa_Meridionale);
            plancia[d].adiacenze.push_back(Medio_Oriente);
            plancia[d].adiacenze.push_back(Urali);
            plancia[d].adiacenze.push_back(Afghanistan);
            plancia[d].size_adj_list = 6;
            break;
                    //Africa
        case Africa_del_Nord:
            plancia[d].adiacenze.push_back(Brasile);
            plancia[d].adiacenze.push_back(Europa_Occidentale);
            plancia[d].adiacenze.push_back(Europa_Meridionale);
            plancia[d].adiacenze.push_back(Egitto);
            plancia[d].adiacenze.push_back(Congo);
            plancia[d].adiacenze.push_back(Africa_Orientale);
            plancia[d].size_adj_list = 6;
            break;
        case Egitto:
            plancia[d].adiacenze.push_back(Africa_del_Nord);
            plancia[d].adiacenze.push_back(Europa_Meridionale);
            plancia[d].adiacenze.push_back(Medio_Oriente);
            plancia[d].adiacenze.push_back(Africa_Orientale);
            plancia[d].size_adj_list = 4;
            break;
        case Congo:
            plancia[d].adiacenze.push_back(Africa_del_Nord);
            plancia[d].adiacenze.push_back(Africa_Orientale);
            plancia[d].adiacenze.push_back(Africa_del_Sud);
            plancia[d].size_adj_list = 3;
            break;
        case Africa_Orientale:
            plancia[d].adiacenze.push_back(Egitto);
            plancia[d].adiacenze.push_back(Africa_del_Nord);
            plancia[d].adiacenze.push_back(Congo);
            plancia[d].adiacenze.push_back(Madagascar);
            plancia[d].adiacenze.push_back(Africa_del_Sud);
            plancia[d].size_adj_list = 5;
            break;
        case Africa_del_Sud:
            plancia[d].adiacenze.push_back(Madagascar);
            plancia[d].adiacenze.push_back(Congo);
            plancia[d].adiacenze.push_back(Africa_Orientale);
            plancia[d].size_adj_list = 3;
            break;
        case Madagascar:
            plancia[d].adiacenze.push_back(Africa_Orientale);
            plancia[d].adiacenze.push_back(Africa_del_Sud);
            plancia[d].size_adj_list = 2;
            break;
                    //Asia
        case Urali:
            plancia[d].adiacenze.push_back(Ucraina);
            plancia[d].adiacenze.push_back(Afghanistan);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].adiacenze.push_back(Siberia);
            plancia[d].size_adj_list = 4;
            break;
        case Siberia:
            plancia[d].adiacenze.push_back(Urali);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].adiacenze.push_back(Mongolia);
            plancia[d].adiacenze.push_back(Cita);
            plancia[d].adiacenze.push_back(Jacuzia);
            plancia[d].size_adj_list = 5;
            break;
        case Jacuzia:
            plancia[d].adiacenze.push_back(Cita);
            plancia[d].adiacenze.push_back(Siberia);
            plancia[d].adiacenze.push_back(Kamchatka);
            plancia[d].size_adj_list = 3;
            break;
        case Cita:
            plancia[d].adiacenze.push_back(Siberia);
            plancia[d].adiacenze.push_back(Jacuzia);
            plancia[d].adiacenze.push_back(Mongolia);
            plancia[d].adiacenze.push_back(Kamchatka);
            plancia[d].size_adj_list = 4;
            break;
        case Kamchatka:
            plancia[d].adiacenze.push_back(Jacuzia);
            plancia[d].adiacenze.push_back(Mongolia);
            plancia[d].adiacenze.push_back(Cita);
            plancia[d].adiacenze.push_back(Giappone);
            plancia[d].adiacenze.push_back(Alaska);
            plancia[d].size_adj_list = 5;
            break;
        case Giappone:
            plancia[d].adiacenze.push_back(Kamchatka);
            plancia[d].adiacenze.push_back(Mongolia);
            plancia[d].size_adj_list = 2;
            break;
        case Mongolia:
            plancia[d].adiacenze.push_back(Giappone);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].adiacenze.push_back(Siberia);
            plancia[d].adiacenze.push_back(Cita);
            plancia[d].adiacenze.push_back(Kamchatka);
            plancia[d].size_adj_list = 5;
            break;
        case Afghanistan:
            plancia[d].adiacenze.push_back(Ucraina);
            plancia[d].adiacenze.push_back(Urali);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].adiacenze.push_back(Medio_Oriente);
            plancia[d].size_adj_list = 4;
            break;
        case Medio_Oriente:
            plancia[d].adiacenze.push_back(Ucraina);
            plancia[d].adiacenze.push_back(Europa_Meridionale);
            plancia[d].adiacenze.push_back(Egitto);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].adiacenze.push_back(India);
            plancia[d].adiacenze.push_back(Afghanistan);
            plancia[d].size_adj_list = 6;
            break;
        case India:
            plancia[d].adiacenze.push_back(Medio_Oriente);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].adiacenze.push_back(Siam);
            plancia[d].size_adj_list = 3;
            break;
        case Cina:
            plancia[d].adiacenze.push_back(Urali);
            plancia[d].adiacenze.push_back(Siberia);
            plancia[d].adiacenze.push_back(Mongolia);
            plancia[d].adiacenze.push_back(Afghanistan);
            plancia[d].adiacenze.push_back(Medio_Oriente);
            plancia[d].adiacenze.push_back(Siam);
            plancia[d].adiacenze.push_back(India);
            plancia[d].size_adj_list = 7;
            break;
        case Siam:
            plancia[d].adiacenze.push_back(Indonesia);
            plancia[d].adiacenze.push_back(India);
            plancia[d].adiacenze.push_back(Cina);
            plancia[d].size_adj_list = 3;
            break;
                    //Oceania
        case Indonesia:
            plancia[d].adiacenze.push_back(Siam);
            plancia[d].adiacenze.push_back(Nuova_Guinea);
            plancia[d].adiacenze.push_back(Australia_Occidentale);
            plancia[d].size_adj_list = 3;
            break;
        case Nuova_Guinea:
            plancia[d].adiacenze.push_back(Indonesia);
            plancia[d].adiacenze.push_back(Australia_Orientale);
            plancia[d].adiacenze.push_back(Australia_Occidentale);
            plancia[d].size_adj_list = 3;
            break;
        case Australia_Orientale:
            plancia[d].adiacenze.push_back(Australia_Occidentale);
            plancia[d].adiacenze.push_back(Nuova_Guinea);
            plancia[d].size_adj_list = 2;
            break;
        case Australia_Occidentale:
            plancia[d].adiacenze.push_back(Indonesia);
            plancia[d].adiacenze.push_back(Nuova_Guinea);
            plancia[d].adiacenze.push_back(Australia_Orientale);
            plancia[d].size_adj_list = 3;
         default:
			break;
    }

};

/**Costruisce tutta la plancia di gioco come un grafo a liste di adiacenza
 * e completa tutto in maniera standard, senza proprietari e armate.  */
tabellone::tabellone(){
    //Tutti i territori nell'enumerato sono elencati da 0 a 41
    //usando un "for" si rende tutto più veloce
    for(int i=0;i<42;i++){
        //dentro aggiorniamo il proprietario del territorio e le armate presenti
        //e ne completiamo le adiacenze
        plancia[i].territorio = static_cast<territori_t>(i);
        plancia[i].proprietario = nessuno;
        plancia[i].num_armate = 0;
        completa_adiacenze(i);
    }   
};

/**
 * Cambia il proprietario di un territorio aggiornando le armate.
 * @param territorio Il territorio di cui cambiare il proprietario
 * @param nuovo_proprietario Il nuovo proprietario del territorio
 * @param nuove_armate Il numero di armate da aggiornare. Default: 1
 */
void Cambia_proprietario(   territori_t territorio,
                            players_t nuovo_proprietario,
                            int nuove_armate = 1)
{
    int i = static_cast<int>(territorio);
    if(plancia.plancia[i].proprietario != nuovo_proprietario){
        plancia.plancia[i].num_armate = 0;
        ALLPLAYERS[nuovo_proprietario].numero_territori++;
    }
    plancia.plancia[i].proprietario = nuovo_proprietario;
    plancia.plancia[i].num_armate += nuove_armate;
};
