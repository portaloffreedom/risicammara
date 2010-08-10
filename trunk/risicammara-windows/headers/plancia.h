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
    territori_t         territorio;
    continenti_t        continente;
    players_t           proprietario;
    int                 num_armate;
    list<territori_t>   adiacenze;
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
