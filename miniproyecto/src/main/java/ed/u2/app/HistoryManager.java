package ed.u2.app;

import ed.u2.util.ANSI;
import ed.u2.util.ConsoleUtils;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HistoryManager 
 *
 * Autor: R 
 * Fecha: 2025
 *
 * Funcionalidad:
 *  - Registrar eventos del sistema (búsquedas, ordenaciones, cargas, exportaciones, errores...)
 *  - Mostrar historial en consola con colores y paginación
 *  - Filtrar por tipo, por texto, por rango de fechas
 *  - Exportar historial a CSV
 *  - Persistir historial automático en archivo
 *
 * Uso:
 *  HistoryManager.log("SEARCH", "Secuencial:first", "id=CITA-001", 1, duracionNs);
 *  HistoryManager.mostrarHistorial();
 *  HistoryManager.exportarCsv("output/history_export.csv");
 */
public class HistoryManager {

    // ---------- Config ----------
    private static final String DEFAULT_HISTORY_DIR = "resources/history";
    private static final String DEFAULT_HISTORY_FILE = "history.csv";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // In-memory history (thread-safe)
    private static final List<HistoryEntry> HISTORY = Collections.synchronizedList(new ArrayList<>());

    // Auto-persistencia (si true guarda en archivo cada vez que hay un nuevo log)
    private static boolean autoPersist = true;

    // Ruta completa del archivo de historial
    private static final Path HISTORY_PATH;

    static {
        try {
            Files.createDirectories(Paths.get(DEFAULT_HISTORY_DIR));
        } catch (Exception e) {
            // ignore, intentaremos luego al persistir
        }
        HISTORY_PATH = Paths.get(DEFAULT_HISTORY_DIR, DEFAULT_HISTORY_FILE);
        // Cargar historial existente al iniciar
        cargarDesdeArchivo();
    }

    // ---------------------------
    // Estructura de una entrada
    // ---------------------------
    public static class HistoryEntry {
        public final LocalDateTime timestamp;
        public final String categoria;   // e.g., SEARCH, SORT, LOAD, EXPORT, ERROR
        public final String accion;      // e.g., "Secuencial:first", "Burbuja"
        public final String detalles;    // parámetros, query, ruta de archivo, etc.
        public final int resultados;     // número de resultados (si aplica)
        public final long duracionNs;    // duración en nanosegundos (si aplica)
        public final String nota;        // nota extra opcional

        public HistoryEntry(LocalDateTime timestamp,
                            String categoria,
                            String accion,
                            String detalles,
                            int resultados,
                            long duracionNs,
                            String nota) {
            this.timestamp = timestamp;
            this.categoria = categoria;
            this.accion = accion;
            this.detalles = detalles;
            this.resultados = resultados;
            this.duracionNs = duracionNs;
            this.nota = nota;
        }

        public String toCsvLine() {
            // Escape simple: envolver en comillas y reemplazar comillas internas
            return String.join(",",
                    "\"" + timestamp.format(DATE_FMT) + "\"",
                    "\"" + escape(categoria) + "\"",
                    "\"" + escape(accion) + "\"",
                    "\"" + escape(detalles) + "\"",
                    String.valueOf(resultados),
                    String.valueOf(duracionNs),
                    "\"" + escape(nota) + "\""
            );
        }

        private String escape(String s) {
            if (s == null) return "";
            return s.replace("\"", "\"\"");
        }

        @Override
        public String toString() {
            String dur = duracionNs > 0 ? " | " + (duracionNs / 1_000_000) + " ms" : "";
            String res = resultados >= 0 ? " | res=" + resultados : "";
            String notaStr = (nota != null && !nota.isEmpty()) ? " | " + nota : "";
            return timestamp.format(DATE_FMT) + " - [" + categoria + "] " + accion +
                    " -> " + detalles + res + dur + notaStr;
        }
    }

    // ---------------------------
    // API pública - Logging
    // ---------------------------

