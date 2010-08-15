#include <iostream>
#include "headers/regolestruct.h"
using namespace std;
extern giocatore ALLPLAYERS[];
extern unsigned int GIOCATORI;


// Prepara il numero di giocatori per l-inizio della partita
void numerogiocatori()
{
    for(int i=0;i<MAXPLAYERS;i++){
        if(i>=GIOCATORI) ALLPLAYERS[i].isActive    = false;
        else ALLPLAYERS[i].isActive                 = true;

        ALLPLAYERS[i].numero_territori              = 0;
        switch(GIOCATORI){
            default:
            case 3:
                ALLPLAYERS[i].armate_giro                   = 35;
                break;
            case 4:
                ALLPLAYERS[i].armate_giro                   = 30;
                break;
            case 5:
                ALLPLAYERS[i].armate_giro                   = 25;
                break;
            case 6:
                ALLPLAYERS[i].armate_giro                   = 20;
                break;
        }
    }
};
