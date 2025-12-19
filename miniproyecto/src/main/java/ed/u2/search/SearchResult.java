package ed.u2.search;

public class SearchResult {

     private Object resultado;
    private SearchStats stats;

    public SearchResult(Object resultado, SearchStats stats) {
        this.resultado = resultado;
        this.stats = stats;
    }

    public Object getResultado() {
        return resultado;
    }

    public SearchStats getStats() {
        return stats;
    }
    
}
