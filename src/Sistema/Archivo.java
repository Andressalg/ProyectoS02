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
public class Archivo {
    private String nombre;
    private String propietario;
    private int tamaño;
    private int blockCount;
    private ListaSimple BloquesAsignados;
    private Permisos permisos;
    private String fechadecreacion;
    private String lastModified;
    private String content;
    
    public Achivo(String nombre, String propietario, int tamaño) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.tamaño = tamaño;
        this.blockCount = 0;
        this.BloquesAsignados = new ListaSimple();
        this.permisos = new Permisos(propietario);
        this.fechadecreacion = java.time.LocalDateTime.now().toString();
        this.lastModified = this.fechadecreacion;
        this.content = "";
    }
    
    // ===== OPERACIONES CRUD =====
    
    /**
     * CREATE
     */
    public boolean CREATE(String newContent, String username, boolean isAdmin) {
        if (!permisos.canWrite(username, isAdmin)) {
            System.out.println("Error: No tiene permisos de escritura sobre " + nombre);
            return false;
        }
        
        this.content = newContent;
        this.tamaño = newContent.length();
        this.lastModified = java.time.LocalDateTime.now().toString();
        System.out.println("Archivo '" + nombre + "' creado/modificado con " + newContent.length() + " caracteres");
        return true;
    }
    
    /**
     * READ
     */
    public String READ(String username, boolean isAdmin) {
        if (!permisos.canRead(username, isAdmin)) {
            System.out.println("Error: No tiene permisos de lectura sobre " + nombre);
            return null;
        }
        
        System.out.println("Leyendo archivo '" + nombre + "': " + content.length() + " caracteres");
        return content;
    }
    
    /**
     * UPDATE
     */
    public boolean UPDATE(String newContent, String username, boolean isAdmin) {
        if (!permisos.canWrite(username, isAdmin)) {
            System.out.println("Error: No tiene permisos de escritura sobre " + nombre);
            return false;
        }
        
        this.content = newContent;
        this.tamaño = newContent.length();
        this.lastModified = java.time.LocalDateTime.now().toString();
        System.out.println("Archivo '" + nombre + "' actualizado. Nuevo tamaño: " + newContent.length() + " caracteres");
        return true;
    }
    
    /**
     * UPDATE 
     */
    public boolean UPDATEnombre(String newName, String username, boolean isAdmin) {
        if (!permisos.canWrite(username, isAdmin)) {
            System.out.println("Error: No tiene permisos para renombrar " + nombre);
            return false;
        }
        
        String oldName = this.nombre;
        this.nombre = newName;
        this.lastModified = java.time.LocalDateTime.now().toString();
        System.out.println("Archivo '" + oldName + "' renombrado a '" + newName + "'");
        return true;
    }
    
    /**
     * UPDATE
     */
    public boolean UPDATEappend(String additionalContent, String username, boolean isAdmin) {
        if (!permisos.canWrite(username, isAdmin)) {
            System.out.println("Error: No tiene permisos de escritura sobre " + nombre);
            return false;
        }
        
        this.content += additionalContent;
        this.tamaño = this.content.length();
        this.lastModified = java.time.LocalDateTime.now().toString();
        System.out.println("Contenido agregado a '" + nombre + "'. Nuevo tamaño: " + this.tamaño + " caracteres");
        return true;
    }
    
    /**
     * DELETE
     */
    public boolean DELETE(String username, boolean isAdmin) {
        if (!permisos.canWrite(username, isAdmin)) {
            System.out.println("Error: No tiene permisos para eliminar contenido de " + nombre);
            return false;
        }
        
        this.content = "";
        this.tamaño = 0;
        this.lastModified = java.time.LocalDateTime.now().toString();
        System.out.println("Contenido del archivo '" + nombre + "' eliminado");
        return true;
    }
    
    /**
     * DELETE
     */
    public boolean PrepararDELETE(String username, boolean isAdmin) {
        if (!permisos.canWrite(username, isAdmin)) {
            System.out.println("Error: No tiene permisos para eliminar " + nombre);
            return false;
        }
        
        // Liberar bloques asignados
        clearBlocks();
        this.content = "";
        this.tamaño = 0;
        System.out.println("Archivo '" + nombre + "' preparado para eliminación");
        return true;
    }
    
    // ===== OPERACIONES DE BLOQUES =====
    
    public void addBlock(Bloque block) {
        BloquesAsignados.insertFinal(block);
        blockCount++;
    }
    
    public void clearBlocks() {
        BloquesAsignados.clear();
        blockCount = 0;
    }
    
    public int getFirstBlockNumber() {
        if (BloquesAsignados.isEmpty() || blockCount == 0) {
            return -1;
        }
        Bloque firstBlock = (Bloque) BloquesAsignados.get(0);
        return firstBlock.getBlockNumber();
    }
    
    // ===== OPERACIONES DE PERMISOS =====
    
    public boolean setPublicRead(boolean allowed, String username, boolean isAdmin) {
        if (!propietario.equals(username) && !isAdmin) {
            System.out.println("Error: Solo el propietario o administrador puede cambiar permisos");
            return false;
        }
        
        permisos.setPublicRead(allowed);
        System.out.println("Permiso de lectura " + (allowed ? "concedido" : "revocado") + " para todos los usuarios");
        return true;
    }
    
    public boolean setPublicWrite(boolean allowed, String username, boolean isAdmin) {
        if (!propietario.equals(username) && !isAdmin) {
            System.out.println("Error: Solo el propietario o administrador puede cambiar permisos");
            return false;
        }
        
        permisos.setPublicWrite(allowed);
        System.out.println("Permiso de escritura " + (allowed ? "concedido" : "revocado") + " para todos los usuarios");
        return true;
    }
    
    // ===== VERIFICACIONES DE PERMISOS =====
    
    public boolean canRead(String username, boolean isAdmin) {
        return permisos.canRead(username, isAdmin);
    }
    
    public boolean canWrite(String username, boolean isAdmin) {
        return permisos.canWrite(username, isAdmin);
    }
    
    public boolean canExecute(String username, boolean isAdmin) {
        return permisos.canExecute(username, isAdmin);
    }
    
    // ===== GETTERS Y SETTERS =====
    
    public String getName() { return nombre; }
    public String getOwner() { return propietario; }
    public int getSize() { return tamaño; }
    public int getBlockCount() { return blockCount; }
    public ListaSimple getAllocatedBlocks() { return BloquesAsignados; }
    public Permisos getPermissions() { return permisos; }
    public String getCreationDate() { return fechadecreacion; }
    public String getLastModified() { return lastModified; }
    public String getContent() { return content; }
    
    public void setSize(int size) { this.tamaño = size; }
    public void setBlockCount(int blockCount) { this.blockCount = blockCount; }
    
    // ===== INFORMACIÓN DEL ARCHIVO =====
    
    public void displayFileInfo() {
        System.out.println("=== INFORMACIÓN DEL ARCHIVO ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Propietario: " + propietario);
        System.out.println("Tamaño: " + tamaño + " caracteres");
        System.out.println("Bloques asignados: " + blockCount);
        System.out.println("Primer bloque: " + getFirstBlockNumber());
        System.out.println("Creado: " + fechadecreacion);
        System.out.println("Modificado: " + lastModified);
        System.out.println("Permisos: " + permisos.getPermissionString());
        System.out.println("Contenido: \"" + (content.length() > 50 ? content.substring(0, 50) + "..." : content) + "\"");
    }
    
    public String getFileSummary() {
        return String.format("%-20s %-10s %-8d %-6d %-6d", 
            nombre, propietario, tamaño, blockCount, getFirstBlockNumber());
    }
    
    @Override
    public String toString() {
        return "FileEntry{name='" + nombre + "', owner='" + propietario + "', size=" + tamaño + 
               " chars, blocks=" + blockCount + ", modified=" + lastModified + "}";
    }
}