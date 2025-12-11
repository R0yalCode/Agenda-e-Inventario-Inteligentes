package ed.u2.search;

public class SearchStats {

    // estadísticas de cada búsqueda
    public long comparaciones;
    public long tiempoNs;
    public int resultadosEncontrados;

    // Reiniciar stats
    public void reset() {
        comparaciones = 0;
        tiempoNs = 0;
        resultadosEncontrados = 0;
    }

    // Métodos utilitarios
    public void addComparacion() {
        comparaciones++;
    }

    public void addResultado() {
        resultadosEncontrados++;
    }

    public void setTiempo(long ns) {
        tiempoNs = ns;
    }
}
