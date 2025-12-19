package ed.u2.stats;

import ed.u2.data.DatasetManager;
import ed.u2.util.ANSI;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SortingStatsManager {

    private static final int ALTURA_MAX = 15;
    private static final int ANCHO_BARRA = 14;
    // Cache for computed statistics so mostrar() and export use identical data
    private static Map<String, OperationStats> cachedStats = null;

    public static void mostrar() {

        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED + "No hay dataset cargado." + ANSI.RESET);
            return;
        }

        // Use cached statistics (computed once per dataset load)
        Map<String, OperationStats> stats = obtenerEstadisticasCacheadas();

        System.out.println(ANSI.CYAN_BOLD +
                "\n=== ESTADÍSTICAS VISUALES DE ORDENACIÓN ===\n" +
                ANSI.RESET);

        dibujarHistograma(stats);
    }

    // =========================================================
    // EJECUCIÓN AISLADA POR ALGORITMO
    // =========================================================
    @SuppressWarnings("unchecked")
    private static OperationStats ejecutar(
            SortFunction sorter,
            Object[] snapshotBase) {

        //   Copia FRESCA para este algoritmo
        Comparable[] data = Arrays.copyOf(
                snapshotBase,
                snapshotBase.length,
                Comparable[].class
        );

        return sorter.sort(data, true);
    }

    // =========================================================
    // HISTOGRAMA VERTICAL
    // =========================================================
    private static void dibujarHistograma(Map<String, OperationStats> map) {

        long maxTiempo = map.values().stream()
                .mapToLong(OperationStats::getTime)
                .max()
                .orElse(1);

        Map<String, Integer> alturas = new LinkedHashMap<>();
        for (var e : map.entrySet()) {
            int h = (int) ((double) e.getValue().getTime() / maxTiempo * ALTURA_MAX);
            alturas.put(e.getKey(), Math.max(1, h));
        }

        String[] colores = {
                ANSI.YELLOW_BOLD, // Burbuja
                ANSI.BLUE_BOLD,   // Selección
                ANSI.RED_BOLD     // Inserción
        };

        // ───── BARRAS ─────
        for (int nivel = ALTURA_MAX; nivel >= 1; nivel--) {
            int i = 0;
            for (String key : alturas.keySet()) {
                if (alturas.get(key) >= nivel) {
                    System.out.print(colores[i] + "█".repeat(ANCHO_BARRA) + ANSI.RESET);
                } else {
                    System.out.print(" ".repeat(ANCHO_BARRA));
                }
                System.out.print("   ");
                i++;
            }
            System.out.println();
        }

        // ───── BASE ─────
        System.out.println("─".repeat((ANCHO_BARRA + 3) * alturas.size()));

        // ───── NOMBRES ─────
        int i = 0;
        for (String nombre : map.keySet()) {
            System.out.print(colores[i++] +
                    centrar(nombre, ANCHO_BARRA) +
                    ANSI.RESET + "   ");
        }

        System.out.println("\n");
        mostrarDetalle(map, colores);
    }

    // =========================================================
    // DETALLE + MEJOR ALGORITMO
    // =========================================================
    private static void mostrarDetalle(
            Map<String, OperationStats> map,
            String[] colores) {

        String mejor = "";
        long mejorTiempo = Long.MAX_VALUE;

        int i = 0;
        for (var e : map.entrySet()) {

            OperationStats st = e.getValue();

            System.out.println(colores[i++] + e.getKey() + ANSI.RESET);
            System.out.println(" Tiempo (ns)     : " + st.getTime());
            System.out.println(" Comparaciones   : " + st.getComparisons());
            System.out.println(" Intercambios    : " + st.getSwaps());
            System.out.println();

            if (st.getTime() < mejorTiempo) {
                mejorTiempo = st.getTime();
                mejor = e.getKey();
            }
        }

        System.out.println(ANSI.GREEN +
                "\n╔════════════════════════════════════════════════════════╗");
        System.out.println(
                "║                MEJOR ALGORITMO DE ORDENACIÓN           ║");
        System.out.println(
                "╠════════════════════════════════════════════════════════╣");
        System.out.printf(
                "║ Algoritmo               ║ %-28s ║%n", mejor);
        System.out.printf(
                "║ Tiempo (ns)             ║ %-28d ║%n", mejorTiempo);
        System.out.println(
                "╚════════════════════════════════════════════════════════╝"
                        + ANSI.RESET);
    }

    // =========================================================
    private static String centrar(String txt, int ancho) {
        if (txt.length() >= ancho) return txt.substring(0, ancho);
        int left = (ancho - txt.length()) / 2;
        int right = ancho - txt.length() - left;
        return " ".repeat(left) + txt + " ".repeat(right);
    }

    // =========================================================
    // INTERFAZ FUNCIONAL (LIMPIA)
    // =========================================================
    @FunctionalInterface
    private interface SortFunction {
        OperationStats sort(Comparable[] arr, boolean asc);
    }

// ================= EXPORT SUPPORT =================
public static Map<String, OperationStats> exportarEstadisticas() {
    return obtenerEstadisticasCacheadas();
}

    // =========================================================
    // MÉTODO PARA OBTENER EL MEJOR ALGORITMO
    // =========================================================
    public static Map.Entry<String, OperationStats> obtenerMejorAlgoritmo(Map<String, OperationStats> map) {
        String mejor = "";
        long mejorTiempo = Long.MAX_VALUE;
        Map.Entry<String, OperationStats> mejorEntrada = null;

        for (var e : map.entrySet()) {
            OperationStats st = e.getValue();
            if (st.getTime() < mejorTiempo) {
                mejorTiempo = st.getTime();
                mejor = e.getKey();
                mejorEntrada = e;
            }
        }
        return mejorEntrada;
    }

    // =========================================================
    // CACHED STATS HELPERS
    // =========================================================
    public static Map<String, OperationStats> obtenerEstadisticasCacheadas() {
        if (!DatasetManager.hayDataset()) {
            throw new IllegalStateException("No hay dataset cargado.");
        }

        if (cachedStats == null) {
            Object[] base = DatasetManager.getArray();
            cachedStats = SortingBenchmark.ejecutar(base);
        }
        return cachedStats;
    }

    public static void clearCache() {
        cachedStats = null;
    }
}


