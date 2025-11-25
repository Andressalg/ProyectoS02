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
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int EXECUTE = 4;
    public static final int ALL = READ | WRITE | EXECUTE;
    
    private String owner;
    private int userPermissions;  // Permisos del dueño
    private int groupPermissions; // Permisos del grupo
    private int otherPermissions; // Permisos de otros
    
    public Permisos(String owner) {
        this.owner = owner;
        this.userPermissions = ALL;
        this.groupPermissions = READ;
        this.otherPermissions = READ;
    }
    
    public Permisos(String owner, int userPerm, int groupPerm, int otherPerm) {
        this.owner = owner;
        this.userPermissions = userPerm;
        this.groupPermissions = groupPerm;
        this.otherPermissions = otherPerm;
    }
    
    // Verificar permisos
    public boolean canRead(String username, boolean isAdmin) {
        if (isAdmin) return true;
        if (owner.equals(username)) return (userPermissions & READ) != 0;
        return (otherPermissions & READ) != 0; // Simplificado para el proyecto
    }
    
    public boolean canWrite(String username, boolean isAdmin) {
        if (isAdmin) return true;
        if (owner.equals(username)) return (userPermissions & WRITE) != 0;
        return (otherPermissions & WRITE) != 0;
    }
    
    public boolean canExecute(String username, boolean isAdmin) {
        if (isAdmin) return true;
        if (owner.equals(username)) return (userPermissions & EXECUTE) != 0;
        return (otherPermissions & EXECUTE) != 0;
    }
    
    // Setters para permisos
    public void setUserPermissions(int permissions) {
        this.userPermissions = permissions;
    }
    
    public void setGroupPermissions(int permissions) {
        this.groupPermissions = permissions;
    }
    
    public void setOtherPermissions(int permissions) {
        this.otherPermissions = permissions;
    }
    
    public void setPublicRead(boolean allowed) {
        if (allowed) {
            otherPermissions |= READ;
        } else {
            otherPermissions &= ~READ;
        }
    }
    
    public void setPublicWrite(boolean allowed) {
        if (allowed) {
            otherPermissions |= WRITE;
        } else {
            otherPermissions &= ~WRITE;
        }
    }
    
    // Getters
    public String getOwner() {
        return owner;
    }
    
    public int getUserPermissions() {
        return userPermissions;
    }
    
    public int getGroupPermissions() {
        return groupPermissions;
    }
    
    public int getOtherPermissions() {
        return otherPermissions;
    }
    
    public String getPermissionString() {
        return String.format("U:%d G:%d O:%d", userPermissions, groupPermissions, otherPermissions);
    }
    
    @Override
    public String toString() {
        return "Permission{owner='" + owner + "', permissions=" + getPermissionString() + "}";
    }
}