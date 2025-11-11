package Politicas;

import Sistema.ManejadorPolitica;
import EDD.ListaSimple;

public class CScan implements ManejadorPolitica {
    private int currentHead;
    private int diskSize;
    
    public CScan(int diskSize) {
        this.diskSize = diskSize;
        this.currentHead = 0;
    }
    
    @Override
    public String getPolicyName() {
        return "C-SCAN";
    }
    
    @Override
    public int getNextBlock(ListaSimple pendingRequests, int currentHead) {
        if (pendingRequests.isEmpty()) {
            return currentHead;
        }
        
        this.currentHead = currentHead;
        ListaSimple sortedRequests = sortRequests(pendingRequests);
        
        // Buscar el primer request mayor o igual a la cabeza actual
        int nextBlock = -1;
        for (int i = 0; i < sortedRequests.getSize(); i++) {
            int request = (int) sortedRequests.get(i);
            if (request >= currentHead) {
                nextBlock = request;
                break;
            }
        }
        
        // Si no hay requests mayores, ir al inicio del disco
        if (nextBlock == -1) {
            nextBlock = (int) sortedRequests.get(0); // El más pequeño
        }
        
        return nextBlock;
    }
    
    private ListaSimple sortRequests(ListaSimple requests) {
        // Convertir a array para ordenar
        int[] array = new int[requests.getSize()];
        for (int i = 0; i < requests.getSize(); i++) {
            array[i] = (int) requests.get(i);
        }
        
        // Ordenamiento burbuja
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        
        // Convertir de vuelta a ListaSimple
        ListaSimple sorted = new ListaSimple();
        for (int value : array) {
            sorted.insertFinal(value);
        }
        
        return sorted;
    }
    
    @Override
    public void setCurrentHead(int head) {
        this.currentHead = head;
    }
}