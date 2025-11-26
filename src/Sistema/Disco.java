/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;
import EDD.ListaSimple;
import GestionIO.SolicitudIO;

/**
 *
 * @author Andres Salgueiro
 */
public class Disco {
    private ManejadorPolitica politicaActual;
    private ListaSimple solicitudesPendientes;
    private int posicionCabezaActual;
    private int operacionesBusquedaTotales;
    private long tiempoBusquedaTotal;
    private int tamañoDisco;
    
    public Disco(int tamañoDisco) {
        this.tamañoDisco = tamañoDisco;
        this.solicitudesPendientes = new ListaSimple();
        this.posicionCabezaActual = 0;
        this.operacionesBusquedaTotales = 0;
        this.tiempoBusquedaTotal = 0;
        this.politicaActual = null;
    }
    
    public void establecerPolitica(ManejadorPolitica politica) {
        this.politicaActual = politica;
        System.out.println("Política de disco cambiada a: " + politica.getNombrePolitica());
    }
    
    public void agregarSolicitud(SolicitudIO solicitud) {
        if (solicitud.getBloqueObjetivo() != -1) {
            solicitudesPendientes.insertFinal(solicitud.getBloqueObjetivo());
            System.out.println("Solicitud E/S agregada: Bloque " + solicitud.getBloqueObjetivo() + " para " + solicitud.getNombreArchivo());
        }
    }
    
    public int obtenerSiguienteBloque() {
        if (solicitudesPendientes.isEmpty()) {
            return posicionCabezaActual;
        }
        
        if (politicaActual == null) {
            System.out.println("Error: No hay política establecida");
            return posicionCabezaActual;
        }
        
        long tiempoInicio = System.nanoTime();
        int siguienteBloque = politicaActual.obtenerSiguienteBloque(solicitudesPendientes, posicionCabezaActual);
        long tiempoFin = System.nanoTime();
        
        int distanciaBusqueda = Math.abs(siguienteBloque - posicionCabezaActual);
        operacionesBusquedaTotales++;
        tiempoBusquedaTotal += (tiempoFin - tiempoInicio) / 1000000;
        
        removerBloqueServido(siguienteBloque);
        
        posicionCabezaActual = siguienteBloque;
        politicaActual.establecerCabezaActual(posicionCabezaActual);
        
        System.out.println("Disco: Cabeza movida a bloque " + siguienteBloque + " (distancia: " + distanciaBusqueda + ")");
        return siguienteBloque;
    }
    
    private void removerBloqueServido(int numeroBloque) {
        for (int i = 0; i < solicitudesPendientes.getSize(); i++) {
            int bloque = (int) solicitudesPendientes.get(i);
            if (bloque == numeroBloque) {
                solicitudesPendientes.remove(bloque);
                break;
            }
        }
    }
    
    public void completarSolicitud(SolicitudIO solicitud) {
        if (solicitud.getBloqueObjetivo() != -1) {
            removerBloqueServido(solicitud.getBloqueObjetivo());
            solicitud.setEstado("COMPLETADA");
            System.out.println("Solicitud E/S completada: " + solicitud.getNombreArchivo());
        }
    }
    
    public int getCantidadSolicitudesPendientes() {
        return solicitudesPendientes.getSize();
    }
    
    public int getPosicionCabezaActual() {
        return posicionCabezaActual;
    }
    
    public void setPosicionCabezaActual(int posicion) {
        this.posicionCabezaActual = posicion;
        if (politicaActual != null) {
            politicaActual.establecerCabezaActual(posicion);
        }
    }
    
    public String getNombrePoliticaActual() {
        return politicaActual != null ? politicaActual.getNombrePolitica() : "Ninguna";
    }
    
    public double getTiempoBusquedaPromedio() {
        return operacionesBusquedaTotales > 0 ? (double) tiempoBusquedaTotal / operacionesBusquedaTotales : 0;
    }
    
    public int getTamañoDisco() {
        return tamañoDisco;
    }
    
    public ListaSimple getSolicitudesPendientes() {
        return solicitudesPendientes;
    }
    
    public void mostrarEstado() {
        System.out.println("=== ESTADO DEL DISCO ===");
        System.out.println("Tamaño del disco: " + tamañoDisco + " bloques");
        System.out.println("Política actual: " + getNombrePoliticaActual());
        System.out.println("Posición de cabeza: " + posicionCabezaActual);
        System.out.println("Solicitudes pendientes: " + solicitudesPendientes.getSize());
        System.out.println("Operaciones de búsqueda: " + operacionesBusquedaTotales);
        System.out.println("Tiempo promedio de búsqueda: " + String.format("%.2f", getTiempoBusquedaPromedio()) + " ms");
        
        if (!solicitudesPendientes.isEmpty()) {
            System.out.println("Solicitudes pendientes:");
            for (int i = 0; i < solicitudesPendientes.getSize(); i++) {
                System.out.print("[" + solicitudesPendientes.get(i) + "] ");
            }
            System.out.println();
        }
    }
    
    public void limpiarSolicitudesPendientes() {
        solicitudesPendientes.clear();
        System.out.println("Todas las solicitudes pendientes han sido limpiadas");
    }
    
    public boolean tienePoliticaEstablecida() {
        return politicaActual != null;
    }
}