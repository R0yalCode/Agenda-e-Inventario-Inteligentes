package ed.u2.sorting;

public interface Sorter<T> {

    SortStats sort(T[] arr, boolean asc, boolean mostrarBarra);

}
