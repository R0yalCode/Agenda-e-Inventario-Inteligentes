package ed.u2.search;

import ed.u2.sll.Node;
import ed.u2.sll.SinglyLinkedList;
import ed.u2.util.ProgressBar;

public class SLLSearch {

    public static <T> SearchStats buscar(SinglyLinkedList<T> lista, T target) {

        SearchStats st = new SearchStats();
        long t0 = System.nanoTime();

        Node<T> actual = lista.getHead();
        int idx = 0;

        while (actual != null) {

            st.comparaciones++;

            if (actual.getData().equals(target)) {
                st.resultadosEncontrados = 1;
                break;
            }

            actual = actual.getNext();
            idx++;

            ProgressBar.mostrar((double) idx / lista.size());
        }

        st.tiempoNs = System.nanoTime() - t0;
        return st;
    }
}
