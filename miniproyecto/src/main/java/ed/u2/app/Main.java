package ed.u2.app;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Punto de entrada principal del sistema.
 */
public class Main {

    public static void main(String[] args) {
        AppContext context = new AppContext();
        MenuPrincipal menu = new MenuPrincipal(context);

        menu.iniciar();
    }
}


