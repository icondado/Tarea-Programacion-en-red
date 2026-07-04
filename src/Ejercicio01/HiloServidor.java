package Ejercicio01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Clase HiloServidor 
 * Representa un hilo del servidor que atiende aun cliente (Inspector itv)
 * Cada cliente (coche) se gestiona en un hilo independiente
 *
 * @author Irene Condado Alcantarilla
 */
public class HiloServidor implements Runnable {

    // Socket asociado al cliente
    private final Socket s;
    // Recurso compartido entre hilos (gestiona las líneas de inspección)
    private RecursoCompartido rc;
    // Pruebas que se realizan en la ITV
    private static final String[] PRUEBAS = {"Luces", "Frenos", "Emisiones", "Dirección", "Suspensión"};
    // Frases válidas de tipo "cuñao"
    private static final String[] FRASES_CUNAO = {
        "ok jefe", "lo que tu digas", "a mandar", "como usted mande",
        "vamos al lio", "marchando", "manda usted", "perfecto maquina", "de lujo"
    };

    /**
     * Constructor del hilo servidor
     *
     * @param s socket del cliente
     * @param rc recurso compartido
     */
    public HiloServidor(Socket s, RecursoCompartido rc) {
        this.s = s;
        this.rc = rc;
    }

    /**
     * Método principal del hilo
     * Se encarga de la comunicación con el cliente
     */
    @Override
    public void run() {

        try {
            // Obtener número de coche
            int numeroCoche = rc.obtenerNumeroCliente();

            // Intentar ocupar una línea (mostrará mensaje si debe esperar)
            rc.ocuparLinea(numeroCoche);

            // Crear flujos de entrada y salida
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);

            // Mensaje inicial
            pw.println("Buenas tardes, le dejo aqui el walkie-talkie para darle ordenes");

            // Probabilidad inicial
            double probabilidad = 0.6;
            int pruebasSuperadas = 0;
            String[] respuestas = new String[5];
            boolean[] resultados = new boolean[5];
            double[] probabilidades = new double[5];

            // Realizar todas las pruebas
            for (int i = 0; i < PRUEBAS.length; i++) {
                // Guardar la probabilidad actual para esta prueba
                probabilidades[i] = probabilidad;

                // Tiempo de inspeccion entre 1 y 5 segundos
                int tiempo = (int) (Math.random() * 5000) + 1000;
                Thread.sleep(tiempo);

                // Enviar orden al cliente
                String mensaje = "Realice la prueba: " + PRUEBAS[i];
                pw.println(mensaje);

                // Recibir respuesta del cliente
                String respuesta = br.readLine();
                if (respuesta == null) {
                    return;
                }

                respuestas[i] = respuesta;

                // Comprobar si pasa la prueba con la probabilidad actural
                double aleatorio = Math.random();
                if (aleatorio < probabilidad) {
                    pruebasSuperadas++;
                    resultados[i] = true;
                } else {
                    resultados[i] = false;
                }

                // Después de la prueba, comprobar si es frase: cuñao
                // La probabilidad se reduce para las siguientes pruebas
                if (esFraseCunao(respuesta)) {
                    probabilidad = probabilidad - 0.1;
                }

                // No se sale del bucle aunque falle una prueba
                // Continua haciendo todas las pruebas, 5
            }

            // Mensaje de retirada del walkie
            pw.println("Le retiro el walkie, espere en la puerta. Gracias.");

            // Pausa antes del veredicto final
            Thread.sleep(1000);

            // Resultado final (necesita superar todas las pruebas)
            boolean aprobado = (pruebasSuperadas == 5);

            if (aprobado) {
                pw.println("Tome su pegatina");
            } else {
                pw.println("Debe volver de nuevo");
            }

            // Mostrar resultado detallado
            System.out.println("\n--- Resultado Coche Coche" + numeroCoche + " ---");
            if (aprobado) {
                System.out.println("Coche" + numeroCoche + " ITV SUPERADA.");
            } else {
                System.out.println("Coche" + numeroCoche + " ITV NO SUPERADA.");
            }

            // Mostrar detalle de cada prueba con la probabilidad que tenia en ese momento
            for (int i = 0; i < PRUEBAS.length; i++) {
                String resultado = resultados[i] ? "Si" : "No";
                int prob = (int) (probabilidades[i] * 100);  // Usa la probabilidad guardada
                System.out.println(PRUEBAS[i] + ": " + resultado + " (\"" + respuestas[i] + "\" - prob " + prob + "%)");
            }

            System.out.println("---------------------------------------\n");

            // Liberar la línea
            rc.liberarLinea();

            // Cierre de recursos
            br.close();
            pw.close();
            s.close();

        } catch (InterruptedException e) {
            System.err.println("Inspeccion interrumpida: " + e.getMessage());
        } catch (SocketException e) {
            System.out.println("Error de socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de comunicacion: " + e.getMessage());
        }
    }

    /**
     * Comprobar si la respuesta del cliente es Frase Cuñao
     * 
     * @param respuesta texto recibido
     * @return true si la respuesta está en la lista de las frases cuñao
     */
    private boolean esFraseCunao(String respuesta) {

        // Si no hay respuesta, no es válida
        if (respuesta == null) {
            return false;
        }

        // Normalizar texto
        String respuestaMinusculas = respuesta.toLowerCase().trim();

        // Comprobar si coincide con alguna frase cuñao válida
        for (int i = 0; i < FRASES_CUNAO.length; i++) {
            if (respuestaMinusculas.equals(FRASES_CUNAO[i])) {
                return true;
            }
        }

        return false;
    }
}
