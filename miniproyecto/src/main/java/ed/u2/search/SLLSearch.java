package ed.u2.search;

import ed.u2.sll.Node;
import ed.u2.sll.SinglyLinkedList;
import ed.u2.util.ANSI;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Búsqueda secuencial en una Lista Enlazada Simple (SLL)
 */
public class SLLSearch {

    private static void barra(int actual, int total) {
        int width = 35;
        int progreso = (actual * 100) / total;
        int llenos = (progreso * width) / 100;

        StringBuilder b = new StringBuilder("\r[");
        for (int i = 0; i < width; i++)
            b.append(i < llenos ? "=" : " ");
        b.append("] ").append(progreso).append("%");

        System.out.print(ANSI.MAGENTA + b + ANSI.RESET);
    }

    public static <T> SearchStatistics buscar(
            SinglyLinkedList<T> lista, T clave, boolean barraProgreso) {

        long inicio = System.nanoTime();

        Node<T> actual = lista.getHead();
        int index = 0;
        int encontrados = 0;

        while (actual != null) {

            if (barraProgreso) barra(index, lista.size());

            if (actual.getData().equals(clave)) {
                encontrados++;
            }

            actual = actual.getNext();
            index++;
        }

        long fin = System.nanoTime();

        SearchStatistics stats = new SearchStatistics();
        stats.setAlgoritmo("SLL - Secuencial");
        stats.setClave(String.valueOf(clave));
        stats.setCoincidencias(encontrados);
        stats.setTiempoNs(fin - inicio);
        stats.setMarcaTiempo(java.time.LocalDateTime.now().toString());

        return stats;
    }
}
