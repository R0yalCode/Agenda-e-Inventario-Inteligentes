package ed.u2.stats;

import java.util.LinkedHashMap;
import java.util.Map;

public class SortingStatsRepository {

    private static final Map<String, OperationStats> stats = new LinkedHashMap<>();

    private SortingStatsRepository() {}

    public static void registrar(String nombre, OperationStats st) {
        if (st != null) {
            stats.put(nombre, st);
        }
    }

    public static Map<String, OperationStats> getAll() {
        return stats;
    }

    public static boolean hayDatos() {
        return !stats.isEmpty();
    }

    public static void clear() {
        stats.clear();
    }
}
