/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author Andres Salgueiro
 */
public class Nodo {private Object data;
    private Nodo pnext;
    private Nodo pprevious;

    public Nodo(Object data) {
        this.data = data;
        this.pnext = null;
        this.pprevious = null;
    }
    
    public Nodo() {
        this.data = null;
        this.pnext = null;
        this.pprevious = null;
    }
    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Nodo getPnext() {
        return pnext;
    }

    public void setPnext(Nodo pnext) {
        this.pnext = pnext;
    }

    public Nodo getPprevious() {
        return pprevious;
    }

    public void setPprevious(Nodo pprevious) {
        this.pprevious = pprevious;
    }
    
    @Override
    public String toString() {
        return "Nodo{" + "data=" + data + '}';
    }
}
