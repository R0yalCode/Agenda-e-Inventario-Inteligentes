package ed.u2.util;

import java.util.Scanner;

/**
 * Autor: R
 * Fecha: 2025
 *
 * Utilidades para lectura segura desde consola.
 * Entrada: Texto desde el usuario
 * Salida: Valores validados (int, String, double)
 */
public class ConsoleUtils {

    private static final Scanner sc = new Scanner(System.in);

    public static String leerLinea(String mensaje) {
        System.out.print(ANSI.BLUE + mensaje + ANSI.RESET + " ");
        return sc.nextLine();
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(ANSI.CYAN + mensaje + ANSI.RESET + " ");
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println(ANSI.RED + "✘ Error: Ingrese un número entero válido." + ANSI.RESET);
            }
        }
    }

    public static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(ANSI.CYAN + mensaje + ANSI.RESET + " ");
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println(ANSI.RED + "✘ Error: Ingrese un número decimal válido." + ANSI.RESET);
            }
        }
    }

    /**
     * Pausa con mensaje personalizado.
     */
    public static void pausar(String mensaje) {
        System.out.println(ANSI.YELLOW + mensaje + ANSI.RESET);
        System.out.println(ANSI.CYAN + "Presione ENTER para continuar..." + ANSI.RESET);
        sc.nextLine();
    }

    /**
     * Pausa sin mensaje.
     */
    public static void pausar() {
        System.out.println(ANSI.CYAN + "Presione ENTER para continuar..." + ANSI.RESET);
        sc.nextLine();
    }
}
