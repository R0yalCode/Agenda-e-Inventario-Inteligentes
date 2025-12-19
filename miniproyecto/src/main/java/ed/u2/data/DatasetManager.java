package ed.u2.data;

import ed.u2.app.HistoryManager;
import ed.u2.io.CsvLoader;
import ed.u2.model.*;
import ed.u2.sll.SinglyLinkedList;
import ed.u2.sorting.InsertionSorter;
import ed.u2.stats.SortingStatsManager;
import ed.u2.io.ExportUtils;

import java.io.File;
import java.util.*;

/**
 * Autor: R
 * Fecha: 2025
 *
 * Maneja TODO el estado del dataset actual:
 * - Tipo detectado
 * - Lista original (List)
 * - Arreglo para ordenar (T[])
 * - Lista SLL para búsquedas secuenciales
 * - Carga oficial y carga manual
 */
public class DatasetManager {

    // ============================================================
    // ESTADO GLOBAL
    // ============================================================

    private static DatasetType tipoActual = DatasetType.NONE;

    private static List<?> listaActual = null;
    private static Object[] arrayActual = null;
    private static SinglyLinkedList<?> sllActual = null;

    private static String rutaActual = null;

    // ============================================================
    // RUTA DEL DIRECTORIO DE DATASETS
    // ============================================================

    private static final String DATASET_DIR;

    static {

        // Directorio raíz del proyecto
        File base = new File(System.getProperty("user.dir"));

        // Carpeta de datasets relativa al proyecto
        File probable = new File(base, "resources/datasets");

        if (probable.exists() && probable.isDirectory()) {
            DATASET_DIR = probable.getAbsolutePath();
        } else {
            System.out.println("No se encontró 'resources/datasets'. Creando carpeta…");
            probable.mkdirs();
            DATASET_DIR = probable.getAbsolutePath();
        }
    }

    // ============================================================
    // ENUM
    // ============================================================

    public enum DatasetType {
        CITAS,
        PACIENTES,
        INVENTARIO,
        NONE
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public static boolean hayDataset() {
        return tipoActual != DatasetType.NONE && arrayActual != null;
    }

    public static DatasetType getTipoActual() {
        return tipoActual;
    }

    public static Object[] getArray() {
        return arrayActual;
    }

    public static List<?> getList() {
        return listaActual;
    }

    public static SinglyLinkedList<?> getSLL() {
        return sllActual;
    }

    public static String getRutaActual() {
        return rutaActual;
    }

    public static int getRegistrosActuales() {
        return arrayActual == null ? 0 : arrayActual.length;
    }

    // ============================================================
    // LIMPIAR ESTADO
    // ============================================================

    public static void limpiar() {
        tipoActual = DatasetType.NONE;
        listaActual = null;
        arrayActual = null;
        sllActual = null;
        rutaActual = null;
        // Inform ExportUtils that there is no dataset loaded
        ExportUtils.setDatasetsLoaded(false);
    }

    // ============================================================
    // LISTAR DATASETS OFICIALES
    // ============================================================

    public static List<File> listarDatasetsOficiales() {

        System.out.println(" DATASET_DIR está apuntando a: " + DATASET_DIR);

        File dir = new File(DATASET_DIR);

        if (!dir.exists()) {
            System.out.println(" Directorio no existe: " + DATASET_DIR);
            return Collections.emptyList();
        }

        if (!dir.isDirectory()) {
            System.out.println(" No es un directorio: " + DATASET_DIR);
            return Collections.emptyList();
        }

        File[] archivos = dir.listFiles((d, n) -> n.toLowerCase().endsWith(".csv"));

        if (archivos == null || archivos.length == 0) {
            System.out.println(" No hay archivos CSV en: " + DATASET_DIR);
            return Collections.emptyList();
        }

        Arrays.sort(archivos);
        return Arrays.asList(archivos);
    }

    // ============================================================
    // CARGA DESDE DATASETS OFICIALES
    // ============================================================