    /**
     * Registra un evento en el historial.
     *
     * @param categoria  Tipo (SEARCH, SORT, LOAD, EXPORT, ERROR, INFO)
     * @param accion     Acción o sub-tipo (ej: "Secuencial:first", "Burbuja")
     * @param detalles   Detalles o parámetros (ej: "id=CITA-001", "ruta=..."), puede ser null
     * @param resultados Número de resultados (si aplica), -1 si no aplica
     * @param duracionNs Duración en nanosegundos (0 o -1 si no aplica)
     */
    public static void log(String categoria, String accion, String detalles, int resultados, long duracionNs) {
        log(categoria, accion, detalles, resultados, duracionNs, "");
    }

    public static void log(String categoria, String accion, String detalles, int resultados, long duracionNs, String nota) {
        HistoryEntry e = new HistoryEntry(LocalDateTime.now(), categoria, accion,
                detalles == null ? "" : detalles, resultados, duracionNs, nota == null ? "" : nota);
        HISTORY.add(e);
        if (autoPersist) {
            persistir(); // persistir en segundo plano o inmediato (aquí inmediato)
        }
    }

    // ---------------------------
    // Mostrar historial (visual)
    // ---------------------------

    /**
     * Muestra todo el historial con paginación simple.
     */
    public static void mostrarHistorial() {
        mostrarHistorial(50); // por defecto 50 por página
    }

    /**
     * Muestra el historial con N entradas por página.
     * Permite filtrar por tipo o buscar texto interactivo.
     */
    public static void mostrarHistorial(int porPagina) {

        synchronized (HISTORY) {
            if (HISTORY.isEmpty()) {
                System.out.println(ANSI.YELLOW + "El historial está vacío." + ANSI.RESET);
                return;
            }

            int total = HISTORY.size();
            int paginas = (int) Math.ceil(total / (double) porPagina);
            int pagina = 1;

            while (true) {
                int desde = (pagina - 1) * porPagina;
                int hasta = Math.min(desde + porPagina, total);

                System.out.println(ANSI.CYAN_BOLD + "\n=== HISTORIAL (" + pagina + "/" + paginas + ") ===" + ANSI.RESET);

                List<HistoryEntry> sub = HISTORY.subList(desde, hasta);
                for (HistoryEntry e : sub) {
                    printEntryColored(e);
                }

                System.out.println(ANSI.CYAN + "\nMostrando " + (desde + 1) + " - " + hasta + " de " + total + ANSI.RESET);
                System.out.println("[S]iguiente pagina  [P]agina anterior  [F]iltrar  [E]xportar  [L]impiar  [R]egresar");

                String opt = ConsoleUtils.leerLinea("Opción:").trim().toUpperCase();

                switch (opt) {
                    case "S":
                        if (pagina < paginas) pagina++; else System.out.println(ANSI.YELLOW + "Ya estás en la última página." + ANSI.RESET);
                        break;
                    case "P":
                        if (pagina > 1) pagina--; else System.out.println(ANSI.YELLOW + "Ya estás en la primera página." + ANSI.RESET);
                        break;
                    case "F":
                        interactiveFilter();
                        return;
                    case "E":
                        String ruta = ConsoleUtils.leerLinea("Ruta export CSV (enter para default):");
                        if (ruta.trim().isEmpty()) ruta = HISTORY_PATH.toString();
                        exportarCsv(ruta);
                        break;
                    case "L":
                        String conf = ConsoleUtils.leerLinea(ANSI.RED + "¿Eliminar todo el historial? (s/N):" + ANSI.RESET);
                        if (conf.equalsIgnoreCase("s")) {
                            limpiar();
                            System.out.println(ANSI.GREEN + "Historial limpiado." + ANSI.RESET);
                            return;
                        }
                        break;
                    case "R":
                        return;
                    default:
                        System.out.println(ANSI.YELLOW + "Opción no válida." + ANSI.RESET);
                }
            }
        }
    }

