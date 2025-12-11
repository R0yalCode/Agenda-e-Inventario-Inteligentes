package ed.u2.model;

import java.time.LocalDateTime;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Representa una cita médica con:
 *  - id
 *  - apellido del paciente
 *  - fecha y hora de la cita
 */
public class Cita implements Comparable<Cita> {

    private String id;
    private String apellido;
    private LocalDateTime fechaHora;

    // CONSTRUCTOR QUE EL CSV NECESITA
    public Cita(String id, String apellido, LocalDateTime fechaHora) {
        this.id = id;
        this.apellido = apellido;
        this.fechaHora = fechaHora;
    }

   
    // GETTERS / SETTERS
    public String getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    
    // compareTo — criterio de ordenación por defecto

    @Override
    public int compareTo(Cita o) {
        // Ordenamiento por fecha y hora
        return this.fechaHora.compareTo(o.fechaHora);
    }

    // toString (para mostrar en lista)

    @Override
    public String toString() {
        return id + " | " + apellido + " | " + fechaHora;
    }

    
}
