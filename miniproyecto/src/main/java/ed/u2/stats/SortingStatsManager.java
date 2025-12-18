package ed.u2.stats;

import ed.u2.data.DatasetManager;
import ed.u2.sorting.*;
import ed.u2.util.ANSI;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SortingStatsManager {

    private static final int ALTURA_MAX = 15;
    private static final int ANCHO_BARRA = 12;
    private static final String BLOQUE = "██";

    // =========================================================
    // ENTRADA PRINCIPAL
    // =========================================================
    public static void mostrar() {

        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED + "No hay dataset cargado." + ANSI.RESET);
            return;
        }

        System.out.println(
                ANSI.CYAN_BOLD +
                        "\n=== ESTADÍSTICAS VISUALES DE ORDENACIÓN ===\n" +
                        ANSI.RESET);

        Object[] base = DatasetManager.getArray();

        Map<String, OperationStats> stats = new LinkedHashMap<>();

        Comparable[] burbuja = Arrays.copyOf(base, base.length, Comparable[].class);
        Comparable[] seleccion = Arrays.copyOf(base, base.length, Comparable[].class);
        Comparable[] insercion = Arrays.copyOf(base, base.length, Comparable[].class);

        stats.put("Burbuja", BubbleSorter.sort(burbuja, true));
        stats.put("Selección", SelectionSorter.sort(seleccion, true));
        stats.put("Inserción", InsertionSorter.sort(insercion, true));

        lastStats = stats; 
        dibujarHistograma(stats);

    
    }

    // =========================================================
    // HISTOGRAMA VERTICAL
    // =========================================================
    private static void dibujarHistograma(Map<String, OperationStats> stats) {

        long maxTiempo = stats.values().stream()
                .mapToLong(OperationStats::getTime)
                .max()
                .orElse(1);

        Map<String, Integer> alturas = new LinkedHashMap<>();
        for (var e : stats.entrySet()) {
            int h = (int) ((double) e.getValue().getTime() / maxTiempo * ALTURA_MAX);
            alturas.put(e.getKey(), Math.max(1, h));
        }

        String[] colores = {
                ANSI.YELLOW_BOLD,
                ANSI.BLUE_BOLD,
                ANSI.RED_BOLD
        };

        // ----- BARRAS -----
        for (int nivel = ALTURA_MAX; nivel >= 1; nivel--) {
            int i = 0;
            for (String key : stats.keySet()) {
                if (alturas.get(key) >= nivel) {
                    System.out.print(colores[i] + BLOQUE.repeat(ANCHO_BARRA / 2) + ANSI.RESET);
                } else {
                    System.out.print(" ".repeat(ANCHO_BARRA));
                }
                System.out.print("   ");
                i++;
            }
            System.out.println();
        }

        // ----- BASE -----
        System.out.println("─".repeat((ANCHO_BARRA + 3) * stats.size()));

        // ----- NOMBRES -----
        int i = 0;
        for (String nombre : stats.keySet()) {
            System.out.print(
                    colores[i++] +
                            centrar(nombre, ANCHO_BARRA) +
                            ANSI.RESET + "   ");
        }
        System.out.println("\n");

        mostrarDetalle(stats, colores);
    }

    // =========================================================
    // DETALLES + MEJOR
    // =========================================================
    private static void mostrarDetalle(Map<String, OperationStats> stats, String[] colores) {

        String mejor = "";
        long mejorTiempo = Long.MAX_VALUE;

        int i = 0;
        for (var e : stats.entrySet()) {

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
        if (txt.length() >= ancho)
            return txt.substring(0, ancho);
        int left = (ancho - txt.length()) / 2;
        int right = ancho - txt.length() - left;
        return " ".repeat(left) + txt + " ".repeat(right);
    }



    // ================= EXPORT SUPPORT =================

private static Map<String, OperationStats> lastStats;

public static Map<String, OperationStats> getLastStats() {
    return lastStats;
}

}
