package risicammaraServer.messaggiManage;

import java.util.PriorityQueue;
import risicammaraClient.Client;


/**
 * Messaggio che contiene il risultato dei lanci del difensore e dell'attaccante
 * @author stengun
 */
public class MessaggioRisultatoLanci implements Messaggio {
    private PriorityQueue<Integer> lancioAttacco;
    private PriorityQueue<Integer> lancioDifesa;
    private int attaccante;
    private int difensore;

    /**
     * Costruisce un messaggio per il risultato dei lanci.
     * @param lancioAttacco I lanci dell'attacco
     * @param lancioDifesa i lanci della difesa
     * @param attaccante indice dell'attaccante
     * @param difensore indice del difensore
     */
    public MessaggioRisultatoLanci(PriorityQueue<Integer> lancioAttacco,
            PriorityQueue<Integer> lancioDifesa, int attaccante,int difensore) {
        this.lancioAttacco = lancioAttacco;
        this.lancioDifesa = lancioDifesa;
        this.attaccante = attaccante;
        this.difensore = difensore;
    }
    private int[] getValori(PriorityQueue<Integer> sequenza) {
        int valoreLancio = 0;
        int contatoreLanci = 0;
        int valori[] = new int[3];

        if (Client.DEBUG) System.out.println("Risultato dado :");

       while (true) {
            valoreLancio = this.getLancio(sequenza);

            if (valoreLancio < 0)
                break; //se ha estratto -1 allora ha finito di estrarre

            if (Client.DEBUG) System.out.print(" "+valoreLancio);
            valori[contatoreLanci] = valoreLancio;
            contatoreLanci++;
        }

        if (Client.DEBUG) System.out.println();

        int valoriRitorno[] = new int[contatoreLanci];
        System.arraycopy(valori, 0, valoriRitorno, 0, valoriRitorno.length);

        return valoriRitorno;
    }

    private int getLancio(PriorityQueue<Integer> sequenza) {
        Integer result = sequenza.poll();
        if(result == null) result = 1;
        return -result;
    }

    /**
     * Fornisce ad ogni chiamata il risultato di un dado lanciato dall'attaccante.
     * @return se è minore di 0 allora non ci sono più risultati.
     * @deprecated 
     */
    public int getLancioAttacco(){
        return getLancio(lancioAttacco);
    }

    /**
     * Fornisce ad ogni chiamata il risultato di un dado lanciato dal difensore.
     * @return se è minore di 0 allora non ci sono più risultati.
     * @deprecated
     */
    public int getLancioDifesa(){
        return getLancio(lancioDifesa);
    }

    /**
     * Fornisce l'array dei risultati dell'attaccante
     * @return l'array dimensionato giusto dei dadi tirati dall'attaccante
     */
    public int[] getValoriAttacco(){
        return getValori(lancioAttacco);
    }

    /**
     * Fornisce l'array dei risultati dell'difensore
     * @return l'array dimensionato giusto dei dadi tirati dal difensore
     */
    public int[] getValoriDifesa(){
        return getValori(lancioDifesa);
    }

    /**
     * Restituisce l'attaccante
     * @return l'indice dell'attaccante
     */
    public int getAttaccante() {
        return attaccante;
    }

    /**
     * L'indice del difensore.
     * @return
     */
    public int getDifensore() {
        return difensore;
    }
    
    @Override
    public messaggio_t getType() {
        return messaggio_t.RISULTATOLANCI;
    }

    @Override
    public int getSender() {
        return -1;
    }

}
