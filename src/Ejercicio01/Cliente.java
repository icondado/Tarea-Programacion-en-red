package Ejercicio01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Clase Cliente 
 * Representa a un coche que llega a pasar la ITV. Cada coche se ejecuta
 * como un hilo independiente
 *
 * @author Irene Condado Alcantarilla
 */
public class Cliente implements Runnable {

    // Identificador del coche
    private int idCoche;
    // Indica se el coche ha aprobado la ITV
    private boolean aprobado;
    // Frases permitidas
    private static final String[] FRASES_PERMITIDAS = {
        "vale", "recibido", "entendido", "procedo", "hecho", "si", "correcto", "ok", "de acuerdo"
    };
    // Frases válidas de tipo "cuñao"
    private static final String[] FRASES_CUNAO = {
        "ok jefe", "lo que tu digas", "a mandar", "como usted mande",
        "vamos al lio", "marchando", "manda usted", "perfecto maquina", "de lujo"
    };

    /**
     * Constructor del cliente
     *
     * @param idCoche identificador del coche
     */
    public Cliente(int idCoche) {
        this.idCoche = idCoche;
        this.aprobado = false;
    }

    /**
     * Método principal del hilo Se encarga de la comunicación con el servidor
     * ITV
     */
    @Override
    public void run() {
        try {
            // Conexión con el servidor en localhost y puerto 12349
            Socket s = new Socket("localhost", 12349);
            // Crear flujos de entrada y salida
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);

            String mensajeRecibido;

            // Leer mensajes del servidor mientras haya conexión
            while ((mensajeRecibido = br.readLine()) != null) {

                // Primer mensaje del servidor: no se responde
                if (mensajeRecibido.indexOf("walkie-talkie para darle ordenes") != -1) {
                    continue;
                }

                // Mensaje solicitando una prueba: responder con frase aleatoria
                if (mensajeRecibido.indexOf("Realice la prueba:") == 0) {
                    String respuesta = elegirFraseAleatoria();
                    pw.println(respuesta);
                    continue;
                }

                // Mensaje de retirada del walkie: no responder
                if (mensajeRecibido.indexOf("Le retiro el walkie") != -1) {
                    continue;
                }

                // Mensaje final de aprobado
                if (mensajeRecibido.equals("Tome su pegatina")) {
                    aprobado = true;
                    break;
                }

                // Mensaje final de suspenso
                if (mensajeRecibido.equals("Debe volver de nuevo")) {
                    aprobado = false;
                    break;
                }
            }

            // Cierre de recursos
            br.close();
            pw.close();
            s.close();

        } catch (IOException e) {
            System.out.println("Error al conectar al servidor: " + e.getMessage());
        }
    }

    /**
     * Devuelve una frase aleatoria. - 70% de probabilidad de frase permitida -
     * 30% de probabilidad de frase tipo "cuñao"
     *
     * @return frase seleccionada aleatoriamente
     */
    private String elegirFraseAleatoria() {

        double aleatorio = Math.random();

        if (aleatorio < 0.7) {
            // Elegir frase permitida (70%)
            int indice = (int) (Math.random() * FRASES_PERMITIDAS.length);
            return FRASES_PERMITIDAS[indice];
        } else {
            // Elegir frase de tipo "cuñao" (30%)
            int indice = (int) (Math.random() * FRASES_CUNAO.length);
            return FRASES_CUNAO[indice];
        }
    }

    /**
     * Indica si el coche ha aprobado la ITV
     *
     * @return true si ha aprobado
     */
    public boolean haAprobado() {
        return aprobado;
    }

    /**
     * Devuelve el identificador del coche
     *
     * @return id del coche
     */
    public int getIdCoche() {
        return idCoche;
    }
}
