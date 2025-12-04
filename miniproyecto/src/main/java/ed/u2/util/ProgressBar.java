package ed.u2.util;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Clase reutilizable para mostrar barras de progreso en terminal
 * estilo Linux.
 * Entrada: índice actual y total
 * Salida: progreso visual animado
 */
public class ProgressBar {

    private static final int WIDTH = 40;

    /**
     * Dibuja una barra de progreso visual.
     *
     * @param actual índice actual
     * @param total  total de elementos
     */
    public static void render(int actual, int total) {

        if (total <= 0) total = 1;

        int progreso = (actual * 100) / total;
        int llenos = (progreso * WIDTH) / 100;

        StringBuilder b = new StringBuilder("\r[");
        for (int i = 0; i < WIDTH; i++) {
            b.append(i < llenos ? "=" : " ");
        }
        b.append("] ").append(progreso).append("%");

        System.out.print(ANSI.GREEN + b + ANSI.RESET);
    }

}
