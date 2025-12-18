package ed.u2.stats;

import ed.u2.data.DatasetManager;
import ed.u2.sorting.*;
import ed.u2.util.ANSI;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SortingStatsManager {

    private static final int ALTURA_MAX = 20;
    private static final int ANCHO_BARRA = 14;

    public static void mostrar() {

        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED + "No hay dataset cargado." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.CYAN_BOLD +
                "\n=== ESTADÍSTICAS VISUALES DE ORDENACIÓN ===\n" + ANSI.RESET);

        Object[] base = DatasetManager.getArray();

        Object[] burbuja = Arrays.copyOf(base, base.length);
        Object[] seleccion = Arrays.copyOf(base, base.length);
        Object[] insercion = Arrays.copyOf(base, base.length);

        Map<String, OperationStats> stats = new LinkedHashMap<>();
        stats.put("Burbuja", BubbleSorter.sort((Comparable[]) burbuja, true));
        stats.put("Selección", SelectionSorter.sort((Comparable[]) seleccion, true));
        stats.put("Inserción", InsertionSorter.sort((Comparable[]) insercion, true));

        dibujarHistograma(stats);
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
                ANSI.BLUE_BOLD, // Selección
                ANSI.RED_BOLD // Inserción
        };

        // ---------- Dibujar barras ----------
        for (int nivel = ALTURA_MAX; nivel >= 1; nivel--) {

            for (int i = 0; i < alturas.size(); i++) {
                String key = (String) map.keySet().toArray()[i];
                int h = alturas.get(key);

                if (h >= nivel) {
                    System.out.print(colores[i] + "█".repeat(ANCHO_BARRA) + ANSI.RESET);
                } else {
                    System.out.print(" ".repeat(ANCHO_BARRA));
                }
                System.out.print("   ");
            }
            System.out.println();
        }

        // ---------- Línea base ----------
        System.out.println("─".repeat((ANCHO_BARRA + 3) * alturas.size()));

        // ---------- Nombres ----------
        int i = 0;
        for (String nombre : map.keySet()) {
            System.out.print(colores[i++] +
                    centrar(nombre, ANCHO_BARRA) +
                    ANSI.RESET + "   ");
        }
        System.out.println("\n");

        // ---------- Estadísticas ----------
        mostrarDetalle(map, colores);
    }

    // =========================================================

    private static void mostrarDetalle(Map<String, OperationStats> map, String[] colores) {

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

        System.out.println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                MEJOR ALGORITMO DE ORDENACIÓN           ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf(
                "║ Algoritmo               ║ %-28s ║\n", mejor);
        System.out.printf(
                "║ Tiempo (ns)             ║ %-28d ║\n", mejorTiempo);
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
}
