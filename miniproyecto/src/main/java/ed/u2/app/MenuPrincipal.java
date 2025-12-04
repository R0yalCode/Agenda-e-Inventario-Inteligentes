package ed.u2.app;

import ed.u2.util.ANSI;
import ed.u2.util.ConsoleUtils;
import ed.u2.io.FileUtils;
import ed.u2.data.DatasetManager;
import ed.u2.data.DatasetType;
import ed.u2.io.CsvLoader;
import ed.u2.model.*;

import ed.u2.sorting.BubbleSorter;
import ed.u2.sorting.SelectionSorter;
import ed.u2.sorting.InsertionSorter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Controlador del menú principal y lógica de interacción.
 */
public class MenuPrincipal {

    private final AppContext context;

    public MenuPrincipal(AppContext context) {
        this.context = context;
    }

    // ==========================================================
    // MÉTODO PRINCIPAL
    // ==========================================================
    public void iniciar() {

        String opcion;

        do {
            mostrarMenu();
            opcion = ConsoleUtils.leerLinea("Seleccione una opción: ");
            procesarOpcion(opcion);

        } while (!opcion.equals("8"));
    }

    // ==========================================================
    // MENÚ
    // ==========================================================
    private void mostrarMenu() {

        System.out.println(ANSI.CYAN_BOLD +
                "\n===== SISTEMA DE GESTIÓN: CITAS Y STOCK =====" + ANSI.RESET);

        System.out.println("1. Usar datasets oficiales .csv (embebidos)");
        System.out.println("2. Cargar archivo CSV manualmente");
        System.out.println("3. Ordenar registros");
        System.out.println("4. Ejecutar búsquedas");
        System.out.println("5. Ver historial de búsquedas");
        System.out.println("6. Ver estadísticas visuales");
        System.out.println("7. Exportar estadísticas / resultados");
        System.out.println("8. Salir\n");
    }

    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                usarDatasetsOficiales();
                break;
            case "2":
                cargarCsvManual();
                break;
            case "3":
                ordenarRegistros();
                break;
            case "4":
                ejecutarBusquedas();
                break;
            case "5":
                HistoryManager.mostrarHistorial();
                ConsoleUtils.pausar("");
                break;
            case "6":
                System.out.println(ANSI.YELLOW + "[Estadísticas aún no implementadas]" + ANSI.RESET);
                ConsoleUtils.pausar("");
                break;
            case "7":
                System.out.println(ANSI.YELLOW + "[Exportaciones aún no implementadas]" + ANSI.RESET);
                ConsoleUtils.pausar("");
                break;
            case "8":
                System.out.println(ANSI.GREEN + "Saliendo del sistema..." + ANSI.RESET);
                break;
            default:
                System.out.println(ANSI.RED_BOLD + "Opción inválida." + ANSI.RESET);
        }
    }

    // ==========================================================
    // OPCIÓN 1 – DATASETS OFICIALES
    // ==========================================================
     private void usarDatasetsOficiales() {

        System.out.println(ANSI.BLUE_BOLD + "\n=== DATASETS OFICIALES ===" + ANSI.RESET);
        System.out.println("1. citas_100");
        System.out.println("2. citas_100_casi_ordenadas");
        System.out.println("3. inventario_500_inverso");
        System.out.println("4. pacientes_500");

        int op = ConsoleUtils.leerEntero("Seleccione dataset: ");

        DatasetType tipo;

        switch (op) {
            case 1:
                tipo = DatasetType.CITAS_100;
                break;
            case 2:
                tipo = DatasetType.CITAS_100_CASI;
                break;
            case 3:
                tipo = DatasetType.INVENTARIO_500_INVERSO;
                break;
            case 4:
                tipo = DatasetType.PACIENTES_500;
                break;
            default:
                System.out.println(ANSI.RED + "Opción inválida." + ANSI.RESET);
                return;
        }

        DatasetManager.cargar(tipo);

        System.out.println(ANSI.GREEN + "Dataset '" + tipo + "' cargado correctamente." + ANSI.RESET);
        System.out.println(ANSI.CYAN + "Registros cargados: " + DatasetManager.getDatasetArray().length + ANSI.RESET);
    }

    // ==========================================================
    // OPCIÓN 2 – CARGA MANUAL
    // ==========================================================
    private void cargarCsvManual() {

        System.out.println(ANSI.CYAN_BOLD + "\n=== CARGAR CSV MANUAL ===" + ANSI.RESET);

        String ruta = ConsoleUtils.leerLinea("Ingrese la ruta del archivo CSV: ");

        String error = FileUtils.validarArchivoCsv(ruta);

        if (error != null) {
            System.out.println(ANSI.RED_BOLD + "Error: " + error + ANSI.RESET);
            return;
        }

        cargarDataset(ruta);
    }

    // ==========================================================
    // MÉTODO CENTRAL PARA CARGAR CSV
    // ==========================================================
    private void cargarDataset(String ruta) {

        System.out.println(ANSI.YELLOW + "\nCargando dataset..." + ANSI.RESET);

        List<String> lineas;

        try {
            lineas = Files.readAllLines(Paths.get(ruta));
        } catch (Exception e) {
            System.out.println(ANSI.RED_BOLD +
                    "Error al leer archivo: " + e.getMessage() + ANSI.RESET);
            return;
        }

        if (lineas.isEmpty()) {
            System.out.println(ANSI.RED_BOLD + "El CSV está vacío." + ANSI.RESET);
            return;
        }

        Map<String, Object> datos = CsvLoader.cargar(ruta);

        if (datos.isEmpty()) {
            System.out.println(ANSI.RED_BOLD + "No se reconoce el dataset." + ANSI.RESET);
            return;
        }

        if (datos.containsKey("citas")) {
            context.setCitas((List<Cita>) datos.get("citas"));
            context.setCitasArray(context.getCitas().toArray(new Cita[0]));
            System.out.println(ANSI.GREEN + "✔ Dataset de CITAS cargado." + ANSI.RESET);
        }

        if (datos.containsKey("inventario")) {
            context.setInventario((List<InventarioItem>) datos.get("inventario"));
            context.setInventarioArray(context.getInventario().toArray(new InventarioItem[0]));
            System.out.println(ANSI.GREEN + "✔ Dataset de INVENTARIO cargado." + ANSI.RESET);
        }

        if (datos.containsKey("pacientes")) {
            context.setPacientes((List<Paciente>) datos.get("pacientes"));
            context.setPacientesArray(context.getPacientes().toArray(new Paciente[0]));
            System.out.println(ANSI.GREEN + "✔ Dataset de PACIENTES cargado." + ANSI.RESET);
        }

        context.setDatasetActual(ruta);

        System.out.println(ANSI.GREEN_BOLD + "\nDataset cargado correctamente." + ANSI.RESET);
        ConsoleUtils.pausar("");
    }

    // ==========================================================
    // OPCIÓN 3 – ORDENACIÓN
    // ==========================================================
    private void ordenarRegistros() {

        if (!context.hayDatasetCargado()) {
            System.out.println(ANSI.RED_BOLD +
                    "❌ No hay dataset cargado." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.CYAN_BOLD + "\n=== ORDENACIÓN ===" + ANSI.RESET);
        System.out.println("1. Burbuja");
        System.out.println("2. Selección");
        System.out.println("3. Inserción");

        String tipo = ConsoleUtils.leerLinea("Algoritmo: ");

        System.out.println("1. Ascendente");
        System.out.println("2. Descendente");

        boolean asc = ConsoleUtils.leerLinea("Sentido: ").equals("1");

        if (context.getCitasArray() != null)
            ordenarCitas(tipo, asc);

        else if (context.getInventarioArray() != null)
            ordenarInventario(tipo, asc);

        else if (context.getPacientesArray() != null)
            ordenarPacientes(tipo, asc);

        else
            System.out.println(ANSI.RED_BOLD + "Dataset desconocido." + ANSI.RESET);
    }

    // ------------------------------
    private void ordenarCitas(String tipo, boolean asc) {

        Cita[] arr = context.getCitasArray();

        System.out.println(ANSI.YELLOW + "\nOrdenando CITAS..." + ANSI.RESET);

        long t0 = System.nanoTime();
        aplicarAlgoritmo(tipo, arr, asc);
        long t1 = System.nanoTime();

        System.out.println(ANSI.GREEN +
                "✔ Ordenamiento completado en " + (t1 - t0) + " ns" + ANSI.RESET);

        mostrarPrevioPosterior(arr);
    }

    private void ordenarPacientes(String tipo, boolean asc) {

        Paciente[] arr = context.getPacientesArray();

        System.out.println(ANSI.YELLOW + "\nOrdenando PACIENTES..." + ANSI.RESET);

        long t0 = System.nanoTime();
        aplicarAlgoritmo(tipo, arr, asc);
        long t1 = System.nanoTime();

        System.out.println(ANSI.GREEN +
                "✔ Ordenamiento completado en " + (t1 - t0) + " ns" + ANSI.RESET);

        mostrarPrevioPosterior(arr);
    }

    private void ordenarInventario(String tipo, boolean asc) {

        InventarioItem[] arr = context.getInventarioArray();

        System.out.println(ANSI.YELLOW + "\nOrdenando INVENTARIO..." + ANSI.RESET);

        long t0 = System.nanoTime();
        aplicarAlgoritmo(tipo, arr, asc);
        long t1 = System.nanoTime();

        System.out.println(ANSI.GREEN +
                "✔ Ordenamiento completado en " + (t1 - t0) + " ns" + ANSI.RESET);

        mostrarPrevioPosterior(arr);
    }

    // ------------------------------
    private <T extends Comparable<T>> void aplicarAlgoritmo(String tipo, T[] arr, boolean asc) {

        switch (tipo) {
            case "1":
                BubbleSorter.sort(arr, asc, true);
                break;
            case "2":
                SelectionSorter.sort(arr, asc, true);
                break;
            case "3":
                InsertionSorter.sort(arr, asc, true);
                break;
            default:
                System.out.println(ANSI.RED_BOLD + "Algoritmo inválido." + ANSI.RESET);
        }
    }

    // ------------------------------
    private <T> void mostrarPrevioPosterior(T[] arr) {

        System.out.println(ANSI.CYAN_BOLD + "\nPrimeros 10 registros:" + ANSI.RESET);
        for (int i = 0; i < Math.min(10, arr.length); i++)
            System.out.println(arr[i]);

        System.out.println(ANSI.CYAN_BOLD + "\nÚltimos 10 registros:" + ANSI.RESET);
        for (int i = Math.max(0, arr.length - 10); i < arr.length; i++)
            System.out.println(arr[i]);

        ConsoleUtils.pausar("\nEnter para continuar...");
    }

    // ==========================================================
    // OPCIÓN 4 – BÚSQUEDAS (impl. después)
    // ==========================================================
    private void ejecutarBusquedas() {
        System.out.println(ANSI.YELLOW +
                "\nBúsquedas aún no implementadas." + ANSI.RESET);
        ConsoleUtils.pausar("");
    }
}
