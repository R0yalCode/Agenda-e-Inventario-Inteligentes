package ed.u2.search;


import ed.u2.sll.Node;
import ed.u2.sll.SinglyLinkedList;
import ed.u2.stats.OperationStats;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchManager
 *
 * Autor: R 
 * Fecha: 2025
 *
 * Implementa búsquedas:
 *  - Lineal
 *  - Centinela (sin modificar objetos)
 *  - findAll
 *  - first / last
 *  - Binaria iterativa (mid = low + (high - low) / 2)
 *  - lowerBound / upperBound (para duplicados)
 *  - Equivalentes para SLL
 *
 * Todos los métodos devuelven OperationStats con:
 *  - tiempo (ns)
 *  - comparaciones
 *  - encontrado / índice / lista de índices (si aplica)
 *
 * Las comparaciones son case-insensitive sobre IDs/atributos.
 */
public class SearchManager {

    // -----------------------
    // BÚSQUEDA LINEAL POR ID
    // -----------------------
    public static OperationStats busquedaLineal(Object[] arr, String idBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(idBuscar);
        long comps = 0;
        long t0 = System.nanoTime();

        for (int i = 0; i < arr.length; i++) {
            comps++;
            String idActual = safeGetId(arr[i]);
            if (idActual != null && idActual.equalsIgnoreCase(target)) {
                long t1 = System.nanoTime();
                stats.setComparisons(comps);
                stats.setTime(t1 - t0);
                stats.setFound(true);
                stats.setIndex(i);
                return stats;
            }
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(false);
        stats.setIndex(-1);
        return stats;
    }

    // -----------------------
    // BÚSQUEDA CENTINELA (sin modificar objetos)
    // -----------------------
    public static OperationStats busquedaCentinela(Object[] arr, String idBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null || arr.length == 0) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(idBuscar);

        // Construir arreglo de ids (Strings) para aplicar centinela sin tocar objetos
        int n = arr.length;
        String[] ids = new String[n];
        for (int i = 0; i < n; i++) ids[i] = safeGetId(arr[i]);

        long comps = 0;
        long t0 = System.nanoTime();

        // guardar último id real
        String lastIdOriginal = ids[n - 1];
        // colocar centinela (target) en la última posición
        ids[n - 1] = target;

        int i = 0;
        while (true) {
            comps++;
            if (ids[i] != null && ids[i].equalsIgnoreCase(target)) break;
            i++;
        }

        // restaurar (no necesario para los objetos originales, solo para ids)
        ids[n - 1] = lastIdOriginal;

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);

