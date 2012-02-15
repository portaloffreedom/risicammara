package risicammara2.global;

import java.awt.Point;
import java.awt.Rectangle;
import risicammaraClient.territori_t;

/**
 * Interfaccia per oggetti che devono far parte della plancia.
 * @author stengun
 */
public interface Territorio_Plancia {
    public boolean isAdiacent(territori_t terr);
    public void addArmate(int armate);
    public void setArmate(int armate);
    public int getArmate();
    public void setProprietario(long player_id);
    public long getProprietario();
    //lato client
    public void setPosizione(Rectangle rect);
    public Rectangle getPosizione();
    public void setPosizioneBollino(Point punto_bollino);
    public Point getPosizioneBollino();
}
