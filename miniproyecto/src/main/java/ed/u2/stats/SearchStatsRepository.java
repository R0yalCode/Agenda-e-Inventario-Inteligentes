package ed.u2.stats;

import ed.u2.search.SearchStats;

import java.util.LinkedHashMap;
import java.util.Map;

public class SearchStatsRepository {

    private static final Map<String, SearchStats> stats = new LinkedHashMap<>();

    private SearchStatsRepository() {}

    public static void registrar(String nombre, SearchStats st) {
        stats.put(nombre, st);
    }

    public static Map<String, SearchStats> getAll() {
        return stats;
    }

    public static boolean hayDatos() {
        return !stats.isEmpty();
    }
}
