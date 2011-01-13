/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package risicammaraJava.turnManage;

/**
 *
 * @author Sten_Gun
 */
public enum Fasi_t {
    RINFORZO    (false,false),
    ATTACCO     (true, false),
    SPOSTAMENTO (true, true),
    FINETURNO   (false, true);
    boolean isoptional;
    boolean mustend;
    Fasi_t(boolean is ,boolean mustend){
        this.isoptional = is;
        this.mustend = mustend;
    }

    public boolean isOptional(){
        return isoptional;
    }

    public boolean mustEnd(){
        return mustend;
    }

}
