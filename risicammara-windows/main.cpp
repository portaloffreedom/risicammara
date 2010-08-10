/** Main per provare le funzioni di mazzo
 */
#include <cstdlib>
#include <iostream>
#include "headers/regolestruct.h"
#include "headers/mazzo.h"
#include "headers/plancia.h"
#include "headers/giocatori.h"
#include "headers/fasi_gioco.h"
using namespace std;

giocatore ALLPLAYERS[MAXPLAYERS];
players_t CURRENTPLAYER     = nessuno;
tabellone plancia;
mazzo maz;
unsigned int GIOCATORI = 4;

int parte_opengl(int &argc, char **argv);

int main(int argc, char** argv)
{
#ifdef DEBUG
#ifdef DEBUG_ROB

#endif

    char scelta;
    cout<<  "Che interfaccia vuoi provare?"
            "- (O)penGL\n"
            "- (G)TK\n"
            "- G(l)ade\n"
            "scelta:";
    cin>>scelta;
    switch(scelta){
        case 'o':
        case 'O':
            cout<<"-------------------------------------\n"
                "COMANDI:\n"
                "f -> qualità immagine\n"
                "l -> luci\n"
                "freccie -> movimento\n"
                "ESC -> esci\n"
              "-------------------------------------\n";
        case 'g':
        case 'G':
        case 'l':
        case 'L':
            cout<<"fillo metti la tua interfaccia qui\n";
            return 0;
        default:
            cout<<"Input non accetto\n";
    }

#endif
    return 13;
};
