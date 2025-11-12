/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionIO;

import Enums.EstadosProceso;
import Enums.Politica;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Andres Salgueiro
 */
public class ManejadorProcesos {
    
    
    // Colas de procesos 
    private ProcessHeap readyQueue;
    private final CustomList<Proceso> blockedQueue;
    private final CustomList<Proceso> blockedSuspendedQueue; 
    private final CustomList<Proceso> readySuspendedQueue;
    private final CustomList<Proceso> terminatedProcesses;
    private final CustomList<Proceso> newQueue;
    private int maxMultiprogrammingLevel = 5; 
    
    // estado de la sim
    private Proceso currentProcess;
    private Politica currentAlgorithm;
    private int timeQuantum;
    private int currentQuantum;
    private long globalCycle;
    private volatile boolean isOperatingSystemRunning; 
    private final ReentrantLock mutex;
    private volatile boolean isCpuIdle; 
    private volatile int cycleDuration = 1000;
    private final int totalMemory;
    private int usedMemory;
    private Proceso justSuspended = null;
    
    private Thread simulationThread;
    private ExceptionHandlerThread exceptionHandlerThread;

    private volatile Object[] readyQueueCache = new Object[0];
    private volatile CustomList<Proceso> blockedQueueCache = new CustomList<>();
    private volatile CustomList<Proceso> blockedSuspendedQueueCache = new CustomList<>();
    private volatile CustomList<Proceso> readySuspendedQueueCache = new CustomList<>();
    private volatile CustomList<Proceso> terminatedQueueCache = new CustomList<>();
    private volatile CustomList<Proceso> newQueueCache = new CustomList<>();
    
    // Métricas 
    private int completedProcesses;
    private long totalCpuBusyTime;
    private long totalWaitTime;
    private long totalResponseTime;
    private final long startTime;
    private CustomList<Integer> cpuUsageHistory = new CustomList<>(); 
    private CustomList<Integer> globalCycleHistory = new CustomList<>();
    
    
    private volatile CustomList<Integer> cpuUsageHistoryCache = new CustomList<>();
    private volatile CustomList<Integer> globalCycleHistoryCache = new CustomList<>();
    private CustomList<Integer> terminatedHistory = new CustomList<>();
    private volatile CustomList<Integer> terminatedHistoryCache;

    public ManejadorProcesos(SimulationConfig config) {
        this.newQueue = new CustomList<>();
        this.usedMemory = 0;
        this.readyQueue = new ProcessHeap(100, config.getStartAlgorithm()); 
        this.blockedQueue = new CustomList<>();
        this.blockedSuspendedQueue = new CustomList<>();
        this.readySuspendedQueue = new CustomList<>();
        this.terminatedProcesses = new CustomList<>();
        this.currentAlgorithm = config.getStartAlgorithm(); 
        this.timeQuantum = 4;
        this.mutex = new ReentrantLock();
        this.isOperatingSystemRunning = false;
        this.isCpuIdle = true;
        this.startTime = System.currentTimeMillis();

        this.totalMemory = config.getTotalMemory(); 

        this.cycleDuration = config.getInitialCycleDuration(); 

        this.cpuUsageHistoryCache = new CustomList<>();
        this.globalCycleHistoryCache = new CustomList<>();
        this.terminatedHistoryCache = new CustomList<>();
        this.blockedSuspendedQueueCache = new CustomList<>();
        this.readySuspendedQueueCache = new CustomList<>();
    }
    
