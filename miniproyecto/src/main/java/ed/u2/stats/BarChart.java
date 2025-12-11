package ed.u2.stats;

import ed.u2.util.ANSI;
import java.util.Map;

public class BarChart {

    /**
     * Imprime un gráfico de barras ASCII horizontal
     */
    public static void mostrar(String titulo, Map<String, Integer> datos) {

        System.out.println(ANSI.CYAN_BOLD + "\n=== " + titulo + " ===" + ANSI.RESET);

        if (datos.isEmpty()) {
            System.out.println(ANSI.RED + "No hay datos para mostrar." + ANSI.RESET);
            return;
        }

        int maxValue = datos.values().stream().mapToInt(i -> i).max().orElse(1);

        for (var entry : datos.entrySet()) {
            int barras = ChartUtils.scale(entry.getValue(), maxValue, 50);
            System.out.printf("%-20s | %s %d\n",
                    entry.getKey(),
                    ChartUtils.repeat('█', barras),
                    entry.getValue());
        }
    }
}
