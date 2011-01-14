/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraTest;

import risicammaraClient.Client;
import risicammaraServer.Server;

/**
 *
 * @author matteo
 */
public class TestLocale {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client client = new Client(true, Client.PORT);
        Server server = new Server(Client.PORT);
    }

}
