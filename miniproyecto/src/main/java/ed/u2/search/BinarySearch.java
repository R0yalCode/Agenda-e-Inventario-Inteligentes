package ed.u2.search;

import ed.u2.util.ProgressBar;

public class BinarySearch {

    public static <T extends Comparable<T>> SearchStats buscar(T[] arr, T target) {

        SearchStats st = new SearchStats();
        long t0 = System.nanoTime();

        int left = 0, right = arr.length - 1;
        int iter = 0;

        while (left <= right) {

            int mid = (left + right) / 2;
            st.comparaciones++;

            if (arr[mid].equals(target)) {
                st.resultadosEncontrados = 1;
                break;
            }

            if (arr[mid].compareTo(target) < 0)
                left = mid + 1;
            else
                right = mid - 1;

            iter++;
            ProgressBar.mostrar((double) iter / arr.length);
        }

        st.tiempoNs = System.nanoTime() - t0;
        return st;
    }
}
