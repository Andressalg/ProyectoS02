/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestionArchivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ManejadorTXT {

    public static void guardar(DefaultTreeModel modelo, File archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) modelo.getRoot();
            if (root == null) return;
            escribirNodo(root, "", bw);
        }
    }

    private static void escribirNodo(DefaultMutableTreeNode nodo, String prefijo, BufferedWriter bw) throws IOException {
        String nombre = nodo.getUserObject() == null ? "" : nodo.getUserObject().toString();
        String ruta = prefijo.isEmpty() ? nombre : prefijo + "/" + nombre;
        bw.write(ruta);
        bw.newLine();
        for (int i = 0; i < nodo.getChildCount(); i++) {
            DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) nodo.getChildAt(i);
            escribirNodo(hijo, ruta, bw);
        }
    }

    public static DefaultTreeModel cargar(File archivo) throws IOException {
        DefaultMutableTreeNode root = null;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split("/");
                if (partes.length == 0) continue;
                if (root == null) {
                    root = new DefaultMutableTreeNode(partes[0]);
                } else {
                }
                DefaultMutableTreeNode actual = root;
                int inicio = (actual.getUserObject().toString().equals(partes[0])) ? 1 : 0;
                for (int i = inicio; i < partes.length; i++) {
                    String nombre = partes[i];
                    DefaultMutableTreeNode hijo = buscarHijo(actual, nombre);
                    if (hijo == null) {
                        hijo = new DefaultMutableTreeNode(nombre);
                        actual.add(hijo);
                    }
                    actual = hijo;
                }
            }
        }
        if (root == null) root = new DefaultMutableTreeNode("Sistema");
        return new DefaultTreeModel(root);
    }

    private static DefaultMutableTreeNode buscarHijo(DefaultMutableTreeNode padre, String nombre) {
        for (int i = 0; i < padre.getChildCount(); i++) {
            DefaultMutableTreeNode hijo = (DefaultMutableTreeNode) padre.getChildAt(i);
            if (nombre.equals(hijo.getUserObject().toString())) return hijo;
        }
        return null;
    }
}