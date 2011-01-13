package risicammaraClient;

/**
 * Enumerato che rappresenta tutti i bonus
 * @author stengun
 */
 public enum Bonus_t {
        CANNONE (4),
        FANTE   (6),
        CAVALLO (8),
        JOLLY   (12);
        int numarm;
        Bonus_t(int numarm){
            this.numarm=numarm;
        };
        int TrisValue(boolean diversi){
            if(diversi) return 12;
            return numarm;
        }
 }
