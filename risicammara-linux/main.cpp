/** Main per provare le funzioni di mazzo
 */
#include <cstdlib>
#include <iostream>
#include "headers/main.h"

#ifdef DEBUG
#include "headers/debug_console_messages.h"
#endif
using namespace std;

giocatore ALLPLAYERS[MAXPLAYERS];
players_t CURRENTPLAYER     = nessuno;
tabellone plancia;
mazzo maz;
unsigned int GIOCATORI = 4;

int main(int argc, char** argv)
{
#ifdef DEBUG

    char scelta;
    cout<<  "Che interfaccia vuoi provare?"
            "- (O)penGL\n"
            "- (G)TK\n"
            "- G(l)ade\n"
#ifdef DEBUG
            "- (C)ommand line\n"
#endif
            "scelta:";
    cin>>scelta;
    switch(scelta){
        case 'o':
        case 'O':
        case 'g':
        case 'G':
        case 'l':
        case 'L':
            cout<<"TO DO!\n";
            break;
#ifdef DEBUG
        case 'c':
        case 'C':
            break;
#endif

        default:
            cout<<"Input non accetto\n";
    }

#endif
    return 0;
};
