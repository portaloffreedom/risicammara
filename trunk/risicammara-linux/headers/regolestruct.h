/* 
 * File:   regolestruct.h
 * Author: stengun
 *
 * Created on 24 giugno 2010, 19.55
 */


//parte per creare un tracing decente
#ifdef DEBUG
#define DBG(A) {A}
#define NDBG(B)
#else
#define DBG(A)
#define NDBG(B) {B}
#endif

#ifndef REGOLESTRUCT_H
#define	REGOLESTRUCT_H

#include <list>
using namespace std;

// Carte massime del mazzo e massimo numero di giocatori lo preleviamo da qui
// questo header racchiude gli struct per questo specifico gioco (risicazzi)
const int MAXCARDS = 44;
const int MAXPLAYERS = 6;
const int MAX_OBJ_CARDS = 14;

enum armate_t { blu,
                rosse,
                nere,
                gialle,
                viola,
                verdi};

enum continenti_t { Nord_America    = 0,
                    Sud_America     = 1,
                    Europa          = 2,
                    Africa          = 3,
                    Asia            = 4,
                    Oceania         = 5
};

enum territori_t {  //Nord America
                    Alaska                      = 0,
                    Territori_del_Nord_Ovest    = 1,
                    Groenlandia                 = 2,
                    Alberta                     = 3,
                    Ontario                     = 4,
                    Quebec                      = 5,
                    Stati_Uniti_Occidentali     = 6,
                    Stati_Uniti_Orientali       = 7,
                    America_Centrale            = 8,
                    //America del sud
                    Venezuela                   = 9,
                    Peru                        = 10,
                    Brasile                     = 11,
                    Argentina                   = 12,
                    //Europa
                    Islanda                     = 13,
                    Scandinavia                 = 14,
                    Gran_Bretagna               = 15,
                    Europa_Settentrionale       = 16,
                    Europa_Occidentale          = 17,
                    Europa_Meridionale          = 18,
                    Ucraina                     = 19,
                    //Africa
                    Africa_del_Nord             = 20,
                    Egitto                      = 21,
                    Congo                       = 22,
                    Africa_Orientale            = 23,
                    Africa_del_Sud              = 24,
                    Madagascar                  = 25,
                    //Asia
                    Urali                       = 26,
                    Siberia                     = 27,
                    Jacuzia                     = 28,
                    Cita                        = 29,
                    Kamchatka                   = 30,
                    Giappone                    = 31,
                    Mongolia                    = 32,
                    Afghanistan                 = 33,
                    Medio_Oriente               = 34,
                    India                       = 35,
                    Cina                        = 36,
                    Siam                        = 37,
                    //Oceania
                    Indonesia                   = 38,
                    Nuova_Guinea                = 39,
                    Australia_Orientale         = 40,
                    Australia_Occidentale       = 41,
                    //carte jolly
                    Jolly1                      = 42,
                    Jolly2                      = 43
};

enum obbiettivi_t { Elimina_Blu              = 0,
                    Elimina_Rosso            = 1,
                    Elimina_Giallo           = 2,
                    Elimina_Viola            = 3,
                    Elimina_Nero             = 4,
                    Elimina_Verde            = 5,
                    Ventiquattro_Territori   = 6,
                    Diciotto_Territori       = 7,
                    Nord_America_Oceania     = 8,
                    Nord_America_Africa      = 9,
                    Asia_Sud_America         = 10,
                    Asia_Africa              = 11,
                    Europa_Sud_America_terzo = 12,
                    Europa_Oceania_terzo     = 13,
};

enum bonus_t {  cannone = 0,
                fante   = 1,
                cavallo = 2,
                jolly   = 3
};

enum players_t {    giocatore1  = 0,
                    giocatore2  = 1,
                    giocatore3  = 2,
                    giocatore4  = 3,
                    giocatore5  = 4,
                    giocatore6  = 5,
                    nessuno     = 6,
};

struct carta_territorio{
    territori_t territorio;
    bonus_t     bonus;
};

struct giocatore{
    /** Se il giocatore è in gioco o meno.*/
    bool                    isActive;
    /** Le carte in mano di ogni giocatore*/
    list<carta_territorio>  carte_mano;
    /** Numero di carte in mano (questioni di performance)*/
    int num_carte_mano;
    /** Obbiettivo assegnato al giocatore */
    obbiettivi_t            obbiettivo;
    /** Numero di Territori in possesso del giocatore */
    int                     numero_territori;
    /** Continenti sotto il controllo del giocatore*/
    list<continenti_t>      continenti;
    /** Numero dei continenti */
    int numero_continenti;
    /** Numero di Armate a giro che il giocatore deve posizionare*/
    int                     armate_giro;
    /** Nome del giocatore*/
    char                    nome[21];
    /** Colore delle armate da lui scelte*/
    armate_t                colore;
};
#endif	/* REGOLESTRUCT_H */

