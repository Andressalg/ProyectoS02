/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Politicas;

/**
 *
 * @author Andres Salgueiro
 */

import Sistema.ManejadorPolitica;
import EDD.ListaSimple;

public class CScan implements ManejadorPolitica {
    private int cabezaActual;
    private int tamañoDisco;
    
    public CScan(int tamañoDisco) {
        this.tamañoDisco = tamañoDisco;
        this.cabezaActual = 0;
    }
    
    @Override
    public String getNombrePolitica() {
        return "C-SCAN";
    }
    
    @Override
    public int obtenerSiguienteBloque(ListaSimple solicitudesPendientes, int cabezaActual) {
        if (solicitudesPendientes.isEmpty()) {
            return cabezaActual;
        }
        
        this.cabezaActual = cabezaActual;
        ListaSimple solicitudesOrdenadas = ordenarSolicitudes(solicitudesPendientes);
        
        int siguienteBloque = -1;
        for (int i = 0; i < solicitudesOrdenadas.getSize(); i++) {
            int solicitud = (int) solicitudesOrdenadas.get(i);
            if (solicitud >= cabezaActual) {
                siguienteBloque = solicitud;
                break;
            }
        }
        
        if (siguienteBloque == -1) {
            siguienteBloque = (int) solicitudesOrdenadas.get(0);
        }
        
        return siguienteBloque;
    }
    
    @Override
    public void establecerCabezaActual(int cabeza) {
        this.cabezaActual = cabeza;
    }
    
    private ListaSimple ordenarSolicitudes(ListaSimple solicitudes) {
        int[] arreglo = new int[solicitudes.getSize()];
        for (int i = 0; i < solicitudes.getSize(); i++) {
            arreglo[i] = (int) solicitudes.get(i);
        }
        
        for (int i = 0; i < arreglo.length - 1; i++) {
            for (int j = 0; j < arreglo.length - i - 1; j++) {
                if (arreglo[j] > arreglo[j + 1]) {
                    int temp = arreglo[j];
                    arreglo[j] = arreglo[j + 1];
                    arreglo[j + 1] = temp;
                }
            }
        }
        
        ListaSimple ordenadas = new ListaSimple();
        for (int valor : arreglo) {
            ordenadas.insertFinal(valor);
        }
        
        return ordenadas;
    }
}