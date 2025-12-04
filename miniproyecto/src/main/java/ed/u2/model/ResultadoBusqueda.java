package ed.u2.model;

import java.util.List;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Clase encargada de representar los resultados de una búsqueda,
 * permitiendo exportarlos, mostrarlos en pantalla y almacenarlos en historial.
 *
 * Entrada: Datos generados durante una operación de búsqueda.
 * Salida: Estructura con información clara y reutilizable sobre los resultados.
 */
public class ResultadoBusqueda {

    private String tipoBusqueda;       // Ej: "Secuencial - Primera"
    private String claveBuscada;       // Valor buscado
    private List<Integer> indices;     // Resultados encontrados
    private long tiempoNs;             // Tiempo en nanosegundos
    private int totalResultados;       // Conteo de coincidencias

    public ResultadoBusqueda() {
    }

    public ResultadoBusqueda(String tipoBusqueda, String claveBuscada,
                             List<Integer> indices, long tiempoNs) {
        this.tipoBusqueda = tipoBusqueda;
        this.claveBuscada = claveBuscada;
        this.indices = indices;
        this.tiempoNs = tiempoNs;
        this.totalResultados = (indices != null) ? indices.size() : 0;
    }

    // -----------------------------
    // GETTERS Y SETTERS
    // -----------------------------

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getClaveBuscada() {
        return claveBuscada;
    }

    public void setClaveBuscada(String claveBuscada) {
        this.claveBuscada = claveBuscada;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public long getTiempoNs() {
        return tiempoNs;
    }

    public void setTiempoNs(long tiempoNs) {
        this.tiempoNs = tiempoNs;
    }

    public int getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(int totalResultados) {
        this.totalResultados = totalResultados;
    }

    @Override
    public String toString() {
        return "ResultadoBusqueda{" +
                "tipoBusqueda='" + tipoBusqueda + '\'' +
                ", claveBuscada='" + claveBuscada + '\'' +
                ", indices=" + indices +
                ", tiempoNs=" + tiempoNs +
                ", totalResultados=" + totalResultados +
                '}';
    }
}
