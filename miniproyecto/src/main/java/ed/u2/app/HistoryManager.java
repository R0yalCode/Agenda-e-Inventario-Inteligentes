package ed.u2.app;

import ed.u2.util.ANSI;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Gestiona el historial de búsquedas y ordenaciones.
 * Guarda datos en un archivo .log dentro de /resources/history/
 */
public class HistoryManager {

    private static final String HISTORIAL = "resources/history/historial.log";

    /**
     * Asegura que la carpeta resources/history exista.
     */
    private static void asegurarDirectorio() {
        try {
            Files.createDirectories(Paths.get("resources/history"));
        } catch (IOException e) {
            System.out.println(ANSI.RED + "✘ No se pudo crear el directorio de historial." + ANSI.RESET);
        }
    }

    /**
     * Escribe una entrada en el historial.
     */
    public static void agregar(String mensaje) {
        asegurarDirectorio();

        try (FileWriter fw = new FileWriter(HISTORIAL, true)) {
            fw.write(mensaje + System.lineSeparator());
        } catch (Exception e) {
            System.out.println(ANSI.RED + "✘ No se pudo escribir en el historial." + ANSI.RESET);
        }
    }

    /**
     * Muestra el historial completo en pantalla.
     */
    public static void mostrarHistorial() {
        asegurarDirectorio();

        File archivo = new File(HISTORIAL);

        if (!archivo.exists()) {
            System.out.println(ANSI.YELLOW + "No hay historial disponible." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.CYAN_BOLD + "=== HISTORIAL DE OPERACIONES ===" + ANSI.RESET);

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;  // ← ESTA ES LA VARIABLE FALTANTE
            while ((linea = br.readLine()) != null) {
                System.out.println("• " + linea);
            }

        } catch (Exception e) {
            System.out.println(ANSI.RED + "✘ Error al leer el historial." + ANSI.RESET);
        }
    }

}
