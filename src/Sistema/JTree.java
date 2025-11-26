/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import GestionArchivos.ManejadorTXT;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Andres Salgueiro
 */
public class JTree {

    public static void anadirDirectorio(javax.swing.JTree arbol, DefaultTreeModel modelo) {
        String nombre = preguntarpornombre("Nombre del directorio:");
        if (nombre == null) return;
        DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo.getRoot();
        DefaultMutableTreeNode nuevodirectorio = new DefaultMutableTreeNode(nombre);
        modelo.insertNodeInto(nuevodirectorio, raiz, raiz.getChildCount());
        TreePath camino = new TreePath(nuevodirectorio.getPath());
        arbol.scrollPathToVisible(camino);
        arbol.setSelectionPath(camino);
    }

    public static Archivo anadirArchivo(javax.swing.JTree arbol, DefaultTreeModel modelo, String propietario) {
        TreePath seleccion = arbol.getSelectionPath();
        DefaultMutableTreeNode directorio;
        if (seleccion == null) {
            directorio = (DefaultMutableTreeNode) modelo.getRoot();
        } else {
            directorio = (DefaultMutableTreeNode) seleccion.getLastPathComponent();
        }
        String nombre = preguntarpornombre("Nombre del archivo:");
        if (nombre == null) return null;
        
        int cantidadBloques = preguntarporbloques("¿Cuántos bloques desea asignar?");
        if (cantidadBloques < 0) return null;
        
        Archivo nuevoArchivo = new Archivo(nombre, propietario, 0);
        
        for (int i = 0; i < cantidadBloques; i++) {
            Bloque bloque = new Bloque(i, 512);
            nuevoArchivo.asignarBloque(bloque);
        }
        
        DefaultMutableTreeNode nuevoArchivoNodo = new DefaultMutableTreeNode(nombre);
        modelo.insertNodeInto(nuevoArchivoNodo, directorio, directorio.getChildCount());
        
        TreePath camino = seleccion == null ? new TreePath(nuevoArchivoNodo.getPath()) : seleccion.pathByAddingChild(nuevoArchivoNodo);
        arbol.expandPath(new TreePath(directorio.getPath()));
        arbol.scrollPathToVisible(camino);
        arbol.setSelectionPath(camino);
        
        JOptionPane.showMessageDialog(null, "Archivo '" + nombre + "' creado con " + cantidadBloques + " bloque(s).");
        return nuevoArchivo;
    }

    private static int preguntarporbloques(String mensaje) {
        while (true) {
            String entrada = JOptionPane.showInputDialog(null, mensaje);
            if (entrada == null) return -1;
            
            entrada = entrada.trim();
            if (entrada.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Entrada vacía. Intente de nuevo.");
                continue;
            }
            
            try {
                int bloques = Integer.parseInt(entrada);
                if (bloques < 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor o igual a 0.");
                    continue;
                }
                return bloques;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    public static void renombrarNodo(javax.swing.JTree arbol, DefaultTreeModel modelo) {
        TreePath seleccion = arbol.getSelectionPath();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un archivo/directorio para renombrar.");
            return;
        }
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) seleccion.getLastPathComponent();
        Object userObj = nodo.getUserObject();
        String nodoactual = (userObj instanceof Archivo) ? ((Archivo) userObj).getNombre() : (userObj == null ? "" : userObj.toString());
        String nuevo = JOptionPane.showInputDialog(null, "Nuevo nombre:", nodoactual);
        if (nuevo == null) return;
        nuevo = nuevo.trim();
        if (nuevo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre vacío. Operación cancelada.");
            return;
        }

        if (userObj instanceof Archivo) {
            Archivo archivo = (Archivo) userObj;
            boolean ok = archivo.renombrar(nuevo, archivo.getPropietario(), false);
            if (!ok) {
                JOptionPane.showMessageDialog(null, "No tiene permisos para renombrar el archivo.");
                return;
            }
            nodo.setUserObject(archivo);
        } else {
            nodo.setUserObject(nuevo);
        }

        modelo.nodeChanged(nodo);
    }

    public static void removerNodo(javax.swing.JTree arbol, DefaultTreeModel modelo) {
        TreePath seleccion = arbol.getSelectionPath();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un archivo/directorio para eliminar.");
            return;
        }
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) seleccion.getLastPathComponent();
        if (nodo.getParent() == null) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar el nodo root.");
            return;
        }

        liberarBloquesRecursivo(nodo);

        modelo.removeNodeFromParent(nodo);
    }
    
    private static void liberarBloquesRecursivo(DefaultMutableTreeNode nodo) {
        Object obj = nodo.getUserObject();
        if (obj instanceof Archivo) {
            Archivo archivo = (Archivo) obj;
            archivo.liberarBloques();
        }
        for (int i = 0; i < nodo.getChildCount(); i++) {
            liberarBloquesRecursivo((DefaultMutableTreeNode) nodo.getChildAt(i));
        }
    }

    private static String preguntarpornombre(String mensaje) {
        String nombre = JOptionPane.showInputDialog(null, mensaje);
        if (nombre == null) return null;
        nombre = nombre.trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre vacío. Operación cancelada.");
            return null;
        }
        return nombre;
    }
    
    public static boolean guardarArbolEnCSV(javax.swing.JTree arbol, String rutaArchivo) {
        DefaultTreeModel modelo = (DefaultTreeModel) arbol.getModel();
        try {
            ManejadorTXT.guardar(modelo, new File(rutaArchivo));
            JOptionPane.showMessageDialog(null, "Guardado en: " + rutaArchivo);
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
            return false;
        }
    }

    public static boolean cargarArbolDesdeCSV(javax.swing.JTree arbol, String rutaArchivo) {
        try {
            DefaultTreeModel modelo = ManejadorTXT.cargar(new File(rutaArchivo));
            arbol.setModel(modelo);
            JOptionPane.showMessageDialog(null, "Cargado desde: " + rutaArchivo);
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar: " + ex.getMessage());
            return false;
        }
    }
}
