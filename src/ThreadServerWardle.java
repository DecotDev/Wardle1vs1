import java.io.*;
import java.net.Socket;

public class ThreadServerWardle extends Thread {
    private Socket socket;
    private int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int turnos;

    public ThreadServerWardle(Socket socket, int playerId) throws IOException {
        this.socket = socket;
        this.playerId = playerId;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.turnos = 0;
    }

    @Override
    public void run() {
        try {
            if (playerId == 1) {
                out.writeObject("Escoja una palabra para que su oponente adivine:");
                String palabra = (String) in.readObject();
                ServerWardle.setPalabraAdivinar(palabra);
                out.writeObject("Palabra enviada. Esperando al oponente...");
            } else {
                out.writeObject("Bienvenido. Tu oponente ha elegido una palabra. ¡Adivina!");
            }

            while (true) {
                turnos++;
                out.writeObject("Turno " + turnos + ": Introduce tu intento:");
                Jugada jugada = (Jugada) in.readObject();
                String resultado = comprobarPalabra(jugada.getIntento());
                Jugada respuesta = new Jugada(jugada.getIntento(), resultado, turnos);
                out.writeObject(respuesta);
                if (resultado.equals("VICTORIA")) {
                    out.writeObject("FELICIDADES, HAS GANADO EN " + turnos + " TURNOS!");
                    break;
                }
            }
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String comprobarPalabra(String intento) {
        String palabra = ServerWardle.getPalabraAdivinar();
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palabra.length(); i++) {
            if (i < intento.length() && intento.charAt(i) == palabra.charAt(i)) {
                resultado.append("O"); // Letra correcta en la posición correcta
            } else if (palabra.contains(String.valueOf(intento.charAt(i)))) {
                resultado.append("?"); // Letra correcta en la posición incorrecta
            } else {
                resultado.append("-"); // Letra incorrecta
            }
        }
        if (resultado.toString().equals("O".repeat(palabra.length()))) {
            return "VICTORIA";
        }
        return resultado.toString();
    }
}