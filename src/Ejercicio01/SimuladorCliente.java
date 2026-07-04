package Ejercicio01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase SimuladorCliente 
 * Lanza varios clientes (coches) de forma simultánea
 * para simular la llegada de vehículos a la ITV
 *
 * @author Irene Condado Alcantarilla
 */
public class SimuladorCliente {

    public static void main(String[] args) throws InterruptedException {

        // Scanner para leer datos desde teclado
        Scanner sc = new Scanner(System.in);

        // Lista que almacena los hilos creados
        List<Thread> listaHilos = new ArrayList();

        // Lista que almacena los coches (clientes)
        List<Cliente> listaCoches = new ArrayList();

        // Contador global de coches creados
        int totalCoches = 0;

        // Bucle que permite lanzar coches en varias tandas
        while (true) {
            // Solicitar al usuario cuántos coches llegan
            System.out.print("¿Cuantos coches llegan? ");
            int numCoches = sc.nextInt();

            // Si se introduce 0, finaliza la simulación
            if (numCoches == 0) {
                break;
            }

            // Crear y lanzar los coches indicados
            for (int i = 0; i < numCoches; i++) {

                // Incrementar el identificador del coche
                totalCoches++;

                // Crear nuevo cliente (coche)
                Cliente coche = new Cliente(totalCoches);
                listaCoches.add(coche);

                // Crear hilo asociado al coche
                Thread hilo = new Thread(coche);
                listaHilos.add(hilo);

                // Iniciar el hilo
                hilo.start();

                // Pequeña pausa para simular llegadas escalonadas
                Thread.sleep(100);
            }
        }

        // Esperar a que terminen todos los hilos
        for (Thread hilo : listaHilos) {
            hilo.join();
        }

        // Contardores de resultados
        int aprobados = 0;
        int suspendidos = 0;

        // Recorrer todos los coches para contar aprobados y suspendidos
        for (Cliente coche : listaCoches) {
            if (coche.haAprobado()) {
                aprobados++;
            } else {
                suspendidos++;
            }
        }

        // Mostrar estadisticas finales
        System.out.println("Itv's superadas: " + aprobados);
        System.out.println("Itv's no superadas: " + suspendidos);
    }
}
