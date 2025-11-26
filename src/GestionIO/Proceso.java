/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionIO;

/**
 *
 * @author Andres Salgueiro
 */
public class Proceso {
    public static final int NEW = 1;
    public static final int READY = 2;
    public static final int RUNNING = 3;
    public static final int BLOCKED = 4;
    public static final int TERMINATED = 5;
    
    private int processId;
    private String processName;
    private String userName;
    private int state;
    private SolicitudIO currentRequest;
    private long creationTime;
    private long startTime;
    private long endTime;
    
    public Proceso(int processId, String processName, String userName) {
        this.processId = processId;
        this.processName = processName;
        this.userName = userName;
        this.state = NEW;
        this.currentRequest = null;
        this.creationTime = System.currentTimeMillis();
        this.startTime = -1;
        this.endTime = -1;
    }
    
    // Getters
    public int getProcessId() { return processId; }
    public String getProcessName() { return processName; }
    public String getUserName() { return userName; }
    public int getState() { return state; }
    public SolicitudIO getCurrentRequest() { return currentRequest; }
    public long getCreationTime() { return creationTime; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    
    // State management
    public void setReady() { 
        this.state = READY; 
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
    }
    
    public void setRunning() { 
        this.state = RUNNING; 
    }
    
    public void setBlocked() { 
        this.state = BLOCKED; 
    }
    
    public void setTerminated() { 
        this.state = TERMINATED; 
        this.endTime = System.currentTimeMillis();
    }
    
    public void setCurrentRequest(SolicitudIO request) {
        this.currentRequest = request;
    }
    
    public String getStateName() {
        switch (state) {
            case NEW: return "NEW";
            case READY: return "READY";
            case RUNNING: return "RUNNING";
            case BLOCKED: return "BLOCKED";
            case TERMINATED: return "TERMINATED";
            default: return "UNKNOWN";
        }
    }
    
    public long getExecutionTime() {
        if (startTime == -1) return 0;
        if (endTime == -1) return System.currentTimeMillis() - startTime;
        return endTime - startTime;
    }
    
    public boolean isActive() {
        return state != TERMINATED;
    }
    
    public boolean canBeScheduled() {
        return state == READY || state == BLOCKED;
    }
}