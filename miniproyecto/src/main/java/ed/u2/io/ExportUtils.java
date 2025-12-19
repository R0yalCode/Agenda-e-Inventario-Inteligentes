package ed.u2.io;

import ed.u2.stats.SortingStatsManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class ExportUtils {

    private static final Path EXPORT_DIR = Paths.get("resources", "export");

    public static boolean exportEstadisticas() {
        try {
            Files.createDirectories(EXPORT_DIR);
            exportSortingStats();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================================================
    // ORDENACIÓN
    // ======================================================

    private static void exportSortingStats() throws IOException {

        Map<String, ?> map = SortingStatsManager.exportarEstadisticas();

        Path out = EXPORT_DIR.resolve("sorting_stats.csv");

        try (BufferedWriter bw = Files.newBufferedWriter(out,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            if (map == null || map.isEmpty()) {
                bw.write("Algoritmo,Tiempo(ns),Comparaciones,Intercambios");
                bw.newLine();
                bw.write("SIN_DATOS,0,0,0");
                return;
            }

            bw.write("Algoritmo,Tiempo(ns),Comparaciones,Intercambios");
            bw.newLine();

            for (var e : map.entrySet()) {
                var st = (ed.u2.stats.OperationStats) e.getValue();

                bw.write(String.format(
                        "%s,%d,%d,%d",
                        e.getKey(),
                        st.getTime(),
                        st.getComparisons(),
                        st.getSwaps()
                ));
                bw.newLine();
            }

            // Calculate the best algorithm using the same logic as in mostrarDetalle
            // Use SortingStatsManager to get the best algorithm
            var bestEntry = SortingStatsManager.obtenerMejorAlgoritmo((Map<String, ed.u2.stats.OperationStats>) map);

            if (bestEntry != null) {
                String bestAlgorithm = bestEntry.getKey();
                long bestTime = bestEntry.getValue().getTime();

                bw.newLine();
                bw.write(String.format("Mejor Algoritmo: %s con Tiempo(ns): %d", bestAlgorithm, bestTime));
            } else {
                bw.newLine();
                bw.write("Mejor Algoritmo: No disponible");
            }
        }
    }


}
