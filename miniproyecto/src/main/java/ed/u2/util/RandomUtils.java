package ed.u2.util;

import java.util.Random;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Utilidades para generar números aleatorios o arreglos
 * aleatorios para pruebas.
 */
public class RandomUtils {

    private static final Random rnd = new Random();

    /**
     * Genera un número entero aleatorio entre min y max.
     */
    public static int entero(int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    /**
     * Genera un arreglo de enteros con valores aleatorios.
     */
    public static Integer[] arregloEnteros(int size, int min, int max) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = entero(min, max);
        }
        return arr;
    }

}
