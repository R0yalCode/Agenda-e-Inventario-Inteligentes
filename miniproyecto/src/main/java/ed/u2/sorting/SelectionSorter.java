package ed.u2.sorting;

import ed.u2.util.ANSI;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Algoritmo de Selección (Selection Sort).
 */
public class SelectionSorter {

    public static <T extends Comparable<T>> void sort(
            T[] arr, boolean asc, boolean mostrarBarra) {

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            int idx = i;

            for (int j = i + 1; j < n; j++) {

                boolean condicion =
                        asc ? arr[j].compareTo(arr[idx]) < 0
                            : arr[j].compareTo(arr[idx]) > 0;

                if (condicion) {
                    idx = j;
                }
            }

            T temp = arr[i];
            arr[i] = arr[idx];
            arr[idx] = temp;

            if (mostrarBarra)
                mostrarProgreso(i, n);
        }

        if (mostrarBarra)
            System.out.println();
    }

    private static void mostrarProgreso(int actual, int total) {

        int width = 35;
        int progreso = (actual * 100) / (total - 1);
        int llenos = (progreso * width) / 100;

        StringBuilder barra = new StringBuilder();
        barra.append("\r[");

        for (int i = 0; i < width; i++) {
            barra.append(i < llenos ? "=" : " ");
        }

        barra.append("] ").append(progreso).append("%");

        System.out.print(ANSI.YELLOW + barra + ANSI.RESET);
    }
}
