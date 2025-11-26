/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Politicas;

/**
 *
 * @author Andres Salgueiro
 */
import EDD.ListaSimple;
import Sistema.ManejadorPolitica;

public class FIFO implements ManejadorPolitica {
    
    public FIFO() {}
    
    @Override
    public String getNombrePolitica() {
        return "FIFO";
    }
    
    @Override
    public int obtenerSiguienteBloque(ListaSimple solicitudesPendientes, int cabezaActual) {
        if (solicitudesPendientes.isEmpty()) {
            return cabezaActual;
        }
        
        return (int) solicitudesPendientes.get(0);
    }
    
    @Override
    public void establecerCabezaActual(int cabeza) {
    }
}