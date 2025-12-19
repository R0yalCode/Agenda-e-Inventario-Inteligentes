package ed.u2.util;

/**
 * Barra de progreso tipo Linux terminal.
 */
public class ProgressBar {

    public static void mostrar(double progreso) {
        int total = 30;
        int completos = (int) (progreso * total);

        StringBuilder sb = new StringBuilder("\r[");
        for (int i = 0; i < completos; i++) sb.append("=");
        for (int i = completos; i < total; i++) sb.append(" ");
        sb.append("] ").append(String.format("%.1f%%", progreso * 100));

        System.out.print(sb);
        if (progreso >= 1) System.out.println();
    }
}
