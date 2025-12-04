package ed.u2.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Implementa las variantes de Búsqueda Secuencial:
 *  - Primera coincidencia
 *  - Última coincidencia
 *  - Todas las coincidencias (findAll)
 *  - Centinela
 *
 * Entrada: Arreglo o lista a evaluar, clave de búsqueda.
 * Salida: Índices encontrados + estadísticas de búsqueda.
 */
public class SequentialSearch {

    /**
     * Búsqueda secuencial tradicional para obtener la primera coincidencia.
     *
     * @param arr Arreglo donde se realiza la búsqueda.
     * @param clave Valor a buscar.
     * @return Índice de la primera coincidencia o -1 si no existe.
     */
    public static <T> int buscarPrimero(T[] arr, T clave) {
        return -1; // Implementación en fase posterior
    }

    /**
     * Búsqueda secuencial para obtener la última coincidencia.
     *
     * @param arr Arreglo donde se realiza la búsqueda.
     * @param clave Valor a buscar.
     * @return Índice de la última coincidencia o -1 si no existe.
     */
    public static <T> int buscarUltimo(T[] arr, T clave) {
        return -1;
    }

    /**
     * Búsqueda que obtiene todos los índices donde aparece la clave.
     *
     * @param arr Arreglo donde se realiza la búsqueda.
     * @param clave Valor buscado.
     * @return Lista de índices encontrados.
     */
    public static <T> List<Integer> buscarTodos(T[] arr, T clave) {
        return new ArrayList<>();
    }

    /**
     * Búsqueda secuencial optimizada mediante centinela.
     *
     * @param arr Arreglo donde se colocará un centinela temporal.
     * @param clave Valor buscado.
     * @return Índice encontrado o -1.
     */
    public static <T> int buscarCentinela(T[] arr, T clave) {
        return -1;
    }
}
