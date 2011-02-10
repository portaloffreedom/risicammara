package risicammaraClient;

/**
 * Enumerato che rappresenta tutti i bonus
 * @author stengun
 */
 public enum Bonus_t {
     /** Bonus cannone (tre cannoni uguali = 4 armate in più) */
        CANNONE (4),
     /** Bonus fanti (tre fanti = 6 armate in più)*/
        FANTE   (6),
        /** Bonus cavallo (tre cavalli = 8 armate in più)*/
        CAVALLO (8),
        /** Bonus Jolly. Un jolly e due carte uguale 12 armate in più*/
        JOLLY   (12);

        private int numarm;
        /** Inizializza il valore dell'enumerato in base al valore assegnato*/
        Bonus_t(int numarm){
            this.numarm=numarm;
        };
        /** Restituisce la quantità di armate del tris (di tipo uguale)
         * @param diversi Se le tre carte sono diverse o se sono uguali
         * @return Il valore del bonus corrispondente (territori esclusi)
         */
        public int TrisValue(boolean diversi){
            if(diversi) return 12;
            return numarm;
        }
 }
