package ed.u2.sll;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Representa un nodo dentro de una Lista Enlazada Simple (SLL).
 *
 * Entrada: valor genérico almacenado en el nodo.
 * Salida: nodo con referencia al siguiente elemento.
 */
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node() {
    }

    public Node(T data) {
        this.data = data;
    }

    // -----------------------------
    // GETTERS Y SETTERS
    // -----------------------------

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
