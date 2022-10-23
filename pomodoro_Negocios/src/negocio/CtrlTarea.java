/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio;

import java.util.List;
import objetos.Tarea;
import repositorios.FabricaDatos;
import repositorios.RepTarea;

/**
 *
 * @author Arguello, Encinas, García
 */
public class CtrlTarea {
    
    public FabricaDatos f = new FabricaDatos();
    private RepTarea repTarea = f.getRepTarea();

    /**
     * Método que regresa una lista de todas las tareas.
     *
     * @return regresa una lista de todas las tareas.
     */
    public List<Tarea> consultar() {
        return repTarea.consultar();
    }

    /**
     * Método que guarda una Tarea
     *
     * @param tarea Tarea a guardar
     */
    public void guardar(Tarea tarea) {
        repTarea.guardar(tarea);
    }

    /**
     * Método que elimina una tarea.
     *
     * @param tarea Tarea a eliminar
     */
    public void eliminar(Tarea tarea) {
        repTarea.eliminar(tarea);
    }

    /**
     * Método que actualiza una tarea
     *
     * @param tarea Tarea a actualizar
     */
    public void actualizar(Tarea tarea) {
        repTarea.actualizar(tarea);
    }

    /**
     * Método que busca tareas por estado
     * 
     * @param estado estado de las tareas a buscar
     * @return regresa una lista de todas las tareas con estado coincidente
     */
    public List<Tarea> buscarEstado(String estado) {
        List<Tarea> tareasB = repTarea.buscarEstado(estado);
        return tareasB;
    }
    
}
