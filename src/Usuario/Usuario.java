/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Usuario;
import Enums.ModoUsuario;

/**
 *
 * @author Andres Salgueiro
 */

public class Usuario {
    private String Nombre;
    private String Clave;
    private ModoUsuario usuario;
    
    public Usuario(String Nombre, String Clave) {
        this.Nombre = Nombre;
        this.Clave = Clave;
    }

    public Usuario(String usuario_actual, String pass, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Getters
    public String getUsername() {
        return Nombre;
    }
    
    public String getPassword() {
        return Clave;
    }
    
    
    // Setters
    public void setPassword(String password) {
        this.Clave = password;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Nombre.equals(usuario.usuario);
    }
}