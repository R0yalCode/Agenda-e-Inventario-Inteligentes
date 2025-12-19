package ed.u2.model;

/**
 * Autor:  R
 * Fecha: 2025
 *
 * Representa un paciente con:
 *  - id (PAC-0001)
 *  - apellido (Zambrano)
 *  - prioridad (1,2,3)
 */
public class Paciente implements Comparable<Paciente> {

    private String id;
    private String apellido;
    private int prioridad;

    // ============================================================
    // CONSTRUCTOR COMPATIBLE CON CsvLoader
    // ============================================================

    public Paciente(String id, String apellido, int prioridad) {
        this.id = id;
        this.apellido = apellido;
        this.prioridad = prioridad;
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public int getPrioridad() {
        return prioridad;
    }

    // ============================================================
    // compareTo – criterio de ordenación por defecto
    // ============================================================

    @Override
    public int compareTo(Paciente o) {
        // Ordenar por prioridad ASC (1 = más urgente)
        int cmp = Integer.compare(this.prioridad, o.prioridad);
        if (cmp != 0) return cmp;

        // Si prioridades son iguales, por apellido
        return this.apellido.compareTo(o.apellido);
    }

    // ============================================================
    // toString
    // ============================================================

    @Override
    public String toString() {
        return id + " | " + apellido + " | prioridad=" + prioridad;
    }
}
