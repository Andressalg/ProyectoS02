/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;
import EDD.ListaSimple;

/**
 *
 * @author Andres Salgueiro
 */
public class Disco {
    private int EspacioEnDisco;
    private int blockSize;
    private int BloquesTotal;
    private ListaSimple bloques;
    private int BloquesLibres;
    
public Disco (int EspacioEnDisco, int blockSize) {
        this.EspacioEnDisco = EspacioEnDisco;
        this.blockSize = blockSize;
        this.BloquesTotal = EspacioEnDisco / blockSize;
        this.BloquesLibres = BloquesTotal;
        this.bloques = new ListaSimple();
        initializeBlocks();

private void initializeBlocks() {
        for (int i = 0; i < BloquesTotal; i++) {
            Bloque block = new Bloque(i);
            bloques.insertFinal(block);
        }
    }
    
    public Bloque getBlock(int blockNumber) {
        if (blockNumber >= 0 && blockNumber < BloquesTotal) {
            return (Bloque) bloques.get(blockNumber);
        }
        return null;
    }
    
    public ListaSimple allocateBlocks(int numBlocks, String fileName, String processName) {
        if (numBlocks > BloquesLibres) {
            return null; // No hay suficiente espacio
        }
        
        ListaSimple allocatedBlocks = new ListaSimple();
        int allocated = 0;
        
        for (int i = 0; i < BloquesTotal && allocated < numBlocks; i++) {
            Bloque bloque = (Bloque) bloques.get(i);
            if (!bloque.isOccupied()) {
                bloque.setOccupied(true);
                bloque.setOwnerFile(fileName);
                bloque.setOwnerProcess(processName);
                allocatedBlocks.insertFinal(bloque);
                allocated++;
                BloquesLibres--;
            }
        }
        
        return allocatedBlocks;
    }
    
    public boolean freeBlocks(ListaSimple blocksToFree) {
        for (int i = 0; i < blocksToFree.getSize(); i++) {
            Bloque block = (Bloque) blocksToFree.get(i);
            block.clearBlock();
            BloquesLibres++;
        }
        return true;
    }
    
    public boolean freeBlock(int blockNumber) {
        Bloque bloque = getBlock(blockNumber);
        if (bloque != null && bloque.isOccupied()) {
            bloque.clearBlock();
            BloquesLibres++;
            return true;
        }
        return false;
    }
    
    public int getFreeBlocks() {
        return BloquesLibres;
    }
    
    public int getTotalBlocks() {
        return BloquesTotal;
    }
    
    public int getUsedBlocks() {
        return BloquesTotal - BloquesLibres;
    }
    
    public ListaSimple getAllBlocks() {
        return bloques;
    }
    
    public double getFragmentationPercentage() {
        if (BloquesTotal == 0) return 0;
        return ((double) getUsedBlocks() / BloquesTotal) * 100;
    }

}
