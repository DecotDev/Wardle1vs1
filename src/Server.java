import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private int numJugadors = 0;
    private Joc joc;

    private Server(int port) {
        this.port = port;
        joc = new Joc();
    }

    private void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (numJugadors < 10) {
                System.out.println("Esperant client...");
                clientSocket = serverSocket.accept();
                numJugadors += 1;
                System.out.println("Client nou connectat, jugadors connectats: " + numJugadors);
                joc.setNumJugadros(numJugadors);
                System.out.println(joc.getNumJugadros());
                ThreadServer ThreadSrvModifyList = new ThreadServer(clientSocket, joc);
                Thread client = new Thread(ThreadSrvModifyList);
                client.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server srv = new Server(5558);
        Thread thTcp = new Thread(srv::listen);
        thTcp.start();

    }

}