    private static void printEntryColored(HistoryEntry e) {
        String color;
        switch (e.categoria.toUpperCase()) {
            case "SEARCH": color = ANSI.MAGENTA_BOLD; break;
            case "SORT": color = ANSI.GREEN_BOLD; break;
            case "LOAD": color = ANSI.CYAN_BOLD; break;
            case "EXPORT": color = ANSI.YELLOW_BOLD; break;
            case "ERROR": color = ANSI.RED_BOLD; break;
            default: color = ANSI.RESET;
        }
        System.out.println(color + e.timestamp.format(DATE_FMT) + " " + ANSI.RESET +
                "[" + color + e.categoria + ANSI.RESET + "] " +
                ANSI.RED + e.accion + ANSI.RESET + " -> " +
                e.detalles + (e.resultados >= 0 ? " | res=" + e.resultados : "") +
                (e.duracionNs > 0 ? " | tiempo=" + (e.duracionNs / 1_000_000) + " ms" : "") +
                (e.nota != null && !e.nota.isEmpty() ? " | " + e.nota : "")
        );
    }

    // ---------------------------
    // Filtrado interactivo
    // ---------------------------

    private static void interactiveFilter() {
        System.out.println(ANSI.CYAN + "\n=== FILTRAR HISTORIAL ===" + ANSI.RESET);
        String tipo = ConsoleUtils.leerLinea("Tipo (SEARCH,SORT,LOAD,EXPORT,ERROR,ALL):").trim().toUpperCase();
        String texto = ConsoleUtils.leerLinea("Texto a buscar (vacío = ninguno):").trim();
        String desdeStr = ConsoleUtils.leerLinea("Desde (yyyy-MM-dd) (vacío = ninguno):").trim();
        String hastaStr = ConsoleUtils.leerLinea("Hasta (yyyy-MM-dd) (vacío = ninguno):").trim();

        LocalDate desde = null, hasta = null;
        try {
            if (!desdeStr.isEmpty()) desde = LocalDate.parse(desdeStr);
            if (!hastaStr.isEmpty()) hasta = LocalDate.parse(hastaStr);
        } catch (Exception e) {
            System.out.println(ANSI.YELLOW + "Formato de fecha inválido, se omite rango." + ANSI.RESET);
        }

        List<HistoryEntry> filtrado = filtrar(tipo.equals("ALL") ? null : tipo, texto, desde, hasta);
        if (filtrado.isEmpty()) {
            System.out.println(ANSI.YELLOW + "No se encontraron entradas con ese filtro." + ANSI.RESET);
            return;
        }

        // Mostrar resultado (no paginar)
        System.out.println(ANSI.CYAN_BOLD + "\n=== RESULTADOS FILTRADOS (" + filtrado.size() + ") ===" + ANSI.RESET);
        filtrado.forEach(HistoryManager::printEntryColored);

        // Pregunta si exportar
        String exp = ConsoleUtils.leerLinea("Exportar estos resultados a CSV? (s/N):");
        if (exp.equalsIgnoreCase("s")) {
            String ruta = ConsoleUtils.leerLinea("Ruta export CSV (enter=default):");
            if (ruta.trim().isEmpty()) ruta = HISTORY_PATH.toString();
            exportarCsv(ruta, filtrado);
        }
    }

    /**
     * Filtra el historial según parámetros. Si un parámetro es null/ vacío se ignora.
     *
     * @param tipo  Tipo (SEARCH,SORT,...), null para todos
     * @param texto Texto libre a buscar en accion o detalles
     * @param desde Fecha desde (inclusive) o null
     * @param hasta Fecha hasta (inclusive) o null
     * @return lista filtrada (orden descendente por fecha)
     */
    public static List<HistoryEntry> filtrar(String tipo, String texto, LocalDate desde, LocalDate hasta) {

        synchronized (HISTORY) {
            return HISTORY.stream()
                    .filter(e -> {
                        if (tipo != null && !tipo.isEmpty() && !e.categoria.equalsIgnoreCase(tipo)) return false;
                        if (texto != null && !texto.isEmpty()) {
                            String low = texto.toLowerCase();
                            if (!(e.accion.toLowerCase().contains(low) || e.detalles.toLowerCase().contains(low))) return false;
                        }
                        if (desde != null) {
                            if (e.timestamp.toLocalDate().isBefore(desde)) return false;
                        }
                        if (hasta != null) {
                            if (e.timestamp.toLocalDate().isAfter(hasta)) return false;
                        }
                        return true;
                    })
                    .sorted(Comparator.comparing((HistoryEntry e) -> e.timestamp).reversed())
                    .collect(Collectors.toList());
        }
    }

