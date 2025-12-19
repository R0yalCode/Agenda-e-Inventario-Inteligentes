# MINIPROYECTO
## FEIRNNR - Carrera de Computación (Estructura de Datos)
*Docente:* Ing. Andrés Roberto Navas Castellanos
---
# Mini-proyecto U2: Agenda e Inventario Inteligentes  
**Asignatura:** Estructura de Datos – Unidad 2 (Ordenación y Búsqueda)  
**Carrera:** Computación – FEIRNNR  
**Docente:** Ing. Andrés Roberto Navas Castellanos  

## Autores  
- Ana Panamito  
- Royel Jima  
- Daniel Savedra  
- Anderson Coello  

---

## 1. Descripción del proyecto
Este mini-proyecto implementa un módulo de **gestión de citas, pacientes e inventario** para un hospital veterinario, aplicando **algoritmos de ordenación y búsqueda** estudiados en la Unidad 2.  
El objetivo es **comparar el comportamiento de distintos algoritmos** según el tipo de datos y la estructura utilizada (**arreglos vs listas simplemente enlazadas**), apoyándose en **mediciones de tiempo y contadores de operaciones**.

---

## 2. Alcance funcional
El sistema implementa los siguientes módulos:

- **Agenda de citas (arreglo)**
  - Carga de `citas_100.csv` y `citas_100_casi_ordenadas.csv`
  - Ordenación por clave temporal
  - Comparación de Burbuja, Selección e Inserción
  - Búsqueda binaria y por rangos (`lowerBound` / `upperBound`)

- **Pacientes (SLL)**
  - Lista simplemente enlazada con `(id, apellido, prioridad)`
  - Búsqueda secuencial:
    - primera coincidencia
    - última coincidencia
    - `findAll(prioridad == 1)`

- **Inventario (arreglo)**
  - Carga de `inventario_500_inverso.csv`
  - Ordenación por stock
  - Consulta mediante búsqueda binaria
  - Manejo de duplicados y límites

---

## 3. Decisiones de diseño
- **Arreglos** se utilizan en agenda e inventario porque permiten:
  - Acceso directo
  - Ordenación eficiente
  - Uso de búsqueda binaria (O(log n)) tras ordenar

- **Lista simplemente enlazada (SLL)** se utiliza para pacientes porque:
  - Facilita inserciones dinámicas
  - No requiere reordenar la estructura
  - La búsqueda secuencial es adecuada para este contexto

- **Elección de algoritmos de ordenación**:
  - **Inserción**: preferido para datos casi ordenados
  - **Selección**: mantiene comparaciones constantes en datos inversos
  - **Burbuja**: usado como referencia comparativa

- **Búsqueda binaria** se ejecuta únicamente cuando el arreglo está previamente ordenado, validando esta precondición antes de su uso.

---

## 4. Casos de borde considerados

### Ordenación (arreglos)
- Arreglo vacío (n = 0)
- Arreglo con un solo elemento
- Dataset ya ordenado
- Dataset inverso
- Claves duplicadas en el criterio de ordenación

### Búsqueda binaria
- Búsqueda en arreglo no ordenado (precondición)
- Clave inexistente
- Claves duplicadas (uso de bounds)
- Clave ubicada en el primer o último elemento

### Búsqueda secuencial (SLL)
- Lista vacía
- Coincidencia en el primer nodo
- Coincidencia en el último nodo
- Múltiples coincidencias (`findAll`)
- Ninguna coincidencia

### Medición de rendimiento
- Descarte de las primeras corridas para evitar efectos del calentamiento de la JVM
- Validación de contadores en escenarios extremos

---

## 5. Medición y evidencias
- Tiempo medido con `System.nanoTime()`
- Contadores de:
  - Comparaciones
  - Intercambios / movimientos
- Resultados exportados a:
  - `sorting_stats.csv`
  - `search_stats.csv`
- Las mediciones permiten justificar empíricamente la elección de algoritmos según el tipo de datos.

---

## 6. Estructura del proyecto

```
miniproyecto/
├── history/
│   └── history.csv
├── resources/
│   ├── datasets/
│   │   ├── citas_100_casi_ordenadas.csv
│   │   ├── citas_100.csv
│   │   ├── hola.csv
│   │   ├── inventario_500_inverso.csv
│   │   └── pacientes_500.csv
│   └── export/
│       ├── search_stats.csv
│       └── sorting_stats.csv
├── src/
│   └── main/
│       └── java/
│           └── ed/
│               └── u2/
│                   ├── app/
│                   │   ├── Main.java
│                   │   ├── MenuPrincipal.java
│                   │   └── HistoryManager.java
│                   ├── data/
│                   │   └── DatasetManager.java
│                   ├── io/
│                   │   ├── CsvLoader.java
│                   │   ├── ExportUtils.java
│                   │   └── FileUtils.java
│                   ├── model/
│                   │   ├── Cita.java
│                   │   ├── InventarioItem.java
│                   │   └── Paciente.java
│                   ├── search/
│                   │   ├── SearchEngine.java
│                   │   ├── SearchResult.java
│                   │   └── SearchStats.java
│                   ├── sll/
│                   │   ├── Node.java
│                   │   └── SinglyLinkedList.java
│                   ├── sorting/
│                   │   ├── BubbleSorter.java
│                   │   ├── InsertionSorter.java
│                   │   └── SelectionSorter.java
│                   ├── stats/
│                   │   ├── ChartUtils.java
│                   │   ├── OperationStats.java
│                   │   ├── SearchStatsManager.java
│                   │   ├── SearchStatsRepository.java
│                   │   └── SortingStatsManager.java
│                   └── util/
│                       ├── ANSI.java
│                       ├── ConsoleUtils.java
│                       ├── Holder.java
│                       └── ProgressBar.java
```



## 7. Ejecución
1. Abrir el proyecto en un entorno Java (JDK 17 o superior)
2. Ejecutar la clase `Main`
3. Usar el menú para:
   - Cargar datasets automaticamente o manualmente
   - Ordenar
   - Buscar
   - Exportar estadísticas o historial

---

## 8. Notas finales
Este proyecto demuestra que **la elección del algoritmo y la estructura de datos depende del contexto**, y que las decisiones correctas se respaldan tanto teóricamente como mediante **evidencias experimentales**.


