/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

import EDD.Nodo;

/**
 *
 * @author Ada y Day
 */
public class Pila {
    
    public Nodo peak;
    public int tamano;

    public Pila(){
        this.peak = null;
        this.tamano =0;
        
    
    }
    public boolean isEmpty(){
        return peak == null; 
    }
    
    public void apilar(Object nuevoDato) {
        Nodo nuevoNodo = new Nodo(nuevoDato);
        nuevoNodo.setPnext(peak);
        peak = nuevoNodo;
        tamano++;
        System.out.println("Apilado: " + nuevoDato);
    }

    // POP
    public Object desapilar() {
        if (isEmpty()) {
            System.out.println("Error: La pila está vacía, no se puede desapilar.");
            return null;
        }
        Object datoDesapilado = peak.getData();
        peak = peak.getPnext();
        tamano--;
        System.out.println("Desapilado: " + datoDesapilado);
        return datoDesapilado;
    }

    // PEEK
    public Object verCima() {
        if (isEmpty()) {
            System.out.println("Error: La pila está vacía, no hay elemento en la cima.");
            return null;
        }
        return peak.getData(); // Obtiene el dato de la cima sin eliminarlo
    }

    public int getTamano() { 
        return tamano;
    }

    public void imprimirPila() {
        if (isEmpty()) {
            System.out.println("Pila: [Vacía]");
            return;
        }
        System.out.print("Pila: [");
        Nodo actual = peak;
        while (actual != null) { // Ciclo while recorriendo los nodos
            System.out.print(actual.getData()); // Usa getData()
            if (actual.getPnext() != null) { // Usa getPnext()
                System.out.print(" -> ");
            }
            actual = actual.getPnext(); // Avanza al siguiente nodo usando getPnext()
        }
        System.out.println("] (Cima)");
    }
}
