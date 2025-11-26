/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionIO;

import Sistema.Archivo;

/**
 *
 * @author Andres Salgueiro
 */

public class SolicitudIO {
    public static final int OPERACION_CREAR = 1;
    public static final int OPERACION_LEER = 2;
    public static final int OPERACION_ACTUALIZAR = 3;
    public static final int OPERACION_ELIMINAR = 4;
    
    private int idSolicitud;
    private int tipoOperacion;
    private String nombreArchivo;
    private String rutaArchivo;
    private String nombreProceso;
    private String nombreUsuario;
    private String datosContenido;
    private int bloquesRequeridos;
    private int bloqueObjetivo;
    private String estado;
    private long timestampCreacion;
    private Archivo archivoReferencia;
    
    public SolicitudIO(int idSolicitud, int tipoOperacion, String nombreArchivo, 
                      String nombreProceso, String nombreUsuario) {
        this.idSolicitud = idSolicitud;
        this.tipoOperacion = tipoOperacion;
        this.nombreArchivo = nombreArchivo;
        this.nombreProceso = nombreProceso;
        this.nombreUsuario = nombreUsuario;
        this.timestampCreacion = System.currentTimeMillis();
        this.estado = "PENDIENTE";
        this.datosContenido = "";
        this.bloquesRequeridos = 0;
        this.bloqueObjetivo = -1;
        this.archivoReferencia = null;
        this.rutaArchivo = "";
    }
    
    // ===== CONFIGURACIÓN DE LA SOLICITUD =====
    
    public void configurarCreacion(String contenido, int bloquesNecesarios) {
        if (tipoOperacion == OPERACION_CREAR) {
            this.datosContenido = contenido;
            this.bloquesRequeridos = bloquesNecesarios;
        }
    }
    
    public void configurarActualizacion(String nuevoContenido) {
        if (tipoOperacion == OPERACION_ACTUALIZAR) {
            this.datosContenido = nuevoContenido;
        }
    }
    
    public void configurarBloqueObjetivo(int bloque) {
        this.bloqueObjetivo = bloque;
    }
    
    public void establecerRuta(String ruta) {
        this.rutaArchivo = ruta;
    }
    
    public void vincularArchivo(Archivo archivo) {
        this.archivoReferencia = archivo;
    }
    
    // ===== EJECUCIÓN DE LA SOLICITUD =====
    
    public boolean ejecutarOperacion() {
        if (archivoReferencia == null) {
            System.out.println("Error: No hay archivo vinculado para la operación");
            return false;
        }
        
        switch (tipoOperacion) {
            case OPERACION_CREAR:
                return archivoReferencia.escribirContenido(datosContenido, nombreUsuario, false);
                
            case OPERACION_LEER:
                String contenido = archivoReferencia.leerContenido(nombreUsuario, false);
                return contenido != null;
                
            case OPERACION_ACTUALIZAR:
                return archivoReferencia.escribirContenido(datosContenido, nombreUsuario, false);
                
            case OPERACION_ELIMINAR:
                return archivoReferencia.vaciarContenido(nombreUsuario, false);
                
            default:
                System.out.println("Error: Tipo de operación no válido");
                return false;
        }
    }
    
    // ===== GETTERS =====
    
    public int getIdSolicitud() { return idSolicitud; }
    public int getTipoOperacion() { return tipoOperacion; }
    public String getNombreArchivo() { return nombreArchivo; }
    public String getRutaArchivo() { return rutaArchivo; }
    public String getNombreProceso() { return nombreProceso; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getDatosContenido() { return datosContenido; }
    public int getBloquesRequeridos() { return bloquesRequeridos; }
    public int getBloqueObjetivo() { return bloqueObjetivo; }
    public String getEstado() { return estado; }
    public long getTimestampCreacion() { return timestampCreacion; }
    public Archivo getArchivoReferencia() { return archivoReferencia; }
    
    // ===== SETTERS =====
    
    public void setEstado(String estado) { this.estado = estado; }
    public void setBloquesRequeridos(int bloques) { this.bloquesRequeridos = bloques; }
    
    // ===== VERIFICACIONES =====
    
    public boolean esCreacion() { return tipoOperacion == OPERACION_CREAR; }
    public boolean esLectura() { return tipoOperacion == OPERACION_LEER; }
    public boolean esActualizacion() { return tipoOperacion == OPERACION_ACTUALIZAR; }
    public boolean esEliminacion() { return tipoOperacion == OPERACION_ELIMINAR; }
    public boolean estaCompletada() { return "COMPLETADA".equals(estado); }
    public boolean estaPendiente() { return "PENDIENTE".equals(estado); }
    
    // ===== INFORMACIÓN =====
    
    public String obtenerNombreOperacion() {
        switch (tipoOperacion) {
            case OPERACION_CREAR: return "CREAR";
            case OPERACION_LEER: return "LEER";
            case OPERACION_ACTUALIZAR: return "ACTUALIZAR";
            case OPERACION_ELIMINAR: return "ELIMINAR";
            default: return "DESCONOCIDA";
        }
    }
    
    public String obtenerResumen() {
        return String.format("Solic#%d %-10s %-15s %-12s %-10s %-8s", 
            idSolicitud, obtenerNombreOperacion(), nombreArchivo, nombreProceso, 
            nombreUsuario, estado);
    }
    
    public void mostrarDetalles() {
        System.out.println("=== DETALLES DE SOLICITUD E/S ===");
        System.out.println("ID: " + idSolicitud);
        System.out.println("Operación: " + obtenerNombreOperacion());
        System.out.println("Archivo: " + nombreArchivo);
        System.out.println("Ruta: " + rutaArchivo);
        System.out.println("Proceso: " + nombreProceso);
        System.out.println("Usuario: " + nombreUsuario);
        System.out.println("Estado: " + estado);
        System.out.println("Bloques requeridos: " + bloquesRequeridos);
        System.out.println("Bloque objetivo: " + bloqueObjetivo);
        System.out.println("Contenido: " + (datosContenido.length() > 30 ? 
            datosContenido.substring(0, 30) + "..." : datosContenido));
        System.out.println("Archivo vinculado: " + (archivoReferencia != null ? "Sí" : "No"));
    }
    
    @Override
    public String toString() {
        return "SolicitudIO{id=" + idSolicitud + ", operacion=" + obtenerNombreOperacion() + 
               ", archivo='" + nombreArchivo + "', proceso='" + nombreProceso + 
               "', usuario='" + nombreUsuario + "', estado='" + estado + "'}";
    }
}