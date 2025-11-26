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

public class SSTF implements ManejadorPolitica {
    private int cabezaActual;
    
    public SSTF() {
        this.cabezaActual = 0;
    }
    
    @Override
    public String getNombrePolitica() {
        return "SSTF";
    }
    
    @Override
    public int obtenerSiguienteBloque(ListaSimple solicitudesPendientes, int cabezaActual) {
        if (solicitudesPendientes.isEmpty()) {
            return cabezaActual;
        }
        
        this.cabezaActual = cabezaActual;
        int solicitudMasCercana = -1;
        int distanciaMinima = Integer.MAX_VALUE;
        
        for (int i = 0; i < solicitudesPendientes.getSize(); i++) {
            int solicitud = (int) solicitudesPendientes.get(i);
            int distancia = Math.abs(solicitud - cabezaActual);
            
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                solicitudMasCercana = solicitud;
            }
        }
        
        return solicitudMasCercana;
    }
    
    @Override
    public void establecerCabezaActual(int cabeza) {
        this.cabezaActual = cabeza;
    }
}