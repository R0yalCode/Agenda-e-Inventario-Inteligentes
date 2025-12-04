package ed.u2.app;

import ed.u2.model.*;

import java.util.List;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Contenedor central de datos cargados en la aplicación.
 * Aquí se guarda todo el dataset vigente y sus versiones en array
 * para ordenación y búsqueda.
 */
public class AppContext {

    private String datasetActual;

    private List<Cita> citas;
    private List<Paciente> pacientes;
    private List<InventarioItem> inventario;

    private Cita[] citasArray;
    private Paciente[] pacientesArray;
    private InventarioItem[] inventarioArray;

    public boolean hayDatasetCargado() {
        return citasArray != null ||
               pacientesArray != null ||
               inventarioArray != null;
    }

    // -------------------------
    // GETTERS Y SETTERS
    // -------------------------

    public String getDatasetActual() {
        return datasetActual;
    }

    public void setDatasetActual(String datasetActual) {
        this.datasetActual = datasetActual;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public List<InventarioItem> getInventario() {
        return inventario;
    }

    public void setInventario(List<InventarioItem> inventario) {
        this.inventario = inventario;
    }

    public Cita[] getCitasArray() {
        return citasArray;
    }

    public void setCitasArray(Cita[] citasArray) {
        this.citasArray = citasArray;
    }

    public Paciente[] getPacientesArray() {
        return pacientesArray;
    }

    public void setPacientesArray(Paciente[] pacientesArray) {
        this.pacientesArray = pacientesArray;
    }

    public InventarioItem[] getInventarioArray() {
        return inventarioArray;
    }

    public void setInventarioArray(InventarioItem[] inventarioArray) {
        this.inventarioArray = inventarioArray;
    }
}
