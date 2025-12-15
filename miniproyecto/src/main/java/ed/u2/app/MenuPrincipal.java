    package ed.u2.app;

    import ed.u2.data.DatasetManager;
    import ed.u2.io.FileUtils;
    import ed.u2.model.*;
    import ed.u2.search.SearchEngine;
    import ed.u2.sorting.*;
    import ed.u2.stats.OperationStats;
    import ed.u2.util.ANSI;
    import ed.u2.util.ConsoleUtils;

    import java.io.File;
    import java.util.*;

    import static ed.u2.experiment.SortingComparator.compararCasiOrdenadoVsInverso;
    import static ed.u2.experiment.SortingComparator.compararDatasetActual;

    public class MenuPrincipal {

        public void iniciar() {

            String opcion;
            do {
                mostrarMenu();
                opcion = ConsoleUtils.leerLinea("Seleccione una opción: ");
                procesarOpcion(opcion);

            } while (!opcion.equals("9"));
        }

        private void mostrarMenu() {
            System.out.println(ANSI.CYAN_BOLD +
                    "\n===== SISTEMA DE GESTIÓN: CITAS Y STOCK =====" + ANSI.RESET);

            System.out.println("1. Usar datasets CSV oficiales ");
            System.out.println("2. Cargar archivo CSV manualmente");
            System.out.println("3. Ordenar registros");
            System.out.println("4. Comparar algoritmos de ordenación");  // ← NUEVA
            System.out.println("5. Ejecutar búsquedas");
            System.out.println("6. Ver historial de búsquedas");
            System.out.println("7. Ver estadísticas visuales");
            System.out.println("8 Exportar estadísticas / resultados");
            System.out.println("9. Salir\n");
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
                case "4":  // ← NUEVO: Comparar algoritmos
                    compararAlgoritmos();
                    break;
                case "5":
                    ejecutarBusquedas();
                    break;
                case "6":
                    HistoryManager.mostrarHistorial();
                    ConsoleUtils.pausar("");
                    break;
                case "7":
                    mostrarMatrizDecision();
                    break;
                case "8":
                    exportarResultadosCompletos();
                    break;
                case "9":
                    System.out.println(ANSI.GREEN_BOLD + "Has salido correctamente" + ANSI.RESET);
                    System.out.println(ANSI.GREEN + "Esperamos que vuelvas pronto." + ANSI.RESET);
                    break;
                default:
                    System.out.println(ANSI.RED_BOLD + "Opción inválida." + ANSI.RESET);
            }
        }

        private void compararAlgoritmos() {
            if (!DatasetManager.hayDataset()) {
                System.out.println(ANSI.RED_BOLD + " No hay dataset cargado." + ANSI.RESET);
                return;
            }

            System.out.println(ANSI.CYAN_BOLD + "\n=== COMPARACIÓN DE ALGORITMOS ===" + ANSI.RESET);
            System.out.println("1. Comparar en dataset actual");
            System.out.println("2. Demostración: casi-ordenado vs inverso");
            System.out.println("3. Volver");

            String op = ConsoleUtils.leerLinea("Opción: ");

            switch (op) {
                case "1" -> compararDatasetActual();
                case "2" -> compararCasiOrdenadoVsInverso();
                case "3" -> {
                    return; }
                default -> System.out.println(ANSI.RED + "Opción inválida" + ANSI.RESET);
            }

            ConsoleUtils.pausar("");
        }

        private void mostrarMatrizDecision() {
            System.out.println(ANSI.CYAN_BOLD +
                    "\n╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                   MATRIZ DE DECISIÓN: SI... ENTONCES...                ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ SI la situación es...                    ║ ENTONCES usar...          ║");
            System.out.println("╠══════════════════════════════════════════╬═══════════════════════════╣");
            System.out.println("║ Dataset pequeño (<100 elementos)         ║ Inserción (simple)        ║");
            System.out.println("║ Dataset casi ordenado                   ║ Inserción (Óptimo O(n))    ║");
            System.out.println("║ Dataset totalmente inverso               ║ Selección (swaps O(n))    ║");
            System.out.println("║ Minimizar movimientos de memoria         ║ Selección (swaps mínimos) ║");
            System.out.println("║ No importan swaps, quiero simplicidad    ║ Burbuja (didáctica)       ║");
            System.out.println("║ Necesito búsqueda frecuente              ║ Ordenar + Binaria         ║");
            System.out.println("║ Estructura es SLL (lista enlazada)       ║ Solo búsqueda secuencial  ║");
            System.out.println("║ Hay muchos duplicados                    ║ lowerBound/upperBound     ║");
            System.out.println("║ Datos no caben en memoria                ║ Ordenación externa        ║");
            System.out.println("╚══════════════════════════════════════════╩═══════════════════════════╝" + ANSI.RESET);

            System.out.println("\n" + ANSI.GREEN + "REGLA DE ORO:" + ANSI.RESET);
            System.out.println("Para datasets reales pequeños-medianos: Inserción");
            System.out.println("Para datasets grandes o búsquedas: Ordenar + Binaria");
            System.out.println("Para listas enlazadas: Secuencial (primera, última, findAll)");

            ConsoleUtils.pausar();
        }

        private void exportarResultadosCompletos() {
            if (!DatasetManager.hayDataset()) {
                System.out.println(ANSI.RED_BOLD + " No hay dataset cargado." + ANSI.RESET);
                return;
            }

            System.out.println(ANSI.CYAN_BOLD + "\n=== EXPORTAR RESULTADOS ===" + ANSI.RESET);
            System.out.println("1. Exportar historial completo");
            System.out.println("2. Exportar estadísticas de comparación");
            System.out.println("3. Volver");

            String op = ConsoleUtils.leerLinea("Opción: ");

            switch (op) {
                case "1" -> exportarHistorial();
                case "2" -> { return; }
                default -> System.out.println(ANSI.RED + "Opción inválida" + ANSI.RESET);
            }

            ConsoleUtils.pausar("");
        }

        private void exportarHistorial() {
            String ruta = ConsoleUtils.leerLinea("Ruta del archivo (enter para default): ");
            if (ruta.trim().isEmpty()) {
                ruta = "resources/history/historial_completo_" +
                        java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                        ".csv";
            }

            HistoryManager.exportarCsv(ruta);
        }

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
        }
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
        }

        private void ordenarRegistros() {
            if (!DatasetManager.hayDataset()) {
                System.out.println(ANSI.RED_BOLD + " No hay dataset cargado." + ANSI.RESET);
                return;
            }

            System.out.println(ANSI.CYAN_BOLD + "\n=== ORDENACIÓN ===" + ANSI.RESET);

            String tipo;
            while (true) {
                System.out.println("1. Burbuja");
                System.out.println("2. Selección");
                System.out.println("3. Inserción");

                tipo = ConsoleUtils.leerLinea("Algoritmo: ").trim();

                if (tipo.equals("1") || tipo.equals("2") || tipo.equals("3")) {
                    break;
                }

                System.out.println(ANSI.RED + " Opción inválida. Elija 1, 2 o 3." + ANSI.RESET);
            }

            boolean asc;
            while (true) {
                System.out.println("1. Ascendente");
                System.out.println("2. Descendente");

                String sentido = ConsoleUtils.leerLinea("Sentido: ").trim();

                if (sentido.equals("1")) {
                    asc = true;
                    break;
                }

                if (sentido.equals("2")) {
                    asc = false;
                    break;
                }

                System.out.println(ANSI.RED + " Opción inválida. Elija 1 o 2." + ANSI.RESET);
            }

            switch (DatasetManager.getTipoActual()) {
                case CITAS -> ordenarCitas(tipo, asc);
                case PACIENTES -> ordenarPacientes(tipo, asc);
                case INVENTARIO -> ordenarInventario(tipo, asc);
                default ->
                        System.out.println(ANSI.RED_BOLD + " Dataset desconocido." + ANSI.RESET);
            }
        }


      private <T> void mostrarEstadisticasOrden(T[] arr, OperationStats st) {

        System.out.println(ANSI.CYAN_BOLD + "\nPrimeros 10 registros:" + ANSI.RESET);
        for (int i = 0; i < Math.min(10, arr.length); i++)
            System.out.println(arr[i]);

        System.out.println(ANSI.CYAN_BOLD + "\nÚltimos 10 registros:" + ANSI.RESET);
        for (int i = Math.max(0, arr.length - 10); i < arr.length; i++)
            System.out.println(arr[i]);

        System.out.println(ANSI.YELLOW_BOLD + "\n--- ESTADÍSTICAS DE ORDENACIÓN ---" + ANSI.RESET);
        System.out.println("Comparaciones: " + st.getComparisons());
        System.out.println("Intercambios:  " + st.getSwaps());
        System.out.println("Tiempo (ns):   " + st.getTime());

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

    private void mostrarEstadisticasOrden(OperationStats st) {

        if (st == null) {
            System.out.println("No se generaron estadísticas.");
            return;
        }

        System.out.println(ANSI.YELLOW_BOLD + "\n--- ESTADÍSTICAS DE ORDENACIÓN ---" + ANSI.RESET);

        System.out.println("Comparaciones: " + st.getComparisons());
        System.out.println("Intercambios:  " + st.getSwaps());
        System.out.println("Tiempo (ns):   " + st.getTime());

        ConsoleUtils.pausar("\n");
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

            long t0 = System.nanoTime();
            Object res = DatasetManager.buscarPorId(id);
            long t1 = System.nanoTime();

            if (res == null) {
                System.out.println(ANSI.RED + " No se encontró el registro." + ANSI.RESET);
            } else {
                System.out.println(ANSI.GREEN + " Registro encontrado:" + ANSI.RESET);
                System.out.println(res);
            }

            System.out.println(ANSI.CYAN_BOLD + " Tiempo: " + (t1 - t0) + " ns" + ANSI.RESET);

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "Lineal por ID",
                    "id=" + id.toUpperCase(),
                    1,
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void busquedaCentinela() {
            String id = ConsoleUtils.leerLinea("Ingrese ID a buscar:");

            long t0 = System.nanoTime();
            Object res = DatasetManager.buscarPorId(id);
            long t1 = System.nanoTime();

            if (res == null) {
                System.out.println(ANSI.RED + " No se encontró el registro." + ANSI.RESET);
            } else {
                System.out.println(ANSI.GREEN + " Registro encontrado:" + ANSI.RESET);
                System.out.println(res);
            }

            System.out.println(ANSI.CYAN_BOLD + " Tiempo: " + (t1 - t0) + " ns" + ANSI.RESET);

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

            long t0 = System.nanoTime();
            var lista = SearchEngine.findAllPorAtributo(atributo, valor);
            long t1 = System.nanoTime();

            System.out.println(ANSI.GREEN_BOLD + "\nResultados encontrados: " + lista.size() + ANSI.RESET);

            for (Object o : lista)
                System.out.println(o);

            System.out.println("\nTiempo: " + (t1 - t0) + " ns\n");

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "findAll por " + atributo,
                    atributo + "=" + valor.toUpperCase(),
                    lista.size(),
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void busquedaFirst() {

            String atributo = seleccionarAtributo();
            String valor = ConsoleUtils.leerLinea("Valor a buscar: ");

            long t0 = System.nanoTime();
            Object res = SearchEngine.firstPorAtributo(atributo, valor);
            long t1 = System.nanoTime();

            mostrarResultadoBusqueda(res, "First", t1 - t0);

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "First por " + atributo,
                    atributo + "=" + valor.toUpperCase(),
                    res == null ? 0 : 1,
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void busquedaLast() {

            String atributo = seleccionarAtributo();
            String valor = ConsoleUtils.leerLinea("Valor a buscar: ");

            long t0 = System.nanoTime();
            Object res = SearchEngine.lastPorAtributo(atributo, valor);
            long t1 = System.nanoTime();

            mostrarResultadoBusqueda(res, "Last", t1 - t0);

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "Last por " + atributo,
                    atributo + "=" + valor.toUpperCase(),
                    res == null ? 0 : 1,
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void busquedaBinaria() {
            String id = ConsoleUtils.leerLinea("ID a buscar (requiere ordenado): ");

            Object[] arr = DatasetManager.getArray();

            // VALIDACIÓN CRÍTICA - REQUERIDA POR EL PDF
            if (!SearchEngine.estaOrdenadoPorId(arr)) {
                System.out.println(ANSI.RED_BOLD +
                        "ERROR: Array no ordenado por ID. " +
                        "Búsqueda binaria requiere ordenación previa." + ANSI.RESET);
                System.out.println(ANSI.YELLOW +
                        "Sugerencia: Use primero 'Ordenar registros' (Opción 3)" + ANSI.RESET);
                return;
            }

            long t0 = System.nanoTime();
            int pos = SearchEngine.binarySearch(arr, id);
            long t1 = System.nanoTime();

            if (pos >= 0)
                mostrarResultadoBusqueda(arr[pos], "Binaria", t1 - t0);
            else
                mostrarResultadoBusqueda(null, "Binaria", t1 - t0);

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "Binaria por ID",
                    "id=" + id.toUpperCase(),
                    pos >= 0 ? 1 : 0,
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void busquedaBounds() {

            String id = ConsoleUtils.leerLinea("ID duplicado a buscar: ");

            Object[] arr = DatasetManager.getArray();

            long t0 = System.nanoTime();
            int lb = SearchEngine.lowerBound(arr, id);
            int ub = SearchEngine.upperBound(arr, id);
            long t1 = System.nanoTime();

            System.out.println(ANSI.GREEN_BOLD + "limite inferior = " + lb + ANSI.RESET);
            System.out.println(ANSI.GREEN_BOLD + "limite superior = " + ub + ANSI.RESET);
            System.out.println("Total duplicados = " + (ub - lb));
            System.out.println("\nTiempo: " + (t1 - t0) + " ns");

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "Bounds por ID",
                    "id=" + id.toUpperCase(),
                    (ub - lb),
                    (t1 - t0));

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

            long t0 = System.nanoTime();
            List<Object> resultados = SearchEngine.sllFindAll(atributo, valor);
            long t1 = System.nanoTime();

            System.out.println(ANSI.GREEN_BOLD + "\nResultados encontrados: " + resultados.size() + ANSI.RESET);

            for (Object o : resultados)
                System.out.println(o);

            System.out.println("\nTiempo: " + (t1 - t0) + " ns\n");

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "SLL-findAll por " + atributo,
                    atributo + "=" + valor.toUpperCase(),
                    resultados.size(),
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void sllFirst() {

            String atributo = seleccionarAtributo();
            String valor = ConsoleUtils.leerLinea("Valor: ");

            long t0 = System.nanoTime();
            Object res = SearchEngine.sllFirst(atributo, valor);
            long t1 = System.nanoTime();

            mostrarResultadoBusqueda(res, "SLL-First", t1 - t0);

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "SLL-First por " + atributo,
                    atributo + "=" + valor.toUpperCase(),
                    res == null ? 0 : 1,
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void sllLast() {

            String atributo = seleccionarAtributo();
            String valor = ConsoleUtils.leerLinea("Valor: ");

            long t0 = System.nanoTime();
            Object res = SearchEngine.sllLast(atributo, valor);
            long t1 = System.nanoTime();

            mostrarResultadoBusqueda(res, "SLL-Last", t1 - t0);

            // Registrar en historial
            HistoryManager.log(
                    "SEARCH",
                    "SLL-Last por " + atributo,
                    atributo + "=" + valor.toUpperCase(),
                    res == null ? 0 : 1,
                    (t1 - t0));

            ConsoleUtils.pausar("");
        }

        private void mostrarResultadoBusqueda(Object res, String tipo, long tiempo) {

            if (res == null) {
                System.out.println(ANSI.RED_BOLD + " No encontrado (" + tipo + ")" + ANSI.RESET);
            } else {
                System.out.println(ANSI.GREEN_BOLD + " Encontrado (" + tipo + "):" + ANSI.RESET);
                System.out.println(res);
            }

            System.out.println(" Tiempo: " + tiempo + " ns \n");
        }

        private String seleccionarAtributo() {

            DatasetManager.DatasetType t = DatasetManager.getTipoActual();

            System.out.println("\nAtributo a buscar:");

            switch (t) {

                case CITAS -> {
                    System.out.println("1. id");
                    System.out.println("2. apellido");
                    System.out.println("3. fecha");
                    String op = ConsoleUtils.leerLinea("Opción: ");
                    return switch (op) {
                        case "1" -> "id";
                        case "2" -> "apellido";
                        case "3" -> "fecha";
                        default -> "id";
                    };
                }

                case PACIENTES -> {
                    System.out.println("1. id");
                    System.out.println("2. apellido");
                    System.out.println("3. prioridad");
                    String op = ConsoleUtils.leerLinea("Opción: ");
                    return switch (op) {
                        case "1" -> "id";
                        case "2" -> "apellido";
                        case "3" -> "prioridad";
                        default -> "id";
                    };
                }

                case INVENTARIO -> {
                    System.out.println("1. id");
                    System.out.println("2. insumo");
                    System.out.println("3. stock");
                    String op = ConsoleUtils.leerLinea("Opción: ");
                    return switch (op) {
                        case "1" -> "id";
                        case "2" -> "insumo";
                        case "3" -> "stock";
                        default -> "id";
                    };
                }

                default -> {
                    return "id";
                }
            }
        }
    }
