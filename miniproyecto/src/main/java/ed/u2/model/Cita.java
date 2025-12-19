package ed.u2.model;

import java.time.LocalDateTime;

public class Cita implements Comparable<Cita> {

    private String id;
    private String apellido;
    private LocalDateTime fechaHora;

    public Cita(String id, String apellido, LocalDateTime fechaHora) {
        this.id = id;
        this.apellido = apellido;
        this.fechaHora = fechaHora;
    }

    public String getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    // =====================================================
    // ORDEN NATURAL: POR FECHA Y HORA
    // =====================================================
    @Override
    public int compareTo(Cita otra) {
        return this.fechaHora.compareTo(otra.fechaHora);
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id='" + id + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
