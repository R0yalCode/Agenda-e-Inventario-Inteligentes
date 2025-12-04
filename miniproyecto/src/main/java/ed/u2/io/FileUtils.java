package ed.u2.io;

import java.io.File;
import java.nio.file.*;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Utilidades para manejo de archivos y directorios.
 */
public class FileUtils {

    public static String validarArchivoCsv(String ruta) {

        if (ruta == null || ruta.isEmpty())
            return "Ruta vacía.";

        File f = new File(ruta);

        if (!f.exists())
            return "El archivo no existe.";

        if (f.isDirectory())
            return "La ruta apunta a un directorio, no a un archivo.";

        if (!ruta.endsWith(".csv"))
            return "El archivo no es CSV.";

        return null;
    }

    public static void crearDirectorioSiNoExiste(String ruta) {
        try {
            if (ruta != null && !ruta.isEmpty()) {
                Files.createDirectories(Paths.get(ruta));
            }
        } catch (Exception ignored) {}
    }
}


 
