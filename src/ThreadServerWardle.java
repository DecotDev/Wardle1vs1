// ThreadServerWardle.java (Corregido)
import java.io.*;
import java.net.Socket;

public class ThreadServerWardle extends Thread {
    private Socket socket;
    private int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ThreadServerWardle(Socket socket, int playerId) throws IOException {
        this.socket = socket;
        this.playerId = playerId;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            if (playerId == 1) {
                out.writeObject("Escoja una palabra para que su oponente adivine:");
                out.flush();
                String palabra = (String) in.readObject();
                ServerWardle.setPalabraAdivinar(palabra);
                out.writeObject("Palabra establecida. Esperando al oponente...");
                out.flush();
            } else {
                while (!ServerWardle.getPalabraAdivinar().isEmpty()) {
                    Thread.sleep(100);
                }
                out.writeObject("Tu oponente ha elegido una palabra. ¡Adivina!");
                out.flush();
            }

            while (true) {
                out.writeObject("Introduce tu intento:");
                out.flush();
                Jugada jugada = (Jugada) in.readObject();
                String resultado = comprobarPalabra(jugada.getIntento());
                out.writeObject(new Jugada(jugada.getIntento(), resultado, jugada.getTurno() + 1));
                out.flush();

                if ("VICTORIA".equals(resultado)) {
                    out.writeObject("¡FELICIDADES, HAS GANADO!");
                    out.flush();
                    break;
                }
            }
            socket.close();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
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
        return resultado.toString().equals("O".repeat(palabra.length())) ? "VICTORIA" : resultado.toString();
    }
}
