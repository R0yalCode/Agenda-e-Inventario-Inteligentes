package ed.u2.stats;

public class ChartUtils {

    /** Repite un carácter 'n' veces */
    public static String repeat(char c, int n) {
        return String.valueOf(c).repeat(Math.max(0, n));
    }

    /** Normaliza valores a un rango máximo */
    public static int scale(int value, int maxValue, int maxScale) {
        if (maxValue == 0) return 0;
        return (int) ((value / (double) maxValue) * maxScale);
    }
}
