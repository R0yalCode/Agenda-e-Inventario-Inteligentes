package ed.u2.io;

import ed.u2.util.ANSI;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Autor: R  
 * Fecha: 2025
 *
 * Utilidades para validación robusta de archivos CSV.
 */
public class FileUtils {

    /**
     * Valida un archivo CSV antes de procesarlo.
     *
     * @param ruta Ruta completa del archivo
     * @return null si está OK, o un mensaje de error si está mal
     */
    public static String validarArchivoCsv(String ruta) {

        if (ruta == null || ruta.trim().isEmpty()) {
            return "La ruta está vacía.";
        }

        File f = new File(ruta);

        // 1) ¿Existe?
        if (!f.exists()) {
            return "El archivo no existe.";
        }

        // 2) ¿Es archivo real? (no directorio)
        if (!f.isFile()) {
            return "La ruta no es un archivo válido.";
        }

        // 3) ¿Tiene extensión .csv?
        if (!ruta.toLowerCase().endsWith(".csv")) {
            return "El archivo debe tener extensión .csv";
        }

        // 4) Permisos
        if (!f.canRead()) {
            return "El archivo no tiene permisos de lectura.";
        }

        // 5) Tamaño mínimo
        if (f.length() == 0) {
            return "El archivo está vacío.";
        }
        if (f.length() < 20) {
            return "El archivo es demasiado pequeño para ser un CSV válido.";
        }

        // 6) Validar que tenga encabezado
        try {
            var lineas = Files.readAllLines(Paths.get(ruta));

            if (lineas.isEmpty()) {
                return "El CSV no contiene líneas.";
            }

            String linea1 = lineas.get(0).trim();

            if (!linea1.contains(";") && !linea1.contains(";")) {
                return "El archivo no parece tener encabezado CSV (sin comas en la primera línea).";
            }

        } catch (Exception e) {
            return "Error al leer archivo: " + e.getMessage();
        }

        // Todo correcto
        return null;
    }

    public static void crearDirectorioSiNoExiste(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) return;

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(ruta);
            java.nio.file.Files.createDirectories(path);

            System.out.println(ANSI.GREEN + "Directorio creado/verificado: " +
                    path.toAbsolutePath() + ANSI.RESET);
        } catch (Exception e) {
            System.out.println(ANSI.RED + "Error creando directorio '" + ruta +
                    "': " + e.getMessage() + ANSI.RESET);
        }
    }
}
