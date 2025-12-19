package ed.u2.sorting;

import ed.u2.stats.OperationStats;

public class SelectionSorter {

    public static <T extends Comparable<T>> OperationStats sort(T[] arr, boolean asc) {

        OperationStats st = new OperationStats();

        long comp = 0, swp = 0;
        long t0 = System.nanoTime();

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            int idx = i;

            for (int j = i + 1; j < n; j++) {
                comp++;
                if ((asc && arr[j].compareTo(arr[idx]) < 0) ||
                    (!asc && arr[j].compareTo(arr[idx]) > 0)) {
                    idx = j;
                }
            }

            if (idx != i) {
                T aux = arr[i];
                arr[i] = arr[idx];
                arr[idx] = aux;
                swp++;
            }
        }

        long t1 = System.nanoTime();

        st.setComparisons(comp);
        st.setSwaps(swp);
        st.setTime(t1 - t0);

        return st;
    }
}
