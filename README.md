# MINIPROYECTO
# Gestión de Citas y Stock con Algoritmos de Búsqueda y Ordenación en Java
## Unidad 2 – Estructura de Datos
---
Link del taller:
# [Taller – Algoritmos de Búsqueda y Ordenación]()
---

## 1. Introducción

Este proyecto implementa un módulo de consola en Java para la gestión de citas médicas, pacientes e inventario, aplicando y comparando algoritmos clásicos de ordenación y búsqueda, con medición de métricas y visualización de estadísticas.
El sistema permite trabajar con datos almacenados en arreglos y listas simplemente enlazadas (SLL), evaluando el desempeño real de cada algoritmo según el tipo de estructura y el estado de los datos.

### Funcionalidades principales
1. Gestión de datasets (citas, pacientes, inventario).
2. Ordenación usando:
    * Bubble Sort
    * Selection Sort
    * Insertion Sort

3. Búsquedas:
    * Secuencial (first, last, findAll)
    * Secuencial con centinela
    * Búsqueda binaria en arreglos ordenados
    * Búsqueda sobre Singly Linked List

4. Registro de estadísticas:
    * Tiempo de ejecución (ns)
    * Comparaciones
    * Intercambios (ordenación)
    * Resultados encontrados (búsqueda)

5. Histogramas visuales en consola con colores ANSI.
6. Exportación de estadísticas a CSV.
7. Historial de ejecuciones.
8. Arquitectura por capas y diseño profesional.
---

##  2. Arquitectura del Proyecto

El proyecto sigue una **arquitectura por capas**, organizada para facilitar mantenibilidad, escalabilidad y claridad del código.
```
miniproyecto/
├── resources/
│   ├── datasets/
│   │   ├── citas_100_casi_ordenadas.csv
│   │   ├── citas_100.csv
│   │   ├── hola.csv
│   │   ├── inventario_500_inverso.csv
│   │   └── pacientes_500.csv
│   └── export/
│   │   └── sorting_stats.csv
│   └── history/
│       └── history.csv
src/
└── ed/u2/
    ├── app/               
    │   ├── Main.java
    │   ├── MenuPrincipal.java
    │   └── HistoryManager.java
    ├── data/               
    │   └── DatasetManager.java
    ├── model/              
    │   ├── Cita.java
    │   ├── Paciente.java
    │   └── InventarioItem.java
    ├── search/             
    │   ├── SearchEngine.java
    │   ├── SearchResult.java
    │   └── SearchStats.java
    ├── sorting/           
    │   ├── BubbleSorter.java
    │   ├── InsertionSorter.java
    │   ├── SelectionSorter.java
    ├── stats/             
    │   ├── OperationStats.java
    │   ├── SortingStatsManager.java
    │   ├── SearchStatsManager.java
    │   ├── SearchStatsRepository.java
    ├── io/                 
    │   ├── CsvLoader.java
    │   ├── ExportUtils.java
    │   └── FileUtils.java
    ├── sll/                
    │   ├── Node.java
    │   └── SinglyLinkedList.java
    ├── util/               
    │   ├── ANSI.java
    │   ├── ConsoleUtils.java
    │   ├── Holder.java
    │   └── ProgressBar.java
    └────────────────────────
```
---

###  Descripción breve de cada capa

| Capa | Función |
| :--- | :--- |
| `data` | Carga y administración de datasets |
| `model` | Entidades del sistema |
| `search` | Algoritmos de búsqueda y métricas |
| `sorting` | Algoritmos de Ordenacion  |
| `stats` | Estadísticas visuales e historial  |
| `io` |  Exportacion de resultados |
| `sll` |  Implementación de lista enlazada |
| `app` | Menú e interacción con el usuario  |

---

##  3. Algoritmos Implementados

###  Bubble Sort

* **Estable**
* Optimizado con corte temprano
* MEficiente en listas casi ordenadas
* Alto número de intercambios

###  Selection Sort

* **NO estable**
* Mismo número de comparaciones en todos los casos
* Minimiza intercambios
* Independiente del orden inicial

###  Bubble Sort (Optimizado)

* **Estable**
* Muy eficiente para datos pequeños o casi ordenados
* Menos comparaciones y movimientos en mejor caso

---

##  4. Algoritmos de Búsqueda Implementados

* Búsqueda secuencial:
    * `first`  
    * `last` 
    * `findAll` 
* Búsqueda secuencial con centinela
* Búsqueda binaria (sobre arreglos ordenados)
* Búsqueda sobre Singly Linked List (SLL)

---
##  5. Estadísticas y Visualización
El sistema genera histogramas verticales en consola, usando:

* Bloques ASCII (██)
* Colores ANSI para diferenciar algoritmos
* Escalado proporcional al tiempo de ejecución
* Identificación del mejor algoritmo

#### Métricas mostradas

* Tiempo (ns)
* Comparaciones
* Intercambios (ordenación)
* Resultados encontrados (búsqueda)

---

##  6. Requisitos del Sistema

###  Software 

* **Java JDK 21 o superior**
    * (Recomendado: JDK 25 OpenJDK)
* Cualquier IDE Java:
    * IntelliJ IDEA
    * NetBeans
    * Eclipse
    * Visual Studio Code con extensión Java
* Git (opcional pero recomendado)

###  Sistema operativo

Funciona en:

* Windows
* Linux
* macOS

---

##  7. Cómo clonar y ejecutar el proyecto

###  1. Clonar el repositorio
git clone https://github.com/R0yalCode/Agenda-e-Inventario-Inteligentes.git

###  2. Navegar al proyecto
cd REPO

###  3. Compilar el proyecto
javac -d bin src/ed/u2/**/*.java


###  4. Ejecutar el programa
java -cp bin ed.u2.app.MenuPrincipal

---


## 8. Capturas de Ejecución 

<img width="427" height="719" alt="image" src="https://github.com/user-attachments/assets/2e4c670d-29f4-4491-b7df-a585e761a0bf" />

<img width="432" height="727" alt="image" src="https://github.com/user-attachments/assets/74fc9d7a-01d4-479b-a0c1-33f51b832892" />

<img width="443" height="727" alt="image" src="https://github.com/user-attachments/assets/43b3c5b1-3aa8-42c1-82f1-bb6ddeea4c5d" />

<img width="425" height="722" alt="image" src="https://github.com/user-attachments/assets/e1a67b84-7a89-47e9-b71b-7b53c73adabe" />

---

##  9. Conclusiones

Este proyecto demuestra que:

* Insertion Sort es superior en listas pequeñas o casi ordenadas.
* Selection Sort es consistente cuando el costo de intercambio es alto.
* Bubble Sort es útil en escenarios didácticos y datos parcialmente ordenados.
* La búsqueda binaria es altamente eficiente, pero requiere datos ordenados.
* Las búsquedas secuenciales son más flexibles en estructuras como SLL.
* La elección del algoritmo depende del tipo de estructura, tamaño y estado de los datos.


## Autores: 

### [Ana Panamito](https://github.com/AnaPanamito)
### [Royel Jima](https://github.com/R0yalCode)
### [Daniel Savedra](https://github.com/Dan-San837)
### [Anderson Coello](https://github.com/AndersonC15)

