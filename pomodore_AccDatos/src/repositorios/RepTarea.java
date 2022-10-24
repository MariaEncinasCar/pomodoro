/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositorios;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import objetos.Tarea;
import static repositorios.RepBase.baseDatos;
import org.bson.Document;
import java.util.List;
import java.util.regex.Pattern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

/**
 *
 * @author Arguello, Encinas, García
 */
public class RepTarea {
    
    MongoCollection<Tarea> tareas = baseDatos.getCollection("Tarea", Tarea.class);
    
    /**
     * Método que regresa una lista de todas las tareas.
     * @return regresa una lista de todas las tareas
     */
    public List<Tarea> consultar() {
        List<Tarea> tareasList = new ArrayList<>();
        tareas.find().into(tareasList);
        return tareasList;
    }

    /**
     * Método que guarda una Tarea
     * @param tarea Tarea a guardar
     */
    public void guardar(Tarea tarea) {
        tareas.insertOne(tarea);
    }

    /**
     * Método que elimina un Tareas
     * @param tarea Tarea a eliminar
     */
    public void eliminar(Tarea tarea) {
        tareas.deleteOne(Filters.eq("_id", tarea.getId()));
    }
    
    /**
     * Método que actualiza una Tarea
     * @param tarea Tarea a actualizar
     */
    public void actualizar(Tarea tarea){
        tareas.updateOne(Filters.eq("_id", tarea.getId()), 
                Updates.set("nombre_desc",tarea.getNombre_desc()));
        tareas.updateOne(Filters.eq("_id", tarea.getId()), 
                Updates.set("estado",tarea.getEstado()));
    }
    
    /**
     * Método que busca tareas por estado
     * @param estado Estado a buscar
     * @return regresa una lista de todas las tareas con estado coincidente
     */
    public List<Tarea> buscarEstado(String estado) {
        List<Tarea> tareasB = new ArrayList<>();
        
        Document regQuery = new Document();
        regQuery.append("$regex", "(?)" + Pattern.quote(estado));
        regQuery.append("$options", "i");
        
        Document findQuery = new Document();
        findQuery.append("estado", regQuery);
        
        FindIterable<Document> iterable = baseDatos.getCollection("Tarea").find(findQuery);
        MongoCursor<Document> cursor = iterable.iterator();
        
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String n = document.getString("nombre_desc");
            
            Tarea  busqueda = tareas.find(Filters.eq("nombre_desc", n)).first();
            
            tareasB.add(busqueda);
        }
        
        return tareasB;
    }
    
}
