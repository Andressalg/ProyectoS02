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

    private static void anadirDirectorio(javax.swing.JTree arbol, DefaultTreeModel modelo) {
        String nombre = preguntarpornombre("Nombre del directorio:");
        if (nombre == null) return;
        DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo.getRoot();
        DefaultMutableTreeNode nuevodirectorio = new DefaultMutableTreeNode(nombre);
        modelo.insertNodeInto(nuevodirectorio, raiz, raiz.getChildCount());
        TreePath camino = new TreePath(nuevodirectorio.getPath());
        arbol.scrollPathToVisible(camino);
        arbol.setSelectionPath(camino);
    }

    private static void anadirArchivo(javax.swing.JTree arbol, DefaultTreeModel modelo) {
        TreePath seleccion = arbol.getSelectionPath();
        DefaultMutableTreeNode directorio;
        if (seleccion == null) {
            directorio = (DefaultMutableTreeNode) modelo.getRoot();
        } else {
            directorio = (DefaultMutableTreeNode) seleccion.getLastPathComponent();
        }
        String nombre = preguntarpornombre("Nombre del nodo hijo:");
        if (nombre == null) return;
        DefaultMutableTreeNode nuevoarchivo = new DefaultMutableTreeNode(nombre);
        modelo.insertNodeInto(nuevoarchivo, directorio, directorio.getChildCount());
        TreePath camino = seleccion == null ? new TreePath(nuevoarchivo.getPath()) : seleccion.pathByAddingChild(nuevoarchivo);
        arbol.expandPath(new TreePath(directorio.getPath()));
        arbol.scrollPathToVisible(camino);
        arbol.setSelectionPath(camino);
    }

    private static void renombrarNodo(javax.swing.JTree arbol, DefaultTreeModel modelo) {
        TreePath seleccion = arbol.getSelectionPath();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un archivo/directorio para renombrar.");
            return;
        }
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) seleccion.getLastPathComponent();
        String nodoactual = nodo.getUserObject().toString();
        String nuevo = JOptionPane.showInputDialog(null, "Nuevo nombre:", nodoactual);
        if (nuevo == null) return;
        nuevo = nuevo.trim();
        if (nuevo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre vacío. Operación cancelada.");
            return;
        }
        nodo.setUserObject(nuevo);
        modelo.nodeChanged(nodo);
    }

    private static void removerNodo(javax.swing.JTree arbol, DefaultTreeModel modelo) {
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
        modelo.removeNodeFromParent(nodo);
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
