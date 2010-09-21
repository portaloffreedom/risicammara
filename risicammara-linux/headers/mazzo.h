/* 
 * File:   mazzo.h
 * Author: stengun
 *
 * Created on 30 giugno 2010, 11.13
 */

#ifndef MAZZO_H
#define	MAZZO_H

#include "regolestruct.h"
#include "plancia.h"

class mazzo
{
public:
    mazzo();
    mazzo(bool);
    ~mazzo();
    void mischia(bool first = true);
    void mischia_obj();
    void distribuisci_obj();
    void distribuisci_territori();
    void push(carta_territorio);
    carta_territorio pesca();
    territori_t pesca_first();
    obbiettivi_t pesca_obj();
private:
    //globali private
    int reinizio_point;
    int inizio;
    int fine;
    int new_fine;
    int inizio_obj;
    int fine_obj;
    carta_territorio deck[MAXCARDS];
    obbiettivi_t deck_obj[MAX_OBJ_CARDS];

    void inserisci_bonus_terr(int d);
    //Funzioni  private
};


#endif	/* MAZZO_H */

