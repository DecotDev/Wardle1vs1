import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerWardle {
    private int port;
    private Game game;
    private int numPlayers;

    public ServerWardle(int port) {
        this.port = port;
    }

    private void listen () {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                numPlayers++;
                game.setNumPlayers(numPlayers);
                ThreadServerWardle threadServerWardle = new ThreadServerWardle(clientSocket, game);
                threadServerWardle.start();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        ServerWardle server = new ServerWardle(5225);

        Thread thTcp= new Thread(server::listen);
        thTcp.start();
    }
}
