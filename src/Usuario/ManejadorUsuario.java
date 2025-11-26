/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Usuario;
import EDD.ListaSimple;

/**
 *
 * @author Andres Salgueiro
 */
public class ManejadorUsuario {
    
    private ListaSimple users;
    private Usuario currentUser;
    
    public ManejadorUsuario() {
        this.users = new ListaSimple();
        this.currentUser = null;
        initializeDefaultUsers();
    }
    
    private void initializeDefaultUsers() {
        // Crear usuario administrador por defecto
        Usuario admin = new Usuario("admin", "admin123");
        users.insertFinal(admin);
        
        // Crear usuario regular por defecto
        Usuario user = new Usuario("user", "user123");
        users.insertFinal(user);
    }
    
    public boolean login(String username, String password) {
        for (int i = 0; i < users.getSize(); i++) {
            Usuario user = (Usuario) users.get(i);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public boolean register(String username, String password, boolean isAdmin) {
        // Verificar si el usuario ya existe
        if (userExists(username)) {
            return false;
        }
        
        Usuario newUser = new Usuario(username, password);
        users.insertFinal(newUser);
        return true;
    }
    
    public boolean deleteUser(String username) {
        // No permitir eliminar al usuario actual
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            return false;
        }
        
        for (int i = 0; i < users.getSize(); i++) {
            Usuario user = (Usuario) users.get(i);
            if (user.getUsername().equals(username)) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }
    
    private boolean userExists(String username) {
        for (int i = 0; i < users.getSize(); i++) {
            Usuario user = (Usuario) users.get(i);
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public Usuario getCurrentUser() {
        return currentUser;
    }
    
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    
    public ListaSimple getAllUsers() {
        return users;
    }
    
    public Usuario getUserByUsername(String username) {
        for (int i = 0; i < users.getSize(); i++) {
            Usuario user = (Usuario) users.get(i);
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public boolean changePassword(String username, String newPassword) {
        Usuario user = getUserByUsername(username);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
