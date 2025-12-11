package ed.u2.stats;

import ed.u2.util.ANSI;
import java.util.List;
import java.util.TreeMap;

public class Histogram {

    /**
     * Histogramas para listas de enteros (edades, stock, etc.)
     */
    public static void mostrar(String titulo, List<Integer> valores, int bins) {

        System.out.println(ANSI.CYAN_BOLD + "\n=== " + titulo + " ===" + ANSI.RESET);

        if (valores.isEmpty()) {
            System.out.println(ANSI.RED + "No hay valores." + ANSI.RESET);
            return;
        }

        int min = valores.stream().min(Integer::compare).orElse(0);
        int max = valores.stream().max(Integer::compare).orElse(0);

        int rango = max - min + 1;
        int ancho = Math.max(1, rango / bins);

        TreeMap<String, Integer> hist = new TreeMap<>();

        for (int v : valores) {
            int bin = ((v - min) / ancho) * ancho + min;
            String key = "[" + bin + "-" + (bin + ancho - 1) + "]";
            hist.put(key, hist.getOrDefault(key, 0) + 1);
        }

        BarChart.mostrar(titulo, hist);
    }
}
