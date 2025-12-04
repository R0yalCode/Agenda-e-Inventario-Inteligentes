package ed.u2.sll;

import java.util.ArrayList;
import java.util.List;

/**
 * Autor: R
 * Fecha: 2025
 *
 * Implementación de una Lista Enlazada Simple (SLL) genérica.
 *
 * Entrada: elementos genéricos T.
 * Salida: estructura enlazada, útil para practicar búsquedas secuenciales
 * y para convertir datasets a una estructura no basada en arrays.
 */
public class SinglyLinkedList<T> {

    private Node<T> head;
    private int size;

    public SinglyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // -----------------------------
    // MÉTODOS BÁSICOS
    // -----------------------------

    /**
     * Inserta un elemento al final de la lista.
     *
     * @param data Elemento a insertar.
     */
    public void insertar(T data) {
        Node<T> nuevo = new Node<>(data);

        if (head == null) {
            head = nuevo;
        } else {
            Node<T> aux = head;
            while (aux.getNext() != null) {
                aux = aux.getNext();
            }
            aux.setNext(nuevo);
        }
        size++;
    }

    /**
     * Alias de insertar() para compatibilidad con implementaciones estándar.
     * Agrega un elemento al final de la lista.
     *
     * @param data elemento a agregar
     */
    public void add(T data) {
        insertar(data);
    }

    /**
     * Inserta varios elementos desde una lista estándar de Java.
     *
     * @param lista Lista de elementos.
     */
    public void insertarDesdeLista(List<T> lista) {
        for (T item : lista) {
            insertar(item);
        }
    }

    /**
     * Obtiene el tamaño actual de la lista.
     *
     * @return número de nodos almacenados.
     */
    public int size() {
        return size;
    }

    /**
     * Obtiene el nodo en la posición indicada.
     *
     * @param index índice a buscar.
     * @return nodo o null si no existe.
     */
    public Node<T> getNodeAt(int index) {
        if (index < 0 || index >= size)
            return null;

        Node<T> aux = head;
        int contador = 0;

        while (aux != null) {
            if (contador == index)
                return aux;
            aux = aux.getNext();
            contador++;
        }
        return null;
    }

    /**
     * Convierte toda la SLL a una lista estándar (ArrayList).
     *
     * @return List<T> con los elementos.
     */
    public List<T> toList() {
        List<T> lista = new ArrayList<>();
        Node<T> aux = head;

        while (aux != null) {
            lista.add(aux.getData());
            aux = aux.getNext();
        }
        return lista;
    }

    /**
     * Convierte toda la SLL a un arreglo.
     *
     * @param clazz Clase del tipo T para crear el arreglo.
     * @return Arreglo con los elementos.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(Class<T> clazz) {
        T[] arr = (T[]) java.lang.reflect.Array.newInstance(clazz, size);

        Node<T> aux = head;
        int idx = 0;

        while (aux != null) {
            arr[idx++] = aux.getData();
            aux = aux.getNext();
        }
        return arr;
    }

    public Node<T> getHead() {
        return head;
    }

}
