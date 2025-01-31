import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadServerWardle extends Thread{
    private Game2 game;
    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private int idPlayer;


    public ThreadServerWardle(Socket clientSocket, Game2 game) throws IOException {
        this.game = game;
        this.clientSocket = clientSocket;
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    @Override
    public void run() {
        Jugada j = null;
    }
}
