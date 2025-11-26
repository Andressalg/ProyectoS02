/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Usuario;

/**
 *
 * @author Andres Salgueiro
 */

public class Permisos {
    public static final int LECTURA = 1;
    public static final int ESCRITURA = 2;
    public static final int EJECUCION = 4;
    public static final int TODOS = LECTURA | ESCRITURA | EJECUCION;
    
    private String propietario;
    private int permisosUsuario;   // Permisos del propietario
    private int permisosGrupo;     // Permisos del grupo
    private int permisosOtros;     // Permisos de otros usuarios
    
    public Permisos(String propietario) {
        this.propietario = propietario;
        this.permisosUsuario = TODOS;
        this.permisosGrupo = LECTURA;
        this.permisosOtros = LECTURA;
    }
    
    public Permisos(String propietario, int permisosUsuario, int permisosGrupo, int permisosOtros) {
        this.propietario = propietario;
        this.permisosUsuario = permisosUsuario;
        this.permisosGrupo = permisosGrupo;
        this.permisosOtros = permisosOtros;
    }
    
    // ===== VERIFICACIÓN DE PERMISOS =====
    
    public boolean puedeLeer(String usuario, boolean esAdmin) {
        if (esAdmin) return true;
        if (propietario.equals(usuario)) return (permisosUsuario & LECTURA) != 0;
        return (permisosOtros & LECTURA) != 0;
    }
    
    public boolean puedeEscribir(String usuario, boolean esAdmin) {
        if (esAdmin) return true;
        if (propietario.equals(usuario)) return (permisosUsuario & ESCRITURA) != 0;
        return (permisosOtros & ESCRITURA) != 0;
    }
    
    public boolean puedeEjecutar(String usuario, boolean esAdmin) {
        if (esAdmin) return true;
        if (propietario.equals(usuario)) return (permisosUsuario & EJECUCION) != 0;
        return (permisosOtros & EJECUCION) != 0;
    }
    
    // ===== CONFIGURACIÓN DE PERMISOS DEL PROPIETARIO =====
    
    public void establecerPermisosUsuario(int permisos) {
        this.permisosUsuario = permisos;
    }
    
    public void habilitarLecturaUsuario(boolean habilitar) {
        if (habilitar) {
            permisosUsuario |= LECTURA;
        } else {
            permisosUsuario &= ~LECTURA;
        }
    }
    
    public void habilitarEscrituraUsuario(boolean habilitar) {
        if (habilitar) {
            permisosUsuario |= ESCRITURA;
        } else {
            permisosUsuario &= ~ESCRITURA;
        }
    }
    
    public void habilitarEjecucionUsuario(boolean habilitar) {
        if (habilitar) {
            permisosUsuario |= EJECUCION;
        } else {
            permisosUsuario &= ~EJECUCION;
        }
    }
    
    // ===== CONFIGURACIÓN DE PERMISOS PÚBLICOS =====
    
    public void establecerPermisosOtros(int permisos) {
        this.permisosOtros = permisos;
    }
    
    public void establecerLecturaPublica(boolean permitido) {
        if (permitido) {
            permisosOtros |= LECTURA;
        } else {
            permisosOtros &= ~LECTURA;
        }
    }
    
    public void establecerEscrituraPublica(boolean permitido) {
        if (permitido) {
            permisosOtros |= ESCRITURA;
        } else {
            permisosOtros &= ~ESCRITURA;
        }
    }
    
    public void establecerEjecucionPublica(boolean permitido) {
        if (permitido) {
            permisosOtros |= EJECUCION;
        } else {
            permisosOtros &= ~EJECUCION;
        }
    }
    
    // ===== CONFIGURACIÓN DE PERMISOS DE GRUPO =====
    
    public void establecerPermisosGrupo(int permisos) {
        this.permisosGrupo = permisos;
    }
    
