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
    private String path;
    private String propietario;
    private Permisos permisos;
    private ListaSimple archivos;
    private ListaSimple subdirectorios;
    private Directorio pariente;
    
    public Directorio String nombre, String propietario) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.path = nombre;
        this.permisos = new Permisos(propietario);
        this.archivos = new ListaSimple();
        this.subdirectorios = new ListaSimple();
        this.pariente = null;
    }
    
    public Directorio(String name, String owner, Directorio parent) {
        this(nombre, propietario);
        this.pariente = parent;
        this.path = parent.getPath() + "/" + name;
    }
    
    // Métodos para archivos
    public boolean addFile(Archivo file) {
        if (getFile(file.getName()) != null) {
            return false; // Archivo ya existe
        }
        archivos.insertFinal(file);
        return true;
    }
    
    public boolean removeFile(String fileName) {
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo = (Archivo) archivos.get(i);
            if (file.getName().equals(fileName)) {
                archivos.remove(file);
                return true;
            }
        }
        return false;
    }
    
    public Archivo getFile(String fileName) {
        for (int i = 0; i < archivos.getSize(); i++) {
            Archivo file = (Archivo) archivos.get(i);
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
    
    // Métodos para subdirectorios
    public boolean addSubdirectory(Directorio directory) {
        if (getSubdirectory(directory.getName()) != null) {
            return false; // Directorio ya existe
        }
        subdirectorios.insertFinal(directory);
        directory.setParent(this);
        return true;
    }
    
    public boolean removeSubdirectory(String dirName) {
        for (int i = 0; i < subdirectorios.getSize(); i++) {
            Directorio dir = (Directorio) subdirectorios.get(i);
            if (dir.getName().equals(dirName)) {
                subdirectorios.remove(dir);
                return true;
            }
        }
        return false;
    }
    
    public Directorio getSubdirectory(String dirName) {
        for (int i = 0; i < subdirectorios.getSize(); i++) {
            Directorio dir = (Directorio) subdirectorios.get(i);
            if (dir.getName().equals(dirName)) {
                return dir;
            }
        }
        return null;
    }
    
    // Getters
    public String getName() {
        return nombre;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getOwner() {
        return propietario;
    }
    
    public Permisos getPermissions() {
        return permisos;
    }
    
    public ListaSimple getFiles() {
        return archivos;
    }
    
    public ListaSimple getSubdirectories() {
        return subdirectorios;
    }
    
    public Directorio getParent() {
        return pariente;
    }
    
    // Setters
    public void setName(String name) {
        this.nombre = name;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public void setOwner(String owner) {
        this.propietario = owner;
    }
    
    public void setParent(Directorio parent) {
        this.pariente = parent;
    }
    
    public boolean isEmpty() {
        return archivos.isEmpty() && subdirectorios.isEmpty();
    }
}