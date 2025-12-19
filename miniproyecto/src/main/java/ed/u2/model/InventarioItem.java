package ed.u2.model;

/**
 * Autor: R  
 * Fecha: 2025
 *
 * Representa un ítem del inventario:
 *  - id (ITEM-0001)
 *  - insumo (Gasas 5x5)
 *  - stock (cantidad disponible)
 */
public class InventarioItem implements Comparable<InventarioItem> {

    private String id;
    private String insumo;
    private int stock;

    // ============================================================
    // CONSTRUCTOR COMPATIBLE CON CsvLoader
    // ============================================================

    public InventarioItem(String id, String insumo, int stock) {
        this.id = id;
        this.insumo = insumo;
        this.stock = stock;
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String getId() {
        return id;
    }

    public String getInsumo() {
        return insumo;
    }

    public int getStock() {
        return stock;
    }

    // ============================================================
    // compareTo
    // ============================================================

    @Override
    public int compareTo(InventarioItem o) {
        // Ordenar por stock ascendente
        int cmp = Integer.compare(this.stock, o.stock);
        if (cmp != 0) return cmp;

        // Si stock igual → por nombre
        return this.insumo.compareTo(o.insumo);
    }

    // ============================================================
    // toString
    // ============================================================

    @Override
    public String toString() {
        return id + " | " + insumo + " | stock=" + stock;
    }
}
