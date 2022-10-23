/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio;

/**
 * Fabrica de negocios para los controladores
 *
 * @author Arguello, Encinas, García
 */
public class FabricaNegocios {

    private CtrlTarea tarea;

    /**
     * Método que permite recuperar el controlador de Cliente
     *
     * @return Controlador Cliente
     */
    public CtrlTarea getCtrlTarea() {
        if (tarea != null) {
            return tarea;
        } else {
            tarea = new CtrlTarea();
            return tarea;
        }
    }
}
