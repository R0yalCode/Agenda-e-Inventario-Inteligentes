package ed.u2.io;

import java.nio.file.*;
import java.util.*;

/**
 * Valida CSV reales del mini proyecto.
 * Soporta separador ";" y valida encabezados reales.
 */
public class CsvValidator {

    private static final List<String> HEADERS_CITAS =
            Arrays.asList("id", "apellido", "fechaHora");

    private static final List<String> HEADERS_PACIENTES =
            Arrays.asList("id", "apellido", "prioridad");

    private static final List<String> HEADERS_INVENTARIO =
            Arrays.asList("id", "insumo", "stock");

    public static String validarEstructura(String ruta) {

        List<String> lineas;
        try {
            lineas = Files.readAllLines(Paths.get(ruta));
        } catch (Exception e) {
            return "No se pudo leer el archivo.";
        }

        if (lineas.isEmpty()) {
            return "El CSV está vacío.";
        }

        // PRIMERA LÍNEA ES EL HEADER REAL
        String header = lineas.get(0).trim();

        String[] columnas = header.split(";");

        List<String> headers = new ArrayList<>();
        for (String h : columnas) {
            headers.add(h.trim());
        }

        if (headers.equals(HEADERS_CITAS)) return null;
        if (headers.equals(HEADERS_PACIENTES)) return null;
        if (headers.equals(HEADERS_INVENTARIO)) return null;

        return "El CSV no coincide con ningún dataset conocido.\nDetectado: " + headers;
    }
}
