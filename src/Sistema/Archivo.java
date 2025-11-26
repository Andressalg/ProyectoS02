package Sistema;

import EDD.ListaSimple;
import Usuario.Permisos;

public class Archivo {
    private String nombre;
    private String propietario;
    private int tamaño;
    private int cantidadBloques;
    private ListaSimple bloquesAsignados;
    private Permisos permisos;
    private String fechaCreacion;
    private String fechaModificacion;
    private String contenido;
    
    public Archivo(String nombre, String propietario, int tamaño) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.tamaño = tamaño;
        this.cantidadBloques = 0;
        this.bloquesAsignados = new ListaSimple();
        this.permisos = new Permisos(propietario);
        this.fechaCreacion = obtenerFechaActual();
        this.fechaModificacion = this.fechaCreacion;
        this.contenido = "";
    }
    
    public boolean escribirContenido(String nuevoContenido, String usuario, boolean esAdmin) {
        if (!permisos.puedeEscribir(usuario, esAdmin)) {
            System.out.println("Error: Sin permisos de escritura sobre " + nombre);
            return false;
        }
        
        this.contenido = nuevoContenido;
        this.tamaño = nuevoContenido.length();
        this.fechaModificacion = obtenerFechaActual();
        System.out.println("Archivo '" + nombre + "' escrito. Tamaño: " + tamaño + " caracteres");
        return true;
    }
    
    public String leerContenido(String usuario, boolean esAdmin) {
        if (!permisos.puedeLeer(usuario, esAdmin)) {
            System.out.println("Error: Sin permisos de lectura sobre " + nombre);
            return null;
        }
        
        System.out.println("Leyendo archivo '" + nombre + "': " + contenido.length() + " caracteres");
        return contenido;
    }
    
    public boolean agregarContenido(String contenidoAdicional, String usuario, boolean esAdmin) {
        if (!permisos.puedeEscribir(usuario, esAdmin)) {
            System.out.println("Error: Sin permisos de escritura sobre " + nombre);
            return false;
        }
        
        this.contenido += contenidoAdicional;
        this.tamaño = this.contenido.length();
        this.fechaModificacion = obtenerFechaActual();
        System.out.println("Contenido agregado a '" + nombre + "'. Nuevo tamaño: " + tamaño + " caracteres");
        return true;
    }
    
    public boolean vaciarContenido(String usuario, boolean esAdmin) {
        if (!permisos.puedeEscribir(usuario, esAdmin)) {
            System.out.println("Error: Sin permisos para vaciar " + nombre);
            return false;
        }
        
        this.contenido = "";
        this.tamaño = 0;
        this.fechaModificacion = obtenerFechaActual();
        System.out.println("Archivo '" + nombre + "' vaciado");
        return true;
    }
    
    public boolean renombrar(String nuevoNombre, String usuario, boolean esAdmin) {
        if (!permisos.puedeEscribir(usuario, esAdmin)) {
            System.out.println("Error: Sin permisos para renombrar " + nombre);
            return false;
        }
        
        String nombreAnterior = this.nombre;
        this.nombre = nuevoNombre;
        this.fechaModificacion = obtenerFechaActual();
        System.out.println("Archivo '" + nombreAnterior + "' renombrado a '" + nuevoNombre + "'");
        return true;
    }
    
    public void asignarBloque(Bloque bloque) {
        bloquesAsignados.insertFinal(bloque);
        cantidadBloques++;
    }
    
    public void liberarBloques() {
        bloquesAsignados.clear();
        cantidadBloques = 0;
    }
    
    public int obtenerPrimerBloque() {
        if (bloquesAsignados.isEmpty() || cantidadBloques == 0) {
            return -1;
        }
        Bloque primerBloque = (Bloque) bloquesAsignados.get(0);
        return primerBloque.getNumeroBloque();
    }
    
    public boolean establecerLecturaPublica(boolean permitido, String usuario, boolean esAdmin) {
        if (!propietario.equals(usuario) && !esAdmin) {
            System.out.println("Error: Solo el propietario o administrador puede cambiar permisos");
            return false;
        }
        
        permisos.establecerLecturaPublica(permitido);
        System.out.println("Lectura pública " + (permitido ? "habilitada" : "deshabilitada"));
        return true;
    }
    
    public boolean establecerEscrituraPublica(boolean permitido, String usuario, boolean esAdmin) {
        if (!propietario.equals(usuario) && !esAdmin) {
            System.out.println("Error: Solo el propietario o administrador puede cambiar permisos");
            return false;
        }
        
        permisos.establecerEscrituraPublica(permitido);
        System.out.println("Escritura pública " + (permitido ? "habilitada" : "deshabilitada"));
        return true;
    }
    
    public boolean puedeLeer(String usuario, boolean esAdmin) {
        return permisos.puedeLeer(usuario, esAdmin);
    }
    
    public boolean puedeEscribir(String usuario, boolean esAdmin) {
        return permisos.puedeEscribir(usuario, esAdmin);
    }
    
    public boolean puedeEjecutar(String usuario, boolean esAdmin) {
        return permisos.puedeEjecutar(usuario, esAdmin);
    }
    
    public String getNombre() { return nombre; }
    public String getPropietario() { return propietario; }
    public int getTamaño() { return tamaño; }
    public int getCantidadBloques() { return cantidadBloques; }
    public ListaSimple getBloquesAsignados() { return bloquesAsignados; }
    public Permisos getPermisos() { return permisos; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaModificacion() { return fechaModificacion; }
    public String getContenido() { return contenido; }
    
    public void mostrarInformacion() {
        System.out.println("=== INFORMACIÓN DEL ARCHIVO ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Propietario: " + propietario);
        System.out.println("Tamaño: " + tamaño + " caracteres");
        System.out.println("Bloques: " + cantidadBloques);
        System.out.println("Primer bloque: " + obtenerPrimerBloque());
        System.out.println("Creado: " + fechaCreacion);
        System.out.println("Modificado: " + fechaModificacion);
        System.out.println("Permisos: " + permisos.obtenerCadenaPermisos());
        System.out.println("Contenido: \"" + (contenido.length() > 50 ? contenido.substring(0, 50) + "..." : contenido) + "\"");
    }
    
    public String obtenerResumen() {
        return String.format("%-20s %-12s %-8d %-6d %-8d", 
            nombre, propietario, tamaño, cantidadBloques, obtenerPrimerBloque());
    }
    
    @Override
    public String toString() {
        return "Archivo{nombre='" + nombre + "', propietario='" + propietario + 
               "', tamaño=" + tamaño + " chars, bloques=" + cantidadBloques + "}";
    }
    
    private String obtenerFechaActual() {
        return java.time.LocalDateTime.now().toString();
    }
}