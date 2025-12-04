package ed.u2.sorting;

import ed.u2.util.ANSI;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Implementación del algoritmo Burbuja (Bubble Sort).
 *
 * Incluye:
 *  - Orden asc / desc
 *  - Barra de progreso visual
 */
public class BubbleSorter {

    public static <T extends Comparable<T>> void sort(
            T[] arr, boolean asc, boolean mostrarBarra) {

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                boolean condicion =
                        asc ? arr[j].compareTo(arr[j + 1]) > 0
                            : arr[j].compareTo(arr[j + 1]) < 0;

                if (condicion) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }

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

        System.out.print(ANSI.BLUE + barra + ANSI.RESET);
    }
}
