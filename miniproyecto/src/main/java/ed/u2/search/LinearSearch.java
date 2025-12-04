package ed.u2.search;

import ed.u2.util.ANSI;
import java.util.ArrayList;
import java.util.List;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Búsqueda secuencial:
 *  - primera coincidencia
 *  - última coincidencia
 *  - todas las coincidencias (findAll)
 * Con barra de progreso opcional.
 */
public class LinearSearch {

    private static void barra(int actual, int total) {
        int width = 35;
        int progreso = (actual * 100) / total;
        int llenos = (progreso * width) / 100;

        StringBuilder b = new StringBuilder("\r[");
        for (int i = 0; i < width; i++)
            b.append(i < llenos ? "=" : " ");
        b.append("] ").append(progreso).append("%");

        System.out.print(ANSI.BLUE + b + ANSI.RESET);
    }

    // ==============================
    // PRIMERA COINCIDENCIA
    // ==============================
    public static <T> SearchStatistics primera(T[] arr, T clave, boolean barraProgreso) {

        long inicio = System.nanoTime();
        int encontrado = -1;

        for (int i = 0; i < arr.length; i++) {

            if (barraProgreso) barra(i, arr.length - 1);

            if (arr[i].equals(clave)) {
                encontrado = i;
                break;
            }
        }

        long fin = System.nanoTime();

        SearchStatistics stats = new SearchStatistics();
        stats.setAlgoritmo("Secuencial - Primera");
        stats.setClave(String.valueOf(clave));
        stats.setCoincidencias(encontrado == -1 ? 0 : 1);
        stats.setTiempoNs(fin - inicio);
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }

    // ==============================
    // ÚLTIMA COINCIDENCIA
    // ==============================
    public static <T> SearchStatistics ultima(T[] arr, T clave, boolean barraProgreso) {

        long inicio = System.nanoTime();
        int encontrado = -1;

        for (int i = 0; i < arr.length; i++) {

            if (barraProgreso) barra(i, arr.length - 1);

            if (arr[i].equals(clave)) {
                encontrado = i;
            }
        }

        long fin = System.nanoTime();

        SearchStatistics stats = new SearchStatistics();
        stats.setAlgoritmo("Secuencial - Última");
        stats.setClave(String.valueOf(clave));
        stats.setCoincidencias(encontrado == -1 ? 0 : 1);
        stats.setTiempoNs(fin - inicio);
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }

    // ==============================
    // FIND ALL (TODAS LAS COINCIDENCIAS)
    // ==============================
    public static <T> SearchStatistics findAll(T[] arr, T clave, boolean barraProgreso) {

        long inicio = System.nanoTime();
        List<Integer> encontrados = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {

            if (barraProgreso) barra(i, arr.length - 1);

            if (arr[i].equals(clave)) {
                encontrados.add(i);
            }
        }

        long fin = System.nanoTime();

        SearchStatistics stats = new SearchStatistics();
        stats.setAlgoritmo("Secuencial - Todas");
        stats.setClave(String.valueOf(clave));
        stats.setCoincidencias(encontrados.size());
        stats.setTiempoNs(fin - inicio);
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }
}
