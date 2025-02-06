import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ServerWardle {
    private static final int PORT = 5225;
    private static Socket player1 = null;
    private static Socket player2 = null;
    private static String palabraAdivinar = "";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        System.out.println("Servidor esperando jugadores...");

        while (true) {
            if (player1 == null) {
                player1 = serverSocket.accept();
                System.out.println("Jugador 1 conectado");
                pool.execute(new ThreadServerWardle(player1, 1));
            } else if (player2 == null) {
                player2 = serverSocket.accept();
                System.out.println("Jugador 2 conectado");
                pool.execute(new ThreadServerWardle(player2, 2));
            }
        }
    }

    public static synchronized void setPalabraAdivinar(String palabra) {
        palabraAdivinar = palabra;
    }

    public static synchronized String getPalabraAdivinar() {
        return palabraAdivinar;
    }
}