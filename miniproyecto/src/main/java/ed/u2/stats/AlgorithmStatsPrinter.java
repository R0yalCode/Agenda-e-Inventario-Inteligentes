package ed.u2.stats;

import ed.u2.util.ANSI;
import java.util.*;

public class AlgorithmStatsPrinter {

    public static void print(String titulo, Map<String,OperationStats> stats) {

        System.out.println(ANSI.YELLOW_BOLD + "\n=== " + titulo + " ===" + ANSI.RESET);

        // Buscar máximos para la barra
        long maxTime = stats.values().stream().mapToLong(OperationStats::getTime).max().orElse(1);

        for (var e : stats.entrySet()) {
            String name = e.getKey();
            OperationStats s = e.getValue();

            int barLen = (int)((double)s.getTime() / maxTime * 40);

            System.out.printf(
                ANSI.CYAN + "%-20s " + ANSI.GREEN + "%s" + ANSI.WHITE + " %d ns\n" + ANSI.RESET,
                name,
                "█".repeat(Math.max(1, barLen)),
                s.getTime()
            );

            if (s.getComparisons() > 0)
                System.out.println("   comparar.: " + s.getComparisons());

            if (s.getSwaps() > 0)
                System.out.println("   intercamb: " + s.getSwaps());

            if (s.isFound())
                System.out.println("   encontrado en índice: " + s.getIndex());

            if (s.getIndices() != null)
                System.out.println("   coincidencias: " + Arrays.toString(s.getIndices()));

            System.out.println();
        }

        // Conclusión automática
        var mejor = stats.entrySet().stream()
                .min(Comparator.comparingLong(e2 -> e2.getValue().getTime()))
                .orElse(null);

        if (mejor != null) {
            System.out.println(
                ANSI.GREEN_BOLD +
                "⇒ Mejor algoritmo: " + mejor.getKey() +
                " (" + mejor.getValue().getTime() + " ns)" +
                ANSI.RESET
            );
        }
    }
}
