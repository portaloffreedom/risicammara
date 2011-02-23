package risicammaraServer.messaggiManage;

import java.util.PriorityQueue;


/**
 * Messaggio che contiene il risultato dei lanci del difensore e dell'attaccante
 * @author stengun
 */
public class MessaggioRisultatoLanci implements Messaggio {
    private PriorityQueue<Integer> lancioAttacco;
    private PriorityQueue<Integer> lancioDifesa;
    private int sender;

    public MessaggioRisultatoLanci(PriorityQueue<Integer> lancioAttacco, PriorityQueue<Integer> lancioDifesa, int sender) {
        this.lancioAttacco = lancioAttacco;
        this.lancioDifesa = lancioDifesa;
        this.sender = sender;
    }
/**
 * Fornisce ad ogni chiamata il risultato di un dado lanciato dall'attaccante.
 * @return se è minore di 0 allora non ci sono più risultati.
 */
    public int getLancioAttacco(){
        Integer result = lancioAttacco.poll();
        if(result == null) result = 1;
        return -result;
    }
/**
 * Fornisce ad ogni chiamata il risultato di un dado lanciato dal difensore.
 * @return se è minore di 0 allora non ci sono più risultati.
 */
    public int getLancioDifesa(){
        Integer result =lancioDifesa.poll();
        if(result == null) result = 1;
        return -result;
    }
    
    public messaggio_t getType() {
        return messaggio_t.RISULTATOLANCI;
    }

    public int getSender() {
        return sender;
    }

}
