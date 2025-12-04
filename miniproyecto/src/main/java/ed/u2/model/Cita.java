package ed.u2.model;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Modelo que representa una Cita en el sistema.
 *
 * Comparable: se ordena por fecha (String ISO), luego por id_cita.
 */
public class Cita implements Comparable<Cita> {

    private int idCita;
    private String fecha;        // formato YYYY-MM-DD
    private String idPaciente;
    private String estado;

    public Cita(int idCita, String idPaciente, String fecha, String estado) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.idPaciente = idPaciente;
        this.estado = estado;
    }

    // -------------------------------
    // GETTERS Y SETTERS
    // -------------------------------
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // -------------------------------
    // COMPARABLE
    // -------------------------------
    @Override
    public int compareTo(Cita o) {
        int cmp = this.fecha.compareTo(o.fecha);
        if (cmp != 0) return cmp;

        return Integer.compare(this.idCita, o.idCita);
    }

    // -------------------------------
    // TO STRING
    // -------------------------------
    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", idPaciente='" + idPaciente + '\'' +
                ", fecha='" + fecha + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
