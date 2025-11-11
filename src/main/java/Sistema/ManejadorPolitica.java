/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistema;
import EDD.ListaSimple;

/**
 *
 * @author Andres Salgueiro
 */
public interface ManejadorPolitica { //CLASE ABSTRACTA PARA OPTIMIZAR SU USO
    String getPolicyName();
    int getNextBlock(ListaSimple pendingRequests, int currentHead);
    void setCurrentHead(int head);
}
