package risicammaraTest;

import java.io.Serializable;

/**
 * Enumerato che gestisce le costanti delle notifiche al server.
 * @author stengun
 */
public enum Notify implements Serializable{
    DISCONNESSIONE,
    CONNESSIONE,
    ESPELLI,
    BANNA,
    TERMINA,
    AVVIA;
}
