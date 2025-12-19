package ed.u2.data;

import ed.u2.app.HistoryManager;
import ed.u2.io.CsvLoader;
import ed.u2.model.*;
import ed.u2.sll.SinglyLinkedList;
import ed.u2.stats.SortingStatsManager;

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

        if (o instanceof Cita c) idActual = c.getId();
        if (o instanceof Paciente p) idActual = p.getId();
        if (o instanceof InventarioItem it) idActual = it.getId();

        if (idActual != null && idActual.equalsIgnoreCase(buscado))
            return o;
    }
    return null;
}



// ============================================================
// GETTERS TIPADOS (para facilitar uso en MenuPrincipal)
// ============================================================

public static Cita[] getCitasArray() {
    if (tipoActual != DatasetType.CITAS) return null;
    return Arrays.copyOf(arrayActual, arrayActual.length, Cita[].class);
}

public static Paciente[] getPacientesArray() {
    if (tipoActual != DatasetType.PACIENTES) return null;
    return Arrays.copyOf(arrayActual, arrayActual.length, Paciente[].class);
}

public static InventarioItem[] getInventarioArray() {
    if (tipoActual != DatasetType.INVENTARIO) return null;
    return Arrays.copyOf(arrayActual, arrayActual.length, InventarioItem[].class);
}

}
