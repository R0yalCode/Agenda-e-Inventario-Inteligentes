package ed.u2.model;

/**
 * Autor: R + ChatGPT
 * Fecha: 2025
 *
 * Modelo para ítems del stock/inventario.
 *
 * Comparable: se ordena por cantidad (stock). En empate, por nombre.
 */
public class InventarioItem implements Comparable<InventarioItem> {

    private int idItem;
    private String nombre;
    private int stock;

    public InventarioItem(int idItem, String nombre, int stock) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.stock = stock;
    }

    // -------------------------------
    // GETTERS Y SETTERS
    // -------------------------------
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // -------------------------------
    // COMPARABLE
    // -------------------------------
    @Override
    public int compareTo(InventarioItem o) {
        int cmp = Integer.compare(this.stock, o.stock);
        if (cmp != 0) return cmp;

        return this.nombre.compareToIgnoreCase(o.nombre);
    }

    // -------------------------------
    // TO STRING
    // -------------------------------
    @Override
    public String toString() {
        return "InventarioItem{" +
                "idItem=" + idItem +
                ", nombre='" + nombre + '\'' +
                ", stock=" + stock +
                '}';
    }
}
