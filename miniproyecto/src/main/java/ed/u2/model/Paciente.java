package ed.u2.model;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Modelo para un Paciente.
 *
 * Comparable: se ordena alfabéticamente por nombre.
 */
public class Paciente implements Comparable<Paciente> {

    private int idPaciente;
    private String nombre;
    private String telefono;

    public Paciente(int idPaciente, String nombre, String telefono) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // -------------------------------
    // GETTERS Y SETTERS
    // -------------------------------
    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // -------------------------------
    // COMPARABLE
    // -------------------------------
    @Override
    public int compareTo(Paciente o) {
        return this.nombre.compareToIgnoreCase(o.nombre);
    }

    // -------------------------------
    // TO STRING
    // -------------------------------
    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
