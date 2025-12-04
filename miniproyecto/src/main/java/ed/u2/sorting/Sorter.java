package ed.u2.sorting;

import java.util.function.BiConsumer;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Clase utilitaria general para ejecutar algoritmos de ordenación y
 * obtener métricas de rendimiento de forma unificada.
 *
 * Entrada:
 *  - Un arreglo T[] a ordenar
 *  - Algoritmo (lambda o referencia a método)
 *  - Orden asc/desc
 *
 * Salida:
 *  - Objeto SortStatistics con toda la información de ejecución
 */
public class Sorter {

    /**
     * Ejecuta un algoritmo de ordenación y calcula las estadísticas.
     *
     * @param arr          Arreglo de datos
     * @param algoritmo    Función de ordenamiento (T[] arr, Boolean asc)
     * @param asc          Si es ascendente o descendente
     * @param <T>          Tipo genérico que extiende Comparable
     * @return             Objeto SortStatistics con los resultados
     */
    public static <T extends Comparable<T>> SortStatistics ejecutar(
            T[] arr,
            BiConsumer<T[], Boolean> algoritmo,
            boolean asc
    ) {

        long inicio = System.nanoTime();

        algoritmo.accept(arr, asc);

        long fin = System.nanoTime();

        long tiempo = fin - inicio;

        SortStatistics stats = new SortStatistics();
        stats.setTiempoNs(tiempo);
        stats.setTotalElementos(arr.length);
        stats.setOrden(asc ? "Ascendente" : "Descendente");
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }
}
