/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;
import EDD.ListaSimple;
import Usuario.Permisos;

/**
 *
 * @author Andres Salgueiro
 */
public class Directorio {
    private String nombre;
    private String ruta;
    private String propietario;
    private Usuario.Permisos permisos;
    private ListaSimple archivos;
    private ListaSimple subdirectorios;
    private Directorio directorioPadre;
    
    public Directorio(String nombre, String propietario) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.ruta = nombre;
        this.permisos = new Usuario.Permisos(propietario);
        this.archivos = new ListaSimple();
        this.subdirectorios = new ListaSimple();
        this.directorioPadre = null;
    }
    
    public Directorio(String nombre, String propietario, Directorio directorioPadre) {
        this(nombre, propietario);
        this.directorioPadre = directorioPadre;
        this.ruta = directorioPadre.getRuta() + "/" + nombre;
    }
    
    public boolean agregarArchivo(Archivo archivo) {
        if (obtenerArchivo(archivo.getNombre()) != null) {
            return false;
        }
        archivos.insertFinal(archivo);
        return true;
    }
    
    public boolean eliminarArchivo(String nombreArchivo) {
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo archivo = (Archivo) archivos.get(i);
            if (archivo.getNombre().equals(nombreArchivo)) {
                archivos.remove(archivo);
                return true;
            }
        }
        return false;
    }
    
    public Archivo obtenerArchivo(String nombreArchivo) {
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo archivo = (Archivo) archivos.get(i);
            if (archivo.getNombre().equals(nombreArchivo)) {
                return archivo;
            }
        }
        return null;
    }
    
    public boolean agregarSubdirectorio(Directorio directorio) {
        if (obtenerSubdirectorio(directorio.getNombre()) != null) {
            return false;
        }
        subdirectorios.insertFinal(directorio);
        directorio.setDirectorioPadre(this);
        return true;
    }
    
    public boolean eliminarSubdirectorio(String nombreDirectorio) {
        for (int i = 0; i < subdirectorios.getSize(); i++) {
            Directorio dir = (Directorio) subdirectorios.get(i);
            if (dir.getNombre().equals(nombreDirectorio)) {
                subdirectorios.remove(dir);
                return true;
            }
        }
        return false;
    }
    
    public Directorio obtenerSubdirectorio(String nombreDirectorio) {
        for (int i = 0; i < subdirectorios.getSize(); i++) {
            Directorio dir = (Directorio) subdirectorios.get(i);
            if (dir.getNombre().equals(nombreDirectorio)) {
                return dir;
            }
        }
        return null;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getRuta() {
        return ruta;
    }
    
    public String getPropietario() {
        return propietario;
    }
    
    public Usuario.Permisos getPermisos() {
        return permisos;
    }
    
    public ListaSimple getArchivos() {
        return archivos;
    }
    
    public ListaSimple getSubdirectorios() {
        return subdirectorios;
    }
    
    public Directorio getDirectorioPadre() {
        return directorioPadre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    
    public void setDirectorioPadre(Directorio directorioPadre) {
        this.directorioPadre = directorioPadre;
    }
    
    public boolean estaVacio() {
        return archivos.isEmpty() && subdirectorios.isEmpty();
    }
    
    public int getTamañoTotal() {
        int tamaño = 0;
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo archivo = (Archivo) archivos.get(i);
            tamaño += archivo.getTamaño();
        }
        return tamaño;
    }
    
    @Override
    public String toString() {
        return "Directorio{nombre='" + nombre + "', ruta='" + ruta + "', propietario='" + propietario + 
               "', archivos=" + archivos.getSize() + ", subdirs=" + subdirectorios.getSize() + "}";
    }
}