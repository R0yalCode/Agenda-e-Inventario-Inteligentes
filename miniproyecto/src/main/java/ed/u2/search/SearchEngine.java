package ed.u2.search;

import ed.u2.data.DatasetManager;
import ed.u2.model.*;
import ed.u2.sll.Node;
import ed.u2.sll.SinglyLinkedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchEngine {

    // ============================================================
    // 1. BÚSQUEDA LINEAL POR ID (case-insensitive)
    // ============================================================
    public static Object buscarPorIdLineal(String id) {

        Object[] arr = DatasetManager.getArray();
        if (arr == null)
            return null;

        String buscado = id.trim().toUpperCase();

        for (Object o : arr) {
            if (getId(o).equalsIgnoreCase(buscado))
                return o;
        }
        return null;
    }

    // ============================================================
    // 2. LINEAL CON CENTINELA
    // ============================================================
    public static Object buscarPorIdCentinela(String idBuscado) {

        Object[] arr = DatasetManager.getArray();
        if (arr == null || arr.length == 0)
            return null;

        String buscado = idBuscado.trim().toUpperCase();

        // Crear copia con un elemento más
        Object[] copia = Arrays.copyOf(arr, arr.length + 1);

        // Crear un centinela como un Wrapper simple
        Object centinela = arr[0]; // no importa cuál, solo símbolo
        copia[arr.length] = centinela;

        // Recorrer buscando por ID
        int i = 0;
        while (true) {

            String id = SearchEngine.getField(copia[i], "id");
            if (id != null && id.equalsIgnoreCase(buscado))
                break;

            i++;
        }

        // Caso no encontrado
        if (i == arr.length)
            return null;

        // Caso encontrado
        return copia[i];
    }

    // ============================================================
    // 3. FIND ALL (TODAS LAS COINCIDENCIAS)
    // ============================================================
    public static List<Object> findAllPorAtributo(String atributo, String valor) {

        Object[] arr = DatasetManager.getArray();
        List<Object> resultados = new ArrayList<>();

        String buscado = valor.trim().toUpperCase();

        for (Object o : arr) {

            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado))
                resultados.add(o);
        }

        return resultados;
    }

    // ============================================================
    // 4. FIRST — PRIMERA APARICIÓN
    // ============================================================
    public static Object firstPorAtributo(String atributo, String valor) {

        Object[] arr = DatasetManager.getArray();
        String buscado = valor.trim().toUpperCase();

        for (Object o : arr) {
            String campo = getField(o, atributo);
            if (campo != null && campo.equalsIgnoreCase(buscado))
                return o;
        }
        return null;
    }

    // ============================================================
    // 5. LAST — ÚLTIMA APARICIÓN
    // ============================================================
    public static Object lastPorAtributo(String atributo, String valor) {

        Object[] arr = DatasetManager.getArray();
        String buscado = valor.trim().toUpperCase();

        for (int i = arr.length - 1; i >= 0; i--) {
            String campo = getField(arr[i], atributo);
            if (campo != null && campo.equalsIgnoreCase(buscado))
                return arr[i];
        }
        return null;
    }

    // ============================================================
    // 6. BÚSQUEDA BINARIA ITERATIVA
    // ============================================================
    public static int binarySearch(Object[] arr, String id) {

        String buscado = id.trim().toUpperCase();

        int low = 0, high = arr.length - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            int cmp = getId(arr[mid]).compareToIgnoreCase(buscado);

            if (cmp == 0)
                return mid;

            if (cmp < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    // ============================================================
    // 7. BOUNDS — primer y último índice con duplicados
    // ============================================================
    public static int lowerBound(Object[] arr, String id) {

        String buscado = id.trim().toUpperCase();
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (getId(arr[mid]).compareToIgnoreCase(buscado) < 0)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    public static int upperBound(Object[] arr, String id) {

        String buscado = id.trim().toUpperCase();
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (getId(arr[mid]).compareToIgnoreCase(buscado) <= 0)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    // ============================================================
    // 8. BÚSQUEDAS EN SLL
    // ============================================================

    public static List<Object> sllFindAll(String atributo, String valor) {

        SinglyLinkedList<?> sll = DatasetManager.getSLL();
        List<Object> results = new ArrayList<>();

        String buscado = valor.trim().toUpperCase();

        Node<?> actual = sll.getHead();

        while (actual != null) {
            Object o = actual.getData();
            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado))
                results.add(o);

            actual = actual.getNext();
        }
        return results;
    }

    public static Object sllFirst(String atributo, String valor) {

        SinglyLinkedList<?> sll = DatasetManager.getSLL();
        String buscado = valor.trim().toUpperCase();

        Node<?> actual = sll.getHead();

        while (actual != null) {
            Object o = actual.getData();
            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado))
                return o;

            actual = actual.getNext();
        }
        return null;
    }
    // ============================================================
// VALIDACIÓN PARA BÚSQUEDA BINARIA
// ============================================================

    /**
     * Verifica si un array está ordenado por ID (requisito para binaria).
     */
    public static boolean estaOrdenadoPorId(Object[] arr) {
        if (arr == null || arr.length <= 1) return true;

        for (int i = 0; i < arr.length - 1; i++) {
            String idActual = getId(arr[i]).toUpperCase();
            String idSiguiente = getId(arr[i + 1]).toUpperCase();

            if (idActual.compareTo(idSiguiente) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Versión sobrecargada del binarySearch que valida antes.
     */
    public static int binarySearchValidado(Object[] arr, String id) {
        if (!estaOrdenadoPorId(arr)) {
            throw new IllegalStateException("Array no está ordenado por ID. "
                    + "Búsqueda binaria requiere ordenación previa.");
        }
        return binarySearch(arr, id);
    }

    public static Object sllLast(String atributo, String valor) {

        SinglyLinkedList<?> sll = DatasetManager.getSLL();
        String buscado = valor.trim().toUpperCase();

        Object last = null;

        Node<?> actual = sll.getHead();

        while (actual != null) {
            Object o = actual.getData();
            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado))
                last = o;

            actual = actual.getNext();
        }
        return last;
    }

    // ============================================================
    // MÉTODOS UTILITARIOS
    // ============================================================

    private static String getId(Object o) {
        if (o instanceof Cita c)
            return c.getId();
        if (o instanceof Paciente p)
            return p.getId();
        if (o instanceof InventarioItem i)
            return i.getId();
        return "";
    }

    private static String getField(Object o, String atributo) {

        atributo = atributo.toLowerCase();

        // CITA
        if (o instanceof Cita c) {
            return switch (atributo) {
                case "id" -> c.getId();
                case "apellido" -> c.getApellido();
                case "fecha" -> c.getFechaHora().toString();
                default -> null;
            };
        }

        // PACIENTE
        if (o instanceof Paciente p) {
            return switch (atributo) {
                case "id" -> p.getId();
                case "apellido" -> p.getApellido();
                case "prioridad" -> String.valueOf(p.getPrioridad());
                default -> null;
            };
        }

        // INVENTARIO
        if (o instanceof InventarioItem i) {
            return switch (atributo) {
                case "id" -> i.getId();
                case "insumo" -> i.getInsumo();
                case "stock" -> String.valueOf(i.getStock());
                default -> null;
            };
        }

        return null;
    }
}