    // ---------------------------
    // Exportar CSV
    // ---------------------------

    /**
     * Exporta TODO el historial (por defecto) a CSV.
     */
    public static boolean exportarCsv(String ruta) {
        return exportarCsv(ruta, null);
    }

    /**
     * Exporta la lista indicada a CSV. Si lista==null exporta todo.
     */
    public static boolean exportarCsv(String ruta, List<HistoryEntry> lista) {

        List<HistoryEntry> toExport;
        synchronized (HISTORY) {
            toExport = lista == null ? new ArrayList<>(HISTORY) : new ArrayList<>(lista);
        }

        try {
            Path p = Paths.get(ruta);
            Files.createDirectories(p.getParent());

            try (BufferedWriter bw = Files.newBufferedWriter(p)) {
                // cabecera
                bw.write("\"timestamp\",\"categoria\",\"accion\",\"detalles\",\"resultados\",\"duracion_ns\",\"nota\"");
                bw.newLine();
                for (HistoryEntry e : toExport) {
                    bw.write(e.toCsvLine());
                    bw.newLine();
                }
            }

            System.out.println(ANSI.GREEN + "Historial exportado a: " + ruta + ANSI.RESET);
            return true;

        } catch (Exception ex) {
            System.out.println(ANSI.RED + "Error exportando CSV: " + ex.getMessage() + ANSI.RESET);
            return false;
        }
    }

    // ---------------------------
    // Persistencia / carga desde archivo por defecto
    // ---------------------------

    /**
     * Persiste historial actual al archivo por defecto.
     */
    public static void persistir() {
        exportarCsv(HISTORY_PATH.toString());
    }

    /**
     * Carga historial desde archivo por defecto (si existe).
     */
    private static void cargarDesdeArchivo() {
        if (!Files.exists(HISTORY_PATH)) return;

        try (BufferedReader br = Files.newBufferedReader(HISTORY_PATH)) {
            // saltar cabecera
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                // CSV simple; asumimos formato generado por toCsvLine
                // separa por coma respetando comillas rudimentariamente
                // aquí hacemos parse sencillo: quitar primeras y últimas comillas y split por "," fuera
                List<String> cols = parseCsvLine(linea);
                if (cols.size() < 7) continue;
                LocalDateTime ts = LocalDateTime.parse(cols.get(0).replace("\"", ""), DATE_FMT);
                String categoria = unquote(cols.get(1));
                String accion = unquote(cols.get(2));
                String detalles = unquote(cols.get(3));
                int resultados = Integer.parseInt(cols.get(4));
                long dur = Long.parseLong(cols.get(5));
                String nota = unquote(cols.get(6));
                HistoryEntry e = new HistoryEntry(ts, categoria, accion, detalles, resultados, dur, nota);
                HISTORY.add(e);
            }
        } catch (Exception e) {
            System.out.println(ANSI.YELLOW + "No se pudo cargar historial: " + e.getMessage() + ANSI.RESET);
        }
    }

    // método auxiliar: parse csv line simple (divide por coma fuera de comillas)
    private static List<String> parseCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                cur.append(c); // mantener comilla para unquote posterior
            } else if (c == ',' && !inQuotes) {
                cols.add(cur.toString());
                cur = new StringBuilder();
            } else {
                cur.append(c);
            }
        }
        cols.add(cur.toString());
        return cols;
    }

    private static String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }
        return s.replace("\"\"", "\"");
    }

    // ---------------------------
    // Otras utilidades
    // ---------------------------

    /**
     * Limpia historial en memoria y borra archivo si existe.
     */
    public static void limpiar() {
        synchronized (HISTORY) {
            HISTORY.clear();
        }
        try {
            Files.deleteIfExists(HISTORY_PATH);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Devuelve una copia inmutable del historial actual.
     */
    public static List<HistoryEntry> obtenerHistorial() {
        synchronized (HISTORY) {
            return Collections.unmodifiableList(new ArrayList<>(HISTORY));
        }
    }

    /**
     * Configura si persistir automáticamente cada log.
     */
    public static void setAutoPersist(boolean v) {
        autoPersist = v;
    }
}
