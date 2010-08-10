
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

mazzo::mazzo()
{
    inizio = 0;
    fine = MAXCARDS;
    inizio_obj = 0;
    fine_obj = MAX_OBJ_CARDS;
    for(int i=0;i<fine;i++){
        deck[i].bonus = cannone;
        deck[i].territorio = static_cast<territori_t>(i);
    }
};

mazzo::~mazzo(){
    inizio = 0;
    fine = MAXCARDS;
    inizio_obj = 0;
    fine_obj = MAX_OBJ_CARDS;
    for(int i=0;i<fine;i++){
        deck[i].bonus = cannone;
        deck[i].territorio = static_cast<territori_t>(i);
    }
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
