package ed.u2.util;

/**
 * Utilidades matemáticas para estadísticas.
 */
public class MathUtils {

    /**
     * Calcula la mediana de un array de longs.
     */
    public static long mediana(long[] valores) {
        if (valores == null || valores.length == 0) return 0;

        long[] copia = java.util.Arrays.copyOf(valores, valores.length);
        java.util.Arrays.sort(copia);

        int n = copia.length;
        if (n % 2 == 1) {
            return copia[n / 2];
        } else {
            return (copia[n/2 - 1] + copia[n/2]) / 2;
        }
    }

    /**
     * Calcula el promedio.
     */
    public static double promedio(long[] valores) {
        if (valores == null || valores.length == 0) return 0;

        long suma = 0;
        for (long v : valores) suma += v;
        return (double) suma / valores.length;
    }

    /**
     * Calcula la desviación estándar.
     */
    public static double desviacionEstandar(long[] valores) {
        if (valores == null || valores.length <= 1) return 0;

        double avg = promedio(valores);
        double sumaCuadrados = 0;

        for (long v : valores) {
            double diff = v - avg;
            sumaCuadrados += diff * diff;
        }

        return Math.sqrt(sumaCuadrados / (valores.length - 1));
    }

    /**
     * Calcula el coeficiente de variación (CV).
     */
    public static double coeficienteVariacion(long[] valores) {
        double promedio = promedio(valores);
        if (promedio == 0) return 0;
        return desviacionEstandar(valores) / promedio * 100;
    }

    /**
     * Formatea un número con separadores de miles.
     */
    public static String formatConSeparadores(long numero) {
        return String.format("%,d", numero);
    }

    /**
     * Formatea tiempo en nanosegundos a una unidad legible.
     */
    public static String formatTiempo(long nanos) {
        if (nanos < 1_000) {
            return nanos + " ns";
        } else if (nanos < 1_000_000) {
            return String.format("%.1f µs", nanos / 1_000.0);
        } else if (nanos < 1_000_000_000) {
            return String.format("%.2f ms", nanos / 1_000_000.0);
        } else {
            return String.format("%.3f s", nanos / 1_000_000_000.0);
        }
    }
}