    /**
     * Inicia el hilo de simulación y el hilo de excepciones.
     */
    public void start() {
        mutex.lock();
        try {
            this.isOperatingSystemRunning = true;
            
            cpuUsageHistory.clear();
            globalCycleHistory.clear();
            cpuUsageHistoryCache.clear();
            globalCycleHistoryCache.clear();
            terminatedHistory.clear(); 
            terminatedHistoryCache.clear(); 
            
            if (this.exceptionHandlerThread == null || !this.exceptionHandlerThread.isAlive()) {
                this.exceptionHandlerThread = new ExceptionHandlerThread(this);
                this.exceptionHandlerThread.start();
            }
            
            if (this.simulationThread == null || !this.simulationThread.isAlive()) {
                this.simulationThread = new Thread(this);
                this.simulationThread.setName("SchedulerThread");
                this.simulationThread.start();
            }
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Bucle principal del hilo de simulación.
     */
    public void run() {
        while (isOperatingSystemRunning) {
            try {
                executeCycle(); 
                
                Thread.sleep(this.cycleDuration); 
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                break; 
            } catch (Exception e) {
                System.err.println("Error en el bucle de simulación: " + e.getMessage());
            }
        }
        System.out.println("Hilo del Scheduler detenido.");
    }

    /**
     * Detiene el planificador y sus hilos asociados.
     */
    public void shutdown() {
        this.isOperatingSystemRunning = false; 
        cpuUsageHistory.clear();
        globalCycleHistory.clear();
        cpuUsageHistoryCache.clear();
        globalCycleHistoryCache.clear();
        terminatedHistory.clear(); 
        terminatedHistoryCache.clear(); 
        
        if (this.exceptionHandlerThread != null) {
            this.exceptionHandlerThread.stopHandler();
        }
        
        if (this.simulationThread != null) {
            this.simulationThread.interrupt(); 
        }
    }

    /**
     * Ciclo principal de ejecución.
     * sección crítica.
     */
    public void executeCycle() {
        mutex.lock();
        try {
            if (!isOperatingSystemRunning) {
                return;
            }
            globalCycle++;
            
            globalCycleHistory.add((int)globalCycle);
            cpuUsageHistory.add(this.isCpuIdle ? 0 : 1);
            terminatedHistory.add(terminatedProcesses.size());
            
            resumeSuspendedProcesses();
            longTermScheduler();
            mediumTermScheduler();
            
            this.isCpuIdle = (currentProcess == null);
            
            
            if (currentProcess == null || currentProcess.getState() != EstadosProceso.EJECUTANDO || 
                (currentAlgorithm == Politica.RR && currentQuantum >= timeQuantum)) {
                scheduleNextProcess();
            }
            
            if (currentProcess != null && currentProcess.getState() == EstadosProceso.EJECUTANDO ) {
                executeCurrentProcess();
            }
            
            updateMetrics();
            updateGUICache();
            this.justSuspended = null;
            
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Agrega un nuevo proceso a la cola de listos 
     */
    public void addProcess(Proceso process) {
        mutex.lock();
        try {
            newQueue.add(process);

            this.newQueueCache = createSnapshot(newQueue);
            
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Desbloquea un proceso 
     */
    public void unblockProcess(Proceso process) {
        mutex.lock();
        try {
            boolean removedFromBlocked = blockedQueue.remove(process);
            
            if (removedFromBlocked) {
                process.setState(EstadosProceso.LISTO);
                process.setLastReadyQueueTime(globalCycle);
                readyQueue.insert(process);
                
                this.readyQueueCache = readyQueue.toArray();
                this.blockedQueueCache = createSnapshot(blockedQueue);
                
            } else {
                boolean removedFromSuspended = blockedSuspendedQueue.remove(process);
                
                if (removedFromSuspended) {
                    process.setState(EstadosProceso.SUSPENDED); 
                    readySuspendedQueue.add(process); 
                    
                    this.blockedSuspendedQueueCache = createSnapshot(blockedSuspendedQueue);
                    this.readySuspendedQueueCache = createSnapshot(readySuspendedQueue);
                    System.out.println("Kernel: E/S de " + process.getName() + " terminó (en Suspensión). Moviendo a Ready, Suspended.");
                } else {
                    System.err.println("WARN: unblockProcess no encontró a " + process.getName() + " ni en BLOCKED ni en BLOCKED_SUSPENDED.");
                }
            }
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Cambia el algoritmo de planificación
     */
    public void setSchedulingAlgorithm(Politica algorithm) {
        mutex.lock();
        try {
            this.currentAlgorithm = algorithm;
            ProcessHeap newReadyQueue = new ProcessHeap(100, this.currentAlgorithm);
            
            while (!this.readyQueue.isEmpty()) {
                newReadyQueue.insert(this.readyQueue.extract()); 
            }
            this.readyQueue = newReadyQueue;
            
            this.readyQueueCache = readyQueue.toArray();
            
        } finally {
            mutex.unlock();
        }
    }

    
    /**
     * Planificador de Largo Plazo (Long-Term Scheduler).
     * proceso de NEW a READY.
     */
    private void longTermScheduler() {
        if (!readySuspendedQueue.isEmpty()) {
            return; 
        }
        // ---

        if (!newQueue.isEmpty()) {
            Proceso processToAdmit = newQueue.get(0);
            if (usedMemory + processToAdmit.getMemorySize() <= totalMemory) {
                // ... (el resto del método es igual)
                Proceso process = newQueue.removeAt(0);
                process.setState(EstadosProceso.LISTO);
                process.setLastReadyQueueTime(globalCycle); 
                readyQueue.insert(process);
                usedMemory += process.getMemorySize();
                System.out.println("LTS: Proceso " + process.getName() + " admitido a READY. (Memoria: " + usedMemory + "/" + totalMemory + ")");
            }
        }
    }
    
    /**
    * Planificador de Mediano Plazo (Medium-Term Scheduler).
    * suspender un proceso para liberar memoria.
    */
    private void mediumTermScheduler() {
        if (newQueue.isEmpty()) {
            return; 
        }

        Proceso nextNewProcess = newQueue.get(0);
        int availableMemory = totalMemory - usedMemory;

        if (nextNewProcess.getMemorySize() > availableMemory && !blockedQueue.isEmpty()) {

            Proceso processToSuspend = blockedQueue.removeAt(0);
            this.blockedQueueCache = createSnapshot(blockedQueue);

            processToSuspend.setState(EstadosProceso.SUSPENDED);
            blockedSuspendedQueue.add(processToSuspend); 
            this.justSuspended = processToSuspend;
            this.justSuspended = processToSuspend;

            usedMemory -= processToSuspend.getMemorySize();

            System.out.println("MTS: Proceso " + processToSuspend.getName() + " SUSPENDIDO (desde BLOQUEADO). (Memoria: " + usedMemory + "/" + totalMemory + ")");

            this.blockedSuspendedQueueCache = createSnapshot(blockedSuspendedQueue);
        }
    }

    private void scheduleNextProcess() {
        if (currentProcess != null && currentProcess.getState() == EstadosProceso.EJECUTANDO) {
            currentProcess.setState(EstadosProceso.LISTO);
            currentProcess.setLastReadyQueueTime(globalCycle);
            readyQueue.insert(currentProcess);
        }
        
        if (!readyQueue.isEmpty()) {
            currentProcess = readyQueue.extract();
            currentProcess.setState(EstadosProceso.EJECUTANDO);
            currentQuantum = 0;
            if (currentProcess.getResponseTime() == -1) {
                currentProcess.setResponseTime(globalCycle);
            }
        } else {
            currentProcess = null;
            this.isCpuIdle = true;
        }
    }
    
    private void executeCurrentProcess() {
        totalCpuBusyTime++;
        this.isCpuIdle = false;
        currentProcess.executeInstruction(); 
        currentQuantum++;
        
        if (currentProcess.getState() == EstadosProceso.TERMINADO) {
            currentProcess.setTurnaroundTime(globalCycle);
            terminatedProcesses.add(currentProcess);
            completedProcesses++;
            usedMemory -= currentProcess.getMemorySize(); 
            System.out.println("Kernel: Proceso " + currentProcess.getName() + " TERMINADO. (Memoria: " + usedMemory + "/" + totalMemory + ")");
            currentProcess = null;
            this.isCpuIdle = true;
        } else if (currentProcess.getState() == EstadosProceso.BLOQUEADO) {
            blockedQueue.add(currentProcess);
            currentProcess = null;
            this.isCpuIdle = true;
        }
    }
    
    private void updateMetrics() {
        Object[] readyProcesses = readyQueue.toArray(); 
        for (Object obj : readyProcesses) {
            if (obj instanceof Proceso) {
                ((Proceso) obj).incrementWaitingTime();
                totalWaitTime++;
            }
        }
    }
    
    private void resumeSuspendedProcesses() {
        if (readySuspendedQueue.isEmpty()) {
            return; 
        }

        Proceso processToResume = readySuspendedQueue.get(0);

        if (usedMemory + processToResume.getMemorySize() <= totalMemory) {
            readySuspendedQueue.removeAt(0); 
            
            processToResume.setState(EstadosProceso.LISTO);
            processToResume.setLastReadyQueueTime(globalCycle);
            readyQueue.insert(processToResume);
            
            usedMemory += processToResume.getMemorySize();
            
            System.out.println("MTS: Proceso " + processToResume.getName() + " REANUDADO a READY. (Memoria: " + usedMemory + "/" + totalMemory + ")");

            this.readySuspendedQueueCache = createSnapshot(readySuspendedQueue);
        }
    }

    private void updateGUICache() {
        this.newQueueCache = createSnapshot(newQueue);
        this.readyQueueCache = readyQueue.toArray();
        this.blockedQueueCache = createSnapshot(blockedQueue);
        this.blockedSuspendedQueueCache = createSnapshot(blockedSuspendedQueue);
        this.readySuspendedQueueCache = createSnapshot(readySuspendedQueue);
        this.terminatedQueueCache = createSnapshot(terminatedProcesses);
        this.cpuUsageHistoryCache = createSnapshot(cpuUsageHistory);
        this.globalCycleHistoryCache = createSnapshot(globalCycleHistory);
        this.terminatedHistoryCache = createSnapshot(terminatedHistory);
    }


    public CustomList<Proceso> getNewQueueSnapshot() {
        return newQueueCache; 
    }
    
    public Object[] getReadyQueueSnapshot() {
        return readyQueueCache; 
    }

    public CustomList<Proceso> getBlockedQueueSnapshot() {
        return blockedQueueCache; 
    }

    public CustomList<Proceso> getBlockedSuspendedQueueSnapshot() {
        return blockedSuspendedQueueCache;
    }
    
    public CustomList<Proceso> getReadySuspendedQueueSnapshot() {
        return readySuspendedQueueCache;
    }

    public CustomList<Proceso> getTerminatedQueueSnapshot() {
        return terminatedQueueCache; 
    }

    public Proceso getCurrentProcessSnapshot() {
        mutex.lock();
        try { return currentProcess; }
        finally { mutex.unlock(); }
    }

    public long getGlobalCycleSnapshot() {
        mutex.lock();
        try { return globalCycle; }
        finally { mutex.unlock(); }
    }
    
    public boolean getIsOperatingSystemRunningSnapshot() {
        return isOperatingSystemRunning;
    }
    
    public boolean getIsCpuIdleSnapshot() {
        return isCpuIdle; 
    }
    
    public Politica getAlgorithmSnapshot() {
        mutex.lock();
        try { return currentAlgorithm; }
        finally { mutex.unlock(); }
    }
    
    public Map<String, Double> getPerformanceMetricsSnapshot() {
        mutex.lock();
        try {
            Map<String, Double> metrics = new HashMap<>();
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            double throughput = elapsedTime > 0 ? (double) completedProcesses / elapsedTime : 0;
            double cpuUtilization = globalCycle > 0 ? (double) totalCpuBusyTime / globalCycle : 0;
            int totalProcesses = completedProcesses + readyQueue.size() + blockedQueue.size() + blockedSuspendedQueue.size() + readySuspendedQueue.size();
            if (currentProcess != null) totalProcesses++;
            double avgWaitTime = totalProcesses > 0 ? (double) totalWaitTime / totalProcesses : 0;
            double avgResponseTime = completedProcesses > 0 ? (double) totalResponseTime / completedProcesses : 0;
            metrics.put("Throughput", throughput);
            metrics.put("CPU_Utilization", cpuUtilization);
            metrics.put("Avg_Wait_Time", avgWaitTime);
            metrics.put("Avg_Response_Time", avgResponseTime);
            return metrics;
        } finally {
            mutex.unlock();
        }
    }
    
    public CustomList<Integer> getCpuUsageHistory() {
        return cpuUsageHistoryCache; 
    }
    
    public CustomList<Integer> getGlobalCycleHistory() {
        return globalCycleHistoryCache; 
    }
    
    private <T> CustomList<T> createSnapshot(CustomList<T> original) {
        CustomList<T> snapshot = new CustomList<>();
        for (int i = 0; i < original.size(); i++) {
            snapshot.add(original.get(i));
        }
        return snapshot;
    }
    
    public CustomList<Integer> getTerminatedHistory() {
        return terminatedHistoryCache; 
    }
    
    
    public void setCycleDuration(int duration) {
        this.cycleDuration = duration; 
    }
    
    public int getCycleDuration() {
        return cycleDuration;
    }
}
