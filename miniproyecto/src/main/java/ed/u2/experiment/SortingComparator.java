package ed.u2.experiment;

import ed.u2.data.DatasetManager;
import ed.u2.model.*;
import ed.u2.stats.OperationStats;
import ed.u2.util.ANSI;
import ed.u2.util.ConsoleUtils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.nio.file.Files.newBufferedWriter;

/**
 * Compara los 3 algoritmos de ordenación en diferentes datasets.
 */
public class SortingComparator {

    // ============================================================
    // COMPARACIÓN PARA DATASET ACTUAL
    // ============================================================

    public static void compararDatasetActual() {

        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED + "No hay dataset cargado." + ANSI.RESET);
            return;
        }

        String nombreDataset = DatasetManager.getRutaActual();
        nombreDataset = nombreDataset.substring(nombreDataset.lastIndexOf('/') + 1);

        System.out.println(ANSI.CYAN_BOLD + "\n=== INICIANDO COMPARACIÓN DE ALGORITMOS ===" + ANSI.RESET);
        System.out.println("Dataset: " + nombreDataset);
        System.out.println("Tamaño: " + DatasetManager.getRegistrosActuales() + " registros");
        System.out.println("\nEste proceso ejecutará 13 corridas por algoritmo");
        System.out.println("(10 válidas + 3 de descarte para warm-up JVM)");

        String confirmar = ConsoleUtils.leerLinea("\n¿Continuar? (s/N): ");
        if (!confirmar.equalsIgnoreCase("s")) return;

        // Obtener array según tipo
        Object[] arrOriginal = DatasetManager.getArray();

        // Ejecutar experimentos para cada algoritmo
        OperationStats burbuja = null;
        OperationStats seleccion = null;
        OperationStats insercion = null;

        switch (DatasetManager.getTipoActual()) {
            case CITAS:
                Cita[] citas = Arrays.copyOf(arrOriginal, arrOriginal.length, Cita[].class);
                burbuja = ExperimentRunner.ejecutarExperimento(citas, "burbuja", true);
                seleccion = ExperimentRunner.ejecutarExperimento(citas, "seleccion", true);
                insercion = ExperimentRunner.ejecutarExperimento(citas, "insercion", true);
                break;

            case PACIENTES:
                Paciente[] pacientes = Arrays.copyOf(arrOriginal, arrOriginal.length, Paciente[].class);
                burbuja = ExperimentRunner.ejecutarExperimento(pacientes, "burbuja", true);
                seleccion = ExperimentRunner.ejecutarExperimento(pacientes, "seleccion", true);
                insercion = ExperimentRunner.ejecutarExperimento(pacientes, "insercion", true);
                break;

            case INVENTARIO:
                InventarioItem[] inventario = Arrays.copyOf(arrOriginal, arrOriginal.length, InventarioItem[].class);
                burbuja = ExperimentRunner.ejecutarExperimento(inventario, "burbuja", true);
                seleccion = ExperimentRunner.ejecutarExperimento(inventario, "seleccion", true);
                insercion = ExperimentRunner.ejecutarExperimento(inventario, "insercion", true);
                break;
        }

        // Mostrar tabla comparativa
        ExperimentRunner.imprimirTablaComparativa(nombreDataset, burbuja, seleccion, insercion);

        // Mostrar conclusiones
        mostrarConclusiones(burbuja, seleccion, insercion, nombreDataset);

        // Preguntar si exportar resultados
        String exportar = ConsoleUtils.leerLinea("\n¿Exportar resultados a CSV? (s/N): ");
        if (exportar.equalsIgnoreCase("s")) {
            exportarResultadosCSV(nombreDataset, burbuja, seleccion, insercion);
        }
    }

    // ============================================================
    // COMPARACIÓN ESPECÍFICA (para casi-ordenado vs inverso)
    // ============================================================

    public static void compararCasiOrdenadoVsInverso() {
        System.out.println(ANSI.CYAN_BOLD +
                "\n=== COMPARACIÓN ESPECIAL: CASI-ORDENADO vs INVERSO ===" + ANSI.RESET);

        // Esta función debería cargar ambos datasets y comparar
        // Por ahora muestra información
        System.out.println("\nRecomendaciones del PDF:");
        System.out.println("1. Para casi ordenado: Inserción es óptima (O(n) en mejor caso)");
        System.out.println("2. Para inverso: Selección mantiene swaps constantes");
        System.out.println("3. Burbuja es la menos eficiente en ambos casos");

        System.out.println("\nEjecuta:");
        System.out.println("1. Cargar 'citas_100_casi_ordenadas.csv' y usar Opción 3 -> Comparar");
        System.out.println("2. Cargar 'inventario_500_inverso.csv' y usar Opción 3 -> Comparar");

    }

    // ============================================================
    // CONCLUSIONES AUTOMÁTICAS
    // ============================================================

    private static void mostrarConclusiones(
            OperationStats b, OperationStats s, OperationStats i,
            String dataset) {

        System.out.println(ANSI.YELLOW_BOLD + "\n=== CONCLUSIONES ===" + ANSI.RESET);

        // Determinar el más rápido
        String masRapido = "Burbuja";
        long menorTiempo = b.getTime();

        if (s.getTime() < menorTiempo) {
            masRapido = "Selección";
            menorTiempo = s.getTime();
        }
        if (i.getTime() < menorTiempo) {
            masRapido = "Inserción";
        }

        System.out.println("• Algoritmo más rápido: " + masRapido);
        System.out.printf("• Tiempo relativo Burbuja/Inserción: %.1fx\n",
                (double)b.getTime() / i.getTime());

        // Recomendación según tipo de dataset
        if (dataset.contains("casi_ordenadas")) {
            System.out.println("• Dataset CASI ORDENADO: Inserción debería ser óptima");
        } else if (dataset.contains("inverso")) {
            System.out.println("• Dataset INVERSO: Selección tiene swaps mínimos");
        }

        // Regla general
        System.out.println("\n" + ANSI.GREEN + "REGLA PRÁCTICA:" + ANSI.RESET);
        System.out.println("1. Datos pequeños o casi ordenados → Inserción");
        System.out.println("2. Minimizar swaps → Selección");
        System.out.println("3. Generalmente → evitar Burbuja");
    }

    // ============================================================
    // EXPORTACIÓN DE RESULTADOS
    // ============================================================

    private static void exportarResultadosCSV(
            String dataset,
            OperationStats b, OperationStats s, OperationStats i) {

        try {
            Path dir = Paths.get("miniproyecto/resources/reports");
            Files.createDirectories(dir);

            String nombreArchivo = "comparacion_" +
                    dataset.replace(".csv", "") + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                    ".csv";

            Path archivo = dir.resolve(nombreArchivo);

            BufferedWriter writer = newBufferedWriter(archivo);

            // Cabecera
            writer.write("dataset,algoritmo,comparaciones,swaps,tiempo_ns");
            writer.newLine();

            // Datos
            writer.write(String.format("%s,burbuja,%d,%d,%d",
                    dataset, b.getComparisons(), b.getSwaps(), b.getTime()));
            writer.newLine();

            writer.write(String.format("%s,seleccion,%d,%d,%d",
                    dataset, s.getComparisons(), s.getSwaps(), s.getTime()));
            writer.newLine();

            writer.write(String.format("%s,insercion,%d,%d,%d",
                    dataset, i.getComparisons(), i.getSwaps(), i.getTime()));
            writer.newLine();

            writer.close();

            System.out.println(ANSI.GREEN + "Resultados exportados a: " + archivo + ANSI.RESET);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ANSI.RED + "Error exportando: " + e.getMessage() + ANSI.RESET);
        }
    }
}