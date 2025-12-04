package ed.u2.sorting;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Representa los resultados estadísticos de una ordenación.
 *
 * Se utiliza para:
 *  - Mostrar resultados por pantalla
 *  - Exportar estadísticas a CSV
 *  - Guardar el historial de análisis
 */
public class SortStatistics {

    private long tiempoNs;
    private int totalElementos;
    private String orden; // "Ascendente" o "Descendente"
    private String algoritmo; // Bubble, Selection, Insertion (opcional)
    private String marcaTiempo; // fecha y hora

    // ---------------------------
    // GETTERS Y SETTERS
    // ---------------------------
    public long getTiempoNs() {
        return tiempoNs;
    }

    public void setTiempoNs(long tiempoNs) {
        this.tiempoNs = tiempoNs;
    }

    public int getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(int totalElementos) {
        this.totalElementos = totalElementos;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public String getMarcaTiempo() {
        return marcaTiempo;
    }

    public void setMarcaTiempo(String marcaTiempo) {
        this.marcaTiempo = marcaTiempo;
    }

    // ---------------------------
    // FORMATO DE TEXTO
    // ---------------------------
    @Override
    public String toString() {
        return "\n=== ESTADÍSTICAS DE ORDENACIÓN ===\n" +
                "Algoritmo:       " + algoritmo + "\n" +
                "Orden:           " + orden + "\n" +
                "Elementos:       " + totalElementos + "\n" +
                "Tiempo (ns):     " + tiempoNs + "\n" +
                "Fecha de análisis: " + marcaTiempo + "\n";
    }

    /**
     * Devuelve una fila lista para exportar a CSV.
     */
    public String[] toCsvRow() {
        return new String[]{
                algoritmo,
                orden,
                String.valueOf(totalElementos),
                String.valueOf(tiempoNs),
                marcaTiempo
        };
    }
}
