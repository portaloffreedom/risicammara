/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraServer.messaggiManage;

/**
 *
 * @author stengun
 */
public class MessaggioRisultatoDado implements Messaggio {
    private int lancio;
    private int sender;
    public MessaggioRisultatoDado(int lancio,int sender) {
        this.lancio = lancio;
        this.sender = sender;
    }

    public int getLancio() {
        return lancio;
    }
    
    public messaggio_t getType() {
        return messaggio_t.RISULTATODADO;
    }

    public int getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "Risultato Dado (lanciato da "+sender+") :"+lancio;
    }

}
