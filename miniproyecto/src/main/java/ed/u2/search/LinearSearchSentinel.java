package ed.u2.search;

import ed.u2.util.ANSI;

/**
 * Búsqueda secuencial optimizada con Centinela.
 */
public class LinearSearchSentinel {

    private static void barra(int actual, int total) {
        int width = 35;
        int progreso = (actual * 100) / total;
        int llenos = (progreso * width) / 100;

        StringBuilder b = new StringBuilder("\r[");
        for (int i = 0; i < width; i++)
            b.append(i < llenos ? "=" : " ");
        b.append("] ").append(progreso).append("%");

        System.out.print(ANSI.CYAN + b + ANSI.RESET);
    }

    public static <T> SearchStatistics buscar(T[] arr, T clave, boolean barraProgreso) {

        long inicio = System.nanoTime();

        T ultimo = arr[arr.length - 1];
        arr[arr.length - 1] = clave;

        int i = 0;

        while (!arr[i].equals(clave)) {

            if (barraProgreso) barra(i, arr.length - 1);

            i++;
        }

        arr[arr.length - 1] = ultimo;

        boolean encontrado = (i < arr.length - 1) || ultimo.equals(clave);

        long fin = System.nanoTime();

        SearchStatistics stats = new SearchStatistics();
        stats.setAlgoritmo("Secuencial Centinela");
        stats.setClave(String.valueOf(clave));
        stats.setCoincidencias(encontrado ? 1 : 0);
        stats.setTiempoNs(fin - inicio);
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }
}
