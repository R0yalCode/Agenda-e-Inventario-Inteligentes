package ed.u2.app;

import ed.u2.data.DatasetManager;
import ed.u2.io.ExportUtils;
import ed.u2.io.FileUtils;
import ed.u2.model.*;
import ed.u2.search.SearchEngine;
import ed.u2.search.SearchStats;
import ed.u2.sorting.*;
import ed.u2.stats.OperationStats;
import ed.u2.stats.SortingStatsManager;
import ed.u2.util.ANSI;
import ed.u2.util.ConsoleUtils;
import ed.u2.util.Holder;
import java.io.File;
import java.util.*;

public class MenuPrincipal {

    // ============================================================
    // MÉTODO PRINCIPAL
    // ============================================================

    public void iniciar() {

        String opcion;

        do {
            mostrarMenu();
            opcion = ConsoleUtils.leerLinea("Seleccione una opción: ");
            procesarOpcion(opcion);

        } while (!opcion.equals("8"));
    }

    // ============================================================
    // MENÚ
    // ============================================================

    private void mostrarMenu() {

        System.out.println(ANSI.CYAN_BOLD +
                "\n===== SISTEMA DE GESTIÓN: CITAS Y STOCK =====" + ANSI.RESET);

        System.out.println("1. Usar datasets CSV oficiales ");
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
                // El usuario solicitó ver estadísticas visuales -> habilitar exportación de estadísticas
                ExportUtils.setVisualStatsEnabled(true);
                SortingStatsManager.mostrar();
                ConsoleUtils.pausar("");
                break;
            case "7":
                System.out.println(ANSI.CYAN_BOLD + "\n=== EXPORTAR ESTADÍSTICAS / RESULTADOS ===" + ANSI.RESET);
                boolean ok = ExportUtils.exportEstadisticas();
                if (ok) {
                    System.out.println(ANSI.GREEN + "Exportación completada. Archivos guardados en la carpeta 'export'." + ANSI.RESET);
                } else {
                    System.out.println(ANSI.RED_BOLD + "Error al exportar. Revise permisos o inténtelo nuevamente." + ANSI.RESET);
                }
                ConsoleUtils.pausar("");
                break;
            case "8":
                System.out.println(ANSI.GREEN_BOLD + "Has salido correctamente" + ANSI.RESET);
                System.out.println(ANSI.GREEN + "Esperamos que vuelvas pronto." + ANSI.RESET);
                break;
            default:
                System.out.println(ANSI.RED_BOLD + "Opción inválida." + ANSI.RESET);
        }
    }

    

    // ============================================================
    // OPCIÓN 1 – DATASETS OFICIALES
    // ============================================================

    private void usarDatasetsOficiales() {

        System.out.println(ANSI.CYAN_BOLD + "\n=== DATASETS OFICIALES ===" + ANSI.RESET);

        var archivos = DatasetManager.listarDatasetsOficiales();

        if (archivos.isEmpty()) {
            System.out.println(ANSI.RED + "No se encontraron archivos CSV oficiales." + ANSI.RESET);
            return;
        }

        int i = 1;
        for (File f : archivos)
            System.out.println(i++ + ". " + f.getName());

        int opc = ConsoleUtils.leerEntero("Seleccione dataset:");

        if (opc < 1 || opc > archivos.size()) {
            System.out.println(ANSI.RED + "Opción inválida." + ANSI.RESET);
            return;
        }

        DatasetManager.cargarDatasetOficial(archivos.get(opc - 1));

        if (DatasetManager.hayDataset()) {
            System.out.println(ANSI.GREEN_BOLD + "Dataset cargado correctamente.\n" + ANSI.RESET);
        } else {
            System.out.println(ANSI.RED_BOLD + "Error cargando dataset.\n" + ANSI.RESET);
        }
        // Actualizar estado en ExportUtils
        ExportUtils.setDatasetsLoaded(DatasetManager.hayDataset());
    }

    // ============================================================
    // OPCIÓN 2 – CARGAR CSV MANUAL
    // ============================================================

    private void cargarCsvManual() {

        System.out.println(ANSI.CYAN_BOLD + "\n=== CARGAR CSV MANUAL ===" + ANSI.RESET);

        String ruta = ConsoleUtils.leerLinea("Ingrese la ruta del archivo CSV:");

        String error = FileUtils.validarArchivoCsv(ruta);

        if (error != null) {
            System.out.println(ANSI.RED_BOLD + "Error: " + error + ANSI.RESET);
            return;
        }

        DatasetManager.cargarDatasetManual(ruta);

        if (DatasetManager.hayDataset()) {
            System.out.println(ANSI.GREEN_BOLD + "\nDataset cargado correctamente.\n" + ANSI.RESET);

        } else {
            System.out.println(ANSI.RED_BOLD + "El CSV no coincide con ningún dataset conocido.\n" + ANSI.RESET);
        }
        // Actualizar estado en ExportUtils
        ExportUtils.setDatasetsLoaded(DatasetManager.hayDataset());
    }

    // ============================================================
    // OPCIÓN 3 — ORDENACIÓN
    // ============================================================

    private void ordenarRegistros() {
        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED_BOLD + " No hay dataset cargado." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.CYAN_BOLD + "\n=== ORDENACIÓN ===" + ANSI.RESET);
        System.out.println("1. Burbuja");
        System.out.println("2. Selección");
        System.out.println("3. Inserción");

        String tipo = ConsoleUtils.leerLinea("Algoritmo: ");

        // Validar opción de algoritmo
        if (!tipo.equals("1") && !tipo.equals("2") && !tipo.equals("3")) {
            System.out.println(ANSI.RED_BOLD + "Opción inválida. Seleccione 1, 2 o 3." + ANSI.RESET);
            return;
        }

        System.out.println("1. Ascendente");
        System.out.println("2. Descendente");

        // Leer y validar sentido una sola vez
        String sentido = ConsoleUtils.leerLinea("Sentido: ");

        if (!sentido.equals("1") && !sentido.equals("2")) {
            System.out.println(ANSI.RED_BOLD + "Opción inválida. Seleccione 1 o 2 para el sentido." + ANSI.RESET);
            return;
        }

        boolean asc = sentido.equals("1");

        switch (DatasetManager.getTipoActual()) {
            case CITAS -> ordenarCitas(tipo, asc);
            case PACIENTES -> ordenarPacientes(tipo, asc);
            case INVENTARIO -> ordenarInventario(tipo, asc);
            default -> System.out.println(ANSI.RED_BOLD + "Dataset desconocido." + ANSI.RESET);
        }
    }

    private <T> void mostrarEstadisticasOrden(T[] arr, OperationStats st) {

        System.out.println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "║                PRIMEROS 10 REGISTROS                   ║" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        for (int i = 0; i < Math.min(10, arr.length); i++) {
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, arr[i]);
        }
        System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

        System.out.println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "║                ÚLTIMOS 10 REGISTROS                    ║" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        for (int i = Math.max(0, arr.length - 10); i < arr.length; i++) {
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, arr[i]);
        }
        System.out.println(ANSI.CYAN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

        System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║                ESTADÍSTICAS DE ORDENACIÓN              ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
        System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.getComparisons());
        System.out.printf(ANSI.CYAN + "║ Intercambios         ║ %-28d    ║\n" + ANSI.RESET, st.getSwaps());
        System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.getTime());
        System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);

        ConsoleUtils.pausar("\n");
    }

    private void ordenarCitas(String tipo, boolean asc) {

        Cita[] arr = Arrays.copyOf(
                DatasetManager.getArray(),
                DatasetManager.getArray().length,
                Cita[].class);

        // Tiempo real del algoritmo
        long t0 = System.nanoTime();
        OperationStats st = aplicarAlgoritmo(tipo, arr, asc);
        long t1 = System.nanoTime();

        st.setTime(t1 - t0);

        mostrarEstadisticasOrden(arr, st);
    }

    private void ordenarPacientes(String tipo, boolean asc) {

        Paciente[] arr = Arrays.copyOf(
                DatasetManager.getArray(),
                DatasetManager.getArray().length,
                Paciente[].class);

        long t0 = System.nanoTime();
        OperationStats st = aplicarAlgoritmo(tipo, arr, asc);
        long t1 = System.nanoTime();

        st.setTime(t1 - t0);

        mostrarEstadisticasOrden(arr, st);
    }

    private void ordenarInventario(String tipo, boolean asc) {

        InventarioItem[] arr = Arrays.copyOf(
                DatasetManager.getArray(),
                DatasetManager.getArray().length,
                InventarioItem[].class);

        long t0 = System.nanoTime();
        OperationStats st = aplicarAlgoritmo(tipo, arr, asc);
        long t1 = System.nanoTime();

        st.setTime(t1 - t0);

        mostrarEstadisticasOrden(arr, st);
    }

    private <T extends Comparable<T>> OperationStats aplicarAlgoritmo(String tipo, T[] arr, boolean asc) {

        return switch (tipo) {
            case "1" -> BubbleSorter.sort(arr, asc);
            case "2" -> SelectionSorter.sort(arr, asc);
            case "3" -> InsertionSorter.sort(arr, asc);
            default -> null;
        };
    }

    // ==========================================================
    // OPCIÓN 4 – BÚSQUEDAS
    // ==========================================================
    private void ejecutarBusquedas() {

        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED_BOLD + " No hay dataset cargado." + ANSI.RESET);
            ConsoleUtils.pausar("");
            return;
        }

        while (true) {
            System.out.println(ANSI.CYAN_BOLD + "\n=== BÚSQUEDAS ===" + ANSI.RESET);

            System.out.println("1. Búsqueda lineal por ID");
            System.out.println("2. Lineal con centinela");
            System.out.println("3. findAll (todas coincidencias)");
            System.out.println("4. first");
            System.out.println("5. last");
            System.out.println("6. Binaria");
            System.out.println("7. Binary bounds (duplicados)");
            System.out.println("8. Búsquedas en SLL");
            System.out.println("9. Volver al menú principal");

            String op = ConsoleUtils.leerLinea("Opción: ");

            switch (op) {
                case "1" -> busquedaLinealID();
                case "2" -> busquedaCentinela();
                case "3" -> busquedaFindAll();
                case "4" -> busquedaFirst();
                case "5" -> busquedaLast();
                case "6" -> busquedaBinaria();
                case "7" -> busquedaBounds();
                case "8" -> submenuSLL();
                case "9" -> {
                    return;
                }
                default -> System.out.println(ANSI.RED_BOLD + "Opción inválida" + ANSI.RESET);
            }
        }
    }

    private void busquedaLinealID() {

        String id = ConsoleUtils.leerLinea("Ingrese ID a buscar:");

        SearchStats stats = new SearchStats();

        Object res = SearchEngine.buscarPorIdLineal(id, stats);

        if (res == null) {
            System.out.println(ANSI.RED + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.RED + "║                NO SE ENCONTRO EL REGISTRO              ║" + ANSI.RESET);
            System.out.println(ANSI.RED + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        } else {
            System.out
                    .println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "║                    REGISTRO ENCONTRADO                 ║" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, res.toString());
            System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

            System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║          ESTADÍSTICAS DE BÚSQUEDA (Lineal por ID)      ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, stats.comparaciones);
            System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET,
                    stats.resultadosEncontrados);
            System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, stats.tiempoNs);
            System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);
        }
        

        // Registrar en historial
        HistoryManager.log(
                "SEARCH",
                "Lineal por ID",
                "id=" + id.toUpperCase(),
                stats.resultadosEncontrados,
                stats.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void busquedaCentinela() {
        String id = ConsoleUtils.leerLinea("Ingrese ID a buscar:");

        SearchStats stats = new SearchStats();

        long t0 = System.nanoTime();
        Object res = DatasetManager.buscarPorId(id);
        long t1 = System.nanoTime();

        // Rellenar estadísticas básicas (DatasetManager.buscarPorId no las actualiza)
        stats.resultadosEncontrados = (res == null) ? 0 : 1;
        stats.setTiempo(t1 - t0);

        if (res == null) {
            System.out.println(ANSI.RED + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.RED + "║                NO SE ENCONTRO EL REGISTRO              ║" + ANSI.RESET);
            System.out.println(ANSI.RED + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        } else {
            System.out
                    .println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "║                    REGISTRO ENCONTRADO                 ║" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, res.toString());
            System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

            System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║          ESTADÍSTICAS DE BÚSQUEDA (Centinela por ID)   ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, stats.comparaciones);
            System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET,
                    stats.resultadosEncontrados);
            System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, stats.tiempoNs);
            System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);
        }
    

        // Registrar en historial
        HistoryManager.log(
                "SEARCH",
                "Centinela por ID",
                "id=" + id.toUpperCase(),
                1,
                (t1 - t0));

        ConsoleUtils.pausar("");

    }

    private void busquedaFindAll() {

        String atributo = seleccionarAtributo();
        String valor = ConsoleUtils.leerLinea("Valor a buscar: ");

        SearchStats st = new SearchStats();

        long t0 = System.nanoTime();
        List<Object> lista = SearchEngine.findAllPorAtributo(atributo, valor, st);
        long t1 = System.nanoTime();

        st.setTiempo(t1 - t0);

        if (lista.isEmpty()) {
            System.out.println(ANSI.RED + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.RED + "║                NO SE ENCONTRARON RESULTADOS            ║" + ANSI.RESET);
            System.out.println(ANSI.RED + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        } else {
            System.out
                    .println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "║                RESULTADOS ENCONTRADOS                  ║" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            for (Object o : lista) {
                System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, o.toString());
            }
            System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

            System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║                ESTADÍSTICAS DE BÚSQUEDA (findAll)      ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.comparaciones);
            System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET,
                    st.resultadosEncontrados);
            System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.tiempoNs);
            System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);
        }

        HistoryManager.log(
                "SEARCH",
                "findAll por " + atributo,
                atributo + "=" + valor.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void busquedaFirst() {

        String atributo = seleccionarAtributo();
        String valor = ConsoleUtils.leerLinea("Valor a buscar: ");

        SearchStats st = new SearchStats();

        Object res = SearchEngine.firstPorAtributo(atributo, valor, st);

        if (res == null) {
            System.out.println(ANSI.RED + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.RED + "║                NO SE ENCONTRARON RESULTADOS            ║" + ANSI.RESET);
            System.out.println(ANSI.RED + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        } else {
            System.out
                    .println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "║                RESULTADOS ENCONTRADOS                  ║" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, res.toString());
            System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

            System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║                ESTADÍSTICAS DE BÚSQUEDA (First)        ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.comparaciones);
            System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET,
                    st.resultadosEncontrados);
            System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.tiempoNs);
            System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);
        }

        // historial
        HistoryManager.log(
                "SEARCH",
                "First por " + atributo,
                atributo + "=" + valor.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void busquedaLast() {

        String atributo = seleccionarAtributo();
        String valor = ConsoleUtils.leerLinea("Valor a buscar: ");

        SearchStats st = new SearchStats();

        Object res = SearchEngine.lastPorAtributo(atributo, valor, st);

        if (res == null) {
            System.out.println(ANSI.RED + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.RED + "║                NO SE ENCONTRARON RESULTADOS            ║" + ANSI.RESET);
            System.out.println(ANSI.RED + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        } else {
            System.out
                    .println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "║                RESULTADOS ENCONTRADOS                  ║" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, res.toString());
            System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

            System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║                ESTADÍSTICAS DE BÚSQUEDA (Last)         ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
            System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.comparaciones);
            System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET,
                    st.resultadosEncontrados);
            System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.tiempoNs);
            System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);
        }

        // Historial
        HistoryManager.log(
                "SEARCH",
                "Last por " + atributo,
                atributo + "=" + valor.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void busquedaBinaria() {

        String id = ConsoleUtils.leerLinea("ID a buscar (requiere ordenado): ");

        Object[] arr = DatasetManager.getArray();

        SearchStats st = new SearchStats();

        int pos = SearchEngine.binarySearchStats(arr, id, st);

        if (pos >= 0) {
            mostrarResultadoBusqueda(arr[pos], "Binaria", st.tiempoNs);
            
        System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║                ESTADÍSTICAS DE BÚSQUEDA (Binaria)      ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
        System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.comparaciones);
        System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET, st.resultadosEncontrados);
        System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.tiempoNs);
        System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);

        } else {
            mostrarResultadoBusqueda(null, "Binaria", st.tiempoNs);
        }

        HistoryManager.log(
                "SEARCH",
                "Binaria por ID",
                "id=" + id.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void busquedaBounds() {

        String id = ConsoleUtils.leerLinea("ID duplicado a buscar: ");
        Object[] arr = DatasetManager.getArray();

        SearchStats st = SearchEngine.boundsPorId(arr, id);

        System.out.println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "║                RESULTADOS ENCONTRADOS                  ║" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET,
                "Cantidad de resultados: " + st.resultadosEncontrados);
        System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

        System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║                ESTADÍSTICAS DE BÚSQUEDA (Bounds)       ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
        System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.comparaciones);
        System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET, st.resultadosEncontrados);
        System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.tiempoNs);
        System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);

        HistoryManager.log(
                "SEARCH",
                "Bounds por ID",
                "id=" + id.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void submenuSLL() {

        while (true) {
            System.out.println(ANSI.CYAN_BOLD + "\n=== BÚSQUEDAS SLL ===" + ANSI.RESET);

            System.out.println("1. findAll (todas coincidencias)");
            System.out.println("2. first (primera coincidencia)");
            System.out.println("3. last (última coincidencia)");
            System.out.println("4. Volver");

            String op = ConsoleUtils.leerLinea("Opción: ");

            switch (op) {
                case "1" -> sllFindAll();
                case "2" -> sllFirst();
                case "3" -> sllLast();
                case "4" -> {
                    return;
                }
                default -> System.out.println(ANSI.RED_BOLD + "Opción inválida" + ANSI.RESET);
            }
        }
    }

    private void sllFindAll() {

        String atributo = seleccionarAtributo();
        String valor = ConsoleUtils.leerLinea("Valor: ");

        List<Object> resultados = new ArrayList<>();

        SearchStats st = SearchEngine.sllFindAllStats(atributo, valor, resultados);

        System.out.println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "║                RESULTADOS ENCONTRADOS                  ║" + ANSI.RESET);
        System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        for (Object o : resultados) {
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, o.toString());
        }
        System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);

        mostrarStatsBusqueda("SLL-findAll", st);

        HistoryManager.log(
                "SEARCH",
                "SLL-findAll por " + atributo,
                atributo + "=" + valor.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void sllFirst() {

        String atributo = seleccionarAtributo();
        String valor = ConsoleUtils.leerLinea("Valor: ");

        Holder<Object> res = new Holder<>();

        SearchStats st = SearchEngine.sllFirstStats(atributo, valor, res);

        mostrarResultadoBusqueda(res.value, "SLL-First", st.tiempoNs);
        mostrarStatsBusqueda("SLL-First", st);

        HistoryManager.log(
                "SEARCH",
                "SLL-First por " + atributo,
                atributo + "=" + valor.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void sllLast() {

        String atributo = seleccionarAtributo();
        String valor = ConsoleUtils.leerLinea("Valor: ");

        Holder<Object> res = new Holder<>();

        SearchStats st = SearchEngine.sllLastStats(atributo, valor, res);

        mostrarResultadoBusqueda(res.value, "SLL-Last", st.tiempoNs);
        mostrarStatsBusqueda("SLL-Last", st);

        HistoryManager.log(
                "SEARCH",
                "SLL-Last por " + atributo,
                atributo + "=" + valor.toUpperCase(),
                st.resultadosEncontrados,
                st.tiempoNs);

        ConsoleUtils.pausar("");
    }

    private void mostrarStatsBusqueda(String tipo, SearchStats st) {

        System.out.println(ANSI.CYAN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
        System.out.printf(ANSI.CYAN + "║                ESTADÍSTICAS DE BÚSQUEDA (%-10s)        ║\n" + ANSI.RESET,
                tipo);
        System.out.println(ANSI.CYAN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "║ Métrica              ║ Valor                           ║" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "╠══════════════════════╬═════════════════════════════════╣" + ANSI.RESET);
        System.out.printf(ANSI.CYAN + "║ Comparaciones        ║ %-28d    ║\n" + ANSI.RESET, st.comparaciones);
        System.out.printf(ANSI.CYAN + "║ Resultados           ║ %-28d    ║\n" + ANSI.RESET, st.resultadosEncontrados);
        System.out.printf(ANSI.CYAN + "║ Tiempo (ns)          ║ %-28d    ║\n" + ANSI.RESET, st.tiempoNs);
        System.out.println(ANSI.CYAN + "╚══════════════════════╩═════════════════════════════════╝" + ANSI.RESET);
    }

    private void mostrarResultadoBusqueda(Object res, String tipo, long tiempo) {

        if (res == null) {
            System.out.println(ANSI.RED + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.RED + "║                NO SE ENCONTRÓ EL REGISTRO              ║" + ANSI.RESET);
            System.out.println(ANSI.RED + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        } else {
            System.out
                    .println(ANSI.GREEN + "\n╔════════════════════════════════════════════════════════╗" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "║                    REGISTRO ENCONTRADO                 ║" + ANSI.RESET);
            System.out.println(ANSI.GREEN + "╠════════════════════════════════════════════════════════╣" + ANSI.RESET);
            System.out.printf(ANSI.GREEN + "║ %-54s ║\n" + ANSI.RESET, res.toString());
            System.out.println(ANSI.GREEN + "╚════════════════════════════════════════════════════════╝" + ANSI.RESET);
        }

    }

    private String seleccionarAtributo() {

        DatasetManager.DatasetType t = DatasetManager.getTipoActual();

        System.out.println("\nAtributo a buscar:");

        Map<Integer, String> opciones = new LinkedHashMap<>();

        switch (t) {

            case CITAS -> {
                opciones.put(1, "id");
                opciones.put(2, "apellido");
                opciones.put(3, "fecha");
            }

            case PACIENTES -> {
                opciones.put(1, "id");
                opciones.put(2, "apellido");
                opciones.put(3, "prioridad");
            }

            case INVENTARIO -> {
               opciones.put(1, "id");
                opciones.put(2, "insumo");
                opciones.put(3, "stock");
            }

            default -> {
                 System.out.println(ANSI.RED_BOLD + "Dataset desconocido. Se usará 'id' por defecto." + ANSI.RESET);
                return "id";
            }
        }
        
              // Mostrar opciones
        for (Map.Entry<Integer, String> e : opciones.entrySet()) {
            System.out.println(e.getKey() + ". " + e.getValue());
        }

        // Leer y validar en bucle
        while (true) {
            String entrada = ConsoleUtils.leerLinea("Opción: ");
            int op;
            try {
                op = Integer.parseInt(entrada);
            } catch (NumberFormatException ex) {
                System.out.println(ANSI.RED_BOLD + "Entrada inválida. Ingrese un número correspondiente a las opciones mostradas." + ANSI.RESET);
                continue;
            }

            if (!opciones.containsKey(op)) {
                System.out.println(ANSI.RED_BOLD + "Opción inválida. Seleccione una opción que se muestre en el menú." + ANSI.RESET);
                continue;
            }

            return opciones.get(op);
        }




    }
}
