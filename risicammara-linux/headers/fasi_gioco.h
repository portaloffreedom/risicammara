/* 
 * File:   fasi_gioco.h
 * Author: stengun
 *
 * Created on 30 giugno 2010, 15.49
 */

#ifndef FASI_GIOCO_H
#define	FASI_GIOCO_H
#include "regolestruct.h"
//Funzione chiamata ad ogni inizio turno (calcola le armate del giocatore corrente)
void inizio_turno();

/*  Aggiunge una armata ad un territorio se il giocatore ha armate da schierare.
    Se la funzione torna 1 allora il giocatore ha terminato l'aggiunta delle armate
    e puo' passare alla fase successiva.
    Se ritorna 0 allora l'armata e' stata aggiunta con successo.
    Se ritorna -1 allora c'e' stato un errore nell'aggiunta dell'armata         */
int rinforzo(territori_t territorio);

//  Se il giocatore corrente ha conquistato un territorio allora pesca una carta.
//  Viene passato cosi' il turno al prossimo giocatore.
void end_phase();

void nuovo_gioco();
#endif	/* FASI_GIOCO_H */

