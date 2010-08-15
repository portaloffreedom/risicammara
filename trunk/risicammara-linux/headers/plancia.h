/* 
 * File:   plancia.h
 * Author: stengun
 *
 * Created on 25 giugno 2010, 20.46
 */
#ifndef PLANCIA_H
#define	PLANCIA_H

#include <list>
using namespace std;

struct territorio_plancia{
    territori_t         territorio; // probabilmente inutile in quanto il territorio è già identificato dalla posizione dell'array in cui si trova.
    /** Continente a cui appartiene il territorio*/
    continenti_t        continente;
    /** Proprietario a cui appartiene il territorio*/
    players_t           proprietario;
    /** Numero di armate presenti nel territorio.*/
    int                 num_armate;

    /** La lista di tutti i territori adiacenti a questo*/
    list<territori_t>   adiacenze;
    /** Il numero di territori adiacenti a questo (questioni di velocità)*/
    int size_adj_list;
};

class tabellone
{
public:
    territorio_plancia plancia[42];
    tabellone();
private:
    void completa_adiacenze(int d);
};

void Cambia_proprietario(territori_t,players_t,int);
#endif /* PLANCIA_H */