    public static void cargarDatasetOficial(File csv) {

        limpiar();

        rutaActual = csv.getAbsolutePath();

        Map<String, Object> datos = CsvLoader.cargarCsv(rutaActual);

        asignarDataset(datos);

        int registros = getRegistrosActuales();

        HistoryManager.log(
                "LOAD",
                "Dataset oficial",
                "archivo=" + csv.getName(),
                registros,
                0);
    }

    // ============================================================
    // CARGA MANUAL DE CSV
    // ============================================================

    public static void cargarDatasetManual(String ruta) {

        limpiar();

        rutaActual = ruta;

        Map<String, Object> datos = CsvLoader.cargarCsv(rutaActual);

        asignarDataset(datos);

        int registros = getRegistrosActuales();

        HistoryManager.log(
                "LOAD",
                "Carga manual CSV",
                "ruta=" + ruta,
                registros,
                0);
    }

    // ============================================================
    // RECIBE EL MAPA DEL CSV Y ACTUALIZA EL ESTADO GLOBAL
    // ============================================================

    @SuppressWarnings("unchecked")
    private static void asignarDataset(Map<String, Object> datos) {

        if (datos == null || datos.isEmpty()) {
            tipoActual = DatasetType.NONE;
            return;
        }

        // Invalidate sorting stats cache because dataset is about to change
        SortingStatsManager.clearCache();

        if (datos.containsKey("citas")) {

            tipoActual = DatasetType.CITAS;
            listaActual = (List<Cita>) datos.get("citas");
            arrayActual = listaActual.toArray(new Cita[0]);

            SinglyLinkedList<Cita> sll = new SinglyLinkedList<>();
            sll.insertarDesdeLista((List<Cita>) listaActual);
            sllActual = sll;

        } else if (datos.containsKey("pacientes")) {

            tipoActual = DatasetType.PACIENTES;
            listaActual = (List<Paciente>) datos.get("pacientes");
            arrayActual = listaActual.toArray(new Paciente[0]);

            SinglyLinkedList<Paciente> sll = new SinglyLinkedList<>();
            sll.insertarDesdeLista((List<Paciente>) listaActual);
            sllActual = sll;

        } else if (datos.containsKey("inventario")) {

            tipoActual = DatasetType.INVENTARIO;
            listaActual = (List<InventarioItem>) datos.get("inventario");
            arrayActual = listaActual.toArray(new InventarioItem[0]);

            SinglyLinkedList<InventarioItem> sll = new SinglyLinkedList<>();
            sll.insertarDesdeLista((List<InventarioItem>) listaActual);
            sllActual = sll;

        } else {
            tipoActual = DatasetType.NONE;
        }
    }

    public static Object buscarPorId(String id) {
        if (!hayDataset())
            return null;

        String buscado = id.trim().toUpperCase();

        Object[] arr = arrayActual;

        switch (tipoActual) {
            case CITAS:
                return buscarLinealPorId(arr, buscado);
            case PACIENTES:
                return buscarLinealPorId(arr, buscado);
            case INVENTARIO:
                return buscarLinealPorId(arr, buscado);
            default:
                return null;
        }
    }

    private static Object buscarLinealPorId(Object[] arr, String buscado) {
        for (Object o : arr) {
            String idActual = "";

            if (o instanceof Cita c)
                idActual = c.getId();
            if (o instanceof Paciente p)
                idActual = p.getId();
            if (o instanceof InventarioItem it)
                idActual = it.getId();

            if (idActual != null && idActual.equalsIgnoreCase(buscado))
                return o;
        }
        return null;
    }

