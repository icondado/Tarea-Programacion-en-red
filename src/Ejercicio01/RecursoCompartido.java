package Ejercicio01;

/**
 * Clase RecursoCompartido 
 * Gestiona el acceso concurrente a las líneas de
 * inspección de la ITV Controla cuántos coches pueden estar siendo
 * inspeccionados al mismo tiempo
 *
 * @author Irene Condado Alcantarilla
 */
public class RecursoCompartido {

    // Número máximo de líneas de inspección disponibles
    private static final int NUM_LINEAS = 4;

    // Número de líneas actualmente ocupadas
    private int lineasOcupadas = 0;

    // Contador global de coches que llegan a la ITV
    private int contadorCoches = 0;

    /**
     * Devuelve el número identificador del siguiente coche El método es
     * sincronizado para evitar duplicados cuando varios hilos acceden
     * simultáneamente
     *
     * @return número de coche(1,2,3,4...)
     */
    public synchronized int obtenerNumeroCliente() {
        // Pre-incremento para empezar a numerar desde 1
        return ++contadorCoches;
    }

    /**
     * Ocupa una línea de inspección Si todas las líneas están ocupadas, el hilo
     * espera hasta que alguna quede libre
     *
     * @param numeroCoche identificador del coche
     * @throws InterruptedException si el hilo es interrumpido mientras espera
     */
    public synchronized void ocuparLinea(int numeroCoche) throws InterruptedException {

        // Si no hay líneas disponibles, el coche debe esperar
        if (lineasOcupadas >= NUM_LINEAS) {
            System.out.println("Coche" + numeroCoche + " esperando para entrar.");

            // Esperar mientras no haya líneas libres
            while (lineasOcupadas >= NUM_LINEAS) {
                wait();
            }
        }

        // Ocupar una línea
        lineasOcupadas++;
        System.out.println("Coche" + numeroCoche + " entra en la ITV.");
    }

    /**
     * Libera una línea de inspección Notifica a uno de los hilos que estén
     * esperando para que pueda continuar
     */
    public synchronized void liberarLinea() {

        // Liberar una línea
        lineasOcupadas--;

        notify();
    }
}
