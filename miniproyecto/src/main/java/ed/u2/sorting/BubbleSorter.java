package ed.u2.sorting;

import ed.u2.stats.OperationStats;

public class BubbleSorter {

    public static <T extends Comparable<T>> OperationStats sort(T[] arr, boolean asc) {

        OperationStats st = new OperationStats();

        long comp = 0, swp = 0;
        long t0 = System.nanoTime();

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {

                comp++;

                boolean mayor = arr[j].compareTo(arr[j + 1]) > 0;

                if ((asc && mayor) || (!asc && !mayor)) {

                    T aux = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = aux;

                    swapped = true;
                    swp++;
                }
            }

            if (!swapped) break;
        }

        long t1 = System.nanoTime();

        st.setComparisons(comp);
        st.setSwaps(swp);
        st.setTime(t1 - t0);

        return st;
    }
}
