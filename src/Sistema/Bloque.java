/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

/**
 *
 * @author Andres Salgueiro
 */
public class Bloque {
    private int numeroBloque;
    private boolean ocupado;
    private String archivoPropietario;
    private String procesoPropietario;
    private Object datos;
    
    public Bloque(int numeroBloque) {
        this.numeroBloque = numeroBloque;
        this.ocupado = false;
        this.archivoPropietario = null;
        this.procesoPropietario = null;
        this.datos = null;
    }
    
    // Getters
    public int getNumeroBloque() {
        return numeroBloque;
    }
    
    public boolean isOcupado() {
        return ocupado;
    }
    
    public String getArchivoPropietario() {
        return archivoPropietario;
    }
    
    public String getProcesoPropietario() {
        return procesoPropietario;
    }
    
    public Object getDatos() {
        return datos;
    }
    
    // Setters
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
    
    public void setArchivoPropietario(String archivoPropietario) {
        this.archivoPropietario = archivoPropietario;
    }
    
    public void setProcesoPropietario(String procesoPropietario) {
        this.procesoPropietario = procesoPropietario;
    }
    
    public void setDatos(Object datos) {
        this.datos = datos;
    }
    
    public void limpiarBloque() {
        this.ocupado = false;
        this.archivoPropietario = null;
        this.procesoPropietario = null;
        this.datos = null;
    }
    
    @Override
    public String toString() {
        return "Bloque{" + "numero=" + numeroBloque + ", ocupado=" + ocupado + 
               ", archivo=" + archivoPropietario + ", proceso=" + procesoPropietario + '}';
    }
}