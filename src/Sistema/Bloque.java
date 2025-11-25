/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

/**
 *
 * @author Andres Salgueiro
 */
public class Bloque {
    private int blockNumber;
    private boolean isOccupied;
    private String ownerFile;
    private String ownerProcess;
    private Object data;
    
    public Bloque(int blockNumber) {
        this.blockNumber = blockNumber;
        this.isOccupied = false;
        this.ownerFile = null;
        this.ownerProcess = null;
        this.data = null;
    }
    
    // Getters
    public int getBlockNumber() {
        return blockNumber;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
    
    public String getOwnerFile() {
        return ownerFile;
    }
    
    public String getOwnerProcess() {
        return ownerProcess;
    }
    
    public Object getData() {
        return data;
    }
    
    // Setters
    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }
    
    public void setOwnerFile(String ownerFile) {
        this.ownerFile = ownerFile;
    }
    
    public void setOwnerProcess(String ownerProcess) {
        this.ownerProcess = ownerProcess;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public void clearBlock() {
        this.isOccupied = false;
        this.ownerFile = null;
        this.ownerProcess = null;
        this.data = null;
    }
    
    @Override
    public String toString() {
        return "Block{" + "number=" + blockNumber + ", occupied=" + isOccupied + 
               ", file=" + ownerFile + ", process=" + ownerProcess + '}';
    }
}