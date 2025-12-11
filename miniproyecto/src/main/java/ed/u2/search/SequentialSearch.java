package ed.u2.search;

import ed.u2.util.ProgressBar;
import java.util.List;

public class SequentialSearch {

    // ============================================================
    // PRIMERA COINCIDENCIA
    // ============================================================

    public static <T> SearchStats first(T[] arr, T target) {

        SearchStats st = new SearchStats();
        long t0 = System.nanoTime();

        for (int i = 0; i < arr.length; i++) {

            st.comparaciones++;

            if (arr[i].equals(target)) {
                st.resultadosEncontrados = 1;
                break;
            }

            ProgressBar.mostrar((double) i / arr.length);
        }

        st.tiempoNs = System.nanoTime() - t0;
        return st;
    }

    // ============================================================
    // ÚLTIMA COINCIDENCIA
    // ============================================================

    public static <T> SearchStats last(T[] arr, T target) {

        SearchStats st = new SearchStats();
        long t0 = System.nanoTime();

        for (int i = arr.length - 1; i >= 0; i--) {

            st.comparaciones++;

            if (arr[i].equals(target)) {
                st.resultadosEncontrados = 1;
                break;
            }

            ProgressBar.mostrar((double) (arr.length - i) / arr.length);
        }

        st.tiempoNs = System.nanoTime() - t0;
        return st;
    }

    // ============================================================
    // ENCONTRAR TODOS
    // ============================================================

    public static <T> SearchStats findAll(T[] arr, T target, List<T> resultados) {

        SearchStats st = new SearchStats();
        long t0 = System.nanoTime();

        for (int i = 0; i < arr.length; i++) {

            st.comparaciones++;

            if (arr[i].equals(target)) {
                resultados.add(arr[i]);
                st.resultadosEncontrados++;
            }

            ProgressBar.mostrar((double) i / arr.length);
        }

        st.tiempoNs = System.nanoTime() - t0;
        return st;
    }

    // ============================================================
    // CENTINELA
    // ============================================================

    public static <T> SearchStats centinela(T[] arr, T target) {

        SearchStats st = new SearchStats();
        long t0 = System.nanoTime();

        T ultimo = arr[arr.length - 1];
        arr[arr.length - 1] = target;

        int i = 0;
        while (!arr[i].equals(target)) {
            st.comparaciones++;
            i++;

            ProgressBar.mostrar((double) i / arr.length);
        }

        arr[arr.length - 1] = ultimo;

        if (i < arr.length - 1 || ultimo.equals(target))
            st.resultadosEncontrados = 1;

        st.tiempoNs = System.nanoTime() - t0;
        return st;
    }
}
