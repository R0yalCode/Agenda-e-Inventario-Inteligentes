package ed.u2.search;

import ed.u2.util.ANSI;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Implementación de Búsqueda Binaria en arreglos ordenados.
 */
public class BinarySearch {

    private static void barra(int actual, int total) {
        int width = 35;
        int progreso = (actual * 100) / total;
        int llenos = (progreso * width) / 100;

        StringBuilder b = new StringBuilder("\r[");
        for (int i = 0; i < width; i++)
            b.append(i < llenos ? "=" : " ");
        b.append("] ").append(progreso).append("%");

        System.out.print(ANSI.GREEN + b + ANSI.RESET);
    }

    public static <T extends Comparable<T>> SearchStatistics buscar(
            T[] arr, T clave, boolean barraProgreso) {

        long inicio = System.nanoTime();

        int inicioIdx = 0;
        int finIdx = arr.length - 1;
        int pasos = 0;

        boolean encontrado = false;

        while (inicioIdx <= finIdx) {

            if (barraProgreso) barra(pasos++, arr.length);

            int mid = (inicioIdx + finIdx) / 2;
            int cmp = clave.compareTo(arr[mid]);

            if (cmp == 0) {
                encontrado = true;
                break;
            }
            if (cmp > 0)
                inicioIdx = mid + 1;
            else
                finIdx = mid - 1;
        }

        long fin = System.nanoTime();

        SearchStatistics stats = new SearchStatistics();
        stats.setAlgoritmo("Binaria");
        stats.setClave(String.valueOf(clave));
        stats.setCoincidencias(encontrado ? 1 : 0);
        stats.setTiempoNs(fin - inicio);
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }
}
