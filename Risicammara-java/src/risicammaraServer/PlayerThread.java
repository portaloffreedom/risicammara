/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer;

/**
 *
 * @author Sten_Gun
 */
public class PlayerThread implements Runnable{
    private boolean stop;
    private boolean sleep;
    public PlayerThread(){
    }
    public void run() {
        this.stop = false;
        this.sleep = false;
    }

}
