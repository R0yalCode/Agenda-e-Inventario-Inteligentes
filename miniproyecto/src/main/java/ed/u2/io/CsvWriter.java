package ed.u2.io;

import ed.u2.util.ANSI;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Clase para exportar datos a CSV.
 */
public class CsvWriter {

    public static void escribir(String ruta, List<String[]> filas) {

        try {
            FileUtils.crearDirectorioSiNoExiste(Paths.get(ruta).getParent().toString());

            PrintWriter pw = new PrintWriter(new FileWriter(ruta));

            for (String[] f : filas)
                pw.println(String.join(",", f));

            pw.close();

            System.out.println(ANSI.GREEN +
                    "Archivo exportado correctamente: " + ruta + ANSI.RESET);

        } catch (Exception e) {
            System.out.println(ANSI.RED_BOLD +
                    "Error al exportar CSV: " + e.getMessage() + ANSI.RESET);
        }
    }
}
