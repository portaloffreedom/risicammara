package risicammaraJava.boardManage;

/**
 * Eccezione Sollevata quando l'indice di un territorio non è valido.
 * @author matteo
 */
public class TerritorioNonValido extends Exception {

    /**
     * Messaggio associato a questa eccezione.
     * @param message
     */
    public TerritorioNonValido(String message) {
        super(message);
    }

    @Override
    public String toString(){
        return super.getMessage();
    }

}
