package risicammara2.global;

import risicammaraServer.messaggiManage.Messaggio;

/**
 * Classe per i messaggi che vengono inviati dal server.
 * Wrapper per indicare semplicemente i destinatari dei messaggi.
 * @author stengun
 */
public class MessaggioInvio {
    private Messaggio msg;
    private long destinatario;

    public MessaggioInvio(Messaggio msg, long destinatario) {
        this.msg = msg;
        this.destinatario = destinatario;
    }

    public long getDestinatario() {
        return destinatario;
    }

    public Messaggio getMsg() {
        return msg;
    }
    
    
}
