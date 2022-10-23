/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * Esta clase representa a las tareas que se crean
 * para realizar durante los pomodoros.
 * 
 * @author Arguello, Encinas, García
 */
public class Tarea {
    private ObjectId id;
    private String nombre_desc;
    private String estado;

    public Tarea() {
        
    }

    public Tarea(String nombre_desc, String estado) {
        this.nombre_desc = nombre_desc;
        this.estado = estado;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre_desc() {
        return nombre_desc;
    }

    public void setNombre_desc(String nombre_desc) {
        this.nombre_desc = nombre_desc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.nombre_desc);
        hash = 83 * hash + Objects.hashCode(this.estado);
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tarea other = (Tarea) obj;
        if (!Objects.equals(this.nombre_desc, other.nombre_desc)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tareas{" + "id=" + id + ", nombre_desc=" + nombre_desc + '}';
    }
    
}