    public void habilitarLecturaGrupo(boolean habilitar) {
        if (habilitar) {
            permisosGrupo |= LECTURA;
        } else {
            permisosGrupo &= ~LECTURA;
        }
    }
    
    public void habilitarEscrituraGrupo(boolean habilitar) {
        if (habilitar) {
            permisosGrupo |= ESCRITURA;
        } else {
            permisosGrupo &= ~ESCRITURA;
        }
    }
    
    // ===== GETTERS =====
    
    public String getPropietario() {
        return propietario;
    }
    
    public int getPermisosUsuario() {
        return permisosUsuario;
    }
    
    public int getPermisosGrupo() {
        return permisosGrupo;
    }
    
    public int getPermisosOtros() {
        return permisosOtros;
    }
    
    // ===== REPRESENTACIÓN DE PERMISOS =====
    
    public String obtenerCadenaPermisos() {
        return String.format("U:%d G:%d O:%d", permisosUsuario, permisosGrupo, permisosOtros);
    }
    
    public String obtenerCadenaSimbolica() {
        String usuario = permisosToSymbol(permisosUsuario);
        String grupo = permisosToSymbol(permisosGrupo);
        String otros = permisosToSymbol(permisosOtros);
        return usuario + grupo + otros;
    }
    
    private String permisosToSymbol(int permisos) {
        StringBuilder sb = new StringBuilder();
        sb.append((permisos & LECTURA) != 0 ? "r" : "-");
        sb.append((permisos & ESCRITURA) != 0 ? "w" : "-");
        sb.append((permisos & EJECUCION) != 0 ? "x" : "-");
        return sb.toString();
    }
    
    // ===== PERMISOS PREDEFINIDOS =====
    
    public void establecerPermisosPrivados() {
        permisosUsuario = TODOS;
        permisosGrupo = 0;
        permisosOtros = 0;
    }
    
    public void establecerPermisosPublicos() {
        permisosUsuario = TODOS;
        permisosGrupo = LECTURA | ESCRITURA;
        permisosOtros = LECTURA;
    }
    
    public void establecerPermisosSoloLectura() {
        permisosUsuario = LECTURA;
        permisosGrupo = LECTURA;
        permisosOtros = LECTURA;
    }
    
    // ===== VALIDACIÓN =====
    
    public boolean sonPermisosValidos(int permisos) {
        return permisos >= 0 && permisos <= 7; // 0-7 en octal (rwx)
    }
    
    public boolean tienePermisosCompletos(String usuario) {
        return propietario.equals(usuario) && permisosUsuario == TODOS;
    }
    
    public boolean esSoloLectura() {
        return permisosUsuario == LECTURA && permisosGrupo == LECTURA && permisosOtros == LECTURA;
    }
    
    // ===== INFORMACIÓN =====
    
    public void mostrarPermisos() {
        System.out.println("=== PERMISOS ===");
        System.out.println("Propietario: " + propietario);
        System.out.println("Usuario (U): " + obtenerCadenaPermisosDetallada(permisosUsuario));
        System.out.println("Grupo (G): " + obtenerCadenaPermisosDetallada(permisosGrupo));
        System.out.println("Otros (O): " + obtenerCadenaPermisosDetallada(permisosOtros));
        System.out.println("Representación: " + obtenerCadenaSimbolica());
    }
    
    private String obtenerCadenaPermisosDetallada(int permisos) {
        String lectura = (permisos & LECTURA) != 0 ? "Lectura" : "Sin lectura";
        String escritura = (permisos & ESCRITURA) != 0 ? "Escritura" : "Sin escritura";
        String ejecucion = (permisos & EJECUCION) != 0 ? "Ejecución" : "Sin ejecución";
        return String.format("%s, %s, %s", lectura, escritura, ejecucion);
    }
    
    @Override
    public String toString() {
        return "Permisos{propietario='" + propietario + "', permisos=" + obtenerCadenaSimbolica() + "}";
    }
}