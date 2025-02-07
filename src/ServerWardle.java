import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWardle {
    private static final int PORT = 5225;
    private static Socket player1 = null;
    private static Socket player2 = null;
    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor esperando jugadores...");

        while (player1 == null || player2 == null) {
            Socket player = serverSocket.accept();
            if (player1 == null) {
                player1 = player;
                System.out.println("Jugador 1 conectado");
            } else {
                player2 = player;
                System.out.println("Jugador 2 conectado");
                iniciarJuego();
            }
        }
    }

    private static void iniciarJuego() throws IOException {
        ThreadServerWardle t1 = new ThreadServerWardle(player1, 1);
        ThreadServerWardle t2 = new ThreadServerWardle(player2, 2);

        pool.execute(t1);
        pool.execute(t2);
    }
}