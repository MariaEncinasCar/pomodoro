/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositorios;

/**
 *
 * @author Arguello, Encinas, García
 */
public class FabricaDatos {
    
    private RepTarea repTarea;

    /**
     * Método que permite recuperar el repositorio de las tareas
     *
     * @return Repositorio tarea
     */
    public RepTarea getRepTarea() {
        if (repTarea != null) {
            return repTarea;
        } else {
            repTarea = new RepTarea();
            return repTarea;
        }
    }
}
