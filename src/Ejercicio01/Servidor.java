package Ejercicio01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Servidor 
 * Representa la estación de ITV Se encarga de aceptar conexiones
 * de los coches y crear un hilo independiente para cada uno Estación ITV
 *
 * @author Irene Condado Alcantarilla
 */
public class Servidor {

    public static void main(String[] args) throws IOException {

        // Recurso compartido que gestiona las líneas de inspección
        RecursoCompartido rc = new RecursoCompartido();

        // Crea ServerSocket en puerto 12349
        ServerSocket ss = new ServerSocket(12349);
        System.out.println("Servidor ITV del Infierno arrancando...");

        // El servidor se mantiene siempre activo
        while (true) {
            // Espera a que un coche (cliente) se conecte
            Socket s = ss.accept();

            // Por cada coche se crea un hilo del servidor
            // Todos los hilos comparten el mismo recurso (líneas ITV)
            // Iniciar el hilo que atiende al coche
            new Thread(new HiloServidor(s, rc)).start();
        }
    }
}
