
#include "headers/fasi_gioco.h"
#include "headers/regolestruct.h"
#include "headers/plancia.h"
#include "headers/mazzo.h"
#include "headers/giocatori.h"
#include <iostream>
#include <cstdlib>
using namespace std;

const int MAX_NAME_LENGTH   = 20;
extern players_t CURRENTPLAYER;
extern giocatore ALLPLAYERS[];
extern tabellone plancia;
bool CONQUISTATO = false;
extern mazzo maz;
extern unsigned int GIOCATORI;

// INIZIO DEL TURNO
 //aggiunge le armate per i dati continenti
void armate_continenti(continenti_t continente){
    switch(continente){
        case Nord_America:
            ALLPLAYERS[CURRENTPLAYER].armate_giro += 5;
            break;
        case Sud_America:
            ALLPLAYERS[CURRENTPLAYER].armate_giro += 2;
            break;
        case Europa:
            ALLPLAYERS[CURRENTPLAYER].armate_giro += 5;
            break;
        case Africa:
            ALLPLAYERS[CURRENTPLAYER].armate_giro += 3;
            break;
        case Asia:
            ALLPLAYERS[CURRENTPLAYER].armate_giro += 7;
            break;
        case Oceania:
            ALLPLAYERS[CURRENTPLAYER].armate_giro += 2;
            break;
    }
};

//ogni inizio turno vengono calcolate le armate del giocatore corrente.
void inizio_turno(){
    ALLPLAYERS[CURRENTPLAYER].armate_giro = ALLPLAYERS[CURRENTPLAYER].numero_territori / 3;
    //un giocatore che ha meno di 4 territori non puo' avere un continente
    //pertanto si esce direttamente dalla funzione evitando giri inutili.
    if(ALLPLAYERS[CURRENTPLAYER].numero_territori <4) return;
    int re = 0;
    for(int i=0;i<42;i++){
        if(plancia.plancia[i].proprietario != CURRENTPLAYER){
            /*  Se esiste almeno un territorio in cui il proprietario non e'
                quello corrente allora si scala al prossimo blocco di territori
                per il continente successivo, e si ripete il processo
                finche' i territori non sono finiti.*/
            if(i>37 && i<42)    i=41;
            if(i>25 && i<38)    i=37;
            if(i>19 && i<26)    i=25;
            if(i>12 && i<20)    i=19;
            if(i>8  && i<13)    i=12;
            if(i<9)             i=8;
            re = i;
            continue;
        }
        /*  "re" viene incrementato ogni volta che viene trovato un territorio con successo,
            se "re" raggiunge il numero che identifica l'ultimo territorio di un dato continente
            allora vengono aggiunte le armate di quel continente.
            "re" dipende direttamente da i.*/
        re++;
        switch(re){
            case 9:
            case 13:
            case 20:
            case 26:
            case 38:
            case 42:
                armate_continenti(plancia.plancia[i].continente);
            default:
                break;
        }
    }
};

// ---------------------------------------------

//FASE RINFORZO

/*  Funzione di ausilio per la fase di rinforzo(prima fase di gioco)
    Alla funzione viene passato il territorio da rinforzare.
    viene determinato se il territorio appartiene al giocatore corrente
    e in caso affermativo inserisce una armata nel territorio cliccato dal giocatore
    in caso contrario esce restituendo -1. Se le armate a disposizione del giocatore
    finiscono allora la funzione esce ritornando 1. Se l'armata viene inserita con
    successo allora la funzione esce ritornando 0*/

int rinforzo(territori_t territorio){
    int i = static_cast<int>(territorio);
    if( plancia.plancia[i].proprietario != CURRENTPLAYER) return -1;
    if( ALLPLAYERS[CURRENTPLAYER].armate_giro == 0) return 1;
    plancia.plancia[i].num_armate +=1;
    ALLPLAYERS[CURRENTPLAYER].armate_giro--;
    return 0;
};

// ----------------------------------------------
//FASE ATTACCO

/*  Effettua il lancio di un dado. Ritorna il numero ottenuto
 *  con il lancio.*/
int diceroll(){
    srand(time(0));
    return (rand()%6 +1);
};

/* In caso di attacco si dichiara il territorio dal quale attaccare e il territorio
    che si vuole attaccare ( e con quante armate attaccare).
    La funzione effettua un controllo di correttezza sul
    territorio attaccante (deve appartenere al giocatore corrente e deve avere un
    sufficiente numero di armate per poter effettuare un attacco), e un controllo
    sul territorio che si attacca (non deve appartenere al giocatore corrente)
    questa funzione gestisce anche il lancio dei dadi da parte dei due giocatori
    e l'eliminazione delle armate e il passaggio da un territorio all'altro.*/
void attacco(territori_t attacker,territori_t defender);


// -----------------------------------------------------------
//FASE SPOSTAMENTO
void spostamento(territori_t from,territori_t destination,int num_armate);

//--------------------------------------------------------------------------
//FASE FINE TURNO
// se il giocatore corrente ha conquistato un territorio in questo turno allora
// aggiungi una carta al giocatore.
void end_phase(){
    int numplayers = GIOCATORI-1;
    int i = 0;
    if(CURRENTPLAYER != nessuno){
        i = static_cast<int>(CURRENTPLAYER) + 1;
        if(CONQUISTATO){
            CONQUISTATO = false;
            ALLPLAYERS[CURRENTPLAYER].carte_mano.push_front(maz.pesca());
        }
        if(i>numplayers) i = 0;
    }
    CURRENTPLAYER = static_cast<players_t>(i);
};

//Prima mano distrubuisce ai giocatori territori ed obbiettivi
//aggiornando i valori dei territori stessi con i dati necessari.
void nuovo_gioco()
{
#ifdef DEBUG_ROB
    cout<<"inserisci numero di giocatori(maggiore di 3): ";
    cin>>GIOCATORI;
    while(GIOCATORI>6 || GIOCATORI <3) cin>>GIOCATORI;
    cin.ignore(1,'\n');
    numerogiocatori();
    maz.mischia(true);
    for(int i=0;i<GIOCATORI;i++){
    cout<<"inserisci nome giocatore "<<i+1<<": ";
    cin.get(ALLPLAYERS[i].nome,MAX_NAME_LENGTH);
    cin.ignore(1,'\n');
    }
#endif
    maz.~mazzo();
    maz.mischia(true);
    maz.mischia_obj();
    maz.distribuisci_obj();
    maz.distribuisci_territori();
    CURRENTPLAYER = giocatore1;
};

#ifdef DEBUG_ROBz
//  Aggiunta delle tre armate ogni turno (ad inizio gioco)
void trearmate(unsigned int numplayers){
    while(ALLPLAYERS[CURRENTPLAYER].armate_giro != 0){
        int i=0;
        for(int te=rinforzo(territorio);i<3 && (te!=1);)
        {
            if(te < 0) continue;
            i++;
        }
        end_phase(numplayers);
    }
    inizio_turno();
};
#endif
