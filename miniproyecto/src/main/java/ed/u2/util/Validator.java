package ed.u2.util;

import java.io.File;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Utilidades para validaciones generales del sistema.
 * Entrada: valores a validar
 * Salida: boolean o mensajes de error
 */
public class Validator {

    /**
     * Valida si una cadena NO está vacía.
     */
    public static boolean noVacio(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Verifica si una ruta existe.
     */
    public static boolean archivoExiste(String ruta) {
        File f = new File(ruta);
        return f.exists() && f.isFile();
    }

    /**
     * Verifica si una cadena es numérica.
     */
    public static boolean esNumero(String s) {
        if (s == null) return false;
        return s.matches("-?\\d+(\\.\\d+)?");
    }

}
