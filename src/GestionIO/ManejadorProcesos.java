/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionIO;

import EDD.Cola;
import EDD.ListaSimple;

/**
 *
 * @author Andres Salgueiro
 */
import EDD.Cola;
import EDD.ListaSimple;

public class ManejadorProcesos {
    private final ListaSimple procesos;
    private Cola colaListos;
    private Cola colaBloqueados;
    private int siguienteIdProceso;
    
    public ManejadorProcesos()  {
        this.procesos = new ListaSimple();
        this.colaListos = new Cola();
        this.colaBloqueados = new Cola();
        this.siguienteIdProceso = 1;
    }
    
    public Proceso crearProceso(String nombreProceso, String nombreUsuario) {
        Proceso proceso = new Proceso(siguienteIdProceso++, nombreProceso, nombreUsuario);
        procesos.insertFinal(proceso);
        proceso.establecerListo();
        colaListos.encolar(proceso);
        System.out.println("Proceso creado: " + proceso.obtenerResumen());
        return proceso;
    }
    
    public boolean terminarProceso(int idProceso) {
        for (int i = 0; i < procesos.getSize(); i++) {
            Proceso proceso = (Proceso) procesos.get(i);
            if (proceso.getIdProceso() == idProceso) {
                proceso.establecerTerminado();
                removerDeColas(proceso);
                System.out.println("Proceso terminado: " + proceso.obtenerResumen());
                return true;
            }
        }
        return false;
    }
    
    public Proceso programarSiguienteProceso() {
        if (colaListos.estaVacia()) {
            return null;
        }
        
        Proceso siguienteProceso = (Proceso) colaListos.desencolar();
        siguienteProceso.establecerEjecutando();
        System.out.println("Proceso programado: " + siguienteProceso.obtenerResumen());
        return siguienteProceso;
    }
    
    public void bloquearProceso(Proceso proceso) {
        if (proceso != null) {
            proceso.establecerBloqueado();
            colaBloqueados.encolar(proceso);
            System.out.println("Proceso bloqueado: " + proceso.obtenerResumen());
        }
    }
    
    public void desbloquearProceso(Proceso proceso) {
        if (proceso != null && proceso.getEstado() == Proceso.BLOQUEADO) {
            proceso.establecerListo();
            colaListos.encolar(proceso);
            removerDeColaBloqueados(proceso);
            System.out.println("Proceso desbloqueado: " + proceso.obtenerResumen());
        }
    }
    
    public void asignarSolicitudIO(Proceso proceso, SolicitudIO solicitud) {
        if (proceso != null && solicitud != null) {
            proceso.establecerSolicitudActual(solicitud);
            bloquearProceso(proceso);
            System.out.println("Solicitud E/S asignada: " + solicitud.obtenerResumen() + " al proceso " + proceso.getNombreProceso());
        }
    }
    
    public void completarSolicitudIO(Proceso proceso) {
        if (proceso != null && proceso.getSolicitudActual() != null) {
            proceso.getSolicitudActual().setEstado("COMPLETADA");
            proceso.establecerSolicitudActual(null);
            desbloquearProceso(proceso);
            System.out.println("Solicitud E/S completada para proceso: " + proceso.getNombreProceso());
        }
    }
    
    private void removerDeColas(Proceso proceso) {
        removerDeColaListos(proceso);
        removerDeColaBloqueados(proceso);
    }
    
    private void removerDeColaListos(Proceso proceso) {
        Cola nuevaColaListos = new Cola();
        while (!colaListos.estaVacia()) {
            Proceso p = (Proceso) colaListos.desencolar();
            if (p.getIdProceso() != proceso.getIdProceso()) {
                nuevaColaListos.encolar(p);
            }
        }
        this.colaListos = nuevaColaListos;
    }
    
    private void removerDeColaBloqueados(Proceso proceso) {
        Cola nuevaColaBloqueados = new Cola();
        while (!colaBloqueados.estaVacia()) {
            Proceso p = (Proceso) colaBloqueados.desencolar();
            if (p.getIdProceso() != proceso.getIdProceso()) {
                nuevaColaBloqueados.encolar(p);
            }
        }
        this.colaBloqueados = nuevaColaBloqueados;
    }
    
    public ListaSimple obtenerTodosProcesos() {
        return procesos;
    }
    
    public ListaSimple obtenerProcesosActivos() {
        ListaSimple activos = new ListaSimple();
        for (int i = 0; i < procesos.getSize(); i++) {
            Proceso proceso = (Proceso) procesos.get(i);
            if (proceso.estaActivo()) {
                activos.insertFinal(proceso);
            }
        }
        return activos;
    }
    
    public ListaSimple obtenerProcesosListos() {
        ListaSimple listos = new ListaSimple();
        for (int i = 0; i < procesos.getSize(); i++) {
            Proceso proceso = (Proceso) procesos.get(i);
            if (proceso.getEstado() == Proceso.LISTO) {
                listos.insertFinal(proceso);
            }
        }
        return listos;
    }
    
    public ListaSimple obtenerProcesosBloqueados() {
        ListaSimple bloqueados = new ListaSimple();
        for (int i = 0; i < procesos.getSize(); i++) {
            Proceso proceso = (Proceso) procesos.get(i);
            if (proceso.getEstado() == Proceso.BLOQUEADO) {
                bloqueados.insertFinal(proceso);
            }
        }
        return bloqueados;
    }
    
    public Proceso obtenerProcesoPorId(int idProceso) {
        for (int i = 0; i < procesos.getSize(); i++) {
            Proceso proceso = (Proceso) procesos.get(i);
            if (proceso.getIdProceso() == idProceso) {
                return proceso;
            }
        }
        return null;
    }
    
    public void mostrarEstadoProcesos() {
        System.out.println("=== ESTADO DE PROCESOS ===");
        System.out.println("Total procesos: " + procesos.getSize());
        System.out.println("En cola listos: " + colaListos.getTamano());
        System.out.println("En cola bloqueados: " + colaBloqueados.getTamano());
        
        System.out.println("\nLista de procesos:");
        for (int i = 0; i < procesos.getSize(); i++) {
            Proceso proceso = (Proceso) procesos.get(i);
            System.out.println(proceso.obtenerResumen());
        }
    }
}