        if (i == n - 1) {
            // coincidencia solo si el último tenía el target originalmente
            if (lastIdOriginal != null && lastIdOriginal.equalsIgnoreCase(target)) {
                stats.setFound(true);
                stats.setIndex(n - 1);
            } else {
                stats.setFound(false);
                stats.setIndex(-1);
            }
        } else {
            stats.setFound(true);
            stats.setIndex(i);
        }
        return stats;
    }

    // -----------------------
    // FIND ALL (devuelve OperationStats con índices en indices)
    // -----------------------
    public static OperationStats findAll(Object[] arr, String atributo, String valorBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(valorBuscar);
        long comps = 0;
        List<Integer> indices = new ArrayList<>();
        long t0 = System.nanoTime();

        for (int i = 0; i < arr.length; i++) {
            comps++;
            String campo = safeGetField(arr[i], atributo);
            if (campo != null && campo.equalsIgnoreCase(target)) {
                indices.add(i);
            }
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);

        stats.setFound(!indices.isEmpty());
        stats.setIndex(indices.isEmpty() ? -1 : indices.get(0));
        stats.setIndices(indices.stream().mapToInt(Integer::intValue).toArray());
        return stats;
    }

    // -----------------------
    // FIRST (primera aparición por atributo)
    // -----------------------
    public static OperationStats first(Object[] arr, String atributo, String valorBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(valorBuscar);
        long comps = 0;
        long t0 = System.nanoTime();

        for (int i = 0; i < arr.length; i++) {
            comps++;
            String campo = safeGetField(arr[i], atributo);
            if (campo != null && campo.equalsIgnoreCase(target)) {
                long t1 = System.nanoTime();
                stats.setComparisons(comps);
                stats.setTime(t1 - t0);
                stats.setFound(true);
                stats.setIndex(i);
                return stats;
            }
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(false);
        stats.setIndex(-1);
        return stats;
    }

    // -----------------------
    // LAST (última aparición por atributo)
    // -----------------------
    public static OperationStats last(Object[] arr, String atributo, String valorBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(valorBuscar);
        long comps = 0;
        long t0 = System.nanoTime();

        for (int i = arr.length - 1; i >= 0; i--) {
            comps++;
            String campo = safeGetField(arr[i], atributo);
            if (campo != null && campo.equalsIgnoreCase(target)) {
                long t1 = System.nanoTime();
                stats.setComparisons(comps);
                stats.setTime(t1 - t0);
                stats.setFound(true);
                stats.setIndex(i);
                return stats;
            }
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(false);
        stats.setIndex(-1);
        return stats;
    }

    // -----------------------
    // BÚSQUEDA BINARIA ITERATIVA (array ordenado por id)
    // Devuelve posición (o -1) en OperationStats.index
    // -----------------------
    public static OperationStats binarySearch(Object[] arr, String idBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(idBuscar);
        int low = 0;
        int high = arr.length - 1;
        long comps = 0;
        long t0 = System.nanoTime();

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comps++;
            String midId = safeGetId(arr[mid]);
            if (midId == null) midId = "";

            int cmp = midId.compareToIgnoreCase(target);
            if (cmp == 0) {
                long t1 = System.nanoTime();
                stats.setComparisons(comps);
                stats.setTime(t1 - t0);
                stats.setFound(true);
                stats.setIndex(mid);
                return stats;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(false);
        stats.setIndex(-1);
        return stats;
    }

    // -----------------------
    // LOWER / UPPER BOUNDS (para duplicados)
    // -----------------------
    public static OperationStats lowerBound(Object[] arr, String idBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(idBuscar);
        int low = 0;
        int high = arr.length;
        long comps = 0;
        long t0 = System.nanoTime();

        while (low < high) {
            int mid = low + (high - low) / 2;
            comps++;
            String midId = safeGetId(arr[mid]);
            if (midId == null) midId = "";

            if (midId.compareToIgnoreCase(target) < 0) low = mid + 1;
            else high = mid;
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(low < arr.length && safeGetId(arr[low]).equalsIgnoreCase(target));
        stats.setIndex(low);
        return stats;
    }

    public static OperationStats upperBound(Object[] arr, String idBuscar) {
        OperationStats stats = new OperationStats();
        if (arr == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(idBuscar);
        int low = 0;
        int high = arr.length;
        long comps = 0;
        long t0 = System.nanoTime();

        while (low < high) {
            int mid = low + (high - low) / 2;
            comps++;
            String midId = safeGetId(arr[mid]);
            if (midId == null) midId = "";

            if (midId.compareToIgnoreCase(target) <= 0) low = mid + 1;
            else high = mid;
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound((low - 1) >= 0 && safeGetId(arr[low - 1]).equalsIgnoreCase(target));
        stats.setIndex(low);
        return stats;
    }

    // -----------------------
    // BÚSQUEDAS EN SLL
    // -----------------------
    public static OperationStats sllFindAll(SinglyLinkedList<?> sll, String atributo, String valorBuscar) {
        OperationStats stats = new OperationStats();
        if (sll == null || sll.getHead() == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(valorBuscar);
        long comps = 0;
        List<Integer> indices = new ArrayList<>();
        long t0 = System.nanoTime();

        Node<?> nodo = sll.getHead();
        int idx = 0;
        while (nodo != null) {
            comps++;
            String campo = safeGetField(nodo.getData(), atributo);
            if (campo != null && campo.equalsIgnoreCase(target)) indices.add(idx);

            nodo = nodo.getNext();
            idx++;
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(!indices.isEmpty());
        stats.setIndices(indices.stream().mapToInt(Integer::intValue).toArray());
        stats.setIndex(indices.isEmpty() ? -1 : indices.get(0));
        return stats;
    }

    public static OperationStats sllFirst(SinglyLinkedList<?> sll, String atributo, String valorBuscar) {
        OperationStats stats = new OperationStats();
        if (sll == null || sll.getHead() == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(valorBuscar);
        long comps = 0;
        long t0 = System.nanoTime();

        Node<?> nodo = sll.getHead();
        int idx = 0;
        while (nodo != null) {
            comps++;
            String campo = safeGetField(nodo.getData(), atributo);
            if (campo != null && campo.equalsIgnoreCase(target)) {
                long t1 = System.nanoTime();
                stats.setComparisons(comps);
                stats.setTime(t1 - t0);
                stats.setFound(true);
                stats.setIndex(idx);
                return stats;
            }
            nodo = nodo.getNext();
            idx++;
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(false);
        stats.setIndex(-1);
        return stats;
    }

    public static OperationStats sllLast(SinglyLinkedList<?> sll, String atributo, String valorBuscar) {
        OperationStats stats = new OperationStats();
        if (sll == null || sll.getHead() == null) {
            stats.setTime(0);
            return stats;
        }

        String target = safeUpper(valorBuscar);
        long comps = 0;
        long t0 = System.nanoTime();

        Node<?> nodo = sll.getHead();
        int idx = 0;
        int lastIdx = -1;
        while (nodo != null) {
            comps++;
            String campo = safeGetField(nodo.getData(), atributo);
            if (campo != null && campo.equalsIgnoreCase(target)) lastIdx = idx;

            nodo = nodo.getNext();
            idx++;
        }

        long t1 = System.nanoTime();
        stats.setComparisons(comps);
        stats.setTime(t1 - t0);
        stats.setFound(lastIdx != -1);
        stats.setIndex(lastIdx);
        return stats;
    }

    // ================================
    // HELPERS: obtener ID / campo con reflexión flexible
    // ================================
    private static String safeUpper(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }

    /**
     * Intenta obtener ID utilizando reflexión sobre varios nombres de métodos comunes.
     * Si falla, devuelve null.
     */
    private static String safeGetId(Object o) {
        if (o == null) return null;
        // posibles getters de ID
        String[] posibles = new String[] {
                "getId", "getIdCita", "getIdPaciente", "getIdItem", "getCodigo",
                "getItemId", "getPatientId", "getKey"
        };

        for (String mName : posibles) {
            try {
                Method m = o.getClass().getMethod(mName);
                Object val = m.invoke(o);
                if (val != null) return val.toString();
            } catch (Exception ignored) { }
        }

        // fallback: buscar cualquier metodo que comience con "get" y retorne String y contenga "id" en el nombre
        for (Method m : o.getClass().getMethods()) {
            try {
                if (m.getParameterCount() == 0 &&
                        m.getReturnType() == String.class &&
                        m.getName().toLowerCase().contains("id")) {
                    Object val = m.invoke(o);
                    if (val != null) return val.toString();
                }
            } catch (Exception ignored) { }
        }

        return null;
    }

    /**
     * Intenta obtener el valor de un campo lógico (id, apellido, fecha, insumo, stock, prioridad)
     * usando reflexión flexible sobre varios nombres de metodo.
     */
    private static String safeGetField(Object o, String atributo) {
        if (o == null || atributo == null) return null;
        atributo = atributo.toLowerCase();

        String[] posibles;
        switch (atributo) {
            case "id":
                posibles = new String[] {"getId", "getIdCita", "getIdPaciente", "getIdItem", "getCodigo", "getItemId", "getPatientId"};
                break;
            case "apellido":
            case "last":
            case "lastname":
                posibles = new String[] {"getApellido", "getLastName", "getApellidos", "getSurname"};
                break;
            case "fecha":
            case "fechahora":
            case "date":
                posibles = new String[] {"getFechaHora", "getFecha", "getDateTime", "getDate"};
                break;
            case "insumo":
            case "supply":
            case "nombre":
                posibles = new String[] {"getInsumo", "getNombre", "getSupply", "getName"};
                break;
            case "stock":
            case "cantidad":
                posibles = new String[] {"getStock", "getCantidad", "getAmount"};
                break;
            case "prioridad":
            case "priority":
                posibles = new String[] {"getPrioridad", "getPriority"};
                break;
            default:
                posibles = new String[] {}; // intentaremos buscar metodo generico
        }

        // intentar nombres directos
        for (String mName : posibles) {
            try {
                Method m = o.getClass().getMethod(mName);
                Object val = m.invoke(o);
                if (val != null) return val.toString();
            } catch (Exception ignored) {}
        }

        // fallback: intentar cualquier getX que esté relacionado con el atributo
        for (Method m : o.getClass().getMethods()) {
            try {
                if (m.getParameterCount() == 0) {
                    String name = m.getName().toLowerCase();
                    if (name.startsWith("get") && (name.contains(atributo) || name.contains("name") || name.contains("fecha") || name.contains("id") || name.contains("stock") || name.contains("prior"))) {
                        Object val = m.invoke(o);
                        if (val != null) return val.toString();
                    }
                }
            } catch (Exception ignored) {}
        }

        return null;
    }
}
