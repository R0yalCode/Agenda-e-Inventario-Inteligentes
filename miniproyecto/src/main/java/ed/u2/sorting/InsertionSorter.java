package ed.u2.sorting;

import ed.u2.util.ANSI;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Algoritmo de Inserción (Insertion Sort).
 */
public class InsertionSorter {

    public static <T extends Comparable<T>> void sort(
            T[] arr, boolean asc, boolean mostrarBarra) {

        int n = arr.length;

        for (int i = 1; i < n; i++) {

            T key = arr[i];
            int j = i - 1;

            while (j >= 0 &&
                    (asc ? arr[j].compareTo(key) > 0
                         : arr[j].compareTo(key) < 0)) {

                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;

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

        System.out.print(ANSI.GREEN + barra + ANSI.RESET);
    }
}
