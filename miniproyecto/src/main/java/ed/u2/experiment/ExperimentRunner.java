package ed.u2.experiment;

import ed.u2.sorting.*;
import ed.u2.stats.OperationStats;
import ed.u2.util.ANSI;

import java.util.Arrays;

/**
 * Ejecuta experimentos de ordenación con múltiples corridas
 * y calcula medianas según especificación del PDF.
 */
public class ExperimentRunner {

    // ============================================================
    // CONSTANTES DE EXPERIMENTACIÓN
    // ============================================================
    private static final int TOTAL_CORRIDAS = 13;      // 10 válidas + 3 de descarte
    private static final int CORRIDAS_DESCARTE = 3;
    private static final int CORRIDAS_VALIDAS = 10;

    // ============================================================
    // EXPERIMENTO PRINCIPAL
    // ============================================================

    /**
     * Ejecuta experimento completo para un dataset y algoritmo.
     *
     * @param arrOriginal Array original (no modificado)
     * @param algoritmo   "burbuja", "seleccion", "insercion"
     * @param ascendente  true para ascendente
     * @return Estadísticas con medianas
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> OperationStats ejecutarExperimento(
            T[] arrOriginal,
            String algoritmo,
            boolean ascendente) {

        System.out.println(ANSI.CYAN + "\n[EXPERIMENTO] Iniciando " + TOTAL_CORRIDAS +
                " corridas (" + CORRIDAS_DESCARTE + " descarte)" + ANSI.RESET);

        // Arrays para almacenar resultados de cada corrida
        long[] tiempos = new long[TOTAL_CORRIDAS];
        long[] comparaciones = new long[TOTAL_CORRIDAS];
        long[] swaps = new long[TOTAL_CORRIDAS];

        // Ejecutar todas las corridas
        for (int i = 0; i < TOTAL_CORRIDAS; i++) {

            // 1. Crear copia fresca del array original
            T[] copia = Arrays.copyOf(arrOriginal, arrOriginal.length);

            // 2. Medir tiempo y ejecutar algoritmo
            long inicio = System.nanoTime();
            OperationStats stats = ejecutarAlgoritmo(copia, algoritmo, ascendente);
            long fin = System.nanoTime();

            // 3. Almacenar resultados
            tiempos[i] = fin - inicio;
            comparaciones[i] = stats.getComparisons();
            swaps[i] = stats.getSwaps();

            // 4. Mostrar progreso
            System.out.print(ANSI.YELLOW + "  Corrida " + (i+1) + "/" + TOTAL_CORRIDAS);
            System.out.println(": " + (fin-inicio)/1_000_000 + " ms" + ANSI.RESET);

            // Pequeña pausa para evitar optimizaciones de JVM
            try { Thread.sleep(10); } catch (Exception e) {}
        }

        // Calcular medianas (descartando primeras 3)
        long medianaTiempo = calcularMediana(tiempos, CORRIDAS_DESCARTE);
        long medianaComparaciones = calcularMediana(comparaciones, CORRIDAS_DESCARTE);
        long medianaSwaps = calcularMediana(swaps, CORRIDAS_DESCARTE);

        // Crear objeto de resultados
        OperationStats resultado = new OperationStats();
        resultado.setTime(medianaTiempo);
        resultado.setComparisons(medianaComparaciones);
        resultado.setSwaps(medianaSwaps);

        return resultado;
    }

    // ============================================================
    // EJECUTAR ALGORITMO ESPECÍFICO
    // ============================================================

    private static <T extends Comparable<T>> OperationStats ejecutarAlgoritmo(
            T[] arr, String algoritmo, boolean ascendente) {

        return switch (algoritmo.toLowerCase()) {
            case "burbuja" -> BubbleSorter.sort(arr, ascendente);
            case "seleccion" -> SelectionSorter.sort(arr, ascendente);
            case "insercion" -> InsertionSorter.sort(arr, ascendente);
            default -> {
                System.out.println(ANSI.RED + "Algoritmo desconocido: " + algoritmo + ANSI.RESET);
                yield new OperationStats();
            }
        };
    }

    // ============================================================
    // CÁLCULO DE MEDIANA
    // ============================================================

    /**
     * Calcula la mediana de un array, descartando los primeros n elementos.
     */
    private static long calcularMediana(long[] datos, int descartar) {
        if (datos.length <= descartar) return 0;

        // 1. Copiar solo los datos válidos
        long[] validos = new long[datos.length - descartar];
        System.arraycopy(datos, descartar, validos, 0, validos.length);

        // 2. Ordenar
        Arrays.sort(validos);

        // 3. Calcular mediana
        int n = validos.length;
        if (n % 2 == 1) {
            // Impar: elemento central
            return validos[n / 2];
        } else {
            // Par: promedio de dos centrales
            return (validos[n/2 - 1] + validos[n/2]) / 2;
        }
    }

    // ============================================================
    // FORMATO DE TABLA
    // ============================================================

    /**
     * Imprime tabla comparativa de algoritmos.
     */
    public static void imprimirTablaComparativa(
            String nombreDataset,
            OperationStats statsBurbuja,
            OperationStats statsSeleccion,
            OperationStats statsInsercion) {

        System.out.println(ANSI.CYAN_BOLD + "\n" +
                "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        COMPARACIÓN DE ALGORITMOS DE ORDENACIÓN      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf("║ Dataset: %-43s ║\n", nombreDataset);
        System.out.println("╠══════════════╦═══════════════╦══════════╦══════════════╣");
        System.out.println("║ Algoritmo    ║ Comparaciones ║ Swaps    ║ Tiempo (ns)  ║");
        System.out.println("╠══════════════╬═══════════════╬══════════╬══════════════╣");

        imprimirFila("Burbuja", statsBurbuja);
        imprimirFila("Selección", statsSeleccion);
        imprimirFila("Inserción", statsInsercion);

        System.out.println("╚══════════════╩═══════════════╩══════════╩══════════════╝" + ANSI.RESET);
    }

    private static void imprimirFila(String nombre, OperationStats stats) {
        if (stats == null) return;

        String comp = formatNumber(stats.getComparisons());
        String sw = formatNumber(stats.getSwaps());
        String tiempo = formatNumber(stats.getTime());

        System.out.printf("║ %-12s ║ %13s ║ %8s ║ %12s ║\n",
                nombre, comp, sw, tiempo);
    }

    private static String formatNumber(long num) {
        if (num >= 1_000_000) {
            return String.format("%,dM", num / 1_000_000);
        } else if (num >= 1_000) {
            return String.format("%,dk", num / 1_000);
        }
        return String.format("%,d", num);
    }
}