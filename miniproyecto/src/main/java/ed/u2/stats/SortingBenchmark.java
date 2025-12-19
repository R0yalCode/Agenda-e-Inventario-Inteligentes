package ed.u2.stats;

import ed.u2.sorting.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SortingBenchmark {

    public static Map<String, OperationStats> ejecutar(Object[] datasetBase) {

        Map<String, OperationStats> resultados = new LinkedHashMap<>();

        // Bubble
        resultados.put(
                "Burbuja",
                ejecutarAlgoritmo("bubble", datasetBase)
        );

        // Selection
        resultados.put(
                "Selección",
                ejecutarAlgoritmo("selection", datasetBase)
        );

        // Insertion
        resultados.put(
                "Inserción",
                ejecutarAlgoritmo("insertion", datasetBase)
        );

        return resultados;
    }

    @SuppressWarnings("unchecked")
    private static OperationStats ejecutarAlgoritmo(String tipo, Object[] base) {

        //  COPIA LIMPIA, SIEMPRE DESDE EL ORIGINAL
        Comparable[] copia = Arrays.copyOf(base, base.length, Comparable[].class);

        return switch (tipo) {
            case "bubble" -> BubbleSorter.sort(copia, true);
            case "selection" -> SelectionSorter.sort(copia, true);
            case "insertion" -> InsertionSorter.sort(copia, true);
            default -> null;
        };
    }
}
