package ed.u2.search;

import ed.u2.data.DatasetManager;
import ed.u2.model.*;
import ed.u2.sll.Node;
import ed.u2.sll.SinglyLinkedList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ed.u2.util.Holder;

public class SearchEngine {

    // ============================================================
    // 1. BÚSQUEDA LINEAL POR ID + ESTADÍSTICAS
    // ============================================================
    public static Object buscarPorIdLineal(String id, SearchStats st) {

        long t0 = System.nanoTime();

        Object[] arr = DatasetManager.getArray();
        if (arr == null) {
            st.setTiempo(System.nanoTime() - t0);
            return null;
        }

        String buscado = id.trim().toUpperCase();

        for (Object o : arr) {
            st.addComparacion(); // 🔹 comparación real

            if (getId(o).equalsIgnoreCase(buscado)) {
                st.addResultado(); // 🔹 encontrado
                st.setTiempo(System.nanoTime() - t0);
                return o;
            }
        }

        // No encontrado
        st.setTiempo(System.nanoTime() - t0);
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
    // 2. LINEAL CON CENTINELA + ESTADÍSTICAS
    // ============================================================
    public static SearchResult buscarPorIdCentinelaStats(String idBuscado, SearchStats st) {

        long t0 = System.nanoTime();

        Object[] arr = DatasetManager.getArray();
        if (arr == null || arr.length == 0) {
            st.setTiempo(System.nanoTime() - t0);
            return new SearchResult(null, st);
        }

        String buscado = idBuscado.trim().toUpperCase();

        // Copia con centinela
        Object[] copia = Arrays.copyOf(arr, arr.length + 1);
        copia[arr.length] = arr[0]; // centinela

        int i = 0;
        while (true) {
            st.addComparacion();

            String id = getId(copia[i]);
            if (id.equalsIgnoreCase(buscado))
                break;

            i++;
        }

        Object resultado = (i == arr.length) ? null : copia[i];

        if (resultado != null)
            st.addResultado();

        long t1 = System.nanoTime();
        st.setTiempo(t1 - t0);

        return new SearchResult(resultado, st);
    }

    // ============================================================
    // 3. FIND ALL (TODAS LAS COINCIDENCIAS)
    // ============================================================
    public static List<Object> findAllPorAtributo(
            String atributo,
            String valor,
            SearchStats st) {

        Object[] arr = DatasetManager.getArray();
        List<Object> resultados = new ArrayList<>();

        String buscado = valor.trim().toUpperCase();

        for (Object o : arr) {

            st.addComparacion(); // ← comparación real

            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado)) {
                resultados.add(o);
                st.addResultado();
            }
        }