  private static boolean isordenadoPorFecha = false;

public static void ordenarCitasPorFecha() {
    if (!hayDataset() || tipoActual != DatasetType.CITAS || arrayActual == null || arrayActual.length == 0)
        return;

    // Copia tipada a Comparable[] (Cita debe implementar Comparable)
    Comparable[] copia = Arrays.copyOf(arrayActual, arrayActual.length, Comparable[].class);

    InsertionSorter.sort(copia, true);

    // Actualiza el arreglo global con la copia ordenada
    arrayActual = copia;

    isordenadoPorFecha = true;
}

public static boolean isOrdenadoPorFecha() {
    return isordenadoPorFecha;
}
public static boolean isordenado() {
    return isOrdenado();
}

@SuppressWarnings("unchecked")
public static boolean isOrdenado() {
    if (!hayDataset() || arrayActual == null || arrayActual.length <= 1)
        return true;

    // If we know it's ordered by fecha for citas, respect that flag
    if (tipoActual == DatasetType.CITAS && isordenadoPorFecha)
        return true;

    // Ensure elements implement Comparable and are mutually comparable
    for (int i = 1; i < arrayActual.length; i++) {
        Object a = arrayActual[i - 1];
        Object b = arrayActual[i];

        if (!(a instanceof Comparable) || !(b instanceof Comparable))
            return false;

        try {
            Comparable<Object> ca = (Comparable<Object>) a;
            if (ca.compareTo(b) > 0)
                return false;
        } catch (ClassCastException e) {
            return false;
        }
    }
    return true;
}

    // ============================================================
    // GETTERS TIPADOS (para facilitar uso en MenuPrincipal)
    // ============================================================

    public static Cita[] getCitasArray() {
        if (tipoActual != DatasetType.CITAS)
            return null;
        return Arrays.copyOf(arrayActual, arrayActual.length, Cita[].class);
    }

    public static void setArray(Cita[] arr) {
        if (arr == null) {
            if (tipoActual == DatasetType.CITAS) limpiar();
            return;
        }
        tipoActual = DatasetType.CITAS;
        arrayActual = Arrays.copyOf(arr, arr.length, Object[].class);
        listaActual = Arrays.asList(arr);
        SinglyLinkedList<Cita> sll = new SinglyLinkedList<>();
        sll.insertarDesdeLista(Arrays.asList(arr));
        sllActual = sll;
        SortingStatsManager.clearCache();
        ExportUtils.setDatasetsLoaded(true);
    }

    public static Paciente[] getPacientesArray() {
        if (tipoActual != DatasetType.PACIENTES)
            return null;
        return Arrays.copyOf(arrayActual, arrayActual.length, Paciente[].class);
    }

    public static InventarioItem[] getInventarioArray() {
        if (tipoActual != DatasetType.INVENTARIO)
            return null;
        return Arrays.copyOf(arrayActual, arrayActual.length, InventarioItem[].class);
    }

    // estado manual de ordenamiento
    private static boolean ordenado = false;
    private static String ordenadoPor = null;

    public static void setOrdenado(boolean valor) {
        ordenado = valor;
        if (!valor) {
            // al desmarcar, también quitar cualquier criterio de orden conocido
            ordenadoPor = null;
            isordenadoPorFecha = false;
        } else {
            // si se marca como ordenado pero no hay criterio, mantener posible estado por fecha
            if (tipoActual != DatasetType.CITAS) {
                // para datasets distintos de CITAS no hay criterio conocido por defecto
                isordenadoPorFecha = false;
            }
        }
    }

    /**
     * Establece el criterio por el cual se considera ordenado el dataset.
     * Actualmente reconoce "fecha" y "fechaHora" (marca isordenadoPorFecha).
     */
    public static void setOrdenadoPor(String criterio) {
        if (criterio == null) {
            ordenadoPor = null;
            isordenadoPorFecha = false;
            return;
        }
        ordenadoPor = criterio.trim();
        // activar marca específica para citas
        if (ordenadoPor.equalsIgnoreCase("fecha") || ordenadoPor.equalsIgnoreCase("fechahora") || ordenadoPor.equalsIgnoreCase("fechaHora")) {
            isordenadoPorFecha = true;
        } else {
            isordenadoPorFecha = false;
        }
        // si se establece un criterio asumimos que el dataset está ordenado
        ordenado = true;
    }
    public static String getOrdenadoPor() {
        return ordenadoPor;
    }
}
