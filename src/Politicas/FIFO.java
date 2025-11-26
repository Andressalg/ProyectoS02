/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Politicas;

/**
 *
 * @author Andres Salgueiro
 */
import EDD.Cola;
import EDD.ListaSimple;
import Sistema.ManejadorPolitica;

public class FIFO implements ManejadorPolitica {
    private Cola requestQueue;
    
    public FIFO() {
        this.requestQueue = new Cola();
    }
    
    @Override
    public String getPolicyName() {
        return "FIFO";
    }
    
    @Override
    public int getNextBlock(ListaSimple pendingRequests, int currentHead) {
        if (pendingRequests.isEmpty()) {
            return currentHead;
        }
        
        // Para FIFO, simplemente retornamos el primer request en la lista
        return (int) pendingRequests.get(0);
    }
    
    @Override
    public void setCurrentHead(int head) {
        // FIFO no necesita trackear la cabeza actual
    }
    
    public void addRequest(int blockNumber) {
        requestQueue.encolar(blockNumber);
    }
    
    public int getNextRequest() {
        if (requestQueue.estaVacia()) {
            return -1;
        }
        return (int) requestQueue.desencolar();
    }
    
    public boolean hasPendingRequests() {
        return !requestQueue.estaVacia();
    }
    
    public int getQueueSize() {
        return requestQueue.getTamano();
    }
}
