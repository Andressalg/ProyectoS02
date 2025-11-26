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
    public static final int NUEVO = 1;
    public static final int LISTO = 2;
    public static final int EJECUTANDO = 3;
    public static final int BLOQUEADO = 4;
    public static final int TERMINADO = 5;
    
    private int idProceso;
    private String nombreProceso;
    private String nombreUsuario;
    private int estado;
    private SolicitudIO solicitudActual;
    private long tiempoCreacion;
    private long tiempoInicio;
    private long tiempoFin;
    
    public Proceso(int idProceso, String nombreProceso, String nombreUsuario) {
        this.idProceso = idProceso;
        this.nombreProceso = nombreProceso;
        this.nombreUsuario = nombreUsuario;
        this.estado = NUEVO;
        this.solicitudActual = null;
        this.tiempoCreacion = System.currentTimeMillis();
        this.tiempoInicio = -1;
        this.tiempoFin = -1;
    }
    
    public int getIdProceso() { return idProceso; }
    public String getNombreProceso() { return nombreProceso; }
    public String getNombreUsuario() { return nombreUsuario; }
    public int getEstado() { return estado; }
    public SolicitudIO getSolicitudActual() { return solicitudActual; }
    public long getTiempoCreacion() { return tiempoCreacion; }
    public long getTiempoInicio() { return tiempoInicio; }
    public long getTiempoFin() { return tiempoFin; }
    
    public void establecerListo() { 
        this.estado = LISTO; 
        if (tiempoInicio == -1) {
            tiempoInicio = System.currentTimeMillis();
        }
    }
    
    public void establecerEjecutando() { 
        this.estado = EJECUTANDO; 
    }
    
    public void establecerBloqueado() { 
        this.estado = BLOQUEADO; 
    }
    
    public void establecerTerminado() { 
        this.estado = TERMINADO; 
        this.tiempoFin = System.currentTimeMillis();
    }
    
    public void establecerSolicitudActual(SolicitudIO solicitud) {
        this.solicitudActual = solicitud;
    }
    
    public String getNombreEstado() {
        switch (estado) {
            case NUEVO: return "NUEVO";
            case LISTO: return "LISTO";
            case EJECUTANDO: return "EJECUTANDO";
            case BLOQUEADO: return "BLOQUEADO";
            case TERMINADO: return "TERMINADO";
            default: return "DESCONOCIDO";
        }
    }
    
    public long getTiempoEjecucion() {
        if (tiempoInicio == -1) return 0;
        if (tiempoFin == -1) return System.currentTimeMillis() - tiempoInicio;
        return tiempoFin - tiempoInicio;
    }
    
    public boolean estaActivo() {
        return estado != TERMINADO;
    }
    
    public boolean puedeSerProgramado() {
        return estado == LISTO || estado == BLOQUEADO;
    }
    
    public String obtenerResumen() {
        return String.format("PID#%d %-15s %-10s %-12s %s", 
            idProceso, nombreProceso, nombreUsuario, getNombreEstado(),
            solicitudActual != null ? solicitudActual.obtenerNombreOperacion() + " " + solicitudActual.getNombreArchivo() : "Sin Solicitud");
    }
}