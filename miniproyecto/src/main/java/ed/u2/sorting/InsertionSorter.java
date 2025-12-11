package ed.u2.sorting;

import ed.u2.stats.OperationStats;

public class InsertionSorter {

    public static <T extends Comparable<T>> OperationStats sort(T[] arr, boolean asc) {

        OperationStats st = new OperationStats();

        long comp = 0, swp = 0;
        long t0 = System.nanoTime();

        int n = arr.length;

        for (int i = 1; i < n; i++) {

            T key = arr[i];
            int j = i - 1;

            while (j >= 0) {

                comp++;

                boolean mayor = arr[j].compareTo(key) > 0;

                if ((asc && mayor) || (!asc && !mayor)) {
                    arr[j + 1] = arr[j];
                    swp++;
                    j--;
                } else {
                    break;
                }
            }

            arr[j + 1] = key;
        }

        long t1 = System.nanoTime();

        st.setComparisons(comp);
        st.setSwaps(swp);
        st.setTime(t1 - t0);

        return st;
    }
}
