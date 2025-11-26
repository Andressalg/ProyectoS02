/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionIO;

import EDD.Cola;
import Enums.EstadosProceso;
import Usuario.Usuario;

/**
 *
 * @author Andres Salgueiro
 */
public class Proceso {
    private int idProceso;
    private String nombreProceso;
    private Usuario usuario;
    private EstadosProceso estado;
    private SolicitudIO solicitudActual;
    private long tiempoCreacion;
    private long tiempoInicio;
    private long tiempoFin;
    private Cola<SolicitudIO> solicitudesPendientes;

    public Proceso(int idProceso, String nombreProceso, Usuario usuario) {
        this.idProceso = idProceso;
        this.nombreProceso = nombreProceso;
        this.usuario = usuario;
        this.estado = EstadosProceso.NUEVO;
        this.solicitudActual = null;
        this.tiempoCreacion = System.currentTimeMillis();
        this.tiempoInicio = -1;
        this.tiempoFin = -1;
        this.solicitudesPendientes = new Cola<>();
    }

    // Getters
    public int getIdProceso() { return idProceso; }
    public String getNombreProceso() { return nombreProceso; }
    public Usuario getUsuario() { return usuario; }
    public EstadosProceso getEstado() { return estado; }
    public SolicitudIO getSolicitudActual() { return solicitudActual; }
    public long getTiempoCreacion() { return tiempoCreacion; }
    public long getTiempoInicio() { return tiempoInicio; }
    public long getTiempoFin() { return tiempoFin; }
    public Cola<SolicitudIO> getSolicitudesPendientes() { return solicitudesPendientes; }

    // Gestión de estados
    public void establecerListo() { 
        this.estado = EstadosProceso.LISTO; 
        if (tiempoInicio == -1) {
            tiempoInicio = System.currentTimeMillis();
        }
    }
    
    public void establecerEjecutando() { 
        this.estado = EstadosProceso.EJECUTANDO; 
    }
    
    public void establecerBloqueado() { 
        this.estado = EstadosProceso.BLOQUEADO; 
    }
    
    public void establecerTerminado() { 
        this.estado = EstadosProceso.TERMINADO; 
        this.tiempoFin = System.currentTimeMillis();
    }

    public void establecerSolicitudActual(SolicitudIO solicitud) {
        this.solicitudActual = solicitud;
    }

    // Métodos para solicitudes E/S
    public SolicitudIO crearSolicitudIO(int bloqueObjetivo, long tiempo) {
        return crearSolicitudIO(bloqueObjetivo, tiempo, null);
    }

    public SolicitudIO crearSolicitudIO(int bloqueObjetivo, long tiempo, String operacionArchivo) {
        SolicitudIO solicitud = new SolicitudIO(solicitudesPendientes.getTamano() + 1, 
            obtenerTipoOperacion(operacionArchivo), "Archivo_" + bloqueObjetivo, 
            nombreProceso, usuario.getUsername());
        
        solicitud.configurarBloqueObjetivo(bloqueObjetivo);
        solicitudesPendientes.encolar(solicitud);
        establecerBloqueado();
        return solicitud;
    }

    public void notificarIOCompletado() {
        if (!solicitudesPendientes.estaVacia()) {
            solicitudesPendientes.desencolar();
        }
        if (solicitudesPendientes.estaVacia()) {
            establecerListo();
        }
    }

    // Métodos de utilidad
    public boolean estaActivo() {
        return estado != EstadosProceso.TERMINADO;
    }

    public boolean puedeSerProgramado() {
        return estado == EstadosProceso.LISTO || estado == EstadosProceso.BLOQUEADO;
    }

    public long getTiempoEjecucion() {
        if (tiempoInicio == -1) return 0;
        if (tiempoFin == -1) return System.currentTimeMillis() - tiempoInicio;
        return tiempoFin - tiempoInicio;
    }

    public String obtenerResumen() {
        return String.format("PID#%d %-15s %-10s %-12s %s", 
            idProceso, nombreProceso, usuario.getUsername(), estado.toString(),
            solicitudActual != null ? solicitudActual.obtenerNombreOperacion() + " " + 
            solicitudActual.getNombreArchivo() : "Sin Solicitud");
    }

    private int obtenerTipoOperacion(String operacion) {
        if (operacion == null) return SolicitudIO.OPERACION_LEER;
        
        switch (operacion.toUpperCase()) {
            case "CREAR": return SolicitudIO.OPERACION_CREAR;
            case "LEER": return SolicitudIO.OPERACION_LEER;
            case "ACTUALIZAR": return SolicitudIO.OPERACION_ACTUALIZAR;
            case "ELIMINAR": return SolicitudIO.OPERACION_ELIMINAR;
            default: return SolicitudIO.OPERACION_LEER;
        }
    }

    @Override
    public String toString() {
        return "Proceso{id=" + idProceso + ", nombre='" + nombreProceso + "', usuario='" + 
               usuario.getUsername() + "', estado=" + estado + "}";
    }
}