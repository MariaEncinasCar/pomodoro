/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio;

import java.util.List;

/**
 * Interfaz de la conexión de negocios con la Fábrica de Negocios
 * @author Arguello, Encinas, García
 */
public interface INegocios {
    
    /**
     * Se inicializa la Fabrica Negocios
     */
    public FabricaNegocios f = new FabricaNegocios();
    
    /**
     * Método para buscar
     * @return Lista de todos los objetos de un objeto
     */
    public List<Object> buscar();
    
    /**
     * Método que permite guardar el objeto en el parámetro
     * @param obj Objeto a guardar
     */
    public void guardar(Object obj);
    
    /**
     * Método que permite eliminar el objeto en el parámetro
     * @param obj Objeto a eliminar
     */
    public void eliminar(Object obj);
    
    /**
     * Método que permite actualizar el objeto del parámetro
     * @param obj Objeto a actualizar
     */
    public void actualizar(Object obj);
}
