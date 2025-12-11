package ed.u2.io;

import ed.u2.model.*;
import ed.u2.util.ANSI;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Carga y detecta datasets CSV (citas, pacientes, inventario)
 * desde archivos con formato:
 *
 * • Separador: ;
 * • Primera línea: encabezados
 */
public class CsvLoader {

    // ============================================================
    // HEADERS OFICIALES
    // ============================================================

    private static final List<String> H_CITAS =
            Arrays.asList("id", "apellido", "fechaHora");

    private static final List<String> H_PACIENTES =
            Arrays.asList("id", "apellido", "prioridad");

    private static final List<String> H_INVENTARIO =
            Arrays.asList("id", "insumo", "stock");


    // ============================================================
    // MÉTODO PRINCIPAL
    // ============================================================

    /**
     * Carga un CSV manual o oficial.
     *
     * @param ruta ruta del archivo CSV
     * @return Map con la clave ("citas","pacientes","inventario")
     *         y una lista de objetos correspondientes
     */
    public static Map<String, Object> cargarCsv(String ruta) {

        Map<String, Object> respuesta = new HashMap<>();

        List<String> lineas;
        try {
            lineas = Files.readAllLines(Paths.get(ruta));
        } catch (Exception e) {
            System.out.println(ANSI.RED + "Error leyendo archivo: " + e.getMessage() + ANSI.RESET);
            return respuesta;
        }

        if (lineas.isEmpty()) {
            System.out.println(ANSI.RED + "El CSV está vacío." + ANSI.RESET);
            return respuesta;
        }

        // Normalizar separador ;
        String header = lineas.get(0).trim().replace(",", ";");
        List<String> columnas = Arrays.asList(header.split(";"));

        // Detectar tipo
        if (columnas.equals(H_CITAS)) {
            respuesta.put("citas", parseCitas(lineas));
        } else if (columnas.equals(H_PACIENTES)) {
            respuesta.put("pacientes", parsePacientes(lineas));
        } else if (columnas.equals(H_INVENTARIO)) {
            respuesta.put("inventario", parseInventario(lineas));
        } else {
            System.out.println(ANSI.RED + " El archivo no coincide con ningún dataset conocido." + ANSI.RESET);
        }

        return respuesta;
    }


    // ============================================================
    // PARSEO DE CITAS
    // ============================================================

    private static List<Cita> parseCitas(List<String> lineas) {

        List<Cita> lista = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {

            String[] p = lineas.get(i).split(";");

            if (p.length < 3) continue;

            Cita c = new Cita(
                    p[0].trim(),
                    p[1].trim(),
                    LocalDateTime.parse(p[2].trim())
            );

            lista.add(c);
        }

        return lista;
    }


    // ============================================================
    // PARSEO DE PACIENTES
    // ============================================================

    private static List<Paciente> parsePacientes(List<String> lineas) {

        List<Paciente> lista = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {

            String[] p = lineas.get(i).split(";");

            if (p.length < 3) continue;

            Paciente pac = new Paciente(
                    p[0].trim(),
                    p[1].trim(),
                    Integer.parseInt(p[2].trim())
            );

            lista.add(pac);
        }

        return lista;
    }


    // ============================================================
    // PARSEO DE INVENTARIO
    // ============================================================

    private static List<InventarioItem> parseInventario(List<String> lineas) {

        List<InventarioItem> lista = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {

            String[] p = lineas.get(i).split(";");

            if (p.length < 3) continue;

            InventarioItem item = new InventarioItem(
                    p[0].trim(),
                    p[1].trim(),
                    Integer.parseInt(p[2].trim())
            );

            lista.add(item);
        }

        return lista;
    }

}
