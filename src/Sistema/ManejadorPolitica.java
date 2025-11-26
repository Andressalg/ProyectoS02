/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;
import EDD.ListaSimple;

/**
 *
 * @author Andres Salgueiro
 */
public interface ManejadorPolitica {
    String getNombrePolitica();
    int obtenerSiguienteBloque(ListaSimple solicitudesPendientes, int cabezaActual);
    void establecerCabezaActual(int cabeza);
}
