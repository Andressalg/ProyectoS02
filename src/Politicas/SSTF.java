package Politicas;

import Sistema.ManejadorPolitica;
import EDD.ListaSimple;

public class SSTF implements ManejadorPolitica {
    private int currentHead;
    
    public SSTF() {
        this.currentHead = 0;
    }
    
    @Override
    public String getPolicyName() {
        return "SSTF";
    }
    
    @Override
    public int getNextBlock(ListaSimple pendingRequests, int currentHead) {
        if (pendingRequests.isEmpty()) {
            return currentHead;
        }
        
        this.currentHead = currentHead;
        int closestRequest = -1;
        int minDistance = Integer.MAX_VALUE;
        
        // Encontrar el request más cercano a la cabeza actual
        for (int i = 0; i < pendingRequests.getSize(); i++) {
            int request = (int) pendingRequests.get(i);
            int distance = Math.abs(request - currentHead);
            
            if (distance < minDistance) {
                minDistance = distance;
                closestRequest = request;
            }
        }
        
        return closestRequest;
    }
    
    @Override
    public void setCurrentHead(int head) {
        this.currentHead = head;
    }
}