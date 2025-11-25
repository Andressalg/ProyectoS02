/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Sistema;

import Interfaz.ControlDeOperaciones;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Andres Salgueiro
 */
public class Sistema {

public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {
        ControlDeOperaciones frame = new ControlDeOperaciones();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    });
}
}
