/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.boardManage;

/**
 *
 * @author matteo
 */
public class TerritorioNonTrovatoException extends Exception {

    public TerritorioNonTrovatoException(String message) {
        super(message);
    }

    @Override
    public String toString(){
        return super.getMessage();
    }

}
