package ed.u2.util;

/**
 * Autor: R 
 * Fecha: 2025
 *
 * Cronómetro simple para medir tiempos.
 * Entrada: llamadas a start() y stop()
 * Salida: duración en nanosegundos o milisegundos
 */
public class Timer {

    private long inicio;
    private long fin;

    public void start() {
        inicio = System.nanoTime();
    }

    public void stop() {
        fin = System.nanoTime();
    }

    public long getNs() {
        return fin - inicio;
    }

    public long getMs() {
        return (fin - inicio) / 1_000_000;
    }

}
