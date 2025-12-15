package ed.u2.util;

/**
 * Utilidades para manejo de arrays.
 */
public class ArrayUtils {

    /**
     * Verifica si un array de Comparables está ordenado.
     */
    @SuppressWarnings("unchecked")
    public static boolean estaOrdenado(Object[] arr, boolean ascendente) {
        if (arr == null || arr.length <= 1) return true;

        for (int i = 0; i < arr.length - 1; i++) {
            Comparable<Object> current = (Comparable<Object>) arr[i];
            Comparable<Object> next = (Comparable<Object>) arr[i + 1];

            int cmp = current.compareTo(next);

            if (ascendente) {
                if (cmp > 0) return false; // Debería ser <=
            } else {
                if (cmp < 0) return false; // Debería ser >=
            }
        }
        return true;
    }

    /**
     * Verifica si un array está ordenado (asume ascendente).
     */
    public static boolean estaOrdenado(Object[] arr) {
        return estaOrdenado(arr, true);
    }

    /**
     * Crea una copia profunda de un array de objetos Comparable.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] copiarArray(T[] original) {
        if (original == null) return null;
        return java.util.Arrays.copyOf(original, original.length);
    }

    /**
     * Verifica si un array contiene duplicados (basado en equals).
     */
    public static <T> boolean tieneDuplicados(T[] arr) {
        if (arr == null || arr.length <= 1) return false;

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] != null && arr[i].equals(arr[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Genera un array aleatorio de enteros.
     */
    public static Integer[] generarArrayAleatorio(int tamaño, int min, int max) {
        Integer[] arr = new Integer[tamaño];
        java.util.Random rand = new java.util.Random();

        for (int i = 0; i < tamaño; i++) {
            arr[i] = rand.nextInt(max - min + 1) + min;
        }

        return arr;
    }

    /**
     * Formatea un array para mostrar en consola.
     */
    public static String arrayToString(Object[] arr, int maxElementos) {
        if (arr == null) return "null";
        if (arr.length == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        int limit = Math.min(arr.length, maxElementos);

        for (int i = 0; i < limit; i++) {
            sb.append(arr[i]);
            if (i < limit - 1) sb.append(", ");
        }

        if (arr.length > maxElementos) {
            sb.append(", ... (+").append(arr.length - maxElementos).append(" más)");
        }

        sb.append("]");
        return sb.toString();
    }
}