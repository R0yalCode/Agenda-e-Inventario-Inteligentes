package ed.u2.stats;

import ed.u2.data.DatasetManager;
import ed.u2.model.*;
import ed.u2.util.ANSI;
import ed.u2.util.ConsoleUtils;

import java.util.*;

public class StatsManager {

    public static void mostrarEstadisticas() {

        if (!DatasetManager.hayDataset()) {
            System.out.println(ANSI.RED + " No hay dataset cargado." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.CYAN_BOLD + "\n=== ESTADÍSTICAS ===" + ANSI.RESET);

        switch (DatasetManager.getTipoActual()) {
            case CITAS -> statsCitas();
            case PACIENTES -> statsPacientes();
            case INVENTARIO -> statsInventario();
            default -> System.out.println("Dataset desconocido");
        }

        ConsoleUtils.pausar("\nEnter para continuar...");
    }

   
}
