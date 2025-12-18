package ed.u2.stats;

import ed.u2.search.SearchStats;
import ed.u2.util.ANSI;

import java.util.Map;

public class SearchStatsManager {

    private static final int ALTURA_MAX = 15;
    private static final String BLOQUE = "██";

    // =========================================================
    public static void mostrar(Map<String, SearchStats> statsMap) {

        if (statsMap == null || statsMap.isEmpty()) {
            System.out.println(ANSI.RED + "No hay estadísticas de búsquedas." + ANSI.RESET);
            return;
        }

        System.out.println(
                ANSI.CYAN_BOLD +
                "\n=== ESTADÍSTICAS VISUALES DE BÚSQUEDAS ===\n" +
                ANSI.RESET
        );

        long maxTiempo = statsMap.values().stream()
                .mapToLong(s -> s.tiempoNs)
                .max()
                .orElse(1);

        // ----- HISTOGRAMA -----
        for (int nivel = ALTURA_MAX; nivel >= 1; nivel--) {

            for (SearchStats st : statsMap.values()) {

                int altura = (int) ((double) st.tiempoNs / maxTiempo * ALTURA_MAX);

                if (altura >= nivel) {
                    System.out.print(ANSI.BLUE_BOLD + BLOQUE + ANSI.RESET + "   ");
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }

        // ----- NOMBRES -----
        for (String nombre : statsMap.keySet()) {
            System.out.printf(
                    ANSI.YELLOW_BOLD + "%-12s" + ANSI.RESET + " ",
                    nombre
            );
        }
        System.out.println("\n");

        // ----- DETALLES -----
        mostrarDetalle(statsMap);
    }

    // =========================================================
    private static void mostrarDetalle(Map<String, SearchStats> statsMap) {

        String mejor = "";
        long mejorTiempo = Long.MAX_VALUE;

        for (var e : statsMap.entrySet()) {

            SearchStats st = e.getValue();

            System.out.println(ANSI.CYAN_BOLD + e.getKey() + ANSI.RESET);
            System.out.println(" Tiempo (ns)     : " + st.tiempoNs);
            System.out.println(" Comparaciones   : " + st.comparaciones);
            System.out.println(" Resultados      : " + st.resultadosEncontrados);
            System.out.println();

            if (st.tiempoNs < mejorTiempo) {
                mejorTiempo = st.tiempoNs;
                mejor = e.getKey();
            }
        }

        System.out.println(ANSI.GREEN +
                "\n╔════════════════════════════════════════════════════════╗");
        System.out.println(
                "║               MEJOR BÚSQUEDA EJECUTADA                ║");
        System.out.println(
                "╠════════════════════════════════════════════════════════╣");
        System.out.printf(
                "║ Algoritmo              ║ %-28s ║%n", mejor);
        System.out.printf(
                "║ Tiempo (ns)            ║ %-28d ║%n", mejorTiempo);
        System.out.println(
                "╚════════════════════════════════════════════════════════╝"
                + ANSI.RESET);
    }
}
