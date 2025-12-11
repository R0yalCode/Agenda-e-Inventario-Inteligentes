package ed.u2.sorting;

public class SortStats {

    public static long comparaciones = 0;
    public static long intercambios = 0;

    public static void reset() {
        comparaciones = 0;
        intercambios = 0;
    }

    public static void addComparacion() {
        comparaciones++;
    }

    public static void addIntercambio() {
        intercambios++;
    }
}
