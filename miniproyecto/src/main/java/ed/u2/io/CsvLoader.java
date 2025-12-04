package ed.u2.io;

import ed.u2.data.DatasetManager;
import ed.u2.data.DatasetType;
import ed.u2.util.ANSI;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Carga y valida archivos CSV según el tipo de dataset.
 * Integra directamente con DatasetManager.
 *
 * Entrada: ruta de archivo CSV
 * Salida: arreglo + SLL cargados en DatasetManager
 */
public class CsvLoader {

    // ======== CABECERAS OFICIALES ========
    private static final String[] HEADER_CITAS = {"id", "apellido", "fechaHora"};
    private static final String[] HEADER_INVENTARIO = {"id", "insumo", "stock"};
    private static final String[] HEADER_PACIENTES = {"id", "apellido", "prioridad"};

    // =====================================
    //   VALIDAR EL CSV Y DETECTAR SU TIPO
    // =====================================

    /**
     * Valida las cabeceras del CSV para determinar a qué dataset pertenece.
     *
     * @param ruta ruta del archivo
     * @return DatasetType detectado o DESCONOCIDO
     */
    public static DatasetType validarCSV(String ruta) {

        try {

            List<String> lineas = Files.readAllLines(Paths.get(ruta));

            if (lineas.isEmpty()) return DatasetType.DESCONOCIDO;

            String header = lineas.get(0).trim();
            String[] cols = header.split(";");

            if (matchHeader(cols, HEADER_CITAS)) return DatasetType.CITAS_100;
            if (matchHeader(cols, HEADER_INVENTARIO)) return DatasetType.INVENTARIO_500_INVERSO;
            if (matchHeader(cols, HEADER_PACIENTES)) return DatasetType.PACIENTES_500;

        } catch (Exception e) {
            System.out.println(ANSI.RED + "✘ No se pudo leer el archivo CSV." + ANSI.RESET);
        }

        return DatasetType.DESCONOCIDO;
    }

    /**
     * Compara header detectado con esperado.
     */
    private static boolean matchHeader(String[] a, String[] b) {
        if (a.length != b.length) return false;

        for (int i = 0; i < a.length; i++) {
            if (!a[i].trim().equalsIgnoreCase(b[i].trim()))
                return false;
        }
        return true;
    }

    // =====================================
    //          CARGAR EL CSV
    // =====================================

    /**
     * Carga el CSV según el tipo detectado.
     *
     * @param ruta ruta del archivo CSV
     * @param tipo tipo del dataset
     * @return true si se cargó correctamente
     */
    public static boolean cargarCSV(String ruta, DatasetType tipo) {

        try {
            List<String> lineas = Files.readAllLines(Paths.get(ruta));

            if (lineas.size() <= 1) {
                System.out.println(ANSI.RED + "✘ El CSV no contiene datos." + ANSI.RESET);
                return false;
            }

            List<String> datos = new ArrayList<>();

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i).trim();

                if (!linea.isEmpty())
                    datos.add(linea);   // Cada registro es un string completo
            }

            // Convertir a arreglo
            Object[] arr = datos.toArray(new Object[0]);

            // Cargar en DatasetManager
            DatasetManager.cargarDesdeCSV(arr, tipo);

            return true;

        } catch (Exception e) {
            System.out.println(ANSI.RED + "✘ Error al cargar CSV: " + e.getMessage() + ANSI.RESET);
            return false;
        }
    }
}
