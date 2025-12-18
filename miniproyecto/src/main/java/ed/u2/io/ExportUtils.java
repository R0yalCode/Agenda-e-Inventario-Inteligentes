// java
     package ed.u2.io;

     import ed.u2.stats.SortingStatsManager;
     import ed.u2.stats.SearchStatsRepository;

     import java.io.BufferedWriter;
     import java.io.IOException;
     import java.lang.reflect.Field;
     import java.lang.reflect.Method;
     import java.nio.file.*;
     import java.util.*;

     /**
      * ExportUtils - exporta estadísticas a CSV (basado en estilo HistoryManager).
      */
     public class ExportUtils {

         private static final Path EXPORT_DIR = Paths.get("resources", "export");

         public static boolean exportEstadisticas() {
             try {
                 Files.createDirectories(EXPORT_DIR);

                 exportSearchStats();
                 exportSortingStats();

                 return true;
             } catch (Exception e) {
                 e.printStackTrace();
                 return false;
             }
         }

         private static void exportSearchStats() throws IOException {
             Path out = EXPORT_DIR.resolve("search_stats.csv");

             // Intentar obtener lista de stats desde el repositorio
             Object raw = null;
             try {
                 raw = SearchStatsRepository.getAll();
             } catch (Throwable t) {
                 raw = null;
             }

             List<?> lista = asList(raw);

             try (BufferedWriter bw = Files.newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                 if (lista.isEmpty()) {
                     // cabecera simple y mensaje
                     bw.write("\"note\"");
                     bw.newLine();
                     bw.write(escapeCsv("No hay estadísticas de búsquedas disponibles."));
                     bw.newLine();
                     return;
                 }

                 // Construir cabecera a partir de campos de la primera entrada
                 Set<String> headers = collectFieldNames(lista);
                 List<String> headerList = new ArrayList<>(headers);
                 Collections.sort(headerList);
                 writeCsvLine(bw, headerList);

                 // Escribir cada fila
                 for (Object item : lista) {
                     List<String> row = new ArrayList<>();
                     for (String h : headerList) {
                         Object val = fieldValueByName(item, h);
                         row.add(val == null ? "" : val.toString());
                     }
                     writeCsvLine(bw, row);
                 }
             }
         }

         private static void exportSortingStats() throws IOException {
             Path out = EXPORT_DIR.resolve("sorting_stats.csv");

             // Intentar obtener datos desde SortingStatsManager mediante método getAll() si existe
             List<?> lista = Collections.emptyList();
             try {
                 Method m = SortingStatsManager.class.getMethod("getAll");
                 Object raw = m.invoke(null);
                 lista = asList(raw);
             } catch (NoSuchMethodException nsme) {
                 // no hay método, intentaremos capturar salida textual como fallback
                 lista = Collections.emptyList();
             } catch (Throwable t) {
                 lista = Collections.emptyList();
             }

             try (BufferedWriter bw = Files.newBufferedWriter(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                 if (lista.isEmpty()) {
                     // Fallback: capturar la salida textual de mostrar() y guardarla como una sola columna
                     String text = captureSortingMostrar();
                     bw.write("\"text\"");
                     bw.newLine();
                     bw.write(escapeCsv(text));
                     bw.newLine();
                     return;
                 }

                 // Si hay lista, construir cabecera y filas por reflexión similar a búsquedas
                 Set<String> headers = collectFieldNames(lista);
                 List<String> headerList = new ArrayList<>(headers);
                 Collections.sort(headerList);
                 writeCsvLine(bw, headerList);

                 for (Object item : lista) {
                     List<String> row = new ArrayList<>();
                     for (String h : headerList) {
                         Object val = fieldValueByName(item, h);
                         row.add(val == null ? "" : val.toString());
                     }
                     writeCsvLine(bw, row);
                 }
             }
         }

         // Helpers

         private static List<?> asList(Object raw) {
             if (raw == null) return Collections.emptyList();
             if (raw instanceof List) return (List<?>) raw;
             if (raw instanceof Collection) return new ArrayList<>((Collection<?>) raw);
             return Collections.emptyList();
         }

         private static Set<String> collectFieldNames(List<?> lista) {
             Set<String> headers = new LinkedHashSet<>();
             for (Object o : lista) {
                 if (o == null) continue;
                 Class<?> c = o.getClass();
                 for (Field f : c.getDeclaredFields()) {
                     headers.add(f.getName());
                 }
             }
             // si no se obtienen campos, incluir toString
             if (headers.isEmpty()) headers.add("text");
             return headers;
         }

         private static Object fieldValueByName(Object obj, String name) {
             if (obj == null) return null;
             Class<?> c = obj.getClass();
             try {
                 Field f = c.getDeclaredField(name);
                 f.setAccessible(true);
                 return f.get(obj);
             } catch (NoSuchFieldException nsf) {
                 // intentar método getter
                 try {
                     String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
                     Method m = c.getMethod(methodName);
                     return m.invoke(obj);
                 } catch (Throwable t) {
                     // fallback: si header es "text", devolver toString
                     if ("text".equals(name)) return obj.toString();
                     return null;
                 }
             } catch (Throwable t) {
                 return null;
             }
         }

         private static void writeCsvLine(BufferedWriter bw, List<String> cols) throws IOException {
             StringBuilder sb = new StringBuilder();
             boolean first = true;
             for (String s : cols) {
                 if (!first) sb.append(',');
                 sb.append(escapeCsv(s));
                 first = false;
             }
             bw.write(sb.toString());
             bw.newLine();
         }

         private static String escapeCsv(String s) {
             if (s == null) return "\"\"";
             String v = s.replace("\"", "\"\"");
             return "\"" + v + "\"";
         }

         private static String captureSortingMostrar() {
             // Captura la salida de SortingStatsManager.mostrar() si existe
             try {
                 java.io.PrintStream original = System.out;
                 java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                 try (java.io.PrintStream ps = new java.io.PrintStream(baos, true, "UTF-8")) {
                     System.setOut(ps);
                     try {
                         SortingStatsManager.mostrar();
                     } catch (Throwable t) {
                         // ignore
                     }
                 } finally {
                     System.setOut(original);
                 }
                 return baos.toString("UTF-8");
             } catch (Throwable t) {
                 return "No se pudieron obtener estadísticas de ordenación.";
             }
         }
     }