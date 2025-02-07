// Modificado ThreadServerWardle.java para corregir sincronización y permitir que el cliente envíe correctamente
import java.io.*;
import java.net.Socket;

public class ThreadServerWardle extends Thread {
    private Socket socket;
    private int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static String palabraJugador1 = "";
    private static String palabraJugador2 = "";
    private static boolean jugador1Listo = false;
    private static boolean jugador2Listo = false;
    int turno =0;


    public ThreadServerWardle(Socket socket, int playerId) throws IOException {
        this.socket = socket;
        this.playerId = playerId;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            jugador1Listo=true;
            if (playerId == 1) {
                out.writeObject("Esperando a cliente 2...");
                out.flush();

                while (!jugador2Listo) {
                    Thread.sleep(100);
                }
                out.writeObject("Cliente 2 conectado. Elige una palabra para que cliente 2 la adivine:");
                out.flush();
                palabraJugador1 = (String) in.readObject();
                out.writeObject("Esperando a que cliente 2 elija palabra...");
                out.flush();
                jugador1Listo= false;
            } else {
                jugador2Listo = true;
                out.writeObject("Esperando a que cliente 1 elija palabra...");
                out.flush();
                while (palabraJugador1.isEmpty()) {
                    Thread.sleep(100);
                }
                out.writeObject("Elige una palabra para que cliente 1 la adivine:");
                out.flush();
                palabraJugador2 = (String) in.readObject();
                out.writeObject("Ambas palabras elegidas. Comienza el juego.");
                out.flush();
                jugador2Listo=false;
                jugador1Listo=true;
            }

            while (palabraJugador1.isEmpty() || palabraJugador2.isEmpty()) {
                Thread.sleep(100);
            }

            boolean juegoActivo = true;
            while (juegoActivo) {
                if (playerId == 1) {


                    jugador1Listo=true;
                    out.writeObject("Turno:\t"+turno+"\tIntroduce tu intento:");
                    out.flush();
                    String intentoS = (String) in.readObject();


                    String resultado = comprobarPalabra(intentoS, palabraJugador2);
                    out.writeObject(new Jugada(intentoS, resultado, turno + 1));
                    out.flush();

                    if (resultado.equals("OOOOO")) {
                    out.writeObject("¡Has ganado!");
                    out.flush();
                    juegoActivo = false;
                    }
                    jugador1Listo=false;
            }else {

                    jugador2Listo=true;
                        out.writeObject("Turno:\t"+turno+"\tIntroduce tu intento:");
                        out.flush();
                        String intentoS = (String) in.readObject();


                        String resultado = comprobarPalabra(intentoS, palabraJugador1);
                        out.writeObject(new Jugada(intentoS, resultado, turno + 1));
                        out.flush();

                        if (resultado.equals("OOOOO")) {
                            out.writeObject("¡Has ganado!");
                            out.flush();
                            juegoActivo = false;
                        }


                    jugador2Listo=false;
                }
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String comprobarPalabra(String intento, String palabra) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palabra.length(); i++) {
            if (intento.charAt(i) == palabra.charAt(i)) {
                resultado.append("O");
            } else if (palabra.contains(String.valueOf(intento.charAt(i)))) {
                resultado.append("?");
            } else {
                resultado.append("-");
            }
        }
        return resultado.toString();
    }
}
