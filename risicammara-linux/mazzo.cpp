
#include <iostream>
#include <cstdlib>

#include "headers/regolestruct.h"
#include "headers/plancia.h"
#include "headers/mazzo.h"
#include "headers/fasi_gioco.h"

using namespace std;
extern tabellone plancia;
extern mazzo maz;
extern giocatore ALLPLAYERS[];
extern unsigned int GIOCATORI;

/**
 * Completa il territorio della carta e il relativo bonus associato.
 * @param d Indice di servizio per il for delle carte.
 */
void mazzo::inserisci_bonus_terr(int d){

    territori_t territorio = static_cast<territori_t>(d);
    deck[d].territorio = territorio;
    
    switch(territorio)
    {
        case Alaska:
            deck[d].bonus = fante;
            break;
        case Territori_del_Nord_Ovest:
            deck[d].bonus = cannone;
            break;
        case Groenlandia:
            deck[d].bonus = cavallo;
            break;
        case Alberta:
            deck[d].bonus = fante;
            break;
        case Ontario:
            deck[d].bonus = cavallo;
            break;
        case Quebec:
            deck[d].bonus = cannone;
            break;
        case Stati_Uniti_Occidentali:
            deck[d].bonus = fante;
            break;
        case Stati_Uniti_Orientali:
            deck[d].bonus = cannone;
            break;
        case America_Centrale:
            deck[d].bonus = cavallo;
            break;
        case Venezuela:
            deck[d].bonus = cannone;
            break;
        case Peru:
            deck[d].bonus = cavallo;
            break;
        case Brasile:
            deck[d].bonus = cannone;
            break;
        case Argentina:
            deck[d].bonus = fante;
            break;
        case Islanda:
            deck[d].bonus = fante;
            break;
        case Scandinavia:
            deck[d].bonus = cannone;
            break;
        case  Gran_Bretagna:
            deck[d].bonus = cavallo;
            break;
        case Europa_Settentrionale:
            deck[d].bonus = cavallo;
            break;
        case Europa_Occidentale:
            deck[d].bonus = fante;
            break;
        case Europa_Meridionale:
            deck[d].bonus = cavallo;
            break;
        case Ucraina:
            deck[d].bonus = cannone;
            break;
                    //Africa
        case Africa_del_Nord:
            deck[d].bonus = fante;
            break;
        case Egitto:
            deck[d].bonus = fante;
            break;
        case Congo:
            deck[d].bonus = cavallo;
            break;
        case Africa_Orientale:
            deck[d].bonus = cannone;
            break;
        case Africa_del_Sud:
            deck[d].bonus = cannone;
            break;
        case Madagascar:
            deck[d].bonus = fante;
            break;
                    //Asia
        case Urali:
            deck[d].bonus = cavallo;
            break;
        case Siberia:
            deck[d].bonus = cannone;
            break;
        case Jacuzia:
            deck[d].bonus = cavallo;
            break;
        case Cita:
            deck[d].bonus = fante;
            break;
        case Kamchatka:
            deck[d].bonus = cavallo;
            break;
        case Giappone:
            deck[d].bonus = fante;
            break;
        case Mongolia:
            deck[d].bonus = cannone;
            break;
        case Afghanistan:
            deck[d].bonus = fante;
            break;
        case Medio_Oriente:
            deck[d].bonus = cannone;
            break;
        case India:
            deck[d].bonus = fante;
            break;
        case Cina:
            deck[d].bonus = cavallo;
            break;
        case Siam:
            deck[d].bonus = cannone;
            break;
                    //Oceania
        case Indonesia:
            deck[d].bonus = cavallo;
            break;
        case Nuova_Guinea:
            deck[d].bonus = cavallo;
            break;
        case Australia_Orientale:
            deck[d].bonus = fante;
            break;
        case Australia_Occidentale:
            deck[d].bonus = cannone;
            break;
         default:
             deck[d].bonus = jolly;
             break;
    }
};

mazzo::mazzo()
{
    reinizio_point = 0;
    inizio = 0;
    fine = MAXCARDS;
    inizio_obj = 0;
    fine_obj = MAX_OBJ_CARDS;
    for(int i=0;i<fine;i++){
        inserisci_bonus_terr(i);
    }
};

mazzo::mazzo(bool first){
    reinizio_point = 0;
    inizio = 0;
    fine = MAXCARDS;
};
mazzo::~mazzo(){
};

void mazzo::mischia(bool first )
{
    int n;
    if(first){
        n = fine - 2;
    }
    else n = fine;
    srand(time(0));  // initialize seed "randomly"

        //--- Shuffle elements by randomly exchanging each with one other.
        for (int i=0; i<n; i++){
            int r = rand()%n;  // generate a random position
            carta_territorio temp = deck[i];
            deck[i] = deck[r];
            deck[r] = temp;
        }
};

void mazzo::mischia_obj(){
    for(int i=0;i<fine_obj;i++) deck_obj[i] = static_cast<obbiettivi_t>(i);
    //mischia il mazzo degli obbiettivi
    for(int i=0;i<fine_obj;i++)
            {
                int r_obj = rand()%fine_obj;
                obbiettivi_t tmp = deck_obj[i];
                deck_obj[i] = deck_obj[r_obj];
                deck_obj[r_obj] = tmp;
            }
};

carta_territorio mazzo::pesca()
{
    carta_territorio pescaggio = deck[inizio];
    if(inizio == fine){
        mischia(false);
        inizio = 0;
    }
    else inizio++;
    return pescaggio;
};

// pescaggio per la prima mano
territori_t mazzo::pesca_first(){
    int iniz = inizio;
    inizio++;
    return deck[iniz].territorio;
};

//Pescaggio obbiettivi
obbiettivi_t mazzo::pesca_obj(){
    int iniz = inizio_obj;
    inizio_obj++;
    return deck_obj[iniz];
};

void mazzo::distribuisci_obj(){
    for(int i=0;i<GIOCATORI;i++) ALLPLAYERS[i].obbiettivo = pesca_obj();
};

void mazzo::distribuisci_territori(){
    int i = GIOCATORI-1;
    // Finche' il segno dell-inizio non arriva all'ultimo slot del mazzo pesca
    //le carte e assegna come proprietario del territorio chi ha pescato la carta.
    //dopodiche' posiziona una armata nel territorio prendendola dalle armate giro
    while(inizio<fine-2){
            territori_t ter = pesca_first();
            plancia.plancia[ter].proprietario = static_cast<players_t>(i);
            plancia.plancia[ter].num_armate  = 1;
            ALLPLAYERS[i].numero_territori  += 1;
            ALLPLAYERS[i].armate_giro       -=1;
            if(i == 0) i = GIOCATORI - 1;
            else i--;
    }
    inizio = 0;
    mischia(false);
};

void mazzo::push(carta_territorio card){
    deck[reinizio_point].bonus = card.bonus;
    deck[reinizio_point].territorio = card.territorio;
    reinizio_point ++;
};