        return resultados;
    }

    // ============================================================
    // 4. FIRST — PRIMERA APARICIÓN
    // ============================================================
    public static Object firstPorAtributo(
            String atributo,
            String valor,
            SearchStats st) {
        Object[] arr = DatasetManager.getArray();
        if (arr == null)
            return null;

        st.reset(); // reiniciar estadísticas

        String buscado = valor.trim().toUpperCase();

        long t0 = System.nanoTime();

        for (Object o : arr) {
            st.addComparacion();

            String campo = getField(o, atributo);
            if (campo != null && campo.equalsIgnoreCase(buscado)) {
                st.addResultado();
                st.setTiempo(System.nanoTime() - t0);
                return o;
            }
        }

        st.setTiempo(System.nanoTime() - t0);
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
    // LAST con estadísticas
    // ============================================================
    public static Object lastPorAtributo(
            String atributo,
            String valor,
            SearchStats st) {
        Object[] arr = DatasetManager.getArray();
        if (arr == null)
            return null;

        String buscado = valor.trim().toUpperCase();
        Object last = null;

        long t0 = System.nanoTime();

        for (int i = arr.length - 1; i >= 0; i--) {

            st.addComparacion();

            String campo = getField(arr[i], atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado)) {
                last = arr[i];
                st.addResultado();
                break; // LAST → primera coincidencia desde el final
            }
        }

        long t1 = System.nanoTime();
        st.setTiempo(t1 - t0);

        return last;
    }


   public static SearchResult binarySearchPorFecha(LocalDateTime fecha, SearchStats st) {

    // Mensaje claro si no está ordenado y salida temprana (mantener st consistente)
    if (!DatasetManager.isOrdenadoPorFecha()) {
        System.out.println(ed.u2.util.ANSI.RED + "Debe ordenar las citas por fecha antes de usar búsqueda binaria" + ed.u2.util.ANSI.RESET);
        if (st != null) {
            st.reset();
            st.setTiempo(0);
        }
        return new SearchResult(null, st);
    }

    Object[] raw = DatasetManager.getArray();
    if (raw == null || raw.length == 0) {
        if (st != null) {
            st.reset();
            st.setTiempo(0);
        }
        return new SearchResult(null, st);
    }

    Cita[] arr = (raw instanceof Cita[]) ? (Cita[]) raw : Arrays.copyOf(raw, raw.length, Cita[].class);

    long t0 = System.nanoTime();
    int low = 0, high = arr.length - 1;

    while (low <= high) {
        if (st != null) st.addComparacion();

        int mid = (low + high) >>> 1;
        int cmp = arr[mid].getFechaHora().compareTo(fecha);

        if (cmp == 0) {
            if (st != null) {
                st.addResultado();
                st.setTiempo(System.nanoTime() - t0);
            }
            return new SearchResult(arr[mid], st);
        }
        if (cmp < 0) low = mid + 1;
        else high = mid - 1;
    }

    if (st != null) st.setTiempo(System.nanoTime() - t0);
    return new SearchResult(null, st);
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
    // 6. BÚSQUEDA BINARIA CON ESTADÍSTICAS
    // ============================================================
    public static int binarySearchStats(Object[] arr, String id, SearchStats st) {

        long t0 = System.nanoTime();

        String buscado = id.trim().toUpperCase();
        int low = 0, high = arr.length - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            st.addComparacion();
            int cmp = getId(arr[mid]).compareToIgnoreCase(buscado);

            if (cmp == 0) {
                st.addResultado();
                st.setTiempo(System.nanoTime() - t0);
                return mid;
            }

            if (cmp < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }

        st.setTiempo(System.nanoTime() - t0);
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
    // 7. BOUNDS — primer y último índice con duplicados (CON STATS)
    // ============================================================
    public static SearchStats boundsPorId(Object[] arr, String id) {

        SearchStats st = new SearchStats();

        if (arr == null || arr.length == 0)
            return st;

        String buscado = id.trim().toUpperCase();

        long t0 = System.nanoTime();

        // lowerBound
        int low = 0, high = arr.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            st.addComparacion();

            if (getId(arr[mid]).compareToIgnoreCase(buscado) < 0)
                low = mid + 1;
            else
                high = mid;
        }
        int lb = low;

        // upperBound
        low = 0;
        high = arr.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            st.addComparacion();

            if (getId(arr[mid]).compareToIgnoreCase(buscado) <= 0)
                low = mid + 1;
            else
                high = mid;
        }
        int ub = low;

        long t1 = System.nanoTime();

        st.resultadosEncontrados = Math.max(0, ub - lb);
        st.setTiempo(t1 - t0);

        return st;
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

    public static SearchStats sllFindAllStats(String atributo, String valor, List<Object> out) {

        SearchStats st = new SearchStats();
        st.reset();

        SinglyLinkedList<?> sll = DatasetManager.getSLL();
        if (sll == null)
            return st;

        String buscado = valor.trim().toUpperCase();

        long t0 = System.nanoTime();

        Node<?> actual = sll.getHead();

        while (actual != null) {

            st.addComparacion();

            Object o = actual.getData();
            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado)) {
                out.add(o);
                st.addResultado();
            }

            actual = actual.getNext();
        }

        long t1 = System.nanoTime();
        st.setTiempo(t1 - t0);

        return st;
    }

    public static SearchStats sllFirstStats(String atributo, String valor, Holder<Object> result) {

        SearchStats st = new SearchStats();
        st.reset();

        SinglyLinkedList<?> sll = DatasetManager.getSLL();
        if (sll == null)
            return st;

        String buscado = valor.trim().toUpperCase();

        long t0 = System.nanoTime();

        Node<?> actual = sll.getHead();

        while (actual != null) {

            st.addComparacion();

            Object o = actual.getData();
            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado)) {
                result.value = o;
                st.addResultado();
                break;
            }

            actual = actual.getNext();
        }

        long t1 = System.nanoTime();
        st.setTiempo(t1 - t0);

        return st;
    }

    public static SearchStats sllLastStats(String atributo, String valor, Holder<Object> result) {

        SearchStats st = new SearchStats();
        st.reset();

        SinglyLinkedList<?> sll = DatasetManager.getSLL();
        if (sll == null)
            return st;

        String buscado = valor.trim().toUpperCase();

        long t0 = System.nanoTime();

        Node<?> actual = sll.getHead();

        while (actual != null) {

            st.addComparacion();

            Object o = actual.getData();
            String campo = getField(o, atributo);

            if (campo != null && campo.equalsIgnoreCase(buscado)) {
                result.value = o;
                st.addResultado();
            }

            actual = actual.getNext();
        }

        long t1 = System.nanoTime();
        st.setTiempo(t1 - t0);

        return st;
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
