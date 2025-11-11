package EDD;

import EDD.Nodo;

/**
 *
 * @author Buste
 */
public class Cola {
    private Nodo frente;
    private Nodo finalCola;
    private int tamano;

    // Constructor de la Cola
    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.tamano = 0;
    }
    
    public boolean estaVacia() {
        return frente == null;
    }

    public void encolar(Object elemento) {
        Nodo nuevoNodo = new Nodo(elemento);

        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            finalCola.setPnext(nuevoNodo);
        }
        finalCola = nuevoNodo;
        tamano++;
    }


    public Object desencolar() {
        if (estaVacia()) {
            System.out.println("Error: La cola está vacía, no se puede desencolar.");
            return null;
        }

        Object datoDesencolado = frente.getData();
        frente = frente.getPnext();
        
        if (frente == null) {
            finalCola = null;
        }
        
        tamano--;
        System.out.println("Desencolado: " + datoDesencolado);
        return datoDesencolado;
    }


    public Object verFrente() {
        if (estaVacia()) {
            System.out.println("Error: La cola está vacía, no hay elemento en el frente.");
            return null;
        }
        return frente.getData();
    }

    public Object verFinal() {
        if (estaVacia()) {
            System.out.println("Error: La cola está vacía, no hay elemento al final.");
            return null; // O lanzar una excepción
        }
        return finalCola.getData();
    }

    public int getTamano() {
        return tamano;
    }

    public void imprimirCola() {
        if (estaVacia()) {
            System.out.println("Cola: [Vacía]");
            return;
        }
        System.out.print("Cola: [Frente -> ");
        Nodo actual = frente;
        while (actual != null) {
            System.out.print(actual.getData());
            if (actual.getPnext() != null) {
                System.out.print(" -> ");
            }
            actual = actual.getPnext();
        }
        System.out.println(" <- Final]");
}

}
