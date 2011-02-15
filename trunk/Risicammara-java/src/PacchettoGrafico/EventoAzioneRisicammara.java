/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PacchettoGrafico;

import java.awt.Point;
import java.awt.event.ActionEvent;

/**
 * Evento interno della interfaccia Risicammara
 * @author matteo
 */
public class EventoAzioneRisicammara extends ActionEvent {
    private Point punto;

    /**
     * Costruttore
     * @param source sorgente che ha generato l'evento
     * @param id id
     * @param command Stringa che identifica l'evento
     * @param punto posizione dell'evento
     */
    public EventoAzioneRisicammara(Object source, int id, String command, Point punto) {
        super(source, id, command);
        this.punto = punto;
    }

    /**
     * Preleva la posizione dell'evento.
     * @return punto dove si è verificato l'evento
     */
    public Point getPoint(){
        return punto;
    }

    /**
     * Preleva l'ascissa della posizione dell'evento
     * @return intero reppresentante l'ascissa della posizione dell'evento
     */
    public int getX(){
        return punto.x;
    }

    /**
     * Preleva l'ordinata della posizione dell'evento
     * @return intero reppresentante l'ordinata della posizione dell'evento
     */
    public int getY(){
        return punto.y;
    }

}
