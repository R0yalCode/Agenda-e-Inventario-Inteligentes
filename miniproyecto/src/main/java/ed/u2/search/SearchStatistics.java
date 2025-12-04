package ed.u2.search;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Clase que almacena estadísticas sobre cualquier búsqueda:
 *  - clave buscada
 *  - ocurrencias encontradas
 *  - tiempo de ejecución
 *  - algoritmo utilizado
 */
public class SearchStatistics {

    private String algoritmo;
    private String clave;
    private int coincidencias;
    private long tiempoNs;
    private String marcaTiempo;

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getCoincidencias() {
        return coincidencias;
    }

    public void setCoincidencias(int coincidencias) {
        this.coincidencias = coincidencias;
    }

    public long getTiempoNs() {
        return tiempoNs;
    }

    public void setTiempoNs(long tiempoNs) {
        this.tiempoNs = tiempoNs;
    }

    public String getMarcaTiempo() {
        return marcaTiempo;
    }

    public void setMarcaTiempo(String marcaTiempo) {
        this.marcaTiempo = marcaTiempo;
    }

    @Override
    public String toString() {
        return "\n=== ESTADÍSTICAS DE BÚSQUEDA ===\n" +
                "Algoritmo:     " + algoritmo + "\n" +
                "Clave:         " + clave + "\n" +
                "Coincidencias: " + coincidencias + "\n" +
                "Tiempo (ns):   " + tiempoNs + "\n" +
                "Fecha:         " + marcaTiempo + "\n";
    }

    public String[] toCsvRow() {
        return new String[]{
                algoritmo,
                clave,
                String.valueOf(coincidencias),
                String.valueOf(tiempoNs),
                marcaTiempo
        };
    }